package com.particle;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.particle.api.command.BaseCommand;
import com.particle.api.inject.RequestStaticInject;
import com.particle.api.plugin.OverrideModules;
import com.particle.core.ecs.system.ECSSystemManager;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.event.handle.AbstractLevelEventHandle;
import com.particle.game.entity.ai.factory.AiConfigLoader;
import com.particle.game.entity.state.EntityStateService;
import com.particle.game.player.uuid.PlayerUuidService;
import com.particle.game.scene.system.BroadcastUpdateSystemFactory;
import com.particle.game.scene.system.GridBinderUpdateSystemFactory;
import com.particle.game.scene.system.GridKeepAliveSystemFactory;
import com.particle.game.scene.system.SubscriberUpdateSystemFactory;
import com.particle.game.server.command.CommandManager;
import com.particle.game.server.plugin.PluginManager;
import com.particle.game.ui.TextService;
import com.particle.game.ui.VirtualButtonService;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.events.annotation.EventHandler;
import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.monitor.MonitorLoader;
import com.particle.monitor.NetworkKeeper;
import com.particle.network.NetworkManager;
import com.particle.util.configer.ConfigServiceProvider;
import com.particle.util.configer.IConfigService;
import com.particle.util.configer.IPluginConfigManager;
import com.particle.util.configer.anno.PluginConfigBean;
import com.particle.util.loader.IConfigLoader;
import com.particle.util.loader.json.JsonConfigLoader;
import com.particle.util.loader.service.ConfigInjectService;
import com.particle.util.loader.service.ConfigManager;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParticleStarter extends ParticleModule {

    private static final Logger logger = LoggerFactory.getLogger(ParticleStarter.class);

    /**
     * 根目录
     */
    public final static String DATA_PATH = System.getProperty("user.dir") + "/";

    /**
     * 插件目录
     */
    public final static String PLUGIN_PATH = DATA_PATH + "plugins";

    @Inject
    private NetworkManager networkManager;

    /**
     * 初始化监控组件
     */
    @Inject
    private MonitorLoader monitorLoader;

    @Inject
    private PlayerUuidService playerUuidService;

    private IConfigService configService = ConfigServiceProvider.getConfigService();

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    private CommandManager commandManager;

    @Inject
    private TextService textService;

    @Inject
    private VirtualButtonService virtualButtonService;

    @Inject
    private IPluginConfigManager pluginConfigManager;

    @Inject
    private NetworkKeeper networkKeeper;

    private void init() {
        this.networkManager.start();
        this.monitorLoader.enableMonitor();

        // 其它业务从初始化
        this.virtualButtonService.init();
        /*
         * 初始化UUID监控
         */
        playerUuidService.init();

        this.networkKeeper.start();
    }

    /**
     * 注册事件消息
     * 只会监听com.particle包名下的
     *
     * @param injector
     */
    private boolean subscriptLevelEventHandle(Injector injector) {
        try {
            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder().forPackages("com.particle");
            if (PluginManager.getInstance().getCustomUrlClassLoader() != null) {
                configurationBuilder.addClassLoader(PluginManager.getInstance().getCustomUrlClassLoader());
                configurationBuilder.addUrls(PluginManager.getInstance().getCustomUrlClassLoader().getURLs());
            }

            Reflections reflections = new Reflections(configurationBuilder);
            Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(EventHandler.class);
            for (Class classes : classesList) {
                Class<? extends AbstractLevelEventHandle> levelEventClazz = classes.asSubclass(AbstractLevelEventHandle.class);

                AbstractLevelEventHandle instance = injector.getInstance(levelEventClazz);
                Annotation annotation = levelEventClazz.getAnnotation(javax.inject.Singleton.class);
                if (annotation == null) {
                    annotation = levelEventClazz.getAnnotation(Singleton.class);
                }
                if (annotation == null) {
                    throw new ProphetException(ErrorCode.CORE_EROOR, String.format("class[%s]必须增加[@Singleton]注解", classes));
                }
                if (instance == null) {
                    instance = levelEventClazz.newInstance();
                    injector.injectMembers(instance);
                }
                this.eventDispatcher.subscript(instance);
            }
            return true;
        } catch (Exception e) {
            logger.error("注册订阅事件失败", e);
            return false;
        }
    }

    /**
     * 注册命令
     *
     * @param injector
     * @return
     */
    private boolean registerAllCommand(Injector injector) {
        try {
            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder().forPackages("com.particle");
            if (PluginManager.getInstance().getCustomUrlClassLoader() != null) {
                configurationBuilder.addClassLoader(PluginManager.getInstance().getCustomUrlClassLoader());
                configurationBuilder.addUrls(PluginManager.getInstance().getCustomUrlClassLoader().getURLs());
            }

            Reflections reflections = new Reflections(configurationBuilder);
            Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(RegisterCommand.class);
            for (Class classes : classesList) {
                Class<? extends BaseCommand> baseCmdClass = classes.asSubclass(BaseCommand.class);
                BaseCommand object = injector.getInstance(baseCmdClass);
                Annotation annotation = baseCmdClass.getAnnotation(javax.inject.Singleton.class);
                if (annotation == null) {
                    annotation = baseCmdClass.getAnnotation(Singleton.class);
                }
                if (annotation == null) {
                    throw new ProphetException(ErrorCode.CORE_EROOR, String.format("class[%s]必须增加[@Singleton]注解", classes));
                }
                if (object == null) {
                    object = baseCmdClass.newInstance();
                    injector.injectMembers(object);
                }
                this.commandManager.registerCommand(classes, object);
            }
            return true;
        } catch (Exception e) {
            logger.error("注册命令失败", e);
            return false;
        }
    }

    /**
     * 注册配置
     *
     * @return
     */
    private boolean registerAllConfiguration() {
        // 初始化所有配置
        this.pluginConfigManager.readConfigs();

        try {
            // 注册所有ConfigBean
            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder().forPackages("com.particle");
            if (PluginManager.getInstance().getCustomUrlClassLoader() != null) {
                configurationBuilder.addClassLoader(PluginManager.getInstance().getCustomUrlClassLoader());
                configurationBuilder.addUrls(PluginManager.getInstance().getCustomUrlClassLoader().getURLs());
            }

            Reflections reflections = new Reflections(configurationBuilder);
            Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(PluginConfigBean.class);
            for (Class classes : classesList) {
                this.pluginConfigManager.loadConfigBean(classes.newInstance());
            }

            // 自动保存
            this.pluginConfigManager.saveAll();
            return true;
        } catch (Exception e) {
            logger.error("配置初始化失败", e);
            return false;
        }
    }

    private boolean registerAllCommonConfigs() {
        File dir = new File("plugins");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".jsonx")) {
                IConfigLoader configLoader = new JsonConfigLoader("plugins" + "/" + file.getName());
                Map<String, Object> configs = configLoader.loadConfigs();
                List<Map<String, Object>> schemas = configLoader.loadSchemas();
                ConfigManager.getInstance().importConfig(file.getName().substring(0, file.getName().length() - 6), configs, true, schemas);
            }
        }

        ConfigInjectService.getInstance().injectConfig("com.particle", PluginManager.getInstance().getCustomUrlClassLoader());

        return true;
    }

    private static boolean findStaticInjectClass() {
        // 注册所有ConfigBean
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder().forPackages("com.particle");
        if (PluginManager.getInstance().getCustomUrlClassLoader() != null) {
            configurationBuilder.addClassLoader(PluginManager.getInstance().getCustomUrlClassLoader());
            configurationBuilder.addUrls(PluginManager.getInstance().getCustomUrlClassLoader().getURLs());
        }

        classNeedInject = new Reflections(configurationBuilder).getTypesAnnotatedWith(RequestStaticInject.class);

        return true;
    }

    /**
     * 添加插件的guice的module
     *
     * @return
     */
    private static Injector overrideGuiceModule() {
        try {
            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder().forPackages("com.particle");
            if (PluginManager.getInstance().getCustomUrlClassLoader() != null) {
                configurationBuilder.addClassLoader(PluginManager.getInstance().getCustomUrlClassLoader());
                configurationBuilder.addUrls(PluginManager.getInstance().getCustomUrlClassLoader().getURLs());
            }

            Reflections reflections = new Reflections(configurationBuilder);
            Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(OverrideModules.class);
            List<AbstractModule> moduleList = new ArrayList<>();
            moduleList.add(new ParticleStarter());
            for (Class classes : classesList) {
                Class<? extends AbstractModule> module = classes.asSubclass(AbstractModule.class);
                moduleList.add(module.newInstance());
            }
            return Guice.createInjector(moduleList);
        } catch (Exception e) {
            logger.error("注册插件的guice module失败", e);
            return null;
        }
    }

    public static void main(String[] args) {
        /**
         * 加载插件
         */
        if (!PluginManager.getInstance().loadPlugins(PLUGIN_PATH)) {
            System.exit(0);
        }

        /**
         * 统计需要静态注入的class
         */
        findStaticInjectClass();

        /**
         * 添加插件的guice module
         */
        Injector injector = overrideGuiceModule();
        if (injector == null) {
            System.exit(0);
        }

        /**
         * instance插件
         */
        if (!PluginManager.getInstance().instance(injector)) {
            System.exit(0);
        }

        ParticleStarter serverInstance = injector.getInstance(ParticleStarter.class);

        /**
         * 注册事件handler
         */
        if (!serverInstance.subscriptLevelEventHandle(injector)) {
            System.exit(0);
        }

        /**
         * 注册命令
         */
        if (!serverInstance.registerAllCommand(injector)) {
            System.exit(0);
        }

        /*
         * 初始化配置
         */
        if (!serverInstance.registerAllConfiguration()) {
            System.exit(0);
        }
        serverInstance.registerAllCommonConfigs();

        /*
         * 初始化AI
         */
        AiConfigLoader aiConfigLoader = injector.getInstance(AiConfigLoader.class);
        aiConfigLoader.loadConfig(injector);

        /*
         * 初始化生物状态
         */
        EntityStateService entityStateService = injector.getInstance(EntityStateService.class);
        entityStateService.init(injector);

        serverInstance.init();

        /**
         * enable插件
         */
        if (!PluginManager.getInstance().enable()) {
            System.exit(0);
        }

        /*
         * 启动AOI
         */
        ECSSystemManager.registerSystem(new GridBinderUpdateSystemFactory());
        ECSSystemManager.registerSystem(new SubscriberUpdateSystemFactory());
        ECSSystemManager.registerSystem(new BroadcastUpdateSystemFactory());
        ECSSystemManager.registerSystem(new GridKeepAliveSystemFactory());
    }
}