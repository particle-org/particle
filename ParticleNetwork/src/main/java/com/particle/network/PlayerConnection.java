package com.particle.network;

import com.particle.event.dispatcher.PacketDispatcher;
import com.particle.model.network.NetworkInfo;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.PacketBuffer;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.BatchPacket;
import com.particle.network.handler.AbstractPacketHandler;
import com.particle.network.handler.v274.BatchPacketHandler;
import com.particle.network.monitor.PacketLogger;
import com.particle.util.deflater.DeflaterTool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayerConnection extends InetSocketAddress {

    private static Logger logger = LoggerFactory.getLogger(PlayerConnection.class);

    private PlayerConnectionState playerConnectionState;

    /**
     * 客户端session的guid
     */
    private long clientGuid;

    /**
     * 所属哪个组
     */
    private int group;

    /**
     * 网络相关的接口
     */
    private NetInterface netInterface;

    private PacketManager packetManager = PacketManager.getInstance();

    private PacketLogger packetLogger = PacketLogger.getInstance();

    private BatchPacketHandler batchPacketHandler = new BatchPacketHandler();

    /**
     * mc客户端协议版本
     */
    private int protocolVersion = AbstractPacketHandler.DEFAULT_VERSION;

    /**
     * 业务需要发送的数据包缓存
     */
    private BlockingQueue<DataPacket> sendQueue;


    private PacketDispatcher packetDispatcher = PacketDispatcher.getInstance();

    private boolean closed = false;

    private boolean alerted = false;


    protected PlayerConnection(
            NetInterface netInterface,
            InetSocketAddress clientAddress,
            long clientGuid,
            NetworkInfo networkInfo) {
        super(clientAddress.getAddress(), clientAddress.getPort());

        this.netInterface = netInterface;
        this.clientGuid = clientGuid;
        this.sendQueue = new LinkedBlockingQueue<>();

        this.playerConnectionState = PlayerConnectionState.HANDSHAKE;

        clientGuid = Math.abs(clientGuid);
        int maxGroup = networkInfo.getGroup();
        group = (int) (clientGuid % maxGroup);
    }

    /**
     * 获取guid
     *
     * @return
     */
    public long getClientGuid() {
        return clientGuid;
    }

    /**
     * 获取所在的组
     *
     * @return
     */
    public int getGroup() {
        return group;
    }

    public void close() {
        closed = true;
    }

    /**
     * 协议版本
     *
     * @return
     */
    public int getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * 协议版本
     *
     * @param protocolVersion
     */
    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    /**
     * 直接发送消息包
     *
     * @param dataPacket
     * @return
     */
    protected boolean sendDirectDataPacket(DataPacket dataPacket) {
        if (dataPacket == null) {
            return false;
        }
        if (dataPacket.pid() == ProtocolInfo.COMPRESS_PACKET_HEAD) {
            logger.error("cant direct send batchPacket!");
            return false;
        }
        List<DataPacket> directPackets = Arrays.asList(dataPacket);
        BatchPacket batchPacket = this.toBatchPackets(directPackets);
        this.netInterface.sendPacket(this, batchPacket);
        return true;
    }

    /**
     * 供业务调用，发送消息包
     * 放到待发送队列中
     *
     * @param dataPacket
     * @return
     */
    protected boolean sendDataPacket(DataPacket dataPacket) {
        if (dataPacket == null) {
            return false;
        }

        if (this.closed) {
            if (!this.alerted) {
                alerted = true;
                sendQueue.clear();
                if (logger.isDebugEnabled()) {
                    logger.debug("Send packet to closed connection", new RuntimeException());
                }
            }

            return false;
        }

        if (dataPacket.pid() == ProtocolInfo.COMPRESS_PACKET_HEAD) {
            logger.error("cant direct send batchPacket!");
            return false;
        }
        if (!this.sendQueue.offer(dataPacket)) {
            logger.error("sendDataPacket failed，the blockQueue maybe full!");
            return false;
        }

        return true;
    }

    /**
     * 发送消息
     *
     * @param dataPackets
     * @return
     */
    public boolean sendDataPacket(List<DataPacket> dataPackets) {
        if (dataPackets == null || dataPackets.isEmpty()) {
            return false;
        }
        Iterator<DataPacket> itPackets = dataPackets.iterator();
        while (itPackets.hasNext()) {
            DataPacket dataPacket = itPackets.next();
            if (dataPacket.pid() == ProtocolInfo.COMPRESS_PACKET_HEAD) {
                logger.error("cant direct send batchPacket!");
                continue;
            }
            if (!this.sendQueue.offer(dataPacket)) {
                logger.error("sendDataPacket failed，the blockQueue maybe full!");
            }
        }
        return true;
    }

    /**
     * 处理包入口
     *
     * @param packageBuffer
     * @return
     */
    public boolean handleSourceDataPacket(byte[] packageBuffer) {
        if (packageBuffer == null || packageBuffer.length <= 0) {
            return false;
        }
        BatchPacket batchPacket = new BatchPacket();
        batchPacket.setPayload(Unpooled.wrappedBuffer(packageBuffer));

        List<DataPacket> dataPacketLists = this.processBatchPacket(batchPacket);
        if (dataPacketLists == null || dataPacketLists.isEmpty()) {
            return false;
        }
        for (DataPacket dataPacket : dataPacketLists) {
            this.packetLogger.loggerInputPacket(this, dataPacket);

            packetDispatcher.dispatchEvent(this, dataPacket);
        }
        return true;
    }

    /**
     * 后台任务处理sendQueue的数据包接口
     *
     * @return
     */
    public boolean handleSendQueue() {
        if (this.sendQueue.isEmpty()) {
            return false;
        }

        List<DataPacket> drainedQueue = new ArrayList<>();

        // 每次最大拉十个， 不然会出StackOverflowError异常
        this.sendQueue.drainTo(drainedQueue, 10);

        BatchPacket batchPacket = this.toBatchPackets(drainedQueue);
        if (batchPacket != null) {
            this.netInterface.sendPacket(this, batchPacket);
        }
        return true;
    }

    /**
     * DataPacket list 转成batchPacket
     *
     * @param dataPackets
     * @return
     */
    private BatchPacket toBatchPackets(List<DataPacket> dataPackets) {
        if (dataPackets.isEmpty()) {
            return null;
        }

        //合并数据包
        PacketBuffer packetBuffer = new PacketBuffer();
        for (DataPacket dataPacket : dataPackets) {
            if (!dataPacket.isEncoded) {
                AbstractPacketHandler packetHandler = packetManager.getHandler(dataPacket.pid() & 0x1FF, protocolVersion);

                if (packetHandler != null) {
                    packetHandler.encode(dataPacket, protocolVersion);
                } else {
                    continue;
                }

            } else if (dataPacket.encodeVersion != protocolVersion) {
                dataPacket.reset();
                AbstractPacketHandler packetHandler = packetManager.getHandler(dataPacket.pid() & 0x1FF, protocolVersion);

                if (packetHandler != null) {
                    packetHandler.encode(dataPacket, protocolVersion);
                } else {
                    continue;
                }
            }

            this.packetLogger.loggerOutputPacket(this, dataPacket);

            int length = dataPacket.readableLength();

            // 抓包显示，这里小包非常多，wapper可能导致性能下降,
            packetBuffer.writeUnsignedVarInt(length);
            // 这里的Buf不能复用，否则共享同一个包发包时会导致后面的包读不到
            packetBuffer.writeBytes(dataPacket.subSlice(length));
        }

        //压缩
        ByteBuf compressedData = null;
        try {
            // 1.16 改了壓縮頭
            boolean isNeedHead = false;
            if (protocolVersion < AbstractPacketHandler.VERSION_1_16) {
                isNeedHead = true;
            }
            compressedData = DeflaterTool.compress(packetBuffer.readAll(), isNeedHead);
        } catch (IOException e) {
            logger.error("compress failed!", e);
            return null;
        }

        if (compressedData != null) {
            BatchPacket batchPacket = new BatchPacket();
            batchPacket.setPayload(compressedData);
            if (!batchPacket.isEncoded) {
                batchPacketHandler.encode(batchPacket, protocolVersion);
            }
            return batchPacket;
        }

        return null;
    }

    /**
     * 清理数据
     */
    public void clear() {
        if (this.sendQueue != null) {
            this.sendQueue.clear();
        }
    }

    /**
     * 解压BatchPacket，并分割成各个小packet
     *
     * @param packet
     * @return
     */
    private List<DataPacket> processBatchPacket(BatchPacket packet) {
        ByteBuf buffer = packet.getPayload();
        byte[] byteBuffer = Arrays.copyOfRange(buffer.array(), 1, buffer.writerIndex());

        try {
            // 1.16 改了壓縮頭
            boolean isNeedHead = false;
            if (byteBuffer[0] == 120 && byteBuffer[1] == -38) {
                isNeedHead = true;
            }
            buffer = DeflaterTool.uncompress(byteBuffer, isNeedHead);
        } catch (IOException e) {
            logger.error("uncompress BatchPacket failed!", e);
            return new ArrayList<>();
        }

        try {
            PacketBuffer packetBuffer = new PacketBuffer(buffer);

            List<DataPacket> decodedPackets = new LinkedList<>();

            //检查是否还有数据
            while (packetBuffer.canRead()) {
                //读取一个数据包
                int length = (int) packetBuffer.readUnsignedVarLong();

                // 读取pid
                int readerIndex = packetBuffer.getReadIndex();
                int pid = packetBuffer.readUnsignedVarInt();
                packetBuffer.setReadIndex(readerIndex);

                ByteBuf byteBuf = packetBuffer.readSlice(length);

                DataPacket pk;
                if ((pk = this.packetManager.getPacket(pid)) != null) {
                    pk.setBuffer(byteBuf);

                    AbstractPacketHandler packetHandler = this.packetManager.getHandler(pk.pid() & 0x1FF, protocolVersion);
                    if (packetHandler != null) {
                        packetHandler.decode(pk, protocolVersion);
                    } else {
                        logger.error("Miss match packet {}", pk.pid());
                        continue;
                    }
                    // 将byteBuf重置，以允许将该包重新发送发送
                    pk.setReadIndex(0);
                    pk.isEncoded = true;
                    //缓存当前读取的数据包
                    decodedPackets.add(pk);
                } else {
                    logger.debug("Missing match packet {}", pid);
                }
            }

            return decodedPackets;
        } catch (Exception e) {
            logger.error("processBatchPacket failed! from {} version", this.getAddress().toString(), this.getProtocolVersion(), e);
            return new ArrayList<>();
        }
    }

    private String subStr(byte[] array) {
        String s = Arrays.toString(array);
        if (s.length() > 20) {
            return s.substring(0, 20);
        } else {
            return s;
        }
    }
}
