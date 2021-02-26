package com.particle.game.entity.movement;

import com.particle.api.entity.attribute.MovementServiceAPI;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.link.EntityLinkServiceProxy;
import com.particle.game.entity.movement.module.EntityMovementModule;
import com.particle.game.world.level.ChunkService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.attribute.EntityAttribute;
import com.particle.model.level.Chunk;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.data.MovePlayerPacket;
import com.particle.model.player.Player;
import com.particle.model.player.PlayerState;
import com.particle.model.player.PositionMode;
import com.particle.network.NetworkManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovementServiceProxy implements MovementServiceAPI {

    private static final ECSModuleHandler<EntityMovementModule> ENTITY_MOVEMENT_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMovementModule.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);


    @Inject
    private NetworkManager networkManager;

    @Inject
    private ChunkService chunkService;

    @Inject
    private EntityLinkServiceProxy entityLinkServiceProxy;

    /**
     * 初始化移动组件
     *
     * @param entity
     * @param speed
     * @param maxSpeed
     */
    public void enableMovement(Entity entity, float speed, float maxSpeed) {
        EntityMovementModule entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.bindModule(entity);
        entityMovementModule.setSpeed(speed);
        entityMovementModule.setMaxSpeed(maxSpeed);
    }

    /**
     * 更新速度
     *
     * @param entity
     * @param speed
     * @param maxSpeed
     */
    @Override
    public void updateMovement(Entity entity, float speed, float maxSpeed) {
        EntityMovementModule entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.getModule(entity);
        if (entityMovementModule == null) {
            entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.bindModule(entity);
        }

        entityMovementModule.setSpeed(speed);
        entityMovementModule.setMaxSpeed(maxSpeed);
    }

    /**
     * 获取速度属性
     *
     * @param entity
     * @return
     */
    public EntityAttribute getMovementAttribute(Entity entity) {
        EntityMovementModule entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.getModule(entity);
        if (entityMovementModule == null) {
            return null;
        }

        return entityMovementModule.getEntityAttribute();
    }

    /**
     * 获取生物移动速度
     *
     * @param entity
     * @return
     */
    @Override
    public float getMovementSpeed(Entity entity) {
        EntityMovementModule entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.getModule(entity);
        if (entityMovementModule == null) {
            return 0;
        }

        return MovementService.getMovementSpeed(entity, entityMovementModule);
    }

    /**
     * 设置生物是否为跑动状态
     *
     * @param entity
     * @param state
     */
    public void setRunning(Entity entity, boolean state) {
        EntityMovementModule entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.getModule(entity);
        if (entityMovementModule != null) {
            entityMovementModule.setRunning(state);
        }
    }

    /**
     * 判断生物是否是疾跑状态
     *
     * @param entity
     * @return
     */
    public boolean isRunning(Entity entity) {
        EntityMovementModule entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.getModule(entity);
        if (entityMovementModule != null) {
            return entityMovementModule.isRunning();
        }

        return false;
    }

    /**
     * 设置生物移动速度
     *
     * @param entity
     * @param speed
     */
    public void setMovementSpeed(Entity entity, float speed) {
        EntityMovementModule entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.getModule(entity);
        if (entityMovementModule != null) {
            entityMovementModule.setSpeed(speed);
        }
    }


    /**
     * 推动生物
     *
     * @param entity
     * @param motion
     */
    @Override
    public void setMotion(Entity entity, Vector3f motion) {
        EntityMovementModule entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.getModule(entity);
        if (entityMovementModule != null) {
            entityMovementModule.setMotion(motion);
        }
    }

    /**
     * 推动生物
     *
     * @param entity
     * @param motion
     */
    @Override
    public void addMotion(Entity entity, Vector3f motion) {
        EntityMovementModule entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.getModule(entity);
        if (entityMovementModule != null) {
            entityMovementModule.setMotion(entityMovementModule.getMotion().add(motion));
        }
    }

    /**
     * 获取Motion
     *
     * @param entity
     * @return
     */
    public Vector3f getMotion(Entity entity) {
        EntityMovementModule entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.getModule(entity);
        if (entityMovementModule != null) {
            return entityMovementModule.getMotion();
        }

        return new Vector3f(0, 0, 0);
    }

    /**
     * 处理玩家移动
     *
     * @param player
     * @param position
     * @param direction
     */
    public boolean checkPlayerMove(Player player, Vector3f position, Direction direction, PositionMode positionMode) {
        if (positionMode != PositionMode.NORMAL && positionMode != PositionMode.PITCH) {
            return false;
        }

        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(player);

        // 当玩家处于teleport状态时不处理游戏业务逻辑，等待上层逻辑切回spawn状态才继续处理
        if (player.getPlayerState() == PlayerState.DESPAWNED) {
            return false;
        }

        // 目标区块可达性检测
        Chunk chunk = this.chunkService.getChunk(player.getLevel(), position.getFloorX() >> 4, position.getFloorZ() >> 4, false);
        if (chunk == null || !chunk.isRunning()) {
            // 强制刷新玩家坐标
            MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
            movePlayerPacket.setEntityId(player.getRuntimeId());
            movePlayerPacket.setVector3f(transformModule.getPosition().add(0, 1.63f, 0));
            movePlayerPacket.setDirection(transformModule.getDirection());
            movePlayerPacket.setMode(PositionMode.NORMAL);
            movePlayerPacket.setOnGround(true);
            movePlayerPacket.setRidingEntityId(this.entityLinkServiceProxy.getPlayerRidingEntityId(player));
            this.networkManager.sendMessage(player.getClientAddress(), movePlayerPacket);

            return false;
        }

        // 玩家位移距离检测
        long playerRadingEntityId = this.entityLinkServiceProxy.getPlayerRidingEntityId(player);
        if (playerRadingEntityId == 0 && transformModule.getPosition().subtract(position).lengthSquared() > 400) {
            // 强制刷新玩家坐标
            MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
            movePlayerPacket.setEntityId(player.getRuntimeId());
            movePlayerPacket.setVector3f(transformModule.getPosition().add(0, 1.63f, 0));
            movePlayerPacket.setDirection(transformModule.getDirection());
            movePlayerPacket.setMode(PositionMode.NORMAL);
            movePlayerPacket.setOnGround(true);
            movePlayerPacket.setRidingEntityId(playerRadingEntityId);
            this.networkManager.sendMessage(player.getClientAddress(), movePlayerPacket);

            return false;
        }

        return true;
    }

    public void refreshPlayerPosition(Player player) {
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(player);
        long playerRadingEntityId = this.entityLinkServiceProxy.getPlayerRidingEntityId(player);

        MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
        movePlayerPacket.setEntityId(player.getRuntimeId());
        movePlayerPacket.setVector3f(transformModule.getPosition().add(0, 1.63f, 0));
        movePlayerPacket.setDirection(transformModule.getDirection());
        movePlayerPacket.setMode(PositionMode.NORMAL);
        movePlayerPacket.setOnGround(true);
        movePlayerPacket.setRidingEntityId(playerRadingEntityId);
        this.networkManager.sendMessage(player.getClientAddress(), movePlayerPacket);
    }
}
