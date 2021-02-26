package com.particle.game.player.service;

import com.particle.api.ui.BossBarServiceAPI;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.executor.api.ExecutableTask;
import com.particle.executor.service.AsyncScheduleService;
import com.particle.game.entity.attribute.identified.EntityNameService;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.player.hud.BossBarModule;
import com.particle.game.server.Server;
import com.particle.model.entity.EntityType;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.type.*;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.data.*;
import com.particle.model.player.Player;
import com.particle.model.player.PlayerState;
import com.particle.network.NetworkManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class BossBarService implements BossBarServiceAPI {

    private static final long BOSS_ENTITY_ID = Long.MAX_VALUE - 10;

    private ECSModuleHandler<BossBarModule> bossBarModuleHandler = ECSModuleHandler.buildHandler(BossBarModule.class);

    @Inject
    private PositionService positionService;

    @Inject
    private NetworkManager networkManager;

    @Inject
    private EntityNameService entityNameService;

    @Inject
    private Server server;

    @Inject
    public void init() {
        //建立刷新任务
        HUDRefreshTask hudRefreshTask = new HUDRefreshTask();

        // 每隔3秒刷新一次
        AsyncScheduleService.getInstance()
                .getThread()
                .scheduleRepeatingTask(
                        "HUDRefreshTask",
                        hudRefreshTask,
                        3000);
    }

    /**
     * 刷新BossBar的文字
     *
     * @param player
     * @param template
     */
    @Override
    public void updateTemplate(Player player, String template) {
        BossBarModule module = this.bossBarModuleHandler.getModule(player);
        if (module != null) {
            module.updateTemplate(template);

            //立刻渲染一次数据
            this.render(player);
        }
    }

    /**
     * 渲染模板中的文字
     *
     * @param player
     */
    @Override
    public void render(Player player) {
        BossBarModule module = this.bossBarModuleHandler.getModule(player);
        if (module == null) {
            return;
        }

        module.render(this.entityNameService.getEntityName(player));

        //更新EntityData
        SetEntityDataPacket setEntityDataPacket = new SetEntityDataPacket();
        setEntityDataPacket.setEid(BOSS_ENTITY_ID);

        Map<EntityMetadataType, EntityData> metadata = new HashMap<>();
        metadata.put(EntityMetadataType.NAMETAG, new StringEntityData(module.getRenderedData()));
        setEntityDataPacket.setMetaData(metadata);

        this.networkManager.sendMessage(player.getClientAddress(), setEntityDataPacket);

        //更新BossTitle
        BossEventPacket bossEventPacket = new BossEventPacket();
        bossEventPacket.setEntityId(BOSS_ENTITY_ID);
        bossEventPacket.setEventType(BossEventPacket.EventType.UPDATE_NAME);
        bossEventPacket.setBossName(module.getRenderedData());
        bossEventPacket.setHealthPercent(100);

        this.networkManager.sendMessage(player.getClientAddress(), bossEventPacket);
    }

    /**
     * 刷新Boss的血量
     *
     * @param player
     * @param health
     */
    @Override
    public void updateHealth(Player player, float health) {
        //更新缓存的health
        BossBarModule module = this.bossBarModuleHandler.getModule(player);
        if (module == null) {
            return;
        }

        module.updateHealth(health);

        //更新客户端
        BossEventPacket bossEventPacket = new BossEventPacket();
        bossEventPacket.setEntityId(BOSS_ENTITY_ID);
        bossEventPacket.setEventType(BossEventPacket.EventType.UPDATE_PERCENT);
        bossEventPacket.setHealthPercent(health);

        this.networkManager.sendMessage(player.getClientAddress(), bossEventPacket);
    }

    @Override
    public void updateTexture(Player player, Color color) {
        //更新缓存的color
        BossBarModule module = this.bossBarModuleHandler.getModule(player);
        if (module == null) {
            return;
        }

        module.updateTexture(color);

        //更新客户端
        BossEventPacket bossEventPacket = new BossEventPacket();
        bossEventPacket.setEntityId(BOSS_ENTITY_ID);
        bossEventPacket.setEventType(BossEventPacket.EventType.UPDATE_STYLE);
        bossEventPacket.setColor(color.getRGB());

        this.networkManager.sendMessage(player.getClientAddress(), bossEventPacket);
    }

    /**
     * 创建BossBar
     */
    @Override
    public void addBossBar(Player player) {
        this.addBossBar(player, "");
    }

    @Override
    public void addBossBar(Player player, String template) {
        BossBarModule bossBarModule = this.bossBarModuleHandler.bindModule(player);
        bossBarModule.updateTemplate(template);
        bossBarModule.render(entityNameService.getEntityName(player));

        //添加BossEntity
        AddEntityPacket addEntityPacket = new AddEntityPacket();
        addEntityPacket.setActorType(EntityType.ENDER_DRAGON.actorType());
        addEntityPacket.setEntityUniqueId(BOSS_ENTITY_ID);
        addEntityPacket.setEntityRuntimeId(BOSS_ENTITY_ID);
        addEntityPacket.setDirection(new Direction(0, 0, 0));

        Vector3f position = positionService.getPosition(player);
        position.setY(-10);
        addEntityPacket.setPosition(position);

        Map<EntityMetadataType, EntityData> metadata = new HashMap<>();
        metadata.put(EntityMetadataType.FLAGS, new LongEntityData(0));
        metadata.put(EntityMetadataType.AIR, new ShortEntityData((short) 400));
        metadata.put(EntityMetadataType.MAX_AIR, new ShortEntityData((short) 400));
        metadata.put(EntityMetadataType.LEAD_HOLDER_EID, new LongEntityData(-1));
        metadata.put(EntityMetadataType.NAMETAG, new StringEntityData(bossBarModule.getRenderedData()));
        metadata.put(EntityMetadataType.SCALE, new FloatEntityData(0.001f));
        addEntityPacket.setMetadata(metadata);

        this.networkManager.sendMessage(player.getClientAddress(), addEntityPacket);

        //刷新Boss血量
        UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
        updateAttributesPacket.setEntityId(BOSS_ENTITY_ID);
        updateAttributesPacket.setAttributes(bossBarModule.getEntityAttributes());

        this.networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);

        BossEventPacket bossEventPacket = new BossEventPacket();
        bossEventPacket.setEntityId(BOSS_ENTITY_ID);
        bossEventPacket.setEventType(BossEventPacket.EventType.ADD);
        bossEventPacket.setBossName(bossBarModule.getRenderedData());
        bossEventPacket.setHealthPercent(100);

        this.networkManager.sendMessage(player.getClientAddress(), bossEventPacket);
    }

    /**
     * 移除玩家的BossBar
     *
     * @param player
     */
    @Override
    public void removeBossBar(Player player) {
        //移除玩家的component
        this.bossBarModuleHandler.removeModule(player);

        //更新客户端
        BossEventPacket bossEventPacket = new BossEventPacket();
        bossEventPacket.setEntityId(BOSS_ENTITY_ID);
        bossEventPacket.setEventType(BossEventPacket.EventType.REMOVE);

        this.networkManager.sendMessage(player.getClientAddress(), bossEventPacket);

        RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
        removeEntityPacket.setEntityUniqueId(BOSS_ENTITY_ID);

        this.networkManager.sendMessage(player.getClientAddress(), removeEntityPacket);
    }

    /**
     * 刷新bossbar的任务，它会跟随玩家位置进行刷新
     */
    private class HUDRefreshTask implements ExecutableTask {

        @Override
        public void run() {
            Collection<Player> allPlayers = server.getAllPlayers();

            for (Player player : allPlayers) {
                if (player.getPlayerState() != PlayerState.DISCONNECTED) {
                    render(player);
                }
            }
        }
    }

}
