package com.particle.api.chat;

import com.particle.model.player.Player;

public interface ChatServiceApi {

    void handleChatMessage(Player player, String message);

    void setCallback(IChatCallback callback);

    void sendChatMessage(Player player, String message, Player receiver);

    void broadcastChatMessage(Player sender, String message);

    String formatChatMessage(Player player, String message);
}
