package com.particle.game.world.animation;

import com.particle.api.animation.InventoryAnimationServiceApi;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.network.packets.data.BlockEventPacket;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InventoryAnimationService implements InventoryAnimationServiceApi {
    @Inject
    private BroadcastServiceProxy broadcastServiceProxyApi;

    /**
     * 發送開箱包
     *
     * @param position
     */
    @Override
    public void sendOpenInventoryPacket(Level level, Vector3 position) {
        BlockEventPacket blockEventPacket = new BlockEventPacket();
        blockEventPacket.setPosition(position);
        blockEventPacket.setEventType(1);
        blockEventPacket.setEventValue(2);

        broadcastServiceProxyApi.broadcast(level, position, blockEventPacket);
    }

    /**
     * 發送關箱包
     *
     * @param position
     */
    @Override
    public void sendCloseInventoryPacket(Level level, Vector3 position) {
        BlockEventPacket blockEventPacket = new BlockEventPacket();
        blockEventPacket.setPosition(position);
        blockEventPacket.setEventType(1);
        blockEventPacket.setEventValue(0);

        broadcastServiceProxyApi.broadcast(level, position, blockEventPacket);
    }
}
