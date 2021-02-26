package com.particle.game.world.physical.service;

import com.particle.api.inject.RequestStaticInject;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.attribute.health.HealthServiceProxy;
import com.particle.game.entity.movement.module.EntityMovementModule;
import com.particle.game.world.animation.EntityAnimationService;
import com.particle.game.world.level.ChunkService;
import com.particle.game.world.level.LevelService;
import com.particle.game.world.physical.ForbidColliderService;
import com.particle.game.world.physical.collider.AABBCollider;
import com.particle.game.world.physical.collider.PointCollider;
import com.particle.game.world.physical.modules.BoxColliderModule;
import com.particle.game.world.physical.modules.EntityColliderCallbackModule;
import com.particle.game.world.physical.modules.PointColliderModule;
import com.particle.game.world.physical.modules.RigibodyModule;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.entity.model.others.MonsterEntity;
import com.particle.model.events.level.entity.EntityDamageType;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.data.EntityEventPacket;
import com.particle.model.player.Player;

import javax.inject.Inject;

@RequestStaticInject
public class ColliderService {

    private static final int COLLIDER_SEARCH_PADDING = 4;
    private static final float GRAVITY_MULTIPLE = 1.5f;

    private static final ECSModuleHandler<BoxColliderModule> BOX_COLLIDER_MODULE_HANDLER = ECSModuleHandler.buildHandler(BoxColliderModule.class);
    private static final ECSModuleHandler<EntityColliderCallbackModule> ENTITY_COLLIDER_CALLBACK_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityColliderCallbackModule.class);

    @Inject
    private static ChunkService chunkService;
    @Inject
    private static LevelService levelService;

    @Inject
    private static EntityAnimationService entityAnimationService;

    @Inject
    private static HealthServiceProxy healthServiceProxy;

    @Inject
    private static ForbidColliderService forbidColliderService;

    /**
     * Step 1 : 查找会发生碰撞的Entity，计算碰撞与受力
     *
     * @param level
     * @param colliderModule
     * @return
     */
    public static void processCollider(Level level, Entity source, BoxColliderModule colliderModule) {
        // 判断source是否禁止和entity发生碰撞
        if (forbidColliderService.isForbidColliderEntity(source)) {
            return;
        }

        // 计算需要搜索的区块
        // 物理引擎只认自己的位置做碰撞检测
        Vector3f lastPosition = colliderModule.getAABBCollider().getLastPosition();
        int chunkXMin = ((int) (lastPosition.getX() - colliderModule.getAABBCollider().getXPadding() - COLLIDER_SEARCH_PADDING)) >> 4;
        int chunkXMax = ((int) (lastPosition.getX() + colliderModule.getAABBCollider().getXPadding() + COLLIDER_SEARCH_PADDING)) >> 4;
        int chunkZMin = ((int) (lastPosition.getZ() - colliderModule.getAABBCollider().getZPadding() - COLLIDER_SEARCH_PADDING)) >> 4;
        int chunkZMax = ((int) (lastPosition.getZ() + colliderModule.getAABBCollider().getZPadding() + COLLIDER_SEARCH_PADDING)) >> 4;

        // 查找区块，找出所有会碰撞的Entity
        for (int chunkX = chunkXMin; chunkX <= chunkXMax; chunkX++) {
            for (int chunkZ = chunkZMin; chunkZ <= chunkZMax; chunkZ++) {
                processColliderInChunk(chunkService.getChunk(level, chunkX, chunkZ), source, colliderModule);
            }
        }
    }

    public static void processCollider(Level level, Entity source, PointColliderModule colliderModule) {
        if (forbidColliderService.isForbidColliderEntity(source)) {
            return;
        }
        // 计算需要搜索的区块
        // 物理引擎只认自己的位置做碰撞检测
        Vector3f lastPosition = colliderModule.getPointCollider().getLastPosition();
        int chunkXMin = ((int) (lastPosition.getX() - COLLIDER_SEARCH_PADDING)) >> 4;
        int chunkXMax = ((int) (lastPosition.getX() + COLLIDER_SEARCH_PADDING)) >> 4;
        int chunkZMin = ((int) (lastPosition.getZ() - COLLIDER_SEARCH_PADDING)) >> 4;
        int chunkZMax = ((int) (lastPosition.getZ() + COLLIDER_SEARCH_PADDING)) >> 4;

        // 查找区块，找出所有会碰撞的Entity
        for (int chunkX = chunkXMin; chunkX <= chunkXMax; chunkX++) {
            for (int chunkZ = chunkZMin; chunkZ < chunkZMax; chunkZ++) {
                processColliderInChunk(chunkService.getChunk(level, chunkX, chunkZ), source, colliderModule);
            }
        }
    }

    /**
     * Step 2 : 根据环境受力（重力、浮力、摩擦力）计算速度
     *
     * @param level
     * @param entity
     * @param rigibodyModule
     * @param transformModule
     */
    public static void processEnvironmentForce(Level level, Entity entity, RigibodyModule rigibodyModule, TransformModule transformModule, EntityMovementModule entityMovementModule) {
        // 计算重力
        if (rigibodyModule.isEnableGravity()) {
            float motionY = entityMovementModule.getMotionY();

            // 水下检测
            BlockPrototype entityHeadBlockPrototype = levelService.getBlockTypeAt(entity.getLevel(), new Vector3(transformModule.getPosition()));
            if (entityHeadBlockPrototype == BlockPrototype.WATER || entityHeadBlockPrototype == BlockPrototype.FLOWING_WATER) {
                if (motionY < -rigibodyModule.getGravity() * 8f) {
                    motionY += 0.5;
                } else {
                    motionY -= rigibodyModule.getGravity() * GRAVITY_MULTIPLE;
                }
            } else {
                motionY = motionY - rigibodyModule.getGravity() * GRAVITY_MULTIPLE;
            }

            if (transformModule.isOnGround()) {
                if (motionY < 0) {
                    motionY = entityMovementModule.getMotionY();
                }

                float motionX = entityMovementModule.getMotionX();
                if (motionX > 0.5 || motionX < -0.5) {
                    entityMovementModule.setMotionX(motionX / 2);
                } else if (motionX != 0) {
                    entityMovementModule.setMotionX(0);
                }

                float motionZ = entityMovementModule.getMotionZ();
                if (motionZ > 0.5 || motionZ < -0.5) {
                    entityMovementModule.setMotionZ(motionZ / 2);
                } else if (motionZ != 0) {
                    entityMovementModule.setMotionZ(0);
                }
            }

            entityMovementModule.setMotionY(motionY);
        }
    }

    /**
     * Step 3 : 计算碰撞受力
     */
    public static void processColliderForce(BoxColliderModule colliderModule, EntityMovementModule entityMovementModule) {
        entityMovementModule.setMotion(entityMovementModule.getMotion().add(colliderModule.getAABBCollider().getColliderMotion()));
        colliderModule.setColliderMotion(new Vector3f(0, 0, 0));
    }

    /**
     * Step 4 : 根据受力情况计算加速度与当前速度
     */
    public static void processSpeed(TransformModule transformModule, EntityMovementModule entityMovementModule, float deltaTime) {
        float time = deltaTime > 1000 ? 1 : (((float) deltaTime) / 1000);
        transformModule.setPosition(transformModule.getPosition().add(entityMovementModule.getMotion().multiply(time)));
    }

    /**
     * Step 4 : 计算与环境碰撞
     */
    public static void processVoxelHit(Entity entity, BoxColliderModule colliderModule, TransformModule transformModule, EntityMovementModule entityMovementModule) {
        if (forbidColliderService.isForbidColliderVoxel(entity)) {
            return;
        }
        // 缓存Component
        AABBCollider collider = colliderModule.getAABBCollider();

        // 数据初始化检测
        if (transformModule.getPosition().equals(collider.getLastPosition()) && !collider.isColliderWithBlock()) {
            // 若坐标未发生变化，且不在碰撞状态中，不执行碰撞检测
            return;
        }

        // 计算碰撞情况
        Vector3f fixedPosition = BlockColliderDetectTool.doDetect(entity.getLevel(), transformModule, collider);

        // 碰撞处理
        if (fixedPosition != null) {
            // 碰撞回调
            EntityColliderCallbackModule entityColliderCallbackModule = ENTITY_COLLIDER_CALLBACK_MODULE_HANDLER.getModule(entity);
            if (entityColliderCallbackModule != null && entityColliderCallbackModule.getOnColliderWithBlockCallback() != null) {
                entityColliderCallbackModule.getOnColliderWithBlockCallback().accept(fixedPosition);
            }

            // 判断是否接触地面
            Vector3f motion = entityMovementModule.getMotion();
            if (fixedPosition.getY() != transformModule.getY()) {
                if (motion.getY() < -9.8) {
                    entityAnimationService.sendAnimation(entity, EntityEventPacket.GROUND_DUST);
                }
                // 且不在水中
                BlockPrototype block = levelService.getBlockTypeAt(entity.getLevel(), fixedPosition.toVector3());
                if (motion.getY() < -18 && block != BlockPrototype.WATER && block != BlockPrototype.FLOWING_WATER) {
                    if (entity instanceof Player || entity instanceof MobEntity) {
                        healthServiceProxy.damageEntity(entity, (float) Math.floor(-18f - motion.getY()), EntityDamageType.Fall, null);
                    }
                }
            }

            // motion重置
            if (fixedPosition.getX() != transformModule.getX()) {
                motion.setX(0);
            }
            if (fixedPosition.getY() != transformModule.getY()) {
                motion.setY(0);
            }
            if (fixedPosition.getZ() != transformModule.getZ()) {
                motion.setZ(0);
            }
            entityMovementModule.setMotion(motion);

            // 若发送碰撞，则同时更新两个位置
            transformModule.setPosition(fixedPosition);
            collider.setColliderWithBlock(true);
        } else {
            // 未发生碰撞
            collider.setColliderWithBlock(false);
        }
    }

    public static void processVoxelHit(Entity entity, PointColliderModule colliderModule, TransformModule transformModule, EntityMovementModule entityMovementModule) {
        if (forbidColliderService.isForbidColliderVoxel(entity)) {
            return;
        }
        // 缓存Component
        PointCollider collider = colliderModule.getPointCollider();

        // 数据初始化检测
        if (transformModule.getPosition().equals(collider.getLastPosition()) && !collider.isColliderWithBlock()) {
            // 若坐标未发生变化，且不在碰撞状态中，不执行碰撞检测
            return;
        }

        // 计算碰撞情况
        Vector3f fixedPosition = BlockColliderDetectTool.doDetect(entity.getLevel(), transformModule, collider);

        // 碰撞处理
        if (fixedPosition != null) {
            // 碰撞回调
            EntityColliderCallbackModule entityColliderCallbackModule = ENTITY_COLLIDER_CALLBACK_MODULE_HANDLER.getModule(entity);
            if (entityColliderCallbackModule != null && entityColliderCallbackModule.getOnColliderWithBlockCallback() != null) {
                entityColliderCallbackModule.getOnColliderWithBlockCallback().accept(fixedPosition);
            }

            // 判断是否接触地面
            Vector3f motion = entityMovementModule.getMotion();
            if (fixedPosition.getY() != transformModule.getY()) {
                if (motion.getY() < -9.8) {
                    entityAnimationService.sendAnimation(entity, EntityEventPacket.GROUND_DUST);
                }
                // 且不在水中
                BlockPrototype block = levelService.getBlockTypeAt(entity.getLevel(), fixedPosition.toVector3());
                if (motion.getY() < -18 && block != BlockPrototype.WATER && block != BlockPrototype.FLOWING_WATER) {
                    if (entity instanceof Player || entity instanceof MobEntity) {
                        healthServiceProxy.damageEntity(entity, (float) Math.floor(-18f - motion.getY()), EntityDamageType.Fall, null);
                    }
                }
            }

            // motion重置
            if (fixedPosition.getX() != transformModule.getX()) {
                motion.setX(0);
            }
            if (fixedPosition.getY() != transformModule.getY()) {
                motion.setY(0);
            }
            if (fixedPosition.getZ() != transformModule.getZ()) {
                motion.setZ(0);
            }
            entityMovementModule.setMotion(motion);

            // 若发送碰撞，则同时更新两个位置
            transformModule.setPosition(fixedPosition);
            collider.setColliderWithBlock(true);
        } else {
            // 未发生碰撞
            collider.setColliderWithBlock(false);
        }
    }

    private static void processColliderInChunk(Chunk chunk, Entity source, BoxColliderModule colliderModule) {
        if (chunk == null) {
            return;
        }
        EntityColliderCallbackModule entityColliderCallbackModule = ENTITY_COLLIDER_CALLBACK_MODULE_HANDLER.getModule(source);
        // 与玩家碰撞检测
        for (Player player : chunk.getPlayersCollection().getEntitiesViewer()) {
            if (source.ID == player.ID) {
                continue;
            }
            if (!colliderModule.containsColliderEntity(player.getRuntimeId())) {
                BoxColliderModule colliderModuleTo = BOX_COLLIDER_MODULE_HANDLER.getModule(player);
                if (colliderModuleTo != null) {
                    boolean state = EntityColliderDetectTool.checkColliderWithTarget(colliderModule, colliderModuleTo);
                    if (state) {
                        // 处理碰撞结果
                        if (entityColliderCallbackModule != null && entityColliderCallbackModule.getOnColliderWithEntityCallback() != null) {
                            entityColliderCallbackModule.getOnColliderWithEntityCallback().accept(player);
                            colliderModule.addColliderEntity(player.getRuntimeId());
                        }
                        // 处理弹力
                        if (!forbidColliderService.isForbidKinckback(source)) {
                            Vector3f colliderMotion = new Vector3f(colliderModuleTo.distanceTo(colliderModule));
                            colliderMotion.setY(0);
                            colliderMotion = colliderMotion.normalize();
                            colliderMotion = colliderMotion.multiply(2f);
                            colliderModule.setColliderMotion(colliderMotion);
                        }
                    }
                }
            }
        }

        // 与Mob碰撞检测
        for (MobEntity mobEntity : chunk.getMobEntitiesCollection().getEntitiesViewer()) {
            if (source.ID == mobEntity.ID) {
                continue;
            }
            if (!colliderModule.containsColliderEntity(mobEntity.getRuntimeId())) {
                BoxColliderModule colliderModuleTo = BOX_COLLIDER_MODULE_HANDLER.getModule(mobEntity);
                if (colliderModuleTo != null) {
                    boolean state = EntityColliderDetectTool.checkColliderWithTarget(colliderModule, colliderModuleTo);
                    if (state) {
                        // 处理碰撞结果
                        if (entityColliderCallbackModule != null && entityColliderCallbackModule.getOnColliderWithEntityCallback() != null) {
                            entityColliderCallbackModule.getOnColliderWithEntityCallback().accept(mobEntity);
                            colliderModule.addColliderEntity(mobEntity.getRuntimeId());
                        }
                        // 处理弹力
                        if (!forbidColliderService.isForbidKinckback(source)) {
                            Vector3f colliderMotion = new Vector3f(colliderModuleTo.distanceTo(colliderModule));
                            colliderMotion.setY(0);
                            colliderMotion = colliderMotion.normalize();
                            colliderMotion = colliderMotion.multiply(2f);
                            colliderModule.setColliderMotion(colliderMotion);
                        }
                    }
                }
            }
        }

        // 与Monster碰撞检测
        for (MonsterEntity monsterEntity : chunk.getMonsterEntitiesCollection().getEntitiesViewer()) {
            if (source.ID == monsterEntity.ID) {
                continue;
            }
            if (!colliderModule.containsColliderEntity(monsterEntity.getRuntimeId())) {
                BoxColliderModule colliderModuleTo = BOX_COLLIDER_MODULE_HANDLER.getModule(monsterEntity);
                if (colliderModuleTo != null) {
                    boolean state = EntityColliderDetectTool.checkColliderWithTarget(colliderModule, colliderModuleTo);
                    if (state) {
                        // 处理碰撞结果
                        if (entityColliderCallbackModule != null && entityColliderCallbackModule.getOnColliderWithEntityCallback() != null) {
                            entityColliderCallbackModule.getOnColliderWithEntityCallback().accept(monsterEntity);
                            colliderModule.addColliderEntity(monsterEntity.getRuntimeId());
                        }
                        // 处理弹力
                        if (!forbidColliderService.isForbidKinckback(source)) {
                            Vector3f colliderMotion = new Vector3f(colliderModuleTo.distanceTo(colliderModule));
                            colliderMotion.setY(0);
                            colliderMotion = colliderMotion.normalize();
                            colliderMotion = colliderMotion.multiply(2f);
                            colliderModule.setColliderMotion(colliderMotion);
                        }
                    }
                }
            }
        }
    }

    private static void processColliderInChunk(Chunk chunk, Entity source, PointColliderModule colliderModule) {
        if (chunk == null) {
            return;
        }

        EntityColliderCallbackModule entityColliderCallbackModule = ENTITY_COLLIDER_CALLBACK_MODULE_HANDLER.getModule(source);

        // 与玩家碰撞检测
        for (Player player : chunk.getPlayersCollection().getEntitiesViewer()) {
            if (source.ID == player.ID) {
                continue;
            }

            BoxColliderModule colliderModuleTo = BOX_COLLIDER_MODULE_HANDLER.getModule(player);
            if (colliderModuleTo != null) {
                boolean state = EntityColliderDetectTool.checkColliderWithTarget(colliderModule, colliderModuleTo);
                if (state) {
                    // 处理碰撞结果
                    if (entityColliderCallbackModule != null && entityColliderCallbackModule.getOnColliderWithEntityCallback() != null) {
                        entityColliderCallbackModule.getOnColliderWithEntityCallback().accept(player);
                    }
                }
            }
        }

        // 生物碰撞检测
        for (MobEntity mobEntity : chunk.getMobEntitiesCollection().getEntitiesViewer()) {
            if (source.ID == mobEntity.ID) {
                continue;
            }

            BoxColliderModule colliderModuleTo = BOX_COLLIDER_MODULE_HANDLER.getModule(mobEntity);
            if (colliderModuleTo != null) {
                boolean state = EntityColliderDetectTool.checkColliderWithTarget(colliderModule, colliderModuleTo);
                if (state) {
                    // 处理碰撞结果
                    if (entityColliderCallbackModule != null && entityColliderCallbackModule.getOnColliderWithEntityCallback() != null) {
                        entityColliderCallbackModule.getOnColliderWithEntityCallback().accept(mobEntity);
                    }
                }
            }
        }

        for (MonsterEntity monsterEntity : chunk.getMonsterEntitiesCollection().getEntitiesViewer()) {
            if (source.ID == monsterEntity.ID) {
                continue;
            }

            BoxColliderModule colliderModuleTo = BOX_COLLIDER_MODULE_HANDLER.getModule(monsterEntity);
            if (colliderModuleTo != null) {
                boolean state = EntityColliderDetectTool.checkColliderWithTarget(colliderModule, colliderModuleTo);
                if (state) {
                    // 处理碰撞结果
                    if (entityColliderCallbackModule != null && entityColliderCallbackModule.getOnColliderWithEntityCallback() != null) {
                        entityColliderCallbackModule.getOnColliderWithEntityCallback().accept(monsterEntity);
                    }
                }
            }
        }
    }
}
