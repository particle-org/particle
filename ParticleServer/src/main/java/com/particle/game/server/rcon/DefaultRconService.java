package com.particle.game.server.rcon;

import com.particle.api.command.CommandServiceApi;
import com.particle.api.server.RconServiceApi;
import com.particle.model.command.CommandResult;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DefaultRconService implements RconServiceApi {
    @Inject
    private CommandServiceApi commandServiceApi;

    @Override
    public void sendMessage(String message) {
        CommandResult commandResult = this.commandServiceApi.dispatchCommand(this.commandServiceApi.getConsoleCommandSource(),
                message);
        if (commandResult.isError() && commandResult.getErrorType() == CommandResult.TYPE_ERROR_INPUT) {
            // 返回错误消息
            this.commandServiceApi.getConsoleCommandSource().sendError(commandResult.getErrorMsg());
            return;
        }
        if (commandResult.isError()) {
            String msg = StringUtils.isEmpty(commandResult.getErrorMsg()) ? commandResult.getErrorMsg() : "执行失败";
            this.commandServiceApi.getConsoleCommandSource().sendError(msg);
        } else {
            String msg = StringUtils.isEmpty(commandResult.getErrorMsg()) ? commandResult.getErrorMsg() : "执行成功";
            this.commandServiceApi.getConsoleCommandSource().sendMessage(msg);
        }
    }
}
