package com.particle.api.command;

import com.particle.model.command.CommandResult;
import com.particle.model.player.Player;

import java.util.Set;

public interface CommandServiceApi {

    /**
     * 根据player的runtimeId获取特定的commandSource
     *
     * @param clientId 玩家的clientId
     * @return
     */
    CommandSource getPlayerCommandSource(long clientId);

    /**
     * 获取控制台commandSource
     *
     * @return
     */
    CommandSource getConsoleCommandSource();


    /**
     * 分发指令
     *
     * @param sender
     * @param cmdLine
     * @return
     */
    public CommandResult dispatchCommand(Player sender, String cmdLine);

    /**
     * 分发指令
     *
     * @param sender  发送者对象
     * @param cmdLine 发送指令
     * @return 返回命令的执行结果
     */
    CommandResult dispatchCommand(CommandSource sender, String cmdLine);


    /**
     * 获取当前注册的所有指令
     *
     * @return
     */
    Set<String> getAllCmdNames();
}

