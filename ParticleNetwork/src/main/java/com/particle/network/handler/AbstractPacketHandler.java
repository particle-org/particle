package com.particle.network.handler;

import com.particle.model.network.packets.DataPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPacketHandler<T extends DataPacket> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPacketHandler.class);

    public static final int DEFAULT_VERSION = 0;

    public static final int VERSION_1_5 = 274;

    public static final int VERSION_1_6 = 282;

    public static final int VERSION_1_7 = 291;

    public static final int VERSION_1_8 = 313;

    public static final int VERSION_1_9 = 332;

    public static final int VERSION_1_11 = 354;

    public static final int VERSION_1_12 = 361;

    public static final int VERSION_1_13 = 388;

    public static final int VERSION_1_14 = 389;

    public static final int VERSION_1_16 = 410;

    // 是否是中国版本
    // if the server is allowed joined by netease-version client, please set it true;
    public static final boolean isChsVersion = true;

    /**
     * 对外接口，decode消息包
     *
     * @param dataPacket
     * @param version
     */
    public void decode(T dataPacket, int version) {
        if (!dataPacket.isDecoded) {
            this.doDecodeHead(dataPacket, version);
            this.doDecode(dataPacket, version);
            dataPacket.isDecoded = true;
        }
    }

    /**
     * 对外接口，encode消息包
     *
     * @param dataPacket
     * @param version
     */
    public void encode(T dataPacket, int version) {
        if (dataPacket.isEncoded) {
            dataPacket.reset();
        }

        // 写入
        this.doEncodeHead(dataPacket, version);
        this.doEncode(dataPacket, version);
        dataPacket.isEncoded = true;
        dataPacket.encodeVersion = version;
    }

    protected void doDecodeHead(T dataPacket, int version) {
        // 读包头
        dataPacket.readUnsignedVarInt();
        if (version < VERSION_1_6) {
            dataPacket.readShort();
        }
    }

    protected void doEncodeHead(T dataPacket, int version) {
        //写入包头
        dataPacket.writeUnsignedVarInt(dataPacket.pid());
        if (version < VERSION_1_6) {
            dataPacket.writeShort((short) 0);
        }
    }


    /**
     * @param dataPacket
     * @param version
     */
    protected abstract void doDecode(T dataPacket, int version);

    /**
     * @param dataPacket
     * @param version
     */
    protected abstract void doEncode(T dataPacket, int version);
}
