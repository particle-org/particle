package com.particle.api.ui;

import com.particle.model.level.Level;
import com.particle.model.player.Player;

public interface TextServiceAPI {

    /**
     * 发送原始消息
     *
     * @param player  玩家
     * @param message 消息
     */
    public void sendRawMessage(Player player, String message);

    /**
     * 给特定世界的玩家广播原始消息
     *
     * @param level   世界
     * @param message 消息
     */
    public void broadcastRawMessage(Level level, String message);

    /**
     * 给端内的所有玩家广播原始消息
     *
     * @param message 消息
     */
    public void broadcastRawMessage(String message);

    /**
     * 发送聊天消息
     *
     * @param player  玩家
     * @param message 消息
     */
    public void sendChatMessage(Player player, String message);

    /**
     * 给特定世界的玩家广播聊天消息
     *
     * @param level   世界
     * @param message 消息
     */
    public void broadcastChatMessage(Level level, String message);


    /**
     * 给端内的所有玩家广播聊天消息
     *
     * @param message
     */
    public void broadcastChatMessage(String message);

    /**
     * 发送翻译消息
     *
     * @param player  玩家
     * @param message 消息
     */
    public void sendTranslateMessage(Player player, String message);

    /**
     * 给特定世界的玩家广播翻译消息
     *
     * @param level   世界
     * @param message
     */
    public void broadcastTranslateMessage(Level level, String message);

    /**
     * 给端内的所有玩家广播翻译消息
     *
     * @param message 消息
     */
    public void broadcastTranslateMessage(String message);

    /**
     * 发送popup消息
     *
     * @param player  玩家
     * @param message 消息
     */
    public void sendPopupMessage(Player player, String message);

    /**
     * 给特定世界的玩家广播popup消息
     *
     * @param level   世界
     * @param message 消息
     */
    public void broadcastPopupMessage(Level level, String message);

    /**
     * 给端内的所有玩家广播popup消息
     *
     * @param message 消息
     */
    public void broadcastPopupMessage(String message);

    /**
     * 发送JukeboxPopup消息
     *
     * @param player  玩家
     * @param message 消息
     */
    public void sendJukePopupMessage(Player player, String message);

    /**
     * 给特定世界的玩家广播JukeboxPopup消息
     *
     * @param level   世界
     * @param message 消息
     */
    public void broadcastJukePopupMessage(Level level, String message);

    /**
     * 给端内的所有玩家广播JukeboxPopup消息
     *
     * @param message 消息
     */
    public void broadcastJukePopupMessage(String message);

    /**
     * 发送Tip消息
     *
     * @param player  玩家
     * @param message 消息
     */
    public void sendTipMessage(Player player, String message);

    /**
     * 给特定世界的玩家广播Tip消息
     *
     * @param level   世界
     * @param message 消息
     */
    public void broadcastTipMessage(Level level, String message);

    /**
     * 给端内的所有玩家广播Tip消息
     *
     * @param message 消息
     */
    public void broadcastTipMessage(String message);

    /**
     * 发送system消息
     *
     * @param player  玩家
     * @param message 消息
     */
    public void sendSystemMessage(Player player, String message);

    /**
     * 给特定世界的玩家广播system消息
     *
     * @param level   世界
     * @param message 消息
     */
    public void broadcastSystemMessage(Level level, String message);

    /**
     * 给端内的所有玩家广播system消息
     *
     * @param message 消息
     */
    public void broadcastSystemMessage(String message);

    /**
     * 发送Whisper消息
     *
     * @param player  玩家
     * @param message 消息
     */
    public void sendWhisperMessage(Player player, String message);

    /**
     * 给特定世界的玩家广播Whisper消息
     *
     * @param level   世界
     * @param message 消息
     */
    public void broadcastWhisperMessage(Level level, String message);

    /**
     * 给端内的所有玩家广播Whisper消息
     *
     * @param message 消息
     */
    public void broadcastWhisperMessage(String message);

    /**
     * 发送Announcement消息
     *
     * @param player  玩家
     * @param message 消息
     */
    public void sendAnnouncement(Player player, String message);

    /**
     * 给特定世界的玩家广播Announcement消息
     *
     * @param level   世界
     * @param message 消息
     */
    public void broadcastAnnouncement(Level level, String message);

    /**
     * 给端内的所有玩家广播Announcement消息
     *
     * @param message 消息
     */
    public void broadcastAnnouncement(String message);


    /**
     * 发送popup消息
     *
     * @param player
     * @param message
     */
    void sendPopupMessage(Player player, String message, int stayTick);

    /**
     * 给特定世界的玩家广播popup消息
     *
     * @param level
     * @param message
     */
    void broadcastPopupMessage(Level level, String message, int stayTick);

    /**
     * 给端内的所有玩家广播popup消息
     *
     * @param message
     */
    void broadcastPopupMessage(String message, int stayTick);

    /**
     * 发送Tip消息
     *
     * @param player
     * @param message
     */
    void sendTipMessage(Player player, String message, int stayTick);

    /**
     * 给特定世界的玩家广播Tip消息
     *
     * @param level
     * @param message
     */
    void broadcastTipMessage(Level level, String message, int stayTick);

    /**
     * 给端内的所有玩家广播Tip消息
     *
     * @param message
     */
    void broadcastTipMessage(String message, int stayTick);

    /**
     * 发送system消息
     *
     * @param player
     * @param message
     */
    void sendSystemMessage(Player player, String message, int stayTick);

    /**
     * 给特定世界的玩家广播system消息
     *
     * @param level
     * @param message
     */
    void broadcastSystemMessage(Level level, String message, int stayTick);

    /**
     * 给端内的所有玩家广播system消息
     *
     * @param message
     */
    void broadcastSystemMessage(String message, int stayTick);
}
