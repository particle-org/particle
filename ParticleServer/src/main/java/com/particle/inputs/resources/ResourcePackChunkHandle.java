package com.particle.inputs.resources;

import com.particle.game.server.Server;
import com.particle.game.server.behaviors.BehaviorsPackManager;
import com.particle.game.server.resources.ResourcePackManager;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.ResourcePackChunkDataPacket;
import com.particle.model.network.packets.data.ResourcePackChunkRequestPacket;
import com.particle.model.player.Player;
import com.particle.model.resources.ResourcePackInfo;
import com.particle.network.NetworkManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ResourcePackChunkHandle extends PlayerPacketHandle<ResourcePackChunkRequestPacket> {
    @Inject
    private ResourcePackManager resourcePackManager;

    @Inject
    private BehaviorsPackManager behaviorsPackManager;

    @Inject
    private Server server;

    @Inject
    private NetworkManager networkManager;

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.RESOURCE_PACK_CHUNK_REQUEST_PACKET;
    }

    @Override
    protected void handlePacket(Player player, ResourcePackChunkRequestPacket packet) {
        ResourcePackChunkRequestPacket rpcRequestPacket = (ResourcePackChunkRequestPacket) packet;
        ResourcePackInfo resourcePackInfo = resourcePackManager.getResourcePackById(rpcRequestPacket.getId());
        byte[] chunkData = resourcePackManager.getPackData(rpcRequestPacket.getId(), rpcRequestPacket.getChunkIndex());

        // 若沒有材質包
        if (resourcePackInfo == null) {
            // 尋找行為包
            resourcePackInfo = behaviorsPackManager.getBehaviorPackById(rpcRequestPacket.getId());
            chunkData = behaviorsPackManager.getPackData(rpcRequestPacket.getId(), rpcRequestPacket.getChunkIndex());

            if (resourcePackInfo == null) {
                server.close(player, "请求下载資源包资源，请求的packId不存在");
                return;
            }
        }
        ResourcePackChunkDataPacket rpcDataPacket = new ResourcePackChunkDataPacket();
        rpcDataPacket.setId(rpcRequestPacket.getId());
        rpcDataPacket.setChunkIndex(rpcRequestPacket.getChunkIndex());
        rpcDataPacket.setOffset(rpcRequestPacket.getChunkIndex() * ResourcePackManager.CHUNK_RESOURCE_SCALE_SIZE);
        rpcDataPacket.setChunkData(chunkData);
        networkManager.sendMessage(player.getClientAddress(), rpcDataPacket);
        return;
    }
}
