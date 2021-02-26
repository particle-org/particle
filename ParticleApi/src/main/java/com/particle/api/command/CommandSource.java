package com.particle.api.command;

import com.particle.model.player.Player;

import java.util.Set;

public interface CommandSource {

    /**
     * 获取clientId,如果远程命令，返回0
     *
     * @return
     */
    default long getClientId() {
        return 0;
    }

    /**
     * 回消息
     *
     * @param message
     */
    void sendMessage(String message);

    /**
     * 回错误消息
     *
     * @param message
     */
    void sendError(String message);

    /**
     * 判断是否含有权限
     *
     * @param permission
     * @return
     */
    boolean hasPermission(String permission);

    /**
     * 判断是否含有权限, 需要包含所有的权限
     *
     * @param permissions
     * @return
     */
    boolean hasPermission(Set<String> permissions);

    /**
     * 返回玩家
     * 如果是控制台，返回null
     *
     * @return
     */
    Player getPlayer();

    /**
     * 是否是控制台
     *
     * @return
     */
    default boolean isConsole() {
        return false;
    }
}
