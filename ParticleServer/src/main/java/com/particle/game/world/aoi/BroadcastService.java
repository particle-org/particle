package com.particle.game.world.aoi;

import com.particle.api.inject.RequestStaticInject;
import com.particle.core.aoi.SceneManager;
import com.particle.core.aoi.model.Grid;
import com.particle.core.aoi.model.Scene;
import com.particle.game.scene.module.BroadcastModule;
import com.particle.model.entity.Entity;
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
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

@Singleton
@RequestStaticInject
public class BroadcastService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastServiceProxy.class);

    @Inject
    private static NetworkManager networkManager;

    /**
     * 广播指定玩家的数据包
     *
     * @param sender     被订阅者
     * @param dataPacket
     */
    public static void broadcast(Entity sender, BroadcastModule broadcastModule, DataPacket dataPacket) {
        broadcastModule.getBroadcastList().forEach((address) -> {
            networkManager.sendMessage(address, dataPacket);
        });
    }

    public static void broadcast(Entity sender, BroadcastModule broadcastModule, DataPacket dataPacket, boolean selfBroadcast) {
        broadcast(sender, broadcastModule, dataPacket);

        if (selfBroadcast && sender instanceof Player) {
            networkManager.sendMessage(((Player) sender).getClientAddress(), dataPacket);
        }
    }

    /**
     * 批量广播指定玩家的数据包
     *
     * @param sender
     * @param dataPackets
     */
    public static void broadcast(Entity sender, BroadcastModule broadcastModule, List<DataPacket> dataPackets) {
        broadcastModule.getBroadcastList().forEach((address) -> {
            networkManager.sendMessage(address, dataPackets);
        });
    }

    public static void broadcast(Entity sender, BroadcastModule broadcastModule, List<DataPacket> dataPackets, boolean selfBroadcast) {
        broadcast(sender, broadcastModule, dataPackets);

        if (selfBroadcast && sender instanceof Player) {
            networkManager.sendMessage(((Player) sender).getClientAddress(), dataPackets);
        }
    }

    /**
     * 广播数据至指定位置所在的区块
     *
     * @param level
     * @param position
     * @param dataPacket
     */
    public static void broadcast(Level level, Vector3f position, DataPacket dataPacket) {
        Grid grid = SceneManager.getInstance().getGridNode(level.getScene(), position.getFloorX() / Grid.GRID_WIDTH, position.getFloorZ() / Grid.GRID_WIDTH, false);

        if (grid != null) {
            ArrayList<InetSocketAddress> sendPlayers = new ArrayList<>(grid.getSubscribers());
            networkManager.broadcastMessage(sendPlayers, dataPacket);
        }
    }

    /**
     * 广播数据至指定位置所在的区块，排除指定玩家
     *
     * @param level
     * @param position
     * @param dataPacket
     */
    public static void broadcast(Level level, Vector3f position, DataPacket dataPacket, Player player) {
        Grid grid = SceneManager.getInstance().getGridNode(level.getScene(), position.getFloorX() / Grid.GRID_WIDTH, position.getFloorZ() / Grid.GRID_WIDTH, false);

        if (grid != null) {
            ArrayList<InetSocketAddress> sendPlayers = new ArrayList<>();
            grid.getSubscribers().forEach((address) -> {
                if (!address.equals(player.getClientAddress())) {
                    sendPlayers.add(address);
                }
            });
            networkManager.broadcastMessage(sendPlayers, dataPacket);
        }
    }

    /**
     * 广播数据至指定位置所在的区块
     *
     * @param level
     * @param position
     * @param dataPacket
     */
    public static void broadcast(Level level, Vector3 position, DataPacket dataPacket) {
        Grid grid = SceneManager.getInstance().getGridNode(level.getScene(), position.getX() / Grid.GRID_WIDTH, position.getZ() / Grid.GRID_WIDTH, false);

        if (grid != null) {
            ArrayList<InetSocketAddress> sendPlayers = new ArrayList<>(grid.getSubscribers());
            networkManager.broadcastMessage(sendPlayers, dataPacket);
        }
    }

    public static void broadcast(Level level, Chunk chunk, DataPacket dataPacket) {
        ArrayList<InetSocketAddress> sendPlayers = new ArrayList<>(chunk.getGrid().getSubscribers());
        networkManager.broadcastMessage(sendPlayers, dataPacket);
    }

    /**
     * 广播数据至指定位置所在的区块，排除指定玩家
     *
     * @param level
     * @param position
     * @param dataPacket
     */

    public static void broadcast(Level level, Vector3 position, DataPacket dataPacket, Player player) {
        Grid grid = SceneManager.getInstance().getGridNode(level.getScene(), position.getX() / Grid.GRID_WIDTH, position.getZ() / Grid.GRID_WIDTH, false);

        if (grid != null) {
            ArrayList<InetSocketAddress> sendPlayers = new ArrayList<>();
            grid.getSubscribers().forEach((address) -> {
                if (!address.equals(player.getClientAddress())) {
                    sendPlayers.add(address);
                }
            });
            networkManager.broadcastMessage(sendPlayers, dataPacket);
        }
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
    public static void broadcast(Level level, Vector3 position, DataPacket dataPacket, int chunkRadius) {
        SceneManager sceneManager = SceneManager.getInstance();
        Scene scene = level.getScene();

        ArrayList<InetSocketAddress> sendPlayers = new ArrayList<>();

        int chunkLength = 1 + (chunkRadius << 1);
        int xStart = (position.getX() >> 4) - chunkRadius;
        int zStart = (position.getZ() >> 4) - chunkRadius;
        for (int x = 0; x < chunkLength; x++) {
            for (int z = 0; z < chunkLength; z++) {
                Grid gridNode = sceneManager.getGridNode(scene, xStart + x, zStart + z, false);

                if (gridNode != null) {
                    sendPlayers.addAll(gridNode.getSubscribers());
                }
            }
        }

        networkManager.broadcastMessage(sendPlayers, dataPacket);
    }

    /**
     * 全服广播数据包
     *
     * @param level
     * @param dataPacket
     */
    public static void broadcast(Level level, DataPacket dataPacket) {
        ArrayList<InetSocketAddress> sendPlayers = new ArrayList<>();
        level.getPlayers().forEach((uuid, player) -> {
            sendPlayers.add(player.getClientAddress());
        });
        networkManager.broadcastMessage(sendPlayers, dataPacket);
    }
}
