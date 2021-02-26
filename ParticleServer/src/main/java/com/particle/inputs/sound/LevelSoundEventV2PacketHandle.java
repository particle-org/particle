package com.particle.inputs.sound;

import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.LevelSoundEventV2Packet;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LevelSoundEventV2PacketHandle extends PlayerPacketHandle<LevelSoundEventV2Packet> {

    private static final Logger logger = LoggerFactory.getLogger(LevelSoundEventV2PacketHandle.class);

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;

    @Inject
    private PositionService positionService;

    @Override
    protected void handlePacket(Player player, LevelSoundEventV2Packet packet) {
        logger.info("player: {}, receive LevelSoundEventV2Packet : {}", player, packet);
        Vector3f position = positionService.getPosition(player);
        // 广播
        this.broadcastServiceProxy.broadcast(player.getLevel(), position, packet, player);
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.LEVEL_SOUND_EVENT_V2_PACKET;
    }
}
