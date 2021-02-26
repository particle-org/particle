package com.particle.game.server.command;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Set;

public class RegisteredCommand {

    private static final Logger logger = LoggerFactory.getLogger(RegisteredCommand.class);

    private BaseCommand baseCommand;

    private Set<String> commandName;

    private Parameter[] parameters;

    private Set<String> permissions;

    private String description;

    private Method method;

    private int paramCounts;


    public RegisteredCommand(BaseCommand baseCommand, Set<String> commandName,
                             Method method, Set<String> permissions, String description) {
        this.baseCommand = baseCommand;
        this.commandName = commandName;
        this.method = method;
        this.parameters = method.getParameters();
        this.permissions = permissions;
        this.description = description;
        if (this.parameters == null) {
            paramCounts = 0;
        } else if (this.checkExistCommandSourceParam() >= 0) {
            paramCounts = this.parameters.length - 1;
        } else {
            paramCounts = this.parameters.length;
        }
    }

    /**
     * 检测是否存在CommandSource的参数
     *
     * @return
     */
    private int checkExistCommandSourceParam() {
        int commandSourceIndex = 0;
        boolean existedSender = false;
        if (this.parameters != null) {
            for (Parameter parameter : this.parameters) {
                if (parameter.getType() == CommandSource.class) {
                    existedSender = true;
                    break;
                }
                commandSourceIndex++;
            }
        }
        return existedSender ? commandSourceIndex : -1;
    }

    /**
     * 执行命令调用
     * 需要查找CommandSource的调用顺序
     *
     * @param sender
     * @param args
     */
    public Object invoke(CommandSource sender, List<String> args) {
        Object result = null;
        if (sender == null) {
            return false;
        }

        int commandSourceIndex = this.checkExistCommandSourceParam();
        if (commandSourceIndex >= 0) {
            if (args == null && this.parameters.length != 1 ||
                    args != null && args.size() + 1 != this.parameters.length) {
                this.handleException(sender, args, new ProphetException(ErrorCode.PARAM_ERROR, "the param is not match!"));
                return false;
            }
            if (args == null) {
                try {
                    result = method.invoke(baseCommand, sender);
                } catch (Exception e) {
                    logger.error("invoke failed!", e);
                    this.handleException(sender, args, e);
                    return false;
                } catch (Error e) {
                    logger.error("invoke failed!", e);
                    this.handleException(sender, args, e);
                    return false;
                }
            } else {
                List<String> front = args.subList(0, commandSourceIndex);
                List<String> end = args.subList(commandSourceIndex, args.size());
                Object[] invokeParam = new Object[front.size() + 1 + end.size()];
                System.arraycopy(front.toArray(), 0, invokeParam, 0, front.size());
                System.arraycopy(new Object[]{sender}, 0, invokeParam, front.size(), 1);
                System.arraycopy(end.toArray(), 0, invokeParam, front.size() + 1, end.size());
                try {
                    result = method.invoke(baseCommand, invokeParam);
                } catch (Exception e) {
                    logger.error("invoke failed!", e);
                    this.handleException(sender, args, e);
                    return false;
                } catch (Error e) {
                    logger.error("invoke failed!", e);
                    this.handleException(sender, args, e);
                    return false;
                }
            }

        } else {
            if (args == null && parameters.length > 0 || args != null && args.size() != parameters.length) {
                this.handleException(sender, args, new ProphetException(ErrorCode.PARAM_ERROR, "the param is not match!"));
                return false;
            }
            try {
                if (args == null) {
                    result = method.invoke(baseCommand);
                } else {
                    result = method.invoke(baseCommand, args.toArray());
                }

            } catch (Exception e) {
                logger.error("invoke failed!", e);
                this.handleException(sender, args, e);
                return false;
            } catch (Error e) {
                logger.error("invoke failed!", e);
                this.handleException(sender, args, e);
                return false;
            }
        }
        return result;
    }

    /**
     * 处理异常
     * TODO 需要增加help处理
     *
     * @param sender
     * @param args
     * @param exception
     */
    private void handleException(CommandSource sender, List<String> args, Exception exception) {
        String message = exception.getMessage();
        if (message == null) {
            message = "指令异常";
        }
        sender.sendError(message);
    }

    private void handleException(CommandSource sender, List<String> args, Error exception) {
        sender.sendError(exception.getMessage());
    }

    public Set<String> getCommandName() {
        return commandName;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public String getDescription() {
        return description;
    }

    public int getParamCounts() {
        return paramCounts;
    }
}
