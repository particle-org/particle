package com.particle.game.entity.spawn;

import com.particle.api.entity.EntitySpawnServiceApi;
import com.particle.api.entity.function.IAddEntityPacketBuilder;
import com.particle.api.entity.function.IRemoveEntityPacketBuilder;
import com.particle.api.entity.function.ISpawnEntityProcessor;
import com.particle.core.aoi.SceneManager;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.executor.service.AsyncScheduleService;
import com.particle.game.block.tile.TileEntityService;
import com.particle.game.entity.link.EntityLinkService;
import com.particle.game.entity.link.EntityPassengerModule;
import com.particle.game.entity.link.EntityVehicleModule;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.EntityRecorderService;
import com.particle.game.scene.module.BroadcastModule;
import com.particle.game.scene.module.GridBinderModule;
import com.particle.game.world.level.ChunkService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.entity.model.others.MonsterEntity;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.events.level.entity.MobSpawnEvent;
import com.particle.model.events.level.entity.MonsterSpawnEvent;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EntitySpawnService implements EntitySpawnServiceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntitySpawnService.class);

    private static final ECSModuleHandler<SpawnModule> SPAWN_MODULE_HANDLER = ECSModuleHandler.buildHandler(SpawnModule.class);
    private static final ECSModuleHandler<BroadcastModule> BROADCAST_MODULE_HANDLER = ECSModuleHandler.buildHandler(BroadcastModule.class);
    private static final ECSModuleHandler<GridBinderModule> GRID_BINDER_MODULE_HANDLER = ECSModuleHandler.buildHandler(GridBinderModule.class);
    private static final ECSModuleHandler<EntityVehicleModule> ENTITY_VEHICLE_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityVehicleModule.class);
    private static final ECSModuleHandler<EntityPassengerModule> ENTITY_PASSENGER_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityPassengerModule.class);


    @Inject
    private TileEntityService tileEntityService;

    @Inject
    private PositionService positionService;

    @Inject
    private ChunkService chunkService;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    private EntityRecorderService entityRecorderService;

    @Inject
    private NetworkManager networkManager;

    /**
     * 初始化spawn组件
     *
     * @param entity
     * @param spawnEntityProcessor
     * @param addEntityPacketBuilder
     * @param removeEntityPacketBuilder
     */
    @Override
    public void enableSpawn(Entity entity, ISpawnEntityProcessor spawnEntityProcessor, IAddEntityPacketBuilder addEntityPacketBuilder, IRemoveEntityPacketBuilder removeEntityPacketBuilder) {
        SpawnModule spawnModule = SPAWN_MODULE_HANDLER.bindModule(entity);
        spawnModule.setSpawnEntityProcessor(spawnEntityProcessor);
        spawnModule.setAddEntityPacketBuilder(addEntityPacketBuilder);
        spawnModule.setRemoveEntityPacketBuilder(removeEntityPacketBuilder);
    }

    @Override
    public boolean isSpawned(Entity entity) {
        SpawnModule component = SPAWN_MODULE_HANDLER.getModule(entity);

        return component.getSpawnedChunk() != null;
    }

    /**
     * 将生物注册到世界中
     *
     * @param level
     * @param entity
     */
    public boolean spawnEntity(Level level, Entity entity) {
        // 如果spawn的Entity为MobEntity，则发送事件
        if (entity instanceof MobEntity) {
            MobSpawnEvent mobSpawnEvent = new MobSpawnEvent(level);
            mobSpawnEvent.setEntity(entity);
            mobSpawnEvent.setPosition(this.positionService.getPosition(entity));

            this.eventDispatcher.dispatchEvent(mobSpawnEvent);

            if (mobSpawnEvent.isCancelled()) {
                return false;
            }
        }

        // 如果是spawn的Entity是MonsterEntity,则发送事件
        if (entity instanceof MonsterEntity) {
            MonsterSpawnEvent monsterSpawnEvent = new MonsterSpawnEvent(level);
            monsterSpawnEvent.setMonsterEntity((MonsterEntity) entity);
            monsterSpawnEvent.setPosition(this.positionService.getPosition(entity));

            this.eventDispatcher.dispatchEvent(monsterSpawnEvent);

            if (monsterSpawnEvent.isCancelled()) {
                return false;
            }
        }

        this.spawn(level, entity);

        return true;
    }

    /**
     * 将生物注册到世界中
     *
     * @param level
     * @param entity
     */
    public void spawn(Level level, Entity entity) {
        // 检查是否已经spawn过
        if (this.entityRecorderService.getEntity(entity.getRuntimeId()) != null) {
            LOGGER.error("Entity {}:{} spawned while try to spawn!", entity.getClass().getSimpleName(), entity.getRuntimeId());
            return;
        }

        // 绑定level
        entity.setLevel(level);

        // 检查组件
        SpawnModule spawnModule = SPAWN_MODULE_HANDLER.getModule(entity);
        if (spawnModule == null) {
            LOGGER.error("Entity {}:{} spawn without spawn component", entity.getRuntimeId(), entity.getClass().getSimpleName());
            return;
        }

        // 查找区块
        Chunk chunk = null;
        if (entity instanceof Player) {
            Vector3f position = this.positionService.getPosition(entity);
            chunk = chunkService.getChunk(level, (int) Math.floor(position.getX() / 16), (int) Math.floor(position.getZ() / 16), true);
        } else {
            chunk = this.chunkService.indexChunk(level, this.positionService.getPosition(entity));
        }

        if (chunk == null) {
            LOGGER.warn("Entity {}:{} spawn into unload chunk", entity.getRuntimeId(), entity.getClass().getSimpleName());
            return;
        }

        // 标记Spawn的Chunk
        spawnModule.getSpawnEntityProcessor().spawn(level, chunk);
        spawnModule.spawnAt(chunk);

        // 注册AOI
        GridBinderModule gridBinderModule = GRID_BINDER_MODULE_HANDLER.bindModule(entity);
        gridBinderModule.updateScene(entity.getLevel().getScene());

        BroadcastModule broadcastModule = BROADCAST_MODULE_HANDLER.bindModule(entity);
        broadcastModule.initGameObject(level.getScene(),
                (address) -> {
                    return this.networkManager.sendMessage(address, spawnModule.getAddEntityPacketBuilder().buildPacket());
                },
                (address) -> {
                    IRemoveEntityPacketBuilder removeEntityPacketBuilder = spawnModule.getRemoveEntityPacketBuilder();
                    if (removeEntityPacketBuilder != null) {
                        return this.networkManager.sendMessage(address, spawnModule.getRemoveEntityPacketBuilder().buildPacket());
                    }

                    return false;
                });
        if (entity instanceof Player) {
            broadcastModule.setAddress(((Player) entity).getClientAddress());
        }

        if (entity instanceof TileEntity) {
            // TileEntity spawn进世界后立刻保存一下
            Chunk spawnedChunk = chunk;
            AsyncScheduleService.getInstance().getThread().scheduleSerialSimpleTask("Save Tile Entity", "TileEntitySave", () -> {
                spawnedChunk.getProvider().saveTileEntity((TileEntity) entity);
            });
        } else {
            // 缓存已经spawn的entity
            this.entityRecorderService.registerSpawnedEntity(entity);
        }
    }

    /**
     * 切换生物所在区块
     *
     * @param level
     * @param entity
     * @return
     */
    public void respawn(Level level, Entity entity) {
        // 检查组件
        SpawnModule spawnModule = SPAWN_MODULE_HANDLER.getModule(entity);
        if (spawnModule == null) {
            LOGGER.error("Entity {}:{} spawn without spawn component", entity.getRuntimeId(), entity.getClass().getSimpleName());
            return;
        }

        //如果还未spawn，则直接spawn
        if (spawnModule.getSpawnedChunk() == null) {
            this.spawn(level, entity);
            return;
        }

        Chunk spawnedChunk = spawnModule.getSpawnedChunk();

        //查询目标地点
        Vector3f nextPosition = this.positionService.getPosition(entity);
        int nextChunkX = nextPosition.getFloorX() >> 4;
        int nextChunkZ = nextPosition.getFloorZ() >> 4;

        //若在区块间移动，则重新订阅与广播
        if (spawnedChunk.getxPos() != nextChunkX || spawnedChunk.getzPos() != nextChunkZ) {
            Chunk chunk = this.chunkService.indexChunk(level, this.positionService.getPosition(entity));
            if (chunk == null) {
                LOGGER.warn("Entity {}:{} respawn into unload chunk", entity.getRuntimeId(), entity.getClass().getSimpleName());
                return;
            }

            spawnModule.getSpawnEntityProcessor().respawn(level, spawnModule.getSpawnedChunk(), chunk);
            spawnModule.spawnAt(chunk);
        }
    }

    /**
     * 从世界中移除生物
     *
     * @param entity
     * @return
     */
    @Override
    public void despawnEntity(Entity entity) {
        this.despawn(entity);
    }

    /**
     * 从世界中移除生物
     *
     * @param entity
     * @return
     */
    public void despawn(Entity entity) {
        // 检查组件
        SpawnModule spawnModule = SPAWN_MODULE_HANDLER.getModule(entity);
        if (spawnModule == null) {
            LOGGER.error("Entity {}:{} spawn without spawn component", entity.getRuntimeId(), entity.getClass().getSimpleName());
            return;
        }

        //查找区块
        Chunk chunk = spawnModule.resetSpawnedChunk();

        //移除生物
        if (chunk != null) {
            spawnModule.getSpawnEntityProcessor().despawn(chunk);

            // 骑乘检查
            // 如果有骑乘在该entity身上的生物，则解除骑乘关系
            EntityVehicleModule entityVehicleModule = ENTITY_VEHICLE_MODULE_HANDLER.getModule(entity);
            if (entityVehicleModule != null) {
                EntityLinkService.clearPassenger(entity, entityVehicleModule);
            }

            // 如果该entity有骑乘的生物，则解除骑乘关系
            EntityPassengerModule entityPassengerModule = ENTITY_PASSENGER_MODULE_HANDLER.getModule(entity);
            if (entityPassengerModule != null && entityPassengerModule.getVehicle() != null) {
                EntityVehicleModule entityVehicleModuleByPassenger = ENTITY_VEHICLE_MODULE_HANDLER.getModule(entityPassengerModule.getVehicle());
                if (entityVehicleModuleByPassenger != null) {
                    EntityLinkService.unRidingEntity(entityPassengerModule.getVehicle(), entityVehicleModuleByPassenger, entity);
                }
            }

            // 清除已经spawn的entity的缓存
            if (entity instanceof TileEntity) {
            } else {
                this.entityRecorderService.removeSpawnedEntity(entity.getRuntimeId());

                //清空订阅列表
                BroadcastModule broadcastModule = BROADCAST_MODULE_HANDLER.getModule(entity);
                if (broadcastModule != null) {
                    SceneManager.getInstance().clearSubscriberData(broadcastModule.getBroadcaster());
                    broadcastModule.setCurrentGrid(null);
                }

                GridBinderModule gridBinderModule = GRID_BINDER_MODULE_HANDLER.bindModule(entity);
                gridBinderModule.unregisterGameObject();
            }
        } else {
            LOGGER.error("Entity {}:{} not spawned while despawn!", entity.getRuntimeId(), entity.getClass().getSimpleName());
        }
    }

    public void spawnAt(Entity entity, Chunk chunk) {
        SpawnModule component = SPAWN_MODULE_HANDLER.getModule(entity);

        component.spawnAt(chunk);
    }

    public Chunk getSpawnChunk(Entity entity) {
        SpawnModule component = SPAWN_MODULE_HANDLER.getModule(entity);

        return component.getSpawnedChunk();
    }

    /**
     * 将tileEntity移除
     *
     * @param level
     * @param position
     * @return
     */
    public boolean despawnTileEntity(Level level, Vector3 position) {
        TileEntity tileEntity = this.tileEntityService.getEntityAt(level, position);
        if (tileEntity != null) {
            this.despawn(tileEntity);
        }
        return true;
    }

    @Override
    public DataPacket[] getEntitySpawnPacket(Entity entity) {
        SpawnModule component = SPAWN_MODULE_HANDLER.getModule(entity);

        IAddEntityPacketBuilder addEntityPacketBuilder = component.getAddEntityPacketBuilder();

        if (addEntityPacketBuilder != null) {
            return component.getAddEntityPacketBuilder().buildPacket();
        } else {
            return new DataPacket[0];
        }
    }

    @Override
    public DataPacket[] getEntityDespawnPacket(Entity entity) {
        SpawnModule component = SPAWN_MODULE_HANDLER.getModule(entity);

        IRemoveEntityPacketBuilder removeEntityPacketBuilder = component.getRemoveEntityPacketBuilder();

        if (removeEntityPacketBuilder != null) {
            return component.getRemoveEntityPacketBuilder().buildPacket();
        } else {
            return new DataPacket[0];
        }
    }
}
