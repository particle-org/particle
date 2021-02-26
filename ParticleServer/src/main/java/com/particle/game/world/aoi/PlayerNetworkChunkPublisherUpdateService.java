package com.particle.game.world.aoi;

import com.particle.api.inject.RequestStaticInject;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.scene.module.SubscriberModule;
import com.particle.model.network.packets.data.NetworkChunkPublisherUpdatePacket;
import com.particle.network.NetworkManager;

import javax.inject.Inject;

@RequestStaticInject
public class PlayerNetworkChunkPublisherUpdateService {

    @Inject
    private static NetworkManager networkManager;


    public static void updateRadius(TransformModule transformModule, SubscriberModule subscriberModule) {
        NetworkChunkPublisherUpdatePacket networkChunkPublisherUpdatePacket = new NetworkChunkPublisherUpdatePacket();
        networkChunkPublisherUpdatePacket.setBlockX(transformModule.getFloorX());
        networkChunkPublisherUpdatePacket.setBlockY(transformModule.getFloorY());
        networkChunkPublisherUpdatePacket.setBlockZ(transformModule.getFloorZ());
        networkChunkPublisherUpdatePacket.setNewRadiusForView(subscriberModule.getUnloadRadius() * 16);
        networkManager.sendMessage(subscriberModule.getAddress(), networkChunkPublisherUpdatePacket);
    }

}
