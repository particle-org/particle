package com.particle.game.server;

import com.particle.api.ServerApi;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.executor.api.ExecutableTask;
import com.particle.executor.service.AsyncScheduleService;
import com.particle.executor.service.LevelScheduleService;
import com.particle.executor.service.NodeScheduleService;
import com.particle.game.entity.attribute.identified.EntityNameService;
import com.particle.game.player.PlayerOnlineTimeRecorderService;
import com.particle.game.player.PlayerService;
import com.particle.game.player.craft.RecipeManager;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.transaction.TransactionManager;
import com.particle.game.player.save.PlayerDataService;
import com.particle.game.server.command.CommandManager;
import com.particle.game.server.plugin.PluginManager;
import com.particle.game.server.resources.ResourcePackManager;
import com.particle.game.world.level.WorldService;
import com.particle.game.world.level.loader.ChunkLoaderService;
import com.particle.inputs.HandlerLoader;
import com.particle.model.events.level.player.PlayerLeaveLevelEvent;
import com.particle.model.events.level.player.PlayerQuitGameEvent;
import com.particle.model.level.Level;
import com.particle.model.network.packets.data.DisconnectPlayerPacket;
import com.particle.model.network.packets.data.PlayerStatusPacket;
import com.particle.model.player.Player;
import com.particle.model.player.PlayerState;
import com.particle.network.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 玩家数据处理模块.
 */
@Singleton
public class Server implements ServerApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    @Inject
    private HandlerLoader handlerLoader;

    @Inject
    private RecipeManager recipeManager;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private PlayerService playerService;
    @Inject
    private PlayerDataService playerDataService;
    @Inject
    private EntityNameService entityNameService;
    @Inject
    private WorldService worldService;
    @Inject
    private PlayerOnlineTimeRecorderService playerOnlineTimeRecorderService;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();
    @Inject
    private NetworkManager networkManager;
    @Inject
    private ResourcePackManager resourcePackManager;
    @Inject
    private TransactionManager transactionManager;
    @Inject
    private CommandManager commandManager;

    @Inject
    private ChunkLoaderService chunkLoaderService;


    /**
     * 已连接的玩家列表
     */
    private final Map<InetSocketAddress, Player> players = new ConcurrentHashMap<>();

    /**
     * 玩家eid索引
     */
    private Map<Long, Player> playerCache = new ConcurrentHashMap<>();

    /**
     * 玩家name索引
     */
    private Map<String, Player> playerNameCache = new ConcurrentHashMap<>();

    /**
     * 玩家UUID索引
     */
    private Map<UUID, Player> playerUuidCache = new ConcurrentHashMap<>();


    private ServerStatus status = ServerStatus.RUNNING;

    @Inject
    public void init() {
        this.handlerLoader.init();
        this.recipeManager.loadDefault();
        this.inventoryManager.loadCreativeItems();
    }

    /**
     * 添加玩家
     * 端内接口
     *
     * @param player
     */
    public void onPlayerConnectionEstablished(Player player) {
        if (player == null) {
            return;
        }
        if (this.players.containsKey(player.getClientAddress())) {
            return;
        }

        this.players.put(player.getClientAddress(), player);
    }

    /**
     * 创建玩家数据加载任务.
     * 端内接口
     *
     * @param player 登陆的玩家
     */
    public void onPlayerLogin(Player player) {
        LOGGER.info("Player {} login from {}.", player.getIdentifiedStr(), player.getClientAddress());

        // todo 切换为uuid做判断
        if (this.playerNameCache.get(this.entityNameService.getEntityName(player)) != null) {
            this.close(player, "服务器已存在该玩家信息，请稍后登录");
            return;
        }
        //缓存玩家信息
        this.playerCache.put(player.getRuntimeId(), player);
        this.playerNameCache.put(this.entityNameService.getEntityName(player), player);
        this.playerUuidCache.put(this.playerService.getPlayerUUID(player), player);

        //更新玩家版本标签
        LOGGER.info(String.format("player uuid : %s, player name : %s, protocol version : %s.", this.playerService.getPlayerUUID(player).toString(), this.entityNameService.getEntityName(player), player.getProtocolVersion()));
        networkManager.setProtocolVersion(player.getClientAddress(), player.getProtocolVersion());

        //发送登陆成功状态
        PlayerStatusPacket playerStatusPacket = new PlayerStatusPacket();
        playerStatusPacket.setStatus(PlayerStatusPacket.LOGIN_SUCCESS);
        networkManager.sendMessage(player.getClientAddress(), playerStatusPacket);

        // 注册玩家指令
        this.commandManager.addPlayerCommandSource(player);

        //标记玩家状态为连接中
        player.setPlayerState(PlayerState.CONNECTING);

        //开始同步资源包
        resourcePackManager.sendResourcePacket(player);

        // 记录登录时间
        this.playerOnlineTimeRecorderService.markLoginTime(player);
    }

    /**
     * 删除玩家
     * 端内接口
     *
     * @param address
     */
    public void onPlayerConnectionDestroyed(InetSocketAddress address) {
        if (address == null) {
            return;
        }

        Player player = this.players.remove(address);
        if (player == null) {
            return;
        }
        this.commandManager.removePlayerCommandSource(player);
        this.onPlayerClose(player);
    }

    /**
     * 主动关闭与玩家的连接
     *
     * @param player the entity
     * @param reason the reason
     */
    @Override
    public void close(Player player, String reason) {
        LOGGER.info("Close player {} connection because of {}", player.getIdentifiedStr(), reason);

        DisconnectPlayerPacket packet = new DisconnectPlayerPacket();
        packet.setMessage(reason);

        this.networkManager.sendMessage(player.getClientAddress(), packet);

        if (player != null && player.getLevel() != null) {
            player.getLevel().getLevelSchedule().scheduleDelayTask("Server-onClosePlayer", new ExecutableTask() {
                @Override
                public void run() {
                    onClosePlayer(player.getClientAddress(), reason);
                }
            }, 250);
        }
    }

    /**
     * 关闭玩家
     *
     * @param address
     * @param reason
     */
    private void onClosePlayer(InetSocketAddress address, String reason) {
        this.networkManager.removePlayer(address, reason);
    }

    /**
     * 当玩家连接关闭时的操作.
     *
     * @param player the entity
     */
    private void onPlayerClose(Player player) {
        LOGGER.info("Player {} disconnect.", player.getIdentifiedStr());


        // 记录退出时间
        this.playerOnlineTimeRecorderService.markLogoutTime(player);

        // 离开Level
        if (player.getLevel() != null) {
            //若player没有Level，代表还未spawn进入世界过

            //发送玩家离开世界的event
            PlayerLeaveLevelEvent playerLeaveLevelEvent = new PlayerLeaveLevelEvent(player, player.getLevel(), PlayerLeaveLevelEvent.LeaveCause.QUIT);
            this.eventDispatcher.dispatchEvent(playerLeaveLevelEvent);

            this.playerService.deSpawnPlayer(player.getLevel(), player);
        }

        // 发送玩家离开游戏的event，该Event无视Cancel操作
        this.eventDispatcher.dispatchEvent(new PlayerQuitGameEvent(player));

        // 回收合成台的物品
        // TODO: 2019/7/16 避免这里报空指针临时处理
        try {
            this.inventoryManager.onCloseWorkBench(player);
        } catch (Exception e) {
            LOGGER.error("Fail to recovery player workbench!", e);
        }

        //标记玩家状态为断开
        player.setPlayerState(PlayerState.DISCONNECTED);

        //首先清空玩家缓存
        this.playerCache.remove(player.getRuntimeId());

        String entityName = this.entityNameService.getEntityName(player);
        if (entityName != null) {
            this.playerNameCache.remove(entityName);
        }

        UUID playerUUID = this.playerService.getPlayerUUID(player);
        if (playerUUID != null) {
            this.playerUuidCache.remove(playerUUID);
        }

        // 清除inventory缓存
        this.transactionManager.clearCache(player);

        // 跳过存档
        if (playerUUID == null) {
            return;
        }

        //保存玩家数据
        this.playerDataService.save(player, true, null);
        LOGGER.info("保存玩家【{}】存档，玩家离开世界", player.getIdentifiedStr());
    }

    /**
     * 玩家是否在线
     *
     * @param player
     * @return
     */
    public boolean isOnline(Player player) {
        return this.getOnlinePlayer(player.getClientAddress()) != null;
    }

    /**
     * 查询已经连接的玩家
     *
     * @param entityId
     * @return
     */
    @Override
    public Player getPlayer(long entityId) {
        return this.playerCache.get(entityId);
    }

    /**
     * 查询已经连接的玩家
     *
     * @param playerName
     * @return
     */
    @Override
    public Player getPlayer(String playerName) {
        return this.playerNameCache.get(playerName);
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return this.playerUuidCache.get(uuid);
    }

    /**
     * 查询已经连接的玩家
     */
    @Override
    public Player getOnlinePlayer(InetSocketAddress address) {
        return this.players.get(address);
    }

    /**
     * 通过uuid搜索在线玩家
     *
     * @param uuid
     * @return
     */
    public Player searchOnlinePlayerByUuid(UUID uuid) {
        Collection<Player> allPlayers = this.getAllPlayers();
        for (Player player : allPlayers) {
            UUID playerUUID = playerService.getPlayerUUID(player);
            if (uuid.equals(playerUUID)) {
                return player;
            }
        }
        return null;
    }

    /**
     * 获取所有在线玩家
     *
     * @return
     */
    @Override
    public Collection<Player> getAllPlayers() {
        return new ArrayList<>(this.players.values());
    }

    /**
     * 获取玩家数量
     *
     * @return
     */
    @Override
    public int getPlayerAmount() {
        return this.playerCache.size();
    }

    /**
     * 获取合适玩家数量
     *
     * @return
     */
    @Override
    public int getSuitablePlayerAmount() {
        return this.networkManager.getNetworkInfo().getMaxConnection() * 90 / 100;
    }

    /**
     * 获取最大玩家数量
     *
     * @return
     */
    @Override
    public int getMaxPlayerAmount() {
        return this.networkManager.getNetworkInfo().getMaxConnection();
    }

    /**
     * 设置最大玩家数量
     *
     * @return
     */
    @Override
    public void setMaxPlayerAmount(int amount) {
        this.networkManager.getNetworkInfo().setMaxConnection(amount);
    }

    /**
     * 关闭
     *
     * @return
     */
    @Override
    public boolean shutdown() {
        this.status = ServerStatus.STOP;

        // 移除所有玩家
        List<Player> players = new ArrayList<>(this.players.values());
        for (Player player : players) {
            this.close(player, "服务器维护");
        }

        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.error("Shutdown task error!", e);
                    return;
                }

                if (this.players.size() == 0) {
                    // 无效插件
                    PluginManager.getInstance().disable();

                    // 关闭所有的Level
                    for (Level level : this.worldService.getAllLevels()) {
                        this.worldService.removeLevel(level.getLevelName());
                    }
                }

                if (this.worldService.getAllLevels().size() == 0) {
                    // 关闭Network
                    this.networkManager.shutdown();

                    // 关闭schedule
                    NodeScheduleService.getInstance().shutdown();
                    AsyncScheduleService.getInstance().shutdown();

                    // 当玩家数量为0的时候，关闭default thread，这里可以保证所有需要存储的任务都提交成功
                    LevelScheduleService.getInstance().shutdownAll();

                    // 关闭区块保存
                    this.chunkLoaderService.shutdown();
                }

                if (this.players.size() == 0 && this.worldService.getAllLevels().size() == 0) {
                    return;
                }
            }
        });
        thread.setName("ShutdownCheckThread");
        thread.setDaemon(true);
        thread.start();


        return true;
    }

    /**
     * 暂停
     *
     * @return
     */
    @Override
    public boolean pause() {
        if (status == ServerStatus.STOP) {
            return false;
        }
        status = ServerStatus.PAUSED;
        return true;
    }

    /**
     * 重启
     *
     * @return
     */
    @Override
    public boolean restart() {
        if (status == ServerStatus.STOP) {
            return false;
        }
        status = ServerStatus.RUNNING;
        return true;
    }

    /**
     * 获取状态
     *
     * @return
     */
    @Override
    public ServerStatus getStatus() {
        return status;
    }

}
