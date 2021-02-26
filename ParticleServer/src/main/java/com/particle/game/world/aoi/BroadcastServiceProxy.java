package com.particle.game.world.aoi;

import com.particle.api.aoi.BroadcastServiceApi;
import com.particle.api.entity.function.IRemoveEntityPacketBuilder;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.spawn.SpawnModule;
import com.particle.game.scene.module.BroadcastModule;
import com.particle.game.scene.module.GridBinderModule;
import com.particle.model.entity.Entity;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.network.NetworkService;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class BroadcastServiceProxy implements BroadcastServiceApi {

    private static final ECSModuleHandler<BroadcastModule> BROADCAST_MODULE_HANDLER = ECSModuleHandler.buildHandler(BroadcastModule.class);
    private static final ECSModuleHandler<GridBinderModule> GRID_BINDER_MODULE_HANDLER = ECSModuleHandler.buildHandler(GridBinderModule.class);
    private static final ECSModuleHandler<SpawnModule> SPAWN_MODULE_HANDLER = ECSModuleHandler.buildHandler(SpawnModule.class);

    @Inject
    private NetworkService networkService;

    public void register(Level level, Entity entity) {
        SpawnModule spawnModule = SPAWN_MODULE_HANDLER.getModule(entity);

        // 注册aoi
        GridBinderModule gridBinderModule = GRID_BINDER_MODULE_HANDLER.bindModule(entity);
        gridBinderModule.updateScene(level.getScene());

        BroadcastModule broadcastModule = BROADCAST_MODULE_HANDLER.bindModule(entity);
        broadcastModule.initGameObject(level.getScene(),
                (address) -> this.networkService.sendMessage(address, spawnModule.getAddEntityPacketBuilder().buildPacket()),
                (address) -> {
                    IRemoveEntityPacketBuilder removeEntityPacketBuilder = spawnModule.getRemoveEntityPacketBuilder();
                    if (removeEntityPacketBuilder != null) {
                        return this.networkService.sendMessage(address, spawnModule.getRemoveEntityPacketBuilder().buildPacket());
                    }

                    return false;
                });
    }

    @Override
    public void refreshBroadcastList(Entity sender) {
        BroadcastModule broadcastModule = BROADCAST_MODULE_HANDLER.getModule(sender);

        if (broadcastModule != null) {
            broadcastModule.getBroadcaster().setForceUpdate(true);
        }
    }

    /**
     * 广播指定玩家的数据包
     *
     * @param sender     被订阅者
     * @param dataPacket
     */
    @Override
    public void broadcast(Entity sender, DataPacket dataPacket) {
        BroadcastModule broadcastModule = BROADCAST_MODULE_HANDLER.getModule(sender);

        if (broadcastModule != null) {
            BroadcastService.broadcast(sender, broadcastModule, dataPacket);
        }
    }

    @Override
    public void broadcast(Entity sender, DataPacket dataPacket, boolean selfBroadcast) {
        BroadcastModule broadcastModule = BROADCAST_MODULE_HANDLER.getModule(sender);

        if (broadcastModule != null) {
            BroadcastService.broadcast(sender, broadcastModule, dataPacket, selfBroadcast);
        }
    }

    /**
     * 批量广播指定玩家的数据包
     *
     * @param sender
     * @param dataPackets
     */
    @Override
    public void broadcast(Entity sender, List<DataPacket> dataPackets) {
        BroadcastModule broadcastModule = BROADCAST_MODULE_HANDLER.getModule(sender);

        if (broadcastModule != null) {
            BroadcastService.broadcast(sender, broadcastModule, dataPackets);
        }
    }

    @Override
    public void broadcast(Entity sender, List<DataPacket> dataPackets, boolean selfBroadcast) {
        BroadcastModule broadcastModule = BROADCAST_MODULE_HANDLER.getModule(sender);

        if (broadcastModule != null) {
            BroadcastService.broadcast(sender, broadcastModule, dataPackets, selfBroadcast);
        }
    }

    /**
     * 广播数据至指定位置所在的区块
     *
     * @param level
     * @param position
     * @param dataPacket
     */
    @Override
    public void broadcast(Level level, Vector3f position, DataPacket dataPacket) {
        BroadcastService.broadcast(level, position, dataPacket);
    }

    /**
     * 广播数据至指定位置所在的区块，排除指定玩家
     *
     * @param level
     * @param position
     * @param dataPacket
     */
    @Override
    public void broadcast(Level level, Vector3f position, DataPacket dataPacket, Player player) {
        BroadcastService.broadcast(level, position, dataPacket, player);
    }

    /**
     * 广播数据至指定位置所在的区块
     *
     * @param level
     * @param position
     * @param dataPacket
     */
    @Override
    public void broadcast(Level level, Vector3 position, DataPacket dataPacket) {
        BroadcastService.broadcast(level, position, dataPacket);
    }

    /**
     * 广播数据至指定位置所在的区块，排除指定玩家
     *
     * @param level
     * @param position
     * @param dataPacket
     */
    @Override
    public void broadcast(Level level, Vector3 position, DataPacket dataPacket, Player player) {
        BroadcastService.broadcast(level, position, dataPacket, player);
    }

    /**
     * 按照范围广播数据
     * <p>
     * 注意该方法要依次迭代周围chunk，效率不高
     *
     * @param level
     * @param position
     * @param dataPacket
     * @param chunkRadius
     */
    public void broadcast(Level level, Vector3 position, DataPacket dataPacket, int chunkRadius) {
        BroadcastService.broadcast(level, position, dataPacket, chunkRadius);
    }

    /**
     * 全服广播数据包
     *
     * @param level
     * @param dataPacket
     */
    @Override
    public void broadcast(Level level, DataPacket dataPacket) {
        BroadcastService.broadcast(level, dataPacket);
    }
}
