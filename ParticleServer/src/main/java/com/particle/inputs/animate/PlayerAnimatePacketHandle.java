package com.particle.inputs.animate;

import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.AnimationPacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlayerAnimatePacketHandle extends PlayerPacketHandle<AnimationPacket> {

    private static final Logger logger = LoggerFactory.getLogger(AnimationPacket.class);

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.ANIMATE_PACKET;
    }

    @Override
    protected void handlePacket(Player player, AnimationPacket packet) {
        //只有spawn状态的Player才进行相应的处理
        if (player.isSpawned()) {
            AnimationPacket animationPacket = new AnimationPacket();
            animationPacket.setData(packet.getData());
            animationPacket.setAction(packet.getAction());
            animationPacket.setEntityId(packet.getEntityId());

            this.broadcastServiceProxy.broadcast(player, animationPacket);
        }
    }

    protected boolean isRealTimeHandle() {
        return false;
    }
}
