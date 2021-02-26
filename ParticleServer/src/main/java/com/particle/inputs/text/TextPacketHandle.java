package com.particle.inputs.text;

import com.particle.game.player.chat.ChatService;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.TextPacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TextPacketHandle extends PlayerPacketHandle<TextPacket> {

    private static final Logger logger = LoggerFactory.getLogger(TextPacketHandle.class);

    @Inject
    private ChatService chatService;

    @Override
    protected void handlePacket(Player player, TextPacket packet) {
        int messageType = packet.getMessageType();
        if (messageType == TextPacket.ChatType) {
            String message = packet.getMessage().replace("§", "");

            this.chatService.handleChatMessage(player, message);
        }
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.TEXT_PACKET;
    }
}
