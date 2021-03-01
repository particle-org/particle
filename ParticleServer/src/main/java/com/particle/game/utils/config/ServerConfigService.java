package com.particle.game.utils.config;

import com.particle.api.utils.IServerConfigServiceApi;
import com.particle.game.player.chat.ChatConfig;
import com.particle.game.server.ServerConfig;
import com.particle.util.configer.ConfigServiceProvider;
import com.particle.util.configer.IConfigService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ServerConfigService implements IServerConfigServiceApi {

    private IConfigService configService = ConfigServiceProvider.getConfigService();

    private ChatConfig chatConfig;

    private ServerConfig serverConfig;

    @Inject
    public void init() {
        this.serverConfig = this.configService.loadConfigOrSaveDefault(ServerConfig.class);
        this.chatConfig = this.configService.loadConfigOrSaveDefault(ChatConfig.class);
    }

    public boolean isEnableNeteaseAuth() {
        return this.serverConfig.isEnableNeteaseAuth();
    }

    public boolean isPlayerSave() {
        return this.serverConfig.isPlayerSave();
    }

    public boolean isPlayerLoad() {
        return this.serverConfig.isPlayerLoad();
    }

    public boolean isSaveOldData() {
        return this.serverConfig.isSaveOldData();
    }

    public boolean isEnableSign() {
        return this.serverConfig.isEnableSign();
    }


    public String getOnlineCheckApi() {
        return this.chatConfig.getOnlineCheckApi();
    }

    public boolean isEnableOnlineCheck() {
        return this.chatConfig.isEnableOnlineCheck();
    }

    public String getIllegalMessage() {
        return this.chatConfig.getIllegalMessage();
    }

    public String getTooLongMessage() {
        return this.chatConfig.getTooLongMessage();
    }

    public boolean isAllow4DSkin() {
        return serverConfig.isAllow4DSkin();
    }

    public boolean isInventoryServerAuthoritative() {
        return serverConfig.isInventoryServerAuthoritative();
    }

    public boolean isMovementServerAuthoritative() {
        return serverConfig.isMovementServerAuthoritative();
    }

    @Override
    public String getVersion() {
        return this.serverConfig.getVersion();
    }
}
