package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class HurtArmorPacket extends DataPacket {
    private int cause;
    private int damage;

    @Override
    public int pid() {
        return ProtocolInfo.HURT_ARMOR_PACKET;
    }

    public int getCause() {
        return cause;
    }

    public void setCause(int cause) {
        this.cause = cause;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
