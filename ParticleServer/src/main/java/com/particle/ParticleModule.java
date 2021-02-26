package com.particle;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.particle.api.ServerApi;
import com.particle.api.ai.EntityDecisionServiceApi;
import com.particle.api.ai.IAiConfigLoaderApi;
import com.particle.api.animation.EntityAnimationServiceApi;
import com.particle.api.animation.InventoryAnimationServiceApi;
import com.particle.api.aoi.BroadcastServiceApi;
import com.particle.api.aoi.IWhiteListServiceApi;
import com.particle.api.attack.IEntityAttackServiceApi;
import com.particle.api.block.BlockColliderDetectServiceApi;
import com.particle.api.block.IBlockInteractiveServiceApi;
import com.particle.api.chat.ChatServiceApi;
import com.particle.api.chunk.ITileEntity2DocumentApi;
import com.particle.api.command.CommandServiceApi;
import com.particle.api.entity.*;
import com.particle.api.entity.attribute.*;
import com.particle.api.inventory.IInventoryUpdateServiceApi;
import com.particle.api.inventory.IItemDropServiceApi;
import com.particle.api.inventory.InventoryAPI;
import com.particle.api.inventory.InventoryService;
import com.particle.api.inventory.annotation.*;
import com.particle.api.item.IItemAttributeServiceApi;
import com.particle.api.level.*;
import com.particle.api.level.convert.ChunkData2DocumentApi;
import com.particle.api.level.convert.ChunkData2JsonObjectApi;
import com.particle.api.level.convert.ChunkData2NBTTagCompoundApi;
import com.particle.api.level.convert.ChunkData2RegionFormatApi;
import com.particle.api.netease.IPayServiceApi;
import com.particle.api.particle.IParticleAnimationServiceApi;
import com.particle.api.particle.ParticleServiceApi;
import com.particle.api.permission.PermissionApi;
import com.particle.api.physical.ForbidColliderServiceApi;
import com.particle.api.physical.IEntityColliderServiceApi;
import com.particle.api.physical.IPhysicalServiceApi;
import com.particle.api.player.PlayerDatabaseProviderApi;
import com.particle.api.player.PlayerSkinCheckServiceProviderApi;
import com.particle.api.plugin.PluginAPI;
import com.particle.api.recharge.RechargeServiceApi;
import com.particle.api.server.IPowerServiceApi;
import com.particle.api.server.PrisonServiceProviderApi;
import com.particle.api.server.RconServiceProviderApi;
import com.particle.api.sound.SoundServiceApi;
import com.particle.api.ui.*;
import com.particle.api.ui.map.IMapGenerateServiceApi;
import com.particle.api.ui.map.IMapServiceApi;
import com.particle.api.utils.*;
import com.particle.api.world.ChunkServiceAPI;
import com.particle.api.world.LevelServiceAPI;
import com.particle.api.world.WorldServiceApi;
import com.particle.game.block.interactor.BlockInteractiveService;
import com.particle.game.block.tile.TileEntityService;
import com.particle.game.entity.ai.factory.AiConfigLoader;
import com.particle.game.entity.ai.service.EntityDecisionService;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.attack.EntityAttackService;
import com.particle.game.entity.attack.EntityAttackedHandleService;
import com.particle.game.entity.attack.EntityRemoteAttackService;
import com.particle.game.entity.attribute.EntityNBTComponentServiceProxy;
import com.particle.game.entity.attribute.explevel.ExperienceService;
import com.particle.game.entity.attribute.health.HealthServiceProxy;
import com.particle.game.entity.attribute.identified.DynamicDisplayNameRefreshServiceProxy;
import com.particle.game.entity.attribute.identified.EntityNameService;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.attribute.satisfaction.HungerService;
import com.particle.game.entity.attribute.virus.EntityVirusServiceProxy;
import com.particle.game.entity.link.EntityLinkService;
import com.particle.game.entity.link.EntityLinkServiceProxy;
import com.particle.game.entity.link.EntityMountControlService;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.*;
import com.particle.game.entity.service.template.EntityTemplateService;
import com.particle.game.entity.service.template.MobEntityTemplateFactory;
import com.particle.game.entity.spawn.EntityRuntimeSpawnService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.entity.state.EntityStateService;
import com.particle.game.item.ItemAttributeServiceProxy;
import com.particle.game.item.ItemDropService;
import com.particle.game.player.PlayerOnlineTimeRecorderService;
import com.particle.game.player.PlayerService;
import com.particle.game.player.PlayerSkinCheckServiceProvider;
import com.particle.game.player.PlayerSkinService;
import com.particle.game.player.chat.ChatService;
import com.particle.game.player.interactive.EntityInteractiveService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.InventoryUpdateService;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.game.player.inventory.service.impl.*;
import com.particle.game.player.permission.PermissionService;
import com.particle.game.player.save.PlayerDataService;
import com.particle.game.player.save.loader.PlayerDatabaseProvider;
import com.particle.game.player.service.BossBarService;
import com.particle.game.player.uuid.PlayerUuidService;
import com.particle.game.server.Server;
import com.particle.game.server.command.CommandManager;
import com.particle.game.server.plugin.PluginManager;
import com.particle.game.server.prison.PrisonServiceProvider;
import com.particle.game.server.rcon.PowerService;
import com.particle.game.server.rcon.RconServiceProvider;
import com.particle.game.server.recharge.PayService;
import com.particle.game.server.recharge.PayServiceProxy;
import com.particle.game.server.recharge.RechargeService;
import com.particle.game.sound.SoundService;
import com.particle.game.ui.*;
import com.particle.game.utils.DeviceModelService;
import com.particle.game.utils.OnlineMessageCheck;
import com.particle.game.utils.config.ServerConfigService;
import com.particle.game.utils.ecs.ECSComponentManager;
import com.particle.game.utils.ecs.ECSComponentService;
import com.particle.game.utils.logger.PlayerLogService;
import com.particle.game.utils.placeholder.ThreadBindCompiledService;
import com.particle.game.world.animation.EntityAnimationService;
import com.particle.game.world.animation.InventoryAnimationService;
import com.particle.game.world.animation.ParticleAnimationServiceProxy;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.game.world.aoi.WhiteListService;
import com.particle.game.world.level.*;
import com.particle.game.world.level.convert.*;
import com.particle.game.world.level.generate.ChunkGenerateFactory;
import com.particle.game.world.level.loader.anvil.CustomFileChunkProviderFactory;
import com.particle.game.world.level.loader.anvil.FileChunkProviderFactory;
import com.particle.game.world.level.loader.combine.CombineChunkProviderFactory;
import com.particle.game.world.map.MapGenerateService;
import com.particle.game.world.map.MapService;
import com.particle.game.world.particle.ParticleService;
import com.particle.game.world.physical.BlockColliderDetectService;
import com.particle.game.world.physical.EntityColliderService;
import com.particle.game.world.physical.ForbidColliderService;
import com.particle.game.world.physical.PhysicalService;
import com.particle.model.network.NetworkService;
import com.particle.network.NetworkManager;

import java.util.Set;

public abstract class ParticleModule extends AbstractModule {

    protected static Set<Class<?>> classNeedInject;

    @Override
    protected void configure() {
        bind(ServerApi.class).to(Server.class).in(Singleton.class);
        bind(IServerConfigServiceApi.class).to(ServerConfigService.class).in(Singleton.class);
        // 命令
        bind(CommandServiceApi.class).to(CommandManager.class).in(Singleton.class);

        // 背包
        bind(InventoryAPI.class).annotatedWith(Anvil.class).to(AnvilInventoryAPI.class).in(Singleton.class);
        bind(InventoryAPI.class).annotatedWith(Armor.class).to(ArmorInventoryAPI.class).in(Singleton.class);
        bind(InventoryAPI.class).annotatedWith(Brewing.class).to(BrewingInventoryAPI.class).in(Singleton.class);
        bind(InventoryAPI.class).annotatedWith(Chest.class).to(ChestInventoryAPI.class).in(Singleton.class);
        bind(InventoryAPI.class).annotatedWith(Cursor.class).to(PlayerCursorInventoryAPI.class).in(Singleton.class);
        bind(InventoryAPI.class).annotatedWith(Deputy.class).to(DeputyInventoryAPI.class).in(Singleton.class);
        bind(InventoryAPI.class).annotatedWith(Enchant.class).to(EnchantInventoryAPI.class).in(Singleton.class);
        bind(InventoryAPI.class).annotatedWith(Ender.class).to(EnderChestInventoryAPI.class).in(Singleton.class);
        bind(InventoryAPI.class).annotatedWith(Furnace.class).to(FurnaceInventoryAPI.class).in(Singleton.class);
        bind(InventoryAPI.class).annotatedWith(PlayerInventory.class).to(PlayerInventoryAPI.class).in(Singleton.class);
        bind(InventoryAPI.class).annotatedWith(WorkBench.class).to(ContainerInventoryAPI.class).in(Singleton.class);
        bind(InventoryAPI.class).to(InventoryAPIProxy.class).in(Singleton.class);
        bind(InventoryService.class).to(InventoryManager.class).in(Singleton.class);

        bind(IInventoryUpdateServiceApi.class).to(InventoryUpdateService.class).in(Singleton.class);

        // 权限
        bind(PermissionApi.class).to(PermissionService.class).in(Singleton.class);

        // 插件
        bind(PluginAPI.class).toInstance(PluginManager.getInstance());

        // 世界
        bind(WorldServiceApi.class).to(WorldService.class).in(Singleton.class);
        bind(LevelServiceAPI.class).to(LevelService.class).in(Singleton.class);
        bind(ChunkServiceAPI.class).to(ChunkService.class).in(Singleton.class);
        bind(WeatherServiceApi.class).to(WeatherService.class).in(Singleton.class);
        bind(TimeServiceApi.class).to(TimeService.class).in(Singleton.class);
        bind(IChunkSnapshotServiceApi.class).to(ChunkSnapshotServiceProxy.class).in(Singleton.class);

        bind(IBlockInteractiveServiceApi.class).to(BlockInteractiveService.class).in(Singleton.class);

        bind(IFileChunkProviderFactory.class).to(FileChunkProviderFactory.class).in(Singleton.class);
        bind(ICustomFileChunkProviderFactory.class).to(CustomFileChunkProviderFactory.class).in(Singleton.class);

        bind(ICombineChunkProviderFactory.class).to(CombineChunkProviderFactory.class).in(Singleton.class);
        bind(IChunkGenerateFactory.class).to(ChunkGenerateFactory.class).in(Singleton.class);
        bind(ITileEntity2DocumentApi.class).to(TileEntity2Document.class).in(Singleton.class);

        bind(LevelProviderMapperFactoryProviderApi.class).to(LevelProviderMapperFactoryProvider.class).in(Singleton.class);

        // 物品
        bind(IItemAttributeServiceApi.class).to(ItemAttributeServiceProxy.class).in(Singleton.class);

        // ui
        bind(BossBarServiceAPI.class).to(BossBarService.class).in(Singleton.class);
        bind(FormServiceAPI.class).to(FormService.class).in(Singleton.class);
        bind(ScoreBoardServiceAPI.class).to(ScoreBoardService.class).in(Singleton.class);
        bind(TextServiceAPI.class).to(TextService.class).in(Singleton.class);
        bind(TitleServiceAPI.class).to(TitleService.class).in(Singleton.class);
        bind(VirtualButtonServiceApi.class).to(VirtualButtonService.class).in(Singleton.class);
        bind(IChestSelectorServiceApi.class).to(ChestSelectorService.class).in(Singleton.class);
        bind(IEnderBagServiceApi.class).to(EnderBagService.class).in(Singleton.class);
        bind(IPromptServiceApi.class).to(PromptService.class).in(Singleton.class);

        // 生物
        bind(ECSComponentServiceAPI.class).to(ECSComponentService.class).in(Singleton.class);
        bind(IECSComponentManagerApi.class).to(ECSComponentManager.class).in(Singleton.class);
        bind(PlayerServiceApi.class).to(PlayerService.class).in(Singleton.class);
        bind(EntityServiceApi.class).to(EntityService.class).in(Singleton.class);

        bind(PlayerSkinServiceApi.class).to(PlayerSkinService.class).in(Singleton.class);
        bind(PlayerSkinCheckServiceProviderApi.class).to(PlayerSkinCheckServiceProvider.class).in(Singleton.class);

        bind(EntitySpawnServiceApi.class).to(EntitySpawnService.class).in(Singleton.class);
        bind(ItemEntityServiceAPI.class).to(ItemEntityService.class).in(Singleton.class);
        bind(TileEntityServiceApi.class).to(TileEntityService.class).in(Singleton.class);
        bind(NpcServiceApi.class).to(NpcService.class).in(Singleton.class);
        bind(MobEntityServiceApi.class).to(MobEntityService.class).in(Singleton.class);
        bind(VirtualEntityServiceApi.class).to(VirtualEntityService.class).in(Singleton.class);
        bind(IEntityInteractiveServiceApi.class).to(EntityInteractiveService.class).in(Singleton.class);
        bind(EntityAttackedHandleServiceApi.class).to(EntityAttackedHandleService.class).in(Singleton.class);
        bind(IEntityAttackServiceApi.class).to(EntityAttackService.class).in(Singleton.class);
        bind(IEntityTemplateServiceApi.class).to(EntityTemplateService.class).in(Singleton.class);
        bind(IPlayerDataServiceApi.class).to(PlayerDataService.class).in(Singleton.class);
        bind(IEntityMountControlServiceApi.class).to(EntityMountControlService.class).in(Singleton.class);
        bind(IItemDropServiceApi.class).to(ItemDropService.class).in(Singleton.class);
        bind(IPlayerOnlineTimeRecorderServiceApi.class).to(PlayerOnlineTimeRecorderService.class).in(Singleton.class);
        bind(IEntityRemoteAttackServiceApi.class).to(EntityRemoteAttackService.class).in(Singleton.class);
        bind(IProjectileEntityServiceApi.class).to(ProjectileEntityService.class).in(Singleton.class);
        bind(IEntityStateServiceApi.class).to(EntityStateService.class).in(Singleton.class);
        bind(MonsterEntityServiceApi.class).to(MonsterEntityService.class).in(Singleton.class);

        bind(IEntityNBTComponentServiceApi.class).to(EntityNBTComponentServiceProxy.class).in(Singleton.class);
        bind(IDynamicDisplayNameRefreshServiceApi.class).to(DynamicDisplayNameRefreshServiceProxy.class).in(Singleton.class);

        bind(IMobEntityTemplateFactory.class).to(MobEntityTemplateFactory.class).in(Singleton.class);

        bind(EntityVirusServiceAPI.class).to(EntityVirusServiceProxy.class).in(Singleton.class);

        // 生物属性
        bind(EntityLinkServiceAPI.class).to(EntityLinkServiceProxy.class).in(Singleton.class);
        bind(EntityNameServiceApi.class).to(EntityNameService.class).in(Singleton.class);
        bind(ExperienceServiceAPI.class).to(ExperienceService.class).in(Singleton.class);
        bind(HealthServiceAPI.class).to(HealthServiceProxy.class).in(Singleton.class);
        bind(MetaDataServiceApi.class).to(MetaDataService.class).in(Singleton.class);
        bind(MovementServiceAPI.class).to(MovementServiceProxy.class).in(Singleton.class);
        bind(PositionServiceApi.class).to(PositionService.class).in(Singleton.class);
        bind(IEntityRuntimeSpawnServiceApi.class).to(EntityRuntimeSpawnService.class).in(Singleton.class);
        bind(IHungerServiceApi.class).to(HungerService.class).in(Singleton.class);

        // ai
        bind(EntityDecisionServiceApi.class).to(EntityDecisionServiceProxy.class).in(Singleton.class);
        bind(IAiConfigLoaderApi.class).to(AiConfigLoader.class).in(Singleton.class);

        // aoi
        bind(BroadcastServiceApi.class).to(BroadcastServiceProxy.class).in(Singleton.class);
        bind(IWhiteListServiceApi.class).to(WhiteListService.class).in(Singleton.class);

        //
        bind(InventoryAnimationServiceApi.class).to(InventoryAnimationService.class).in(Singleton.class);

        // 网络
        bind(NetworkService.class).to(NetworkManager.class).in(Singleton.class);

        // 声音
        bind(SoundServiceApi.class).to(SoundService.class).in(Singleton.class);

        // 机型
        bind(DeviceModelServiceApi.class).to(DeviceModelService.class).in(Singleton.class);

        // 转换
        bind(ChunkData2DocumentApi.class).to(ChunkData2Document.class).in(Singleton.class);
        bind(ChunkData2JsonObjectApi.class).to(ChunkData2JsonObject.class).in(Singleton.class);
        bind(ChunkData2NBTTagCompoundApi.class).to(ChunkData2NBTTagCompound.class).in(Singleton.class);
        bind(ChunkData2RegionFormatApi.class).to(ChunkData2RegionFormat.class).in(Singleton.class);

        // 動畫
        bind(EntityAnimationServiceApi.class).to(EntityAnimationService.class).in(Singleton.class);

        bind(UuidHelperApi.class).to(PlayerUuidService.class).in(Singleton.class);

        // Utils
        bind(ThreadBindCompiledServiceApi.class).to(ThreadBindCompiledService.class).in(Singleton.class);
        bind(IPlayerLogServiceApi.class).to(PlayerLogService.class).in(Singleton.class);

        // 物理引擎
        bind(BlockColliderDetectServiceApi.class).to(BlockColliderDetectService.class).in(Singleton.class);
        bind(IPhysicalServiceApi.class).to(PhysicalService.class).in(Singleton.class);
        bind(IEntityColliderServiceApi.class).to(EntityColliderService.class).in(Singleton.class);
        bind(ForbidColliderServiceApi.class).to(ForbidColliderService.class).in(Singleton.class);

        // 地图
        bind(IMapGenerateServiceApi.class).to(MapGenerateService.class).in(Singleton.class);
        bind(IMapServiceApi.class).to(MapService.class).in(Singleton.class);

        // 聊天
        bind(ChatServiceApi.class).to(ChatService.class).in(Singleton.class);

        // 聊天檢查
        bind(IOnlineMessageCheck.class).to(OnlineMessageCheck.class).in(Singleton.class);

        // 充值
        bind(RechargeServiceApi.class).to(RechargeService.class).in(Singleton.class);

        // 粒子
        bind(ParticleServiceApi.class).to(ParticleService.class).in(Singleton.class);
        bind(IParticleAnimationServiceApi.class).to(ParticleAnimationServiceProxy.class).in(Singleton.class);

        // fu付费
        bind(IPayServiceApi.class).to(PayServiceProxy.class).in(Singleton.class);
        requestStaticInjection(PayService.class);

        // 服务器相关
        bind(IPowerServiceApi.class).to(PowerService.class).in(Singleton.class);

        // 玩家数据存储Provider
        bind(PlayerDatabaseProviderApi.class).to(PlayerDatabaseProvider.class).in(Singleton.class);

        // 玩家拉黑Service Provider
        bind(PrisonServiceProviderApi.class).to(PrisonServiceProvider.class).in(Singleton.class);

        // 控制台发消息Service Provider
        bind(RconServiceProviderApi.class).to(RconServiceProvider.class).in(Singleton.class);

        // 业务配置
        requestStaticInjection(EntityDecisionService.class);

        requestStaticInjection(EntityLinkService.class);

        classNeedInject.forEach(this::requestStaticInjection);
    }
}
