package com.particle.model.network.packets;

import com.particle.route.jraknet.protocol.message.EncapsulatedPacket;

public abstract class DataPacket extends PacketBuffer implements Cloneable {

    public boolean isDecoded = false;
    public boolean isEncoded = false;
    /**
     * 存储上一次encode的版本号
     */
    public int encodeVersion = 0;

    public EncapsulatedPacket encapsulatedPacket;
    public byte reliability;
    public Integer orderIndex = null;
    public Integer orderChannel = null;

    /**
     * 所属哪个组
     */
    public int group = -1;

    public abstract int pid();

    @Override
    public DataPacket clone() {
        try {
            DataPacket dataPacket = (DataPacket) super.clone();
            // 重置buffer
            dataPacket.reset();
            dataPacket.isEncoded = false;
            return dataPacket;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
