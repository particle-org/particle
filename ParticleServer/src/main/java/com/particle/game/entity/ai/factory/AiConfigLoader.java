package com.particle.game.entity.ai.factory;

import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.particle.api.ai.IAiConfigLoaderApi;
import com.particle.api.ai.behavior.IBranchNode;
import com.particle.api.ai.behavior.ILeafNode;
import com.particle.game.entity.ai.ActionTreeBuilder;
import com.particle.game.entity.ai.model.ActionTree;
import com.particle.game.server.plugin.PluginManager;
import com.particle.util.xml.ViewNode;
import com.particle.util.xml.XmlParseUtils;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class AiConfigLoader implements IAiConfigLoaderApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(AiConfigLoader.class);

    private Map<String, Class<? extends IBranchNode>> branchClassMap = new HashMap<>();
    private Map<String, Class<? extends ILeafNode>> leafClassMap = new HashMap<>();

    private Injector injector;

    public void loadConfig(Injector injector) {
        this.injector = injector;

        try {
            this.loadClass();
        } catch (Exception e) {
            LOGGER.error("Fail to load ai class", e);
        }

        // 读取配置文件
        File configDir = new File("config/ai");
        try {
            this.loadConfig(configDir, injector);
        } catch (Exception e) {
            LOGGER.error("Fail to load ai config.", e);
        }
    }

    public void reloadConfig() {
        // 读取配置文件
        File configDir = new File("config/ai");
        try {
            this.loadConfig(configDir, this.injector);
        } catch (Exception e) {
            LOGGER.error("Fail to load ai config.", e);
        }
    }

    private void loadClass() {
        // 注册所有ConfigBean
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder().forPackages("com.particle");
        if (PluginManager.getInstance().getCustomUrlClassLoader() != null) {
            configurationBuilder.addClassLoader(PluginManager.getInstance().getCustomUrlClassLoader());
            configurationBuilder.addUrls(PluginManager.getInstance().getCustomUrlClassLoader().getURLs());
        }

        Reflections reflections = new Reflections(configurationBuilder);
        for (Class<? extends IBranchNode> clazz : reflections.getSubTypesOf(IBranchNode.class)) {
            this.branchClassMap.put(clazz.getSimpleName(), clazz);
        }
        for (Class<? extends ILeafNode> clazz : reflections.getSubTypesOf(ILeafNode.class)) {
            this.leafClassMap.put(clazz.getSimpleName(), clazz);
        }
    }

    private void loadConfig(File configDir, Injector injector) throws Exception {
        // 读取配置文件
        for (File config : configDir.listFiles()) {
            if (config.isDirectory()) {
                this.loadConfig(config, injector);
                continue;
            }

            LOGGER.debug("Load ai of {}.", config.getAbsolutePath());

            // 读取节点信息
            FileInputStream fileInputStream = new FileInputStream(config);

            this.parseConfigData(fileInputStream);

            fileInputStream.close();
        }
    }

    @Override
    public void parseConfigData(InputStream configData) throws Exception {
        ViewNode configNode = XmlParseUtils.parse(configData);
        String rootName = configNode.getViewName();
        String id = configNode.getAttributes().get("id");
        String binder = configNode.getAttributes().get("binder");

        // 创建行为树构造器
        ActionTreeBuilder actionTreeBuilder = ActionTreeBuilder.create(id);

        // 读取节点信息
        for (ViewNode childNode : configNode.getChildren()) {
            this.loadConfigNode(actionTreeBuilder, injector, childNode);
        }

        // 编译行为树
        ActionTree actionTree = actionTreeBuilder.build();

        if (rootName.equals("decision")) {
            DecisionTreeFactory.bindDecision(binder, actionTree);
        } else if (rootName.equals("action")) {
            ActionTreeFactory.bindAction(binder, actionTree);
        } else if (rootName.equals("interactive")) {
            InteractiveTreeFactory.bingInteractiveResponse(binder, actionTree);
        } else if (rootName.equals("underAttack")) {
            UnderAttackTreeFactory.bingUnderAttackResponse(binder, actionTree);
        } else if (rootName.equals("death")) {
            DeathTreeFactory.bingDeathResponse(binder, actionTree);
        } else if (rootName.equals("message")) {
            String messageId = configNode.getAttributes().get("message");
            if (messageId == null) messageId = "";

            MessageTreeFactory.bindMessageResponse(binder, messageId, actionTree);
        }
    }

    private void loadConfigNode(ActionTreeBuilder builder, Injector injector, ViewNode configNode) throws Exception {
        String nodeType = configNode.getViewName();

        if (nodeType.equals("branch")) {
            // 检索是否有该节点
            Class<? extends IBranchNode> branchNode = this.branchClassMap.get(configNode.getAttributes().get("type"));
            if (branchNode != null) {
                // 读取节点相关信息
                String weightStr = configNode.getAttributes().get("weight");
                int weight = 1;
                if (weightStr != null) {
                    weight = Integer.parseInt(weightStr);
                }

                // 创建分支节点
                builder.branch(injector.getInstance(branchNode), weight);

                // 递归构造
                List<ViewNode> children = configNode.getChildren();
                if (children != null) {
                    for (ViewNode childConfigNode : configNode.getChildren()) {
                        this.loadConfigNode(builder, injector, childConfigNode);
                    }
                }

                // 封闭节点
                builder.upper();
            } else {
                LOGGER.error("Missing node {}", configNode.getAttributes().get("type"));
            }
        } else if (nodeType.equals("leaf")) {
            // 检索是否有该节点
            Class<? extends ILeafNode> leafNode = this.leafClassMap.get(configNode.getAttributes().get("type"));
            if (leafNode != null) {
                // 读取节点相关信息
                String weightStr = configNode.getAttributes().get("weight");
                int weight = 1;
                if (weightStr != null) {
                    weight = Integer.parseInt(weightStr);
                }

                // 创建分支节点
                builder.leaf(injector.getInstance(leafNode), weight);

                // 读取配置
                List<ViewNode> children = configNode.getChildren();
                if (children != null) {
                    for (ViewNode childConfigNode : configNode.getChildren()) {
                        this.loadConfigNode(builder, injector, childConfigNode);
                    }
                }
            } else {
                LOGGER.error("Missing node {}", configNode.getAttributes().get("type"));
            }
        } else if (nodeType.equals("config")) {
            String key = configNode.getAttributes().get("key");

            String val = configNode.getAttributes().get("val");

            if (val.matches("\\d+L")) {
                builder.config(key, Long.parseLong(val.substring(0, val.length() - 1)));
            } else if (val.matches("\\d+(\\.\\d+)?+F")) {
                builder.config(key, Float.parseFloat(val.substring(0, val.length() - 1)));
            } else if (val.matches("\\d+(\\.\\d+)?+D")) {
                builder.config(key, Double.parseDouble(val.substring(0, val.length() - 1)));
            } else if (val.matches("\\d+")) {
                builder.config(key, Integer.parseInt(val));
            } else if (val.equals("True")) {
                builder.config(key, true);
            } else if (val.equals("False")) {
                builder.config(key, false);
            } else {
                builder.config(key, val);
            }
        }
    }
}
