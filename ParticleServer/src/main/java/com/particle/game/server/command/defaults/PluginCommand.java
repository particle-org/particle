package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.api.plugin.PluginBase;
import com.particle.game.server.plugin.PluginManager;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.ParentCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.plugin.model.PluginDescription;
import com.particle.util.configer.IPluginConfigManager;
import com.particle.util.loader.IConfigLoader;
import com.particle.util.loader.json.JsonConfigLoader;
import com.particle.util.loader.service.ConfigInjectService;
import com.particle.util.loader.service.ConfigManager;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.Collection;
import java.util.List;

@RegisterCommand
@Singleton
@CommandPermission(CommandPermissionConstant.CONSOLE)
@ParentCommand("plugins")
public class PluginCommand extends BaseCommand {

    private String errorReloadMsg = "请输入正确的指令: /plugins reload [pluginId] message|config";

    @Inject
    private IPluginConfigManager iPluginConfigManager;

    @SubCommand("list")
    void list(CommandSource commandSource) {
        PluginManager pluginManager = PluginManager.getInstance();
        List<PluginDescription> pluginDescriptions = pluginManager.getAllPluginDescription();
        StringBuilder sb = new StringBuilder("以下是插件列表：\n");
        for (PluginDescription description : pluginDescriptions) {
            sb.append(description.getId()).append("[").append(description.getVersion()).append("]");
            sb.append("\r\n");
        }
        commandSource.sendMessage(sb.toString());
    }

    @SubCommand("info")
    void list(CommandSource commandSource, String filter) {
        PluginManager pluginManager = PluginManager.getInstance();
        List<PluginDescription> pluginDescriptions = pluginManager.getAllPluginDescription();
        StringBuilder sb = new StringBuilder("以下是插件列表：\n");
        if (StringUtils.isEmpty(filter)) {
            commandSource.sendMessage(sb.toString());
            return;
        }
        for (PluginDescription description : pluginDescriptions) {
            if (description.getId().contains(filter) || description.getName().contains(filter)) {
                sb.append(description.getId()).append("[").append(description.getVersion()).append("]");
                sb.append("\r\n");
            }
        }
        commandSource.sendMessage(sb.toString());
    }

    @SubCommand("reloadall")
    void reloadall(CommandSource commandSource) {
        this.reloadall(commandSource, "");
    }

    @SubCommand("reloadall")
    void reloadall(CommandSource commandSource, String target) {
        if (target == null) {
            commandSource.sendError(errorReloadMsg);
            return;
        }

        this.iPluginConfigManager.readConfigs();

        File dir = new File("plugins");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".jsonx")) {
                IConfigLoader configLoader = new JsonConfigLoader("plugins" + "/" + file.getName());
                ConfigManager.getInstance().importConfig(
                        file.getName().substring(0, file.getName().length() - 6),
                        configLoader.loadConfigs(),
                        true,
                        configLoader.loadSchemas());
            }
        }

        ConfigInjectService.getInstance().injectConfig("com.particle", PluginManager.getInstance().getCustomUrlClassLoader());

        PluginManager pluginManager = PluginManager.getInstance();
        Collection<PluginBase> allPluginBase = pluginManager.getAllPluginBase();
        for (PluginBase pluginBase : allPluginBase) {
            pluginBase.notifyConfigurationChange(target);
        }
    }

    @SubCommand("reload")
    void reload(CommandSource commandSource, String plugin) {
        this.reload(commandSource, plugin, "");
    }

    @SubCommand("reload")
    void reload(CommandSource commandSource, String plugin, String target) {
        if (target == null) {
            commandSource.sendError(errorReloadMsg);
            return;
        }
        if (StringUtils.isEmpty(plugin)) {
            commandSource.sendError(errorReloadMsg);
            return;
        }
        PluginManager pluginManager = PluginManager.getInstance();
        PluginBase pluginBase = pluginManager.getPluginById(plugin);
        if (pluginBase == null) {
            commandSource.sendError("该插件不存在");
            return;
        }
        this.iPluginConfigManager.readConfigs();

        File dir = new File("plugins");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".jsonx")) {
                IConfigLoader configLoader = new JsonConfigLoader("plugins" + "/" + file.getName());
                ConfigManager.getInstance().importConfig(
                        file.getName().substring(0, file.getName().length() - 6),
                        configLoader.loadConfigs(),
                        true,
                        configLoader.loadSchemas());
            }
        }

        ConfigInjectService.getInstance().injectConfig("com.particle", PluginManager.getInstance().getCustomUrlClassLoader());

        pluginBase.notifyConfigurationChange(target);
    }
}
