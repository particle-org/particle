package com.particle.inputs.inventory;

import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.MobArmorEquipmentPacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class MobArmorEquipmentPacketHandle extends PlayerPacketHandle<MobArmorEquipmentPacket> {

    private static final Logger logger = LoggerFactory.getLogger(MobArmorEquipmentPacketHandle.class);

    @Override
    protected void handlePacket(Player player, MobArmorEquipmentPacket packet) {
        logger.info("MobArmorEquipmentPacket:{}", packet);
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET;
    }
}
