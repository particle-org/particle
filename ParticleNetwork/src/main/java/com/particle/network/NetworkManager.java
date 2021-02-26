package com.particle.network;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.particle.event.dispatcher.PacketDispatcher;
import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.network.NetworkInfo;
import com.particle.model.network.NetworkService;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.OnCreateSessionPacket;
import com.particle.model.network.packets.data.OnDestroySessionPacket;
import com.particle.network.monitor.PacketLogger;
import com.particle.network.tcp.DispatcherDecoListener;
import com.particle.network.tcp.TcpPackageHandler;
import com.particle.network.udp.DispatcherRaknetListener;
import com.particle.network.udp.UdpPackageHandler;
import com.particle.route.jraknet.identifier.Identifier;
import com.particle.route.jraknet.identifier.MinecraftIdentifier;
import com.particle.route.jraknet.server.RakNetServer;
import com.particle.route.jraknet.tcpnet.server.DecoServer;
import com.particle.util.configer.ConfigServiceProvider;
import com.particle.util.configer.IConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
public class NetworkManager implements NetworkService {

    private static final Logger logger = LoggerFactory.getLogger(NetworkManager.class);

    /**
     * 数据包分发器
     */
    private PacketDispatcher packetDispatcher = PacketDispatcher.getInstance();

    private IConfigService configService = ConfigServiceProvider.getConfigService();

    private PacketLogger packetLogger = PacketLogger.getInstance();

    /**
     * 管理的playerConnection信息
     */
    private ConcurrentHashMap<InetSocketAddress, PlayerConnection> playerConnections = null;

    /**
     * 存储guid为索引的连接信息
     */
    private ArrayList<List<PlayerConnection>> playerConnectionArrays = new ArrayList<>();

    /**
     * 网络相关接口，tcp|udp
     */
    private Set<NetInterface> netInterfaceSets = null;

    private NetworkInfo networkInfo;

    private boolean isNetworkRunning = false;

    /**
     * 网络模块是否处于严重事故中
     */
    private boolean isNetworkInAccident = false;

    public boolean isNetworkInAccident() {
        return isNetworkInAccident;
    }

    private ExecutorService receiveExecutorService = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder()
            .setNameFormat("network_receive_thread")
            .setDaemon(true)
            .build());

    private ExecutorService receiveFixedExecutorService = null;

    public NetworkManager() {
        playerConnections = new ConcurrentHashMap<>();
        netInterfaceSets = new HashSet<>();
    }

    /**
     * 启动网络服务
     */
    public void start() {
        this.startServer();
    }

    /**
     * 对外提供接口，发送消息
     *
     * @param address
     * @param dataPacket
     * @return
     */
    @Override
    public boolean sendMessage(InetSocketAddress address, DataPacket dataPacket) {
        if (!this.isNetworkRunning || address == null || dataPacket == null) {
            return false;
        }

        if (address instanceof PlayerConnection) {
            PlayerConnection playerConnection = (PlayerConnection) address;
            // 第一次发送
            if (dataPacket.group == -1) {
                dataPacket.group = playerConnection.getGroup();
                playerConnection.sendDataPacket(dataPacket);
            } else if (playerConnection.getGroup() == dataPacket.group) {
                // 同一组，不必要复制
                playerConnection.sendDataPacket(dataPacket);
            } else {
                // 不同组，进行复制
                dataPacket = dataPacket.clone();
                dataPacket.group = playerConnection.getGroup();
                playerConnection.sendDataPacket(dataPacket);
            }
            return true;
        }

        return false;
    }

    /**
     * 对外提供接口，发送消息
     *
     * @param address
     * @param dataPackets
     * @return
     */
    @Override
    public boolean sendMessage(InetSocketAddress address, List<DataPacket> dataPackets) {
        if (!this.isNetworkRunning || address == null || dataPackets == null || dataPackets.isEmpty()) {
            return false;
        }
        for (DataPacket dataPacket : dataPackets) {
            if (!this.sendMessage(address, dataPacket)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean sendMessage(InetSocketAddress address, DataPacket[] dataPackets) {
        if (!this.isNetworkRunning || address == null || dataPackets == null) {
            return false;
        }
        for (DataPacket dataPacket : dataPackets) {
            if (!this.sendMessage(address, dataPacket)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 对外提供接口，发送消息
     * 批量发送建议采用该方式
     *
     * @param addresses
     * @param dataPacket
     * @return
     */
    @Override
    public boolean broadcastMessage(ArrayList<InetSocketAddress> addresses, DataPacket dataPacket) {
        if (!this.isNetworkRunning || addresses == null || dataPacket == null || addresses.isEmpty()) {
            return false;
        }
        // 先排序
        addresses.sort(new Comparator<InetSocketAddress>() {
            @Override
            public int compare(InetSocketAddress address1, InetSocketAddress address2) {
                if (address1 == null) {
                    return -1;
                } else if (address2 == null) {
                    return 1;
                }
                if (address1 instanceof PlayerConnection && address2 instanceof PlayerConnection) {
                    PlayerConnection playerConnection1 = (PlayerConnection) address1;
                    PlayerConnection playerConnection2 = (PlayerConnection) address2;
                    if (playerConnection1.getGroup() == playerConnection2.getGroup()) {
                        return 0;
                    } else {
                        return playerConnection1.getGroup() > playerConnection2.getGroup() ? 1 : -1;
                    }
                }
                return 0;
            }
        });

        for (InetSocketAddress address : addresses) {
            this.sendMessage(address, dataPacket);
        }

        return true;
    }

    /**
     * 对外提供接口，发送消息
     * 批量发送建议使用该接口
     *
     * @param addresses
     * @param dataPackets
     * @return
     */
    @Override
    public boolean broadcastMessage(ArrayList<InetSocketAddress> addresses, List<DataPacket> dataPackets) {
        if (!this.isNetworkRunning || addresses == null || dataPackets == null || dataPackets.isEmpty() || addresses.isEmpty()) {
            return false;
        }

        for (DataPacket dataPacket : dataPackets) {
            this.broadcastMessage(addresses, dataPacket);
        }
        return true;
    }

    /**
     * 设置玩家的协议版本
     * 在收到登录包后，需要回调此接口
     *
     * @param address
     * @param version
     */
    public void setProtocolVersion(InetSocketAddress address, int version) {
        if (address == null) {
            return;
        }
        if (address instanceof PlayerConnection) {
            ((PlayerConnection) address).setProtocolVersion(version);
        }
    }

    /**
     * 启动服务
     */
    private void startServer() {
        this.networkInfo = this.configService.loadConfigOrSaveDefault(NetworkInfo.class);

        if (isPortUsing(this.networkInfo.getIp(), this.networkInfo.getPort())) {
            logger.error(String.format("host:%s port:%s is occupied", this.networkInfo.getIp(), this.networkInfo.getPort()));
            System.exit(1);
        }

        //检测p配置
        this.checkNetworkInfo(this.networkInfo);

        if (this.isNetworkRunning) {
            logger.error("the network service is running!");
            return;
        }

        // 分组playerConnections
        this.receiveFixedExecutorService = Executors.newFixedThreadPool(networkInfo.getGroup(),
                new ThreadFactoryBuilder()
                        .setNameFormat("network_send_thread_%d")
                        .setDaemon(true)
                        .build());

        for (int index = 0; index < networkInfo.getGroup(); index++) {
            List<PlayerConnection> connections = new CopyOnWriteArrayList<>();
            this.playerConnectionArrays.add(connections);
        }

        this.isNetworkRunning = true;
        if (this.networkInfo.isNeedTcp()) {
            DecoServer decoServer = new DecoServer(this.networkInfo.getIp(), this.networkInfo.getPort(),
                    this.networkInfo.getMaxConnection(), new Identifier(this.networkInfo.getServerName()));
            TcpPackageHandler tcpPackageHandler = new TcpPackageHandler(decoServer, this);
            DispatcherDecoListener dispatcherDecoListener = new DispatcherDecoListener(tcpPackageHandler);
            decoServer.addListener(dispatcherDecoListener);
            decoServer.startThreaded();
            logger.info("listen tcp port: {}:{}", this.networkInfo.getIp(), this.networkInfo.getPort());
        }

        if (this.networkInfo.isNeedUdp()) {
            RakNetServer rakNetServer = new RakNetServer(
                    this.networkInfo.getIp(),
                    this.networkInfo.getPort(),
                    this.networkInfo.getMaxConnection(),
                    this.networkInfo.getMaxMtu(),
                    new MinecraftIdentifier(
                            this.networkInfo.getServerName(),
                            361,
                            "1.16.0",
                            0,
                            this.networkInfo.getMaxConnection(),
                            new Random().nextLong() /* Server broadcast ID */,
                            "World",
                            "Survival"));
            UdpPackageHandler udpPackageHandler = new UdpPackageHandler(rakNetServer, this);
            DispatcherRaknetListener dispatcherRaknetListener = new DispatcherRaknetListener(udpPackageHandler);
            rakNetServer.addListener(dispatcherRaknetListener);
            rakNetServer.startThreaded();
            logger.info("listen udp port: {}:{}", this.networkInfo.getIp(), this.networkInfo.getPort());
        }

        //配置日志
        this.packetLogger.setEnableDetailLog(this.networkInfo.isEnableDetailLog());

        this.receiveExecutorService.submit(NetworkManager.this::processInterfaces);

        // 分组处理发包
        for (int index = 0; index < this.networkInfo.getGroup(); index++) {
            final int rIndex = index;
            this.receiveFixedExecutorService.submit(() -> {
                processSendQueueV2(rIndex);
            });
        }
        this.receiveFixedExecutorService.submit(() -> {
            watchSendQueueThread();
        });
    }

    /**
     * 根据IP和端口号，查询其是否被占用
     *
     * @param host IP
     * @param port 端口号
     * @return 如果被占用，返回true；否则返回false
     * @throws Exception IP地址不通或错误，则会抛出此异常
     */
    private boolean isPortUsing(String host, int port) {
        boolean flag = false;
        InetAddress theAddress = null;
        try {
            theAddress = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            logger.error(String.format("host:%s is error!", e));
            System.exit(1);
        }

        try {
            Socket socket = new Socket(theAddress, port);
            flag = true;
        } catch (IOException e) {
            //如果所测试端口号没有被占用，那么会抛出异常，这里利用这个机制来判断
            //所以，这里在捕获异常后，什么也不用做
        }
        return flag;

    }

    /**
     * 监听发包线程池
     */
    private void watchSendQueueThread() {
        logger.error("the receiveFixedExecutorService task count is below 2!");
    }

    /**
     * 服务端主动关闭某个用户的网络连接
     * 业务环境的清理，可以在回调中处理
     *
     * @param address
     * @param reason
     */
    public void removePlayer(InetSocketAddress address, String reason) {
        if (netInterfaceSets.isEmpty() || address == null) {
            return;
        }
        logger.info(String.format("the server active remove entity[%s], the reason[%s]", address, reason));
        for (NetInterface netInterface : this.netInterfaceSets) {
            try {
                netInterface.removePlayer(address, reason);
            } catch (Exception e) {
                logger.error(String.format("removePlayer[%s] failed!", address), e);
            }
        }
        return;
    }

    /**
     * 注册网络相关接口
     *
     * @param netInterface
     */
    public void registerNetInterface(NetInterface netInterface) {
        if (netInterface == null) {
            return;
        }
        if (netInterfaceSets.contains(netInterface)) {
            logger.warn(String.format("register netInterface[%s] repeated!", netInterface.toString()));
            return;
        }
        this.netInterfaceSets.add(netInterface);
    }

    /**
     * 反注册相关接口
     *
     * @param netInterface
     */
    public void unRegisterInterface(NetInterface netInterface) {
        if (netInterface == null) {
            return;
        }
        if (!netInterfaceSets.contains(netInterface)) {
            logger.warn(String.format("unRegisterInterface netInterface[%s] is not existed!!", netInterface.toString()));
            return;
        }
        this.netInterfaceSets.remove(netInterface);
    }

    /**
     * 网络层通知，session建立
     *
     * @param advancedSourceInterface
     * @param address
     * @param clientId
     */
    public Object onCreateSession(AdvancedNetInterface advancedSourceInterface, InetSocketAddress address, long clientId) {
        if (playerConnections.containsKey(address)) {
            logger.error("networkManager has contained playerConnection[%s]", address);
            return null;
        }

        PlayerConnection playerConnection = new PlayerConnection(advancedSourceInterface, address, clientId, this.networkInfo);

        this.addPlayerConnection(playerConnection, address);

        // 注册session建立事件
        OnCreateSessionPacket onCreateSessionPacket = new OnCreateSessionPacket();
        onCreateSessionPacket.setClientAddress(playerConnection);
        onCreateSessionPacket.setClientId(clientId);
        packetDispatcher.dispatchEvent(address, onCreateSessionPacket);
        return playerConnection;
    }

    /**
     * 网络层通知，session关闭
     *
     * @param address
     * @param reason
     */
    public void onCloseSession(InetSocketAddress address, String reason) {
        PlayerConnection playerConnection = playerConnections.get(address);
        if (playerConnection == null) {
            logger.error(String.format("when onCloseSession, networkManager has not contained playerConnection[%s]", address));
            return;
        }
        this.removePlayerConnection(playerConnection, address);
        playerConnection.close();
        // 注册session destroy事件
        OnDestroySessionPacket onDestroySessionPacket = new OnDestroySessionPacket();
        onDestroySessionPacket.setReason(reason);
        packetDispatcher.dispatchEvent(address, onDestroySessionPacket);
    }

    /**
     * 添加connection
     *
     * @param playerConnection
     * @param address
     */
    private void addPlayerConnection(PlayerConnection playerConnection, InetSocketAddress address) {
        this.playerConnections.put(address, playerConnection);
        int index = playerConnection.getGroup();
        List<PlayerConnection> rPlayerConnections = this.playerConnectionArrays.get(index);
        rPlayerConnections.add(playerConnection);
    }

    /**
     * 去除connection
     *
     * @param playerConnection
     */
    private void removePlayerConnection(PlayerConnection playerConnection, InetSocketAddress address) {
        this.playerConnections.remove(address);
        int index = playerConnection.getGroup();
        List<PlayerConnection> rPlayerConnections = this.playerConnectionArrays.get(index);
        rPlayerConnections.remove(playerConnection);
    }

    /**
     * 处理网络收发包
     */
    public void processInterfaces() {
        if (netInterfaceSets.isEmpty()) {
            return;
        }

        logger.debug("networkManager processInterfaces begin!");
        while (this.isNetworkRunning) {
            try {
                for (NetInterface netInterface : this.netInterfaceSets) {
                    try {
                        netInterface.process();
                    } catch (Exception e) {
                        logger.error("processInterfaces failed!", e);
                    }
                }

                Thread.sleep(0, 1);
            } catch (Exception e) {
                logger.error("processInterfaces failed!", e);
            }
        }

        logger.info("networkManager processInterfaces end!");
    }

    /**
     * 处理发包V2
     *
     * @param index
     */
    private void processSendQueueV2(int index) {
        logger.debug("networkManager processSendQueueV2({}) begin, ThreadId[{}]", index, Thread.currentThread().getId());
        List<PlayerConnection> playerConnectionIters = this.playerConnectionArrays.get(index);
        if (playerConnectionIters == null) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, "playerConnectionIters参数错误!");
        }
        while (this.isNetworkRunning) {
            try {
                if (playerConnectionIters.isEmpty()) {
                    Thread.sleep(20);
                }

                for (PlayerConnection playerConnection : playerConnectionIters) {
                    if (playerConnection == null) {
                        continue;
                    }
                    playerConnection.handleSendQueue();
                }

                Thread.sleep(0, 1);
            } catch (Exception e) {
                logger.error("processSendQueueV2 failed!", e);
            } catch (Throwable e) {
                logger.error("processSendQueueV2 failed!", e);
                isNetworkInAccident = true;
            }
        }
        logger.info("networkManager processSendQueueV2({}) end!", index);
    }

    /**
     * 处理sendQueue
     */
    @Deprecated
    private void processSendQueue() {
        logger.info("networkManager processSendQueue begin!");
        while (this.isNetworkRunning) {
            try {
                if (playerConnections.isEmpty()) {
                    Thread.sleep(20);
                }

                for (Map.Entry<InetSocketAddress, PlayerConnection> entryPlayerConnection : playerConnections.entrySet()) {
                    if (entryPlayerConnection == null) {
                        continue;
                    }
                    PlayerConnection playerConnection = entryPlayerConnection.getValue();
                    if (playerConnection == null) {
                        continue;
                    }
                    playerConnection.handleSendQueue();
                }

                Thread.sleep(0, 1);
            } catch (Exception e) {
                logger.error("processSendQueue failed!", e);
            }
        }
        logger.info("networkManager processSendQueue end!");
    }

    /**
     * 判断是否已经建立连接
     *
     * @param address
     * @return
     */
    public boolean hasPlayerConnection(InetSocketAddress address) {
        if (!(address instanceof PlayerConnection)) {
            return false;
        }
        return true;
    }

    /**
     * 获取PlayerConnection对象
     *
     * @param address
     * @return
     */
    public PlayerConnection getPlayerConnection(InetSocketAddress address) {
        if (!(address instanceof PlayerConnection)) {
            return null;
        }
        return (PlayerConnection) address;
    }

    /**
     * ban ip
     *
     * @param address
     */
    @Override
    public void blockAddress(String address) {
        this.blockAddress(address, 300);
    }

    /**
     * ban ip
     *
     * @param address
     * @param timeout
     */
    @Override
    public void blockAddress(String address, int timeout) {
        if (netInterfaceSets == null || netInterfaceSets.isEmpty() || StringUtils.isEmpty(address)) {
            return;
        }
        for (NetInterface netInterface : this.netInterfaceSets) {
            try {
                netInterface.blockAddress(address, timeout);
            } catch (Exception e) {
                logger.error(String.format("blockAddress[%s] failed!", address), e);
            }
        }
        return;
    }

    /**
     * 解ban ip
     *
     * @param address
     */
    @Override
    public void unblockAddress(String address) {
        if (netInterfaceSets == null || netInterfaceSets.isEmpty() || StringUtils.isEmpty(address)) {
            return;
        }
        for (NetInterface netInterface : this.netInterfaceSets) {
            try {
                netInterface.unblockAddress(address);
            } catch (Exception e) {
                logger.error(String.format("unblockAddress[%s] failed!", address), e);
            }
        }
    }

    /**
     * 主动关闭服务器
     */
    public void shutdown() {
        this.isNetworkRunning = false;
        if (netInterfaceSets != null && !netInterfaceSets.isEmpty()) {
            for (NetInterface netInterface : this.netInterfaceSets) {
                try {
                    netInterface.shutdown();
                } catch (Exception e) {
                    logger.error("shutdown failed!", e);
                }
            }
        }

        if (playerConnections != null && !playerConnections.isEmpty()) {
            for (Map.Entry<InetSocketAddress, PlayerConnection> entryPlayerConnection : playerConnections.entrySet()) {
                if (entryPlayerConnection == null) {
                    continue;
                }
                PlayerConnection playerConnection = entryPlayerConnection.getValue();
                if (playerConnection == null) {
                    continue;
                }
                playerConnection.clear();
            }
        }
        this.playerConnectionArrays.clear();
        this.playerConnectionArrays.clear();

        // 关闭线程
        this.receiveFixedExecutorService.shutdown();
        this.receiveExecutorService.shutdown();
    }

    /**
     * 检测网络参数
     */
    private void checkNetworkInfo(NetworkInfo networkInfo) {
        if (networkInfo == null) {
            throw new ProphetException(ErrorCode.CONFIG_ERROR, "Config 加载失败！");
        }
        if (StringUtils.isEmpty(networkInfo.getIp())) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, "ip不能为空！");
        }
        if (StringUtils.isEmpty(networkInfo.getServerName())) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, "服务器名称不能为空！");
        }
        if (networkInfo.getMaxConnection() <= 0) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, "允许最大连接数不允许小于零！");
        }
        if (networkInfo.getGroup() <= 0) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, "分组数目必须大于零！");
        }
        if (networkInfo.isNeedUdp()) {
            if (networkInfo.getMaxMtu() <= 0) {
                throw new ProphetException(ErrorCode.PARAM_ERROR, "mtu不允许小于零！");
            }
        }
    }

    @Override
    public NetworkInfo getNetworkInfo() {
        return networkInfo;
    }
}
