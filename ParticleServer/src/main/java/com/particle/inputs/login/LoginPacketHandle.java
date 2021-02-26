package com.particle.inputs.login;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.attribute.identified.EntityNameService;
import com.particle.game.entity.attribute.identified.UUIDModule;
import com.particle.game.player.PlayerSkinService;
import com.particle.game.player.auth.handle.DirectNeteaseLoginDecoder;
import com.particle.game.player.auth.handle.DirectNormalLoginHandle;
import com.particle.game.player.auth.handle.ProxyNeteaseLoginDecoder;
import com.particle.game.player.auth.handle.ProxyNormalLoginHandle;
import com.particle.game.player.auth.model.ClientChainData;
import com.particle.game.server.Server;
import com.particle.game.utils.config.ServerConfigService;
import com.particle.game.utils.logger.PlayerLogDataCacheModule;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.entity.model.skin.PlayerSkinData;
import com.particle.model.network.NetworkInfo;
import com.particle.model.network.NetworkService;
import com.particle.model.network.packets.data.LoginPacket;
import com.particle.model.player.BasicModelSkin;
import com.particle.model.player.Player;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LoginPacketHandle extends PlayerPacketHandle<LoginPacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPacketHandle.class);

    private static final ECSModuleHandler<UUIDModule> UUID_MODULE_HANDLER = ECSModuleHandler.buildHandler(UUIDModule.class);
    private static final ECSModuleHandler<PlayerLogDataCacheModule> PLAYER_LOG_DATA_CACHE_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlayerLogDataCacheModule.class);

    @Inject
    private Server server;

    @Inject
    private PlayerSkinService playerSkinService;
    @Inject
    private EntityNameService entityNameService;

    @Inject
    private ServerConfigService serverConfigService;

    @Inject
    private NetworkService networkService;

    @Override
    public int getTargetPacketID() {
        return 1;
    }

    @Override
    protected void handlePacket(Player player, LoginPacket loginPacket) {
        ClientChainData chainData = null;
        try {
            if (this.serverConfigService.isEnableNeteaseAuth()) {
                if (loginPacket.isInProxy()) {
                    chainData = ProxyNeteaseLoginDecoder.decode(loginPacket.getChainData(), loginPacket.getPlayerData(), loginPacket.getProtocol());
                } else {
                    chainData = DirectNeteaseLoginDecoder.decode(loginPacket.getChainData(), loginPacket.getPlayerData(), loginPacket.getProtocol());
                }
            } else {
                if (loginPacket.isInProxy()) {
                    chainData = ProxyNormalLoginHandle.decode(loginPacket.getChainData(), loginPacket.getPlayerData(), loginPacket.getProtocol());
                } else {
                    chainData = DirectNormalLoginHandle.decode(loginPacket.getChainData(), loginPacket.getPlayerData(), loginPacket.getProtocol());
                }
            }
        } catch (RuntimeException e) {
            LOGGER.info("Fail to parse player {} login information.", player.getIdentifiedStr(), e);
            chainData = null;
        }

        if (chainData == null || chainData.getUsername() == null || chainData.getClientUUID() == null) {
            server.close(player, "Login information illegal");

            LOGGER.info("Player {} fail because of illegal information.", player.getIdentifiedStr());
            return;
        }

        NetworkInfo networkInfo = this.networkService.getNetworkInfo();
        // 检测是否合法
        if (networkInfo.isOnlyObt() && !chainData.isChainValid()) {
            LOGGER.error("玩家非法客户端，被剔出：{}", chainData.toString());
            server.close(player, "Login information illegal");
            return;
        }

        LOGGER.debug("clientChainData: {}", chainData);

        PlayerLogDataCacheModule playerLogDataCacheModule = PLAYER_LOG_DATA_CACHE_MODULE_HANDLER.bindModule(player);
        playerLogDataCacheModule.setUuid(chainData.getClientUUID().toString());
        playerLogDataCacheModule.setRoleId(chainData.getXUID());
        playerLogDataCacheModule.setRoleName(chainData.getUsername());
        playerLogDataCacheModule.setUdid(chainData.getDeviceModel());
        playerLogDataCacheModule.setAddress(player.getClientAddress().getAddress().getHostAddress());
        playerLogDataCacheModule.setPort(player.getClientAddress().getPort());

        PlayerSkinData playerSkinData = chainData.getPlayerSkinData();
        boolean is4DSkin = playerSkinData.getSkinGeometryName() != null && playerSkinData.getSkinGeometryName().startsWith("geometry.netease_skin.");
        // 需要替换4D皮肤
        boolean replace4DSkin = is4DSkin && !this.serverConfigService.isAllow4DSkin();
        // 检测到不合法皮肤，直接替换默认皮肤
        boolean validSkin = playerSkinData.getSkinData() != null && !StringUtils.isEmpty(playerSkinData.getSkinGeometryName());
        if (replace4DSkin || !validSkin) {
            if (replace4DSkin) {
                LOGGER.info("玩家【{}】使用了4D皮肤[{}]", chainData.getUsername(), playerSkinData.getSkinGeometryName());
            }
            if (!validSkin) {
                LOGGER.info("玩家【{}】使用了非法皮肤[{}]", chainData.getUsername(), playerSkinData.getSkinGeometryName());
            }
            // 替换成基础steve模型
            BasicModelSkin basicModelSkin = playerSkinService.getBasicSteveSkin(false);
            playerSkinData.setSkinId(basicModelSkin.getSkinId());
            playerSkinData.setSkinName("");
            playerSkinData.setSkinData(basicModelSkin.getSkinData());
            playerSkinData.setSkinWidth(basicModelSkin.getSkinWidth());
            playerSkinData.setSkinHeight(basicModelSkin.getSkinHeight());
            playerSkinData.setCapeData(basicModelSkin.getCapeData());
            playerSkinData.setCapeWidth(basicModelSkin.getCapeWidth());
            playerSkinData.setCapeHeight(basicModelSkin.getCapeHeight());
            playerSkinData.setSkinGeometryName(basicModelSkin.getSkinGeometryName());
            playerSkinData.setSkinResourcePatch(String.format("{\"geometry\":{\"default\":\"%s\"},\"convert\":true}", basicModelSkin.getSkinGeometryName()));
            playerSkinData.setSkinGeometry(basicModelSkin.getSkinGeometry());
            playerSkinData.setPremiumSkin(basicModelSkin.isPremiumSkin());
            playerSkinData.setPersonSkin(basicModelSkin.isPersonSkin());
            playerSkinData.setPersonCapeOnClassicSkin(basicModelSkin.isPersonCapeOnClassicSkin());
            playerSkinData.setPlayerSkinAnimationData(basicModelSkin.getPlayerSkinAnimationData());
            playerSkinData.setSerializedAnimationData(basicModelSkin.getSerializedAnimationData());
            playerSkinData.setNewVersion(false);
        }

        player.setProtocolVersion(loginPacket.getProtocol());
        UUID_MODULE_HANDLER.getModule(player).setUuid(chainData.getClientUUID());
        UUID_MODULE_HANDLER.getModule(player).setRoleId(chainData.getUid());
        this.playerSkinService.initSkin(player, chainData.getPlayerSkinData());
        this.entityNameService.setEntityName(player, chainData.getUsername());

        player.setIdentifiedStr(chainData.getUsername());

        player.updateRuntimeIdByUuid(chainData.getClientUUID().getLeastSignificantBits());

        server.onPlayerLogin(player);
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}
