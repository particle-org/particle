package com.particle.game.utils.ecs;

import com.particle.api.entity.IECSComponentManagerApi;
import com.particle.core.ecs.serialization.SerializationTool;
import com.particle.core.ecs.system.ECSSystemManager;
import com.particle.game.block.brewing.BrewingFuelModule;
import com.particle.game.block.brewing.BrewingSystemFactory;
import com.particle.game.block.brewing.CauldronModule;
import com.particle.game.block.common.modules.BannerModule;
import com.particle.game.block.common.modules.BlockTextModule;
import com.particle.game.block.common.modules.CookModule;
import com.particle.game.block.common.modules.SkullModule;
import com.particle.game.block.furnace.FurnaceFuelModule;
import com.particle.game.block.furnace.FurnaceSystemFactory;
import com.particle.game.block.liquid.LavaSystemFactory;
import com.particle.game.block.liquid.WaterSystemFactory;
import com.particle.game.block.planting.*;
import com.particle.game.block.planting.components.GrassSeparateModule;
import com.particle.game.block.trigger.EntityEnterBlockTriggerSystemFactory;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.ai.ActionTreeSystemFactory;
import com.particle.game.entity.attribute.explevel.EntityExperienceModule;
import com.particle.game.entity.attribute.fossilization.EntityFossilizationSystemFactory;
import com.particle.game.entity.attribute.health.EntityHealthModule;
import com.particle.game.entity.attribute.identified.DynamicDisplayNameRefreshSystemFactory;
import com.particle.game.entity.attribute.satisfaction.EntitySatisfactionModule;
import com.particle.game.entity.attribute.satisfaction.HungerSystemFactory;
import com.particle.game.entity.attribute.virus.EntityVirusSystemFactory;
import com.particle.game.entity.link.EntityPassengerSystemFactory;
import com.particle.game.entity.movement.system.AutoDirectionFixSystemFactory;
import com.particle.game.entity.movement.system.MotionUpdaterSystemFactory;
import com.particle.game.entity.movement.system.PositionUpdaterSystemFactory;
import com.particle.game.entity.spawn.AutoRemovedSystemFactory;
import com.particle.game.entity.state.EntityStateModule;
import com.particle.game.entity.state.EntityStateSystemFactory;
import com.particle.game.entity.systems.HealthSystemFactory;
import com.particle.game.item.ItemBindModule;
import com.particle.game.item.ItemPickupSystemFactory;
import com.particle.game.item.ItemRecycleSystemFactory;
import com.particle.game.player.components.OnlineTimeRecorderModule;
import com.particle.game.player.inventory.modules.MultiOwedContainerModule;
import com.particle.game.player.inventory.modules.SingleContainerModule;
import com.particle.game.player.state.EntityGameModeModule;
import com.particle.game.serializations.*;
import com.particle.game.server.plugin.loader.PluginLoader;
import com.particle.game.world.aoi.PlayerAutoChunkSubscriberRadiusUpdateSystemFactory;
import com.particle.game.world.map.MapDecorationUpdateSystemFactory;
import com.particle.game.world.physical.system.BoxPhysicalSystemFactory;
import com.particle.game.world.physical.system.PointPhysicalSystemFactory;
import com.particle.model.ecs.ECSSystem;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.ECSComponent;
import com.particle.model.entity.component.farming.CropsModule;
import com.particle.model.entity.component.farming.StemModule;
import com.particle.model.entity.component.liquid.LavaModule;
import com.particle.model.entity.component.liquid.WaterModule;
import com.particle.model.entity.component.saver.IStringSaverComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class ECSComponentManager implements IECSComponentManagerApi {

    public static final int VERSION = 2;

    private static final Logger LOGGER = LoggerFactory.getLogger(ECSComponent.class);

    private ArrayList<ECSSystem> systemsList = new ArrayList<>(64);

    private Set<String> depressedKeys = new HashSet<>();

    @Inject
    public void init() {
        // 配置需要保存的组件
        SerializationTool.registerSerializationWithModuleName("TransformModule", new TransformSerialization(), TransformModule.class);

        SerializationTool.registerSerializationWithModuleName("EntityGameModeModule", new GameModeSerialization(), EntityGameModeModule.class);
        SerializationTool.registerSerializationWithModuleName("EntityExperienceModule", new ExperienceSerialization(), EntityExperienceModule.class);
        SerializationTool.registerSerializationWithModuleName("EntityHealthModule", new HealthSerialization(), EntityHealthModule.class);
        SerializationTool.registerSerializationWithModuleName("EntitySatisfactionModule", new SatisfactionSerialization(), EntitySatisfactionModule.class);

        SerializationTool.registerSerializationWithModuleName("CookModule", new CookSerialization(), CookModule.class);
        SerializationTool.registerSerializationWithModuleName("BrewingFuelModule", new BrewingFuelSerialization(), BrewingFuelModule.class);
        SerializationTool.registerSerializationWithModuleName("FurnaceFuelModule", new FurnaceFuelSerialization(), FurnaceFuelModule.class);
        SerializationTool.registerSerializationWithModuleName("CauldronModule", new CauldronModuleSerialization(), CauldronModule.class);

        SerializationTool.registerSerializationWithModuleName("SkullModule", new SkullSerialization(), SkullModule.class);
        SerializationTool.registerSerializationWithModuleName("BannerModule", new BannerSerialization(), BannerModule.class);
        SerializationTool.registerSerializationWithModuleName("BlockTextModule", new BlockTextSerialization(), BlockTextModule.class);

        SerializationTool.registerSerializationWithModuleName("MultiOwedContainerModule", new MultiOwedContainerSerialization(), MultiOwedContainerModule.class);
        SerializationTool.registerSerializationWithModuleName("SingleContainerModule", new SingleContainerSerialization(), SingleContainerModule.class);

        SerializationTool.registerSerializationWithModuleName("WaterModule", new BlankModuleSerialization(), WaterModule.class);
        SerializationTool.registerSerializationWithModuleName("LavaModule", new BlankModuleSerialization(), LavaModule.class);

        SerializationTool.registerSerializationWithModuleName("GrassSeparateModule", new BlankModuleSerialization(), GrassSeparateModule.class);

        SerializationTool.registerSerializationWithModuleName("ItemBindModule", new ItemBindSerialization(), ItemBindModule.class);

        SerializationTool.registerSerializationWithModuleName("OnlineTimeRecorderModule", new OnlineTimeRecorderSerialization(), OnlineTimeRecorderModule.class);

        SerializationTool.registerSerializationWithModuleName("CropsModule", new CropsModuleSerialization(), CropsModule.class);
        SerializationTool.registerSerializationWithModuleName("StemModule", new StemModuleSerialization(), StemModule.class);

        SerializationTool.registerSerializationWithModuleName("EntityStateModule", new EntityStateModuleSerialization(), EntityStateModule.class);


        // 配置需要保存的组件
        SerializationTool.registerSerializationWithModuleAliasName("com.particle.model.entity.component.player.GamemodeComponent", new GameModeConvertSerialization(), EntityGameModeModule.class);
        this.depressedKeys.add("gamemode");

        SerializationTool.registerSerializationWithModuleAliasName("com.particle.model.entity.component.player.ExperienceComponent", new ExperienceConvertSerialization(), EntityExperienceModule.class);
        this.depressedKeys.add("experience");
        SerializationTool.registerSerializationWithModuleAliasName("com.particle.model.entity.component.player.ExpLevelComponent", new ExperienceLevelConvertSerialization(), EntityExperienceModule.class);
        this.depressedKeys.add("explevel");

        SerializationTool.registerSerializationWithModuleAliasName("com.particle.node.service.entity.components.HealthComponent", new HealthConvertSerialization(), EntityHealthModule.class);
        this.depressedKeys.add("health");

        SerializationTool.registerSerializationWithModuleAliasName("com.particle.node.service.entity.components.HungerComponent", new SatisfactionConvertSerialization(), EntitySatisfactionModule.class);
        this.depressedKeys.add("hunger");

        SerializationTool.registerSerializationWithModuleAliasName("com.particle.model.entity.component.position.TransformComponent", new TransformConvertSerialization(), TransformModule.class);
        this.depressedKeys.add("TransformComponent");

        SerializationTool.registerSerializationWithModuleAliasName("com.particle.game.ui.components.EntityTextComponent", new BlockTextConvertSerialization(), BlockTextModule.class);
        this.depressedKeys.add("Text");

        SerializationTool.registerSerializationWithModuleAliasName("com.particle.model.entity.component.potion.CauldronComponent", new CauldronModuleConvertSerialization(), CauldronModule.class);
        this.depressedKeys.add("cauldron_component");

        SerializationTool.registerSerializationWithModuleAliasName("com.particle.game.player.inventory.components.MultiOwedContainerComponent", new MultiOwedContainerConvertSerialization(), MultiOwedContainerModule.class);
        SerializationTool.registerSerializationWithModuleAliasName("com.particle.model.entity.component.container.MultiOwedContainerComponent", new MultiOwedContainerConvertSerialization(), MultiOwedContainerModule.class);
        this.depressedKeys.add("multi_container");

        SerializationTool.registerSerializationWithModuleAliasName("com.particle.game.player.inventory.components.BrewingContainerComponent", new SingleContainerBrewingConvertSerialization(), SingleContainerModule.class);
        SerializationTool.registerSerializationWithModuleAliasName("com.particle.model.entity.component.container.BrewingContainerComponent", new SingleContainerBrewingConvertSerialization(), SingleContainerModule.class);
        this.depressedKeys.add("BrewingContainer");
        SerializationTool.registerSerializationWithModuleAliasName("com.particle.game.player.inventory.components.FurnaceContainerComponent", new SingleContainerFurnaceConvertSerialization(), SingleContainerModule.class);
        SerializationTool.registerSerializationWithModuleAliasName("com.particle.model.entity.component.container.FurnaceContainerComponent", new SingleContainerFurnaceConvertSerialization(), SingleContainerModule.class);
        this.depressedKeys.add("FurnaceContainer");
        SerializationTool.registerSerializationWithModuleAliasName("com.particle.game.player.inventory.components.ChestContainerComponent", new SingleContainerChestConvertSerialization(), SingleContainerModule.class);
        SerializationTool.registerSerializationWithModuleAliasName("com.particle.model.entity.component.container.ChestContainerComponent", new SingleContainerChestConvertSerialization(), SingleContainerModule.class);
        this.depressedKeys.add("ChestContainer");

        SerializationTool.registerSerializationWithModuleAliasName("com.particle.node.service.entity.components.ItemBindComponent", new ItemBindConvertSerialization(), ItemBindModule.class);
        this.depressedKeys.add("Item");

        SerializationTool.registerSerializationWithModuleAliasName("com.particle.game.player.components.OnlineTimeRecorderComponent", new OnlineTimeRecorderConvertSerialization(), OnlineTimeRecorderModule.class);
        this.depressedKeys.add("OnlineTimeRecorder");

        SerializationTool.registerSerializationWithModuleAliasName("com.particle.model.entity.component.farming.CropsComponent", new CropsModuleConvertSerialization(), CropsModule.class);
        this.depressedKeys.add("cropsComponent");

        SerializationTool.registerSerializationWithModuleAliasName("com.particle.model.entity.component.farming.StemComponent", new StemModuleConvertSerialization(), StemModule.class);
        this.depressedKeys.add("stemComponent");


        this.depressedKeys.add("EntityMeta");
        this.depressedKeys.add("EntityName");
        this.depressedKeys.add("AABBColliderComponent");
        this.depressedKeys.add("Motion");
        this.depressedKeys.add("RigibodyComponent");
        this.depressedKeys.add("UUID");
        this.depressedKeys.add("movement");
    }

    /*
     * 系统的优先级
     * 注册顺序即为系统优先级
     * 1. 底层相关业务
     * 2. 生物相关业务
     * 3. 掉落物相关业务
     * 4. 移动相关业务
     * 5. 合成与背包相关业务
     * 6. 种植相关业务
     * 7. 液體流相關
     * 8. AI相关业务
     * 9. 物理引擎
     * 10. 地图
     * 11. 场景管理器
     */
    @Inject
    public void initSystem(
            PositionUpdaterSystemFactory positionUpdaterSystemFactory,

            HealthSystemFactory healthSystemFactory,
            HungerSystemFactory hungerSystemFactory,
            EntityStateSystemFactory entityStateSystemFactory,
            EntityEnterBlockTriggerSystemFactory entityEnterBlockTriggerSystemFactory,
            EntityPassengerSystemFactory entityPassengerSystemFactory,

            ItemPickupSystemFactory itemPickupSystemFactory,
            ItemRecycleSystemFactory itemRecycleSystemFactory,

            MotionUpdaterSystemFactory motionUpdaterSystemFactory,
            AutoDirectionFixSystemFactory autoDirectionFixSystemFactory,
            AutoRemovedSystemFactory autoRemovedSystemFactory,

            BrewingSystemFactory brewingSystemFactory,
            FurnaceSystemFactory furnaceSystemFactory,

            GrassSeparateSystemFactory grassSeparateSystemFactory,
            CocoaSystemFactory cocoaSystemFactory,
            CactusSystemFactory cactusSystemFactory,
            ReedsSystemFactory reedsSystemFactory,
            SaplingSystemFactory saplingSystemFactory,
            FarmlandSystemFactory farmlandSystemFactory,
            CropsSystemFactory cropsSystemFactory,
            MushroomSystemFactory mushroomSystemFactory,
            StemSystemFactory stemSystem,
            NetherWartSystemFactory netherWartSystemFactory,

            WaterSystemFactory waterSystemFactory,
            LavaSystemFactory lavaSystemFactory,

            ActionTreeSystemFactory actionTreeSystemFactory,

            BoxPhysicalSystemFactory boxPhysicalSystemFactory,
            PointPhysicalSystemFactory pointPhysicalSystemFactory,

            DynamicDisplayNameRefreshSystemFactory dynamicDisplayNameRefreshSystemFactory,
            MapDecorationUpdateSystemFactory mapDecorationUpdateSystemFactory,

            PlayerAutoChunkSubscriberRadiusUpdateSystemFactory playerAutoChunkSubscriberRadiusUpdateSystemFactory,

            EntityVirusSystemFactory entityVirusSystemFactory,
            EntityFossilizationSystemFactory entityFossilizationSystemFactory
    ) {
        // 底层相关业务
        ECSSystemManager.registerSystem(positionUpdaterSystemFactory);

        // 生物相关业务
        ECSSystemManager.registerSystem(healthSystemFactory);
        ECSSystemManager.registerSystem(hungerSystemFactory);
        ECSSystemManager.registerSystem(entityStateSystemFactory);
        ECSSystemManager.registerSystem(entityEnterBlockTriggerSystemFactory);
        ECSSystemManager.registerSystem(entityPassengerSystemFactory);

        // 掉落物相关业务
        ECSSystemManager.registerSystem(itemPickupSystemFactory);
        ECSSystemManager.registerSystem(itemRecycleSystemFactory);

        // 移动相关业务
        ECSSystemManager.registerSystem(motionUpdaterSystemFactory);
        ECSSystemManager.registerSystem(autoDirectionFixSystemFactory);
        ECSSystemManager.registerSystem(autoRemovedSystemFactory);

        // 合成与背包相关业务
        ECSSystemManager.registerSystem(brewingSystemFactory);
        ECSSystemManager.registerSystem(furnaceSystemFactory);

        // 种植相关业务
        ECSSystemManager.registerSystem(grassSeparateSystemFactory);
        ECSSystemManager.registerSystem(cocoaSystemFactory);
        ECSSystemManager.registerSystem(cactusSystemFactory);
        ECSSystemManager.registerSystem(reedsSystemFactory);
        ECSSystemManager.registerSystem(saplingSystemFactory);
        ECSSystemManager.registerSystem(farmlandSystemFactory);
        ECSSystemManager.registerSystem(cropsSystemFactory);
        ECSSystemManager.registerSystem(mushroomSystemFactory);
        ECSSystemManager.registerSystem(stemSystem);
        ECSSystemManager.registerSystem(netherWartSystemFactory);

        // 液體流相關
        ECSSystemManager.registerSystem(waterSystemFactory);
        ECSSystemManager.registerSystem(lavaSystemFactory);

        // AI相关业务
        ECSSystemManager.registerSystem(actionTreeSystemFactory);

        // 物理引擎
        ECSSystemManager.registerSystem(boxPhysicalSystemFactory);
        ECSSystemManager.registerSystem(pointPhysicalSystemFactory);

        // 地图
        ECSSystemManager.registerSystem(dynamicDisplayNameRefreshSystemFactory);
        ECSSystemManager.registerSystem(mapDecorationUpdateSystemFactory);

        // 视距
        ECSSystemManager.registerSystem(playerAutoChunkSubscriberRadiusUpdateSystemFactory);

        // Entity感染病毒
        ECSSystemManager.registerSystem(entityVirusSystemFactory);
        // Entity石化
        ECSSystemManager.registerSystem(entityFossilizationSystemFactory);

    }

    @Override
    public void depress(String key) {
        this.depressedKeys.add(key);
    }

    public boolean isDepressed(String key) {
        return this.depressedKeys.contains(key);
    }

    @Override
    public int getVersion() {
        return VERSION;
    }

    @Override
    public boolean importECSComponent(Entity entity, String data) {
        int classNameIndex = data.indexOf("@");
        int versionIndex = data.indexOf(":");

        if (classNameIndex < 1 || versionIndex < 1) {
            LOGGER.warn("Illegal component data {}", data);

            return false;
        }

        String className = data.substring(0, classNameIndex);
        String versionData = data.substring(classNameIndex + 1, versionIndex);
        String componentData = data.substring(versionIndex + 1);

        // 兼容背包组件
        if (className.startsWith("com.particle.model.entity.component.container.")) {
            className = "com.particle.game.player.inventory.components" + className.substring(className.lastIndexOf("."));
        }

        // 查找组件对应的class
        Class componentClass = null;
        try {
            componentClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            try {
                if (PluginLoader.customUrlLoader != null) {
                    componentClass = PluginLoader.customUrlLoader.loadClass(className);
                }
            } catch (ClassNotFoundException ex) {
                LOGGER.debug("Component class {} not exist!", className);
            }
        }

        if (componentClass != null) {
            // 如果找到class，则反序列化该class并作为组件导入
            return this.importComponentByClass(entity, componentClass, componentData);
        } else {
            // 如果没有找到class，则使用新的转换工具尝试导入
            return SerializationTool.importModuleData(entity, className, componentData);
        }
    }

    private boolean importComponentByClass(Entity entity, Class componentClass, String componentData) {
        ECSComponent component = null;

        try {
            component = (ECSComponent) componentClass.newInstance();

            if (!(component instanceof IStringSaverComponent)) {
                return false;
            }

            if (entity.hasComponent(component.getId())) {
                component = entity.getComponent(component.getId());
            } else {
                entity.setComponent(component);
            }

            ((IStringSaverComponent) component).deserialization(componentData);

            return true;
        } catch (Exception e) {
            LOGGER.error("Fail to get ecs component.", e);
        }

        return false;
    }

    @Override
    public String exportComponent(ECSComponent ecsComponent) {
        if (ecsComponent instanceof IStringSaverComponent) {
            String serialization = ((IStringSaverComponent) ecsComponent).serialization();

            if (serialization != null) {
                return String.format("%s@%d:%s", ecsComponent.getClass().getName(), VERSION, serialization);
            }
        }

        return null;
    }

    /**
     * 重构tickSystem
     *
     * @param entity
     */
    @Override
    public void filterTickedSystem(Entity entity) {
        List<ECSSystem> tickedSystem = new LinkedList<>();

        for (ECSSystem system : this.systemsList) {
            if (entity.checkECSComponents(system.getRequiredComponent())) {
                tickedSystem.add(system);
            }
        }
        entity.setTickedSystem(tickedSystem);
    }

    /**
     * 允许插件注册系统，插件注册的系统一定在端注册的后面
     *
     * @param system
     */
    @Override
    public void registerCustomSystem(ECSSystem system) {
        this.systemsList.add(system);
    }
}
