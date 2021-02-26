package com.particle.inputs.resources;

import com.particle.game.player.PlayerService;
import com.particle.game.server.Server;
import com.particle.game.server.behaviors.BehaviorsPackManager;
import com.particle.game.server.resources.ResourcePackManager;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.ResourcePackClientResponsePacket;
import com.particle.model.network.packets.data.ResourcePackDataInfoPacket;
import com.particle.model.network.packets.data.ResourcePackStackPacket;
import com.particle.model.player.Player;
import com.particle.model.resources.ResourcePackInfo;
import com.particle.model.resources.ResourcePackType;
import com.particle.network.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ResourcePackRespHandle extends PlayerPacketHandle<ResourcePackClientResponsePacket> {

    @Inject
    private ResourcePackManager resourcePackManager;

    @Inject
    private BehaviorsPackManager behaviorsPackManager;

    @Inject
    private Server server;
    @Inject
    private PlayerService playerService;

    @Inject
    private NetworkManager networkManager;

    private static final Logger logger = LoggerFactory.getLogger(ResourcePackRespHandle.class);

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET;
    }

    /**
     * kick entity
     * <p>
     * 业务层的环境清理，会回调给DestorySessionHandler处理
     */
    private void kickPlayer(Player player, String reason) {
        server.close(player, reason);
    }

    /**
     * 处理下载材质包请求
     */
    private void downloadingPack(Player player, ResourcePackClientResponsePacket rpcResponsePacket) {
        for (String id : rpcResponsePacket.getDownloadingPackIds()) {
            ResourcePackInfo resourcePackInfo;
            // add in version 1_6
            int idIndex = id.indexOf("_");
            if (idIndex > 0) {
                resourcePackInfo = resourcePackManager.getResourcePackById(id.substring(0, idIndex));
            } else {
                resourcePackInfo = resourcePackManager.getResourcePackById(id);
            }
            ResourcePackType resourcePackType = ResourcePackType.RESOURCES;

            // 沒有資源包
            if (resourcePackInfo == null) {
                // 去找行為包
                if (idIndex > 0) {
                    resourcePackInfo = behaviorsPackManager.getBehaviorPackById(id.substring(0, idIndex));
                } else {
                    resourcePackInfo = behaviorsPackManager.getBehaviorPackById(id);
                }
                resourcePackType = ResourcePackType.BEHAVIOR;

                // 若都沒有
                if (resourcePackInfo == null) {
                    this.kickPlayer(player, "下载資源包的请求，packId不对！");
                    return;
                }
            }

            ResourcePackDataInfoPacket resourcePackDataInfoPacket = new ResourcePackDataInfoPacket();
            resourcePackDataInfoPacket.setId(resourcePackInfo.getId());
            resourcePackDataInfoPacket.setChunkSize(ResourcePackManager.CHUNK_RESOURCE_SCALE_SIZE);
            resourcePackDataInfoPacket.setChunkCounts(resourcePackInfo.getMaxChunkIndex());
            resourcePackDataInfoPacket.setFileSize(resourcePackInfo.getSize());
            resourcePackDataInfoPacket.setFileHash(resourcePackInfo.getFileHash());
            resourcePackDataInfoPacket.setPackType(resourcePackType);
            networkManager.sendMessage(player.getClientAddress(), resourcePackDataInfoPacket);
        }
    }

    /**
     * 处理下载材质包完成的请求
     */
    private void downloadFinished(Player player, ResourcePackClientResponsePacket rpcResponsePacket) {
        ResourcePackStackPacket resourcePackStackPacket = new ResourcePackStackPacket();
        // TODO 需要根据相关配置进行设置true or false
        resourcePackStackPacket.setTextureRequired(true);

        resourcePackStackPacket.setTexturePacks(resourcePackManager.getResourcePackInfo());
        resourcePackStackPacket.setAddOnPacks(behaviorsPackManager.getBehaviorPackInfo());

        resourcePackStackPacket.setBaseGameVersion("");
        networkManager.sendMessage(player.getClientAddress(), resourcePackStackPacket);
    }

    /**
     * TODO
     * 处理客户端材质包加载完成请求
     * 可以加载玩家存档数据
     */
    private void resourceStackFinished(Player player, ResourcePackClientResponsePacket rpcResponsePacket) {
        this.playerService.initPlayerData(player);

        // 设置玩家模式
        //server.changePlayerGameMode(entity, GameMode.CREATIVE, false);
    }

    @Override
    protected void handlePacket(Player player, ResourcePackClientResponsePacket rpcResponsePacket) {
        switch (rpcResponsePacket.getResponse()) {
            case ResourcePackClientResponsePacket.CANCEL:
                this.kickPlayer(player, "与客户端材质包的协商被拒绝");
                break;
            case ResourcePackClientResponsePacket.DOWNLOADING:
                this.downloadingPack(player, rpcResponsePacket);
                break;
            case ResourcePackClientResponsePacket.DOWNLOADING_FINISHED:
                this.downloadFinished(player, rpcResponsePacket);
                break;
            case ResourcePackClientResponsePacket.RESOURCE_PACK_STACK_FINISHED:
                this.resourceStackFinished(player, rpcResponsePacket);
                break;
            default:
                logger.error("error response!");
                break;
        }
    }
}
