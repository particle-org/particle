package com.particle.game.server.command.defaults.test;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.api.entity.MobEntityServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.attribute.identified.DynamicDisplayNameModule;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.link.EntityLinkServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.template.EntityTemplateService;
import com.particle.game.entity.spawn.EntityRuntimeSpawnService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.player.PlayerService;
import com.particle.game.world.aoi.WhiteListService;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.ParentCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@ParentCommand("testEntity")
@CommandPermission(CommandPermissionConstant.TEST)
public class EntityTestCommand extends BaseCommand {

    private static final ECSModuleHandler<DynamicDisplayNameModule> DYNAMIC_DISPLAY_NAME_MODULE_HANDLER = ECSModuleHandler.buildHandler(DynamicDisplayNameModule.class);


    @Inject
    private PositionService positionService;

    @Inject
    private PlayerService playerService;

    @Inject
    private MobEntityServiceApi mobEntityServiceApi;

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private WhiteListService whiteListService;

    @Inject
    private EntityLinkServiceProxy entityLinkService;

    @Inject
    private MetaDataService metaDataService;

    @Inject
    private EntityRuntimeSpawnService entityRuntimeSpawnService;

    @Inject
    private EntityTemplateService entityTemplateService;

    @SubCommand("create")
    public void test(CommandSource source) {
        Player player = source.getPlayer();

        if (player == null) {
            return;
        }

        Entity entity1 = this.entityTemplateService.createEntityFromTemplate("Fashion:model_modelstorage1", this.positionService.getPosition(player));

        this.entityRuntimeSpawnService.spawn(player.getLevel(), entity1);

        this.whiteListService.setWhiteListState(entity1, true);

        player.getLevel().getLevelSchedule().scheduleDelayTask("Test", () -> {
            this.whiteListService.addWhiteList(entity1, player);
        }, 5000);

        player.getLevel().getLevelSchedule().scheduleDelayTask("Test", () -> {
            this.whiteListService.removeWhiteList(entity1, player);
        }, 10000);

        player.getLevel().getLevelSchedule().scheduleDelayTask("Test", () -> {
            this.whiteListService.addWhiteList(entity1, player);
        }, 15000);
    }

    @SubCommand("sit")
    public void sit(CommandSource source) {
        Player player = source.getPlayer();

        if (player == null) {
            return;
        }

        Entity entity2 = this.mobEntityServiceApi.createEntity("minecraft:zombie", this.positionService.getPosition(player));
        Entity entity3 = this.mobEntityServiceApi.createEntity("minecraft:zombie", this.positionService.getPosition(player));

        this.entitySpawnService.spawn(player.getLevel(), entity2);
        this.entitySpawnService.spawn(player.getLevel(), entity3);

        this.entityLinkService.ridingEntity(entity2, entity3, new Vector3f(0, 1, 0));
    }
}
