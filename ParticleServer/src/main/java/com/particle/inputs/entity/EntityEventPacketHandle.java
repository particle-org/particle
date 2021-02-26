package com.particle.inputs.entity;

import com.particle.game.entity.attribute.explevel.ExperienceService;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.EntityEventPacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EntityEventPacketHandle extends PlayerPacketHandle<EntityEventPacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityEventPacketHandle.class);

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;

    @Inject
    private ExperienceService experienceService;

    @Override
    protected void handlePacket(Player player, EntityEventPacket packet) {
        switch (packet.getEventId()) {
            case EntityEventPacket.ADD_PLAYER_LEVELS:
                int levelChanged = packet.getData();

                // 玩家发来的数据只允许扣除，用于附魔、铁砧业务
                if (levelChanged < 0) {
                    int cost = -levelChanged;

                    this.experienceService.spendLevel(player, cost);
                } else {
                    LOGGER.warn("Player {} try to add level of {}!", player.getIdentifiedStr(), levelChanged);
                }
                break;
            case EntityEventPacket.FEED:
            case EntityEventPacket.DRINK_POTION:
                EntityEventPacket entityEventPacket = new EntityEventPacket();
                entityEventPacket.setEventId(packet.getEventId());
                entityEventPacket.setEntityRuntimeId(packet.getEntityRuntimeId());
                entityEventPacket.setData(packet.getData());

                this.broadcastServiceProxy.broadcast(player, entityEventPacket);

                break;
            default:
                break;
        }
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.ENTITY_EVENT_PACKET;
    }
}
