package com.particle.game.player.chat;

import com.particle.api.chat.ChatServiceApi;
import com.particle.api.chat.IChatCallback;
import com.particle.game.entity.attribute.identified.EntityNameService;
import com.particle.game.server.Server;
import com.particle.game.ui.TextService;
import com.particle.game.utils.config.ServerConfigService;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChatService implements ChatServiceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatService.class);

    @Inject
    private Server server;

    @Inject
    private TextService textService;

    @Inject
    private EntityNameService entityNameService;

    @Inject
    private ServerConfigService serverConfigService;

    private IChatCallback defaultChatCallback = new DefaultChatCallback();

    @Inject
    public void init() {
    }

    @Override
    public void handleChatMessage(Player player, String message) {
        this.defaultChatCallback.handle(player, message);
    }

    @Override
    public void setCallback(IChatCallback callback) {
        this.defaultChatCallback = callback;
    }

    @Override
    public void sendChatMessage(Player player, String message, Player receiver) {
        this.textService.sendChatMessage(receiver, this.formatChatMessage(player, message));
    }

    @Override
    public void broadcastChatMessage(Player sender, String message) {
        String formatMessage = this.formatChatMessage(sender, message);

        this.server.getAllPlayers().forEach(player -> this.textService.sendChatMessage(player, formatMessage));
    }

    @Override
    public String formatChatMessage(Player player, String message) {
        return String.format("§f%s §7说 : §f%s", entityNameService.getDisplayEntityName(player), message);
    }

    private class DefaultChatCallback implements IChatCallback {
        @Override
        public void handle(Player player, String message) {
            if (message.length() > 23) {
                textService.sendChatMessage(player, serverConfigService.getTooLongMessage());
                return;
            }

            broadcastChatMessage(player, message);
        }
    }
}
