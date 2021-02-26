package com.particle.game.server.command;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandServiceApi;
import com.particle.api.command.CommandSource;
import com.particle.game.server.command.annotation.AnnotationCheck;
import com.particle.game.server.command.impl.ConsoleCommandSource;
import com.particle.game.server.command.impl.PlayerCommandSource;
import com.particle.model.command.CmdCharEscap;
import com.particle.model.command.CommandResult;
import com.particle.model.command.annotation.*;
import com.particle.model.player.Player;
import com.particle.model.utils.ProphetPatterns;
import com.particle.network.NetworkManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Singleton
public class CommandManager implements CommandServiceApi {

    private Logger logger = LoggerFactory.getLogger(CommandManager.class);

    /**
     * 存储所有的命令及其定义方法
     */
    private Map<String, List<RegisteredCommand>> allRegisterCommand = new ConcurrentHashMap<>();

    /**
     * 存储所有的指令
     */
    private Set<String> allCmdNames = new HashSet<>();

    @Inject
    private NetworkManager networkManager;

    @Inject
    private ConsoleCommandSource consoleCommandSource;

    /**
     * 存储所有的commandSource
     */
    private Map<Long, CommandSource> allCommandSources = new ConcurrentHashMap<>();

    /**
     * 添加玩家的commandSource
     *
     * @param player
     */
    public void addPlayerCommandSource(Player player) {
        if (this.allCommandSources.containsKey(player.getRuntimeId())) {
            return;
        }
        CommandSource commandSource = new PlayerCommandSource(player, this.networkManager);
        this.allCommandSources.put(player.getRuntimeId(), commandSource);
        return;
    }

    /**
     * 去除玩家的commandSource
     *
     * @param player
     */
    public void removePlayerCommandSource(Player player) {
        if (player == null) {
            return;
        }
        this.allCommandSources.remove(player.getRuntimeId());
    }

    @Override
    public CommandSource getPlayerCommandSource(long clientId) {
        return this.allCommandSources.get(clientId);
    }

    @Override
    public CommandSource getConsoleCommandSource() {
        return this.consoleCommandSource;
    }

    @Override
    public CommandResult dispatchCommand(Player sender, String cmdLine) {
        CommandSource commandSource = this.getPlayerCommandSource(sender.getRuntimeId());
        return this.dispatchCommand(commandSource, cmdLine);
    }

    @Override
    public CommandResult dispatchCommand(CommandSource sender, String cmdLine) {
        ArrayList<String> parsed = parseArguments(cmdLine);
        if (parsed.size() == 0) {
            return CommandResult.builder().error(CommandResult.TYPE_ERROR_INPUT).errorMsg("命令为空").build();
        }
        String command = parsed.remove(0).toLowerCase();
        CommandResult commandResult = this.dispatchCommand(sender, command, parsed);
        if (commandResult.isError()) {
            logger.error("执行命令[{}]失败, 失败原因【{}】", cmdLine, commandResult.getErrorMsg());
        }
        return commandResult;
    }

    @Override
    public Set<String> getAllCmdNames() {
        return allCmdNames;
    }

    /**
     * 注册指令
     *
     * @param cmdClazz
     * @param instance
     */
    public void registerCommand(Class<? extends BaseCommand> cmdClazz, BaseCommand instance) {
        String parentCmdName = AnnotationCheck.getAnnotationValue(cmdClazz, ParentCommand.class);
        String parentPermission = AnnotationCheck.getAnnotationValue(cmdClazz, CommandPermission.class);
        this.registerSubCommand(cmdClazz, instance, parentCmdName, parentPermission);
    }

    /**
     * 注册子命令
     *
     * @param cmdClazz
     * @param instance
     * @param parentCmdName
     * @param parentPermission
     */
    private void registerSubCommand(Class<? extends BaseCommand> cmdClazz, BaseCommand instance, String parentCmdName, String parentPermission) {

        for (Method method : cmdClazz.getDeclaredMethods()) {
            method.setAccessible(true);
            String subCmd = this.getSubCommandValue(method, parentCmdName);
            String helpCmd = AnnotationCheck.getAnnotationValue(method, HelpCommand.class);

            if (StringUtils.isEmpty(subCmd) && !StringUtils.isEmpty(helpCmd)) {
                subCmd = helpCmd;
            }
            if (StringUtils.isEmpty(subCmd)) {
                logger.debug("it is not a command annotation method!");
                continue;
            }
            final String[] subCommandParts = ProphetPatterns.SPACE.split(subCmd);
            Set<String> cmdList = getSubCommandPossibilityList(subCommandParts);
            if (cmdList.isEmpty()) {
                logger.warn("it is not a command annotation method, for cmdList is empty!");
                continue;
            }

            String description = AnnotationCheck.getAnnotationValue(method, CommandDescription.class);
            String subPermission = AnnotationCheck.getAnnotationValue(method, CommandPermission.class);
            Set<String> permissions = new HashSet<>();
            if (!StringUtils.isEmpty(parentPermission)) {
                permissions.add(parentPermission);
            }

            if (!StringUtils.isEmpty(subPermission)) {
                permissions.add(subPermission);
            }

            RegisteredCommand registeredCommand = new RegisteredCommand(instance,
                    cmdList, method, permissions, description);
            this.addCommand(registeredCommand);
        }
    }

    /**
     * 获取子命令的全路径
     * 类似于：parent1|parent2 sub1|sub2
     *
     * @param method
     * @param parentCmdName
     * @return
     */
    private String getSubCommandValue(Method method, String parentCmdName) {
        final String sub = AnnotationCheck.getAnnotationValue(method, SubCommand.class);
        if (sub == null) {
            return "";
        }

        return StringUtils.isEmpty(parentCmdName) ? sub : String.format("%s %s", parentCmdName, sub);
    }

    /**
     * Takes a string like "foo|bar baz|qux" and generates a list of
     * - foo baz
     * - foo qux
     * - bar baz
     * - bar qux
     * <p>
     * For every possible sub command combination
     *
     * @param subCommandParts
     * @return List of all sub command possibilities
     */
    private static Set<String> getSubCommandPossibilityList(String[] subCommandParts) {
        int i = 0;
        Set<String> current = null;
        while (true) {
            Set<String> newList = new HashSet<>();
            if (i < subCommandParts.length) {
                for (String s1 : ProphetPatterns.PIPE.split(subCommandParts[i])) {
                    if (current != null) {
                        newList.addAll(current.stream().map(s -> s + " " + s1).collect(Collectors.toList()));
                    } else {
                        newList.add(s1);
                    }
                }
            }
            if (i + 1 < subCommandParts.length) {
                current = newList;
                i = i + 1;
                continue;
            }
            return newList;
        }
    }

    /**
     * 注册指令
     *
     * @param registeredCommand
     */
    private void addCommand(RegisteredCommand registeredCommand) {
        Set<String> cmdNameSets = registeredCommand.getCommandName();
        for (String cmd : cmdNameSets) {
            // 所有指令，转小写
            cmd = cmd.toLowerCase();
            List<RegisteredCommand> registeredCommands;
            if (this.allRegisterCommand.containsKey(cmd)) {
                registeredCommands = this.allRegisterCommand.get(cmd);
            } else {
                registeredCommands = new ArrayList<>();
                this.allCmdNames.add(cmd);
                this.allRegisterCommand.put(cmd, registeredCommands);
            }
            if (this.checkExistedSameCmd(registeredCommands, registeredCommand)) {
                logger.error("存在相同的命令[{}]", cmd);
                continue;
            }
            registeredCommands.add(registeredCommand);
        }
    }

    /**
     * 检测是否存在命令参数一致的情况
     *
     * @param registeredCommands
     * @param registeredCommand
     * @return
     */
    private boolean checkExistedSameCmd(List<RegisteredCommand> registeredCommands, RegisteredCommand registeredCommand) {
        int validParamCounts = registeredCommand.getParamCounts();
        for (RegisteredCommand rc : registeredCommands) {
            if (rc.getParamCounts() == validParamCounts) {
                return true;
            }
        }
        return false;
    }

    /**
     * 指令分发
     *
     * @param sender
     * @param command
     * @param args
     */
    private CommandResult dispatchCommand(CommandSource sender, String command, List<String> args) {
        RegisteredCommand registeredCommand = this.findValidRegisteredCommand(command, args);
        if (registeredCommand == null) {
            if (args.isEmpty()) {
                return CommandResult.builder().error(CommandResult.TYPE_ERROR_INPUT).errorMsg("找不到命令").build();
            } else {
                command = String.format("%s %s", command, args.remove(0).toLowerCase());
                registeredCommand = this.findValidRegisteredCommand(command, args);
            }
        }

        if (registeredCommand == null) {
            return CommandResult.builder().error(CommandResult.TYPE_ERROR_INPUT).errorMsg("找不到命令").build();
        }

        // 权限判断
        Set<String> permissions = registeredCommand.getPermissions();
        if (permissions != null && !permissions.isEmpty()) {
            if (!sender.hasPermission(permissions)) {
                return CommandResult.builder().error(CommandResult.TYPE_ERROR_INPUT).errorMsg("您没有执行权限").build();
            }
        }

        Object result = registeredCommand.invoke(sender, args);
        if (result == null) {
            return CommandResult.builder().succeed().build();
        } else if (result instanceof Boolean) {
            if (!((Boolean) result).booleanValue()) {
                return CommandResult.builder().error(CommandResult.TYPE_ERROR_CORE).errorMsg("执行命令失败").build();
            }
        } else if (result instanceof Integer) {
            if (((Integer) result).intValue() == -1) {
                return CommandResult.builder().error(CommandResult.TYPE_ERROR_CORE).errorMsg("执行命令失败").build();
            }
        }
        return CommandResult.builder().succeed().result(result).build();
    }

    /**
     * 根据参数寻找有效的注册命令
     *
     * @param command
     * @param args
     * @return
     */
    private RegisteredCommand findValidRegisteredCommand(String command, List<String> args) {
        List<RegisteredCommand> registeredCommands = this.allRegisterCommand.get(command);
        if (registeredCommands == null || registeredCommands.isEmpty()) {
            return null;
        }
        int paramCounts = 0;
        if (args != null) {
            paramCounts = args.size();
        }
        for (RegisteredCommand rcmd : registeredCommands) {
            if (rcmd.getParamCounts() == paramCounts) {
                return rcmd;
            }
        }
        return null;
    }

    /**
     * 解析命令参数
     *
     * @param cmdLine
     * @return
     */
    private ArrayList<String> parseArguments(String cmdLine) {
        StringBuilder sb = new StringBuilder(cmdLine);
        ArrayList<String> args = new ArrayList<>();
        boolean notQuoted = true;
        int start = 0;

        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '\\') {
                sb.deleteCharAt(i);
                continue;
            }

            if (sb.charAt(i) == ' ' && notQuoted) {
                String arg = sb.substring(start, i);
                if (!arg.isEmpty()) {
                    arg = arg.replace(CmdCharEscap.SPACE.getEscape(), CmdCharEscap.SPACE.getSource())
                            .replace(CmdCharEscap.LINE.getEscape(), CmdCharEscap.LINE.getSource());
                    args.add(arg);
                }
                start = i + 1;
            } else if (sb.charAt(i) == '"') {
                sb.deleteCharAt(i);
                --i;
                notQuoted = !notQuoted;
            }
        }

        String arg = sb.substring(start);
        if (!arg.isEmpty()) {
            arg = arg.replace(CmdCharEscap.SPACE.getEscape(), CmdCharEscap.SPACE.getSource())
                    .replace(CmdCharEscap.LINE.getEscape(), CmdCharEscap.LINE.getSource());
            args.add(arg);
        }
        return args;
    }
}
