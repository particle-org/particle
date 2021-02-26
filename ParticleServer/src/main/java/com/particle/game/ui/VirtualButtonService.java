package com.particle.game.ui;

import com.particle.api.entity.IEntityInteractivedHandle;
import com.particle.api.utils.VirtualButtonServiceApi;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.event.dispatcher.EventRank;
import com.particle.event.handle.AbstractLevelEventHandle;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.MobEntityService;
import com.particle.game.entity.service.VirtualEntityService;
import com.particle.game.player.interactive.EntityInteractiveService;
import com.particle.game.ui.model.ButtonEntity;
import com.particle.model.entity.EntityType;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.events.level.player.PlayerJoinGameEvent;
import com.particle.model.events.level.player.PlayerMoveEvent;
import com.particle.model.events.level.player.PlayerQuitGameEvent;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.MoveEntityPacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class VirtualButtonService implements VirtualButtonServiceApi {

    private static final Logger logger = LoggerFactory.getLogger(VirtualButtonService.class);


    private static final Map<String, ButtonEntity> BUTTON_OPENED = new HashMap<>();

    // 用于tp判断的标志
    private static final int MAX_TP_LENGTH_FLAG = 10;

    @Inject
    private MobEntityService mobEntityService;

    @Inject
    private PositionService positionService;

    @Inject
    private EntityInteractiveService entityInteractiveService;

    @Inject
    private VirtualEntityService virtualEntityService;

    @Inject
    private MetaDataService metaDataService;

    @Inject
    private NetworkManager networkManager;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    public void init() {
        this.eventDispatcher.subscript(new ButtonUpdaterHelper());
        this.eventDispatcher.subscript(new ButtonClearHelper());
    }

    /**
     * 获取工具类实例，为每个ID创建一个单例，避免过多的监听
     *
     * @param player
     * @param buttonText
     * @param handle
     */
    @Override
    public void createButton(Player player, String buttonText, IEntityInteractivedHandle handle) {
        // 生成虚拟生物
        MobEntity entity = this.mobEntityService.createEntity(EntityType.VILLAGER.type(), this.positionService.getPosition(player));
        this.virtualEntityService.registerVirtualEntity(entity);

        // 配置虚拟生物的属性
        this.entityInteractiveService.initEntityInteractived(entity, handle);
        this.metaDataService.setDataFlag(entity, MetadataDataFlag.DATA_FLAG_INVISIBLE, true, false);
        this.metaDataService.setFloatData(entity, EntityMetadataType.SCALE, 10);
        this.metaDataService.setStringData(entity, EntityMetadataType.NAMETAG, "");

        // 缓存按钮
        BUTTON_OPENED.put(player.getIdentifiedStr(), new ButtonEntity(entity, buttonText));

        // 将按钮及相关属性刷新给玩家
        this.virtualEntityService.spawnToPlayer(entity, player);
        this.metaDataService.setStringData(player, EntityMetadataType.INTERACTIVE_TAG, "");
        this.metaDataService.refreshPlayerMetadata(player);
        this.metaDataService.setStringData(player, EntityMetadataType.INTERACTIVE_TAG, buttonText);
        this.metaDataService.refreshPlayerMetadata(player);
    }

    /**
     * 刷新按钮名称
     *
     * @param player
     * @param targetRuntimeId
     */
    public void refreshButtonName(Player player, long targetRuntimeId) {
        ButtonEntity updateEntity = BUTTON_OPENED.get(player.getIdentifiedStr());
        if (updateEntity == null) {
            return;
        }
        if (targetRuntimeId != updateEntity.getEntity().getRuntimeId()) {
            return;
        }
        this.metaDataService.setStringData(player, EntityMetadataType.INTERACTIVE_TAG, "");
        this.metaDataService.refreshPlayerMetadata(player);
        this.metaDataService.setStringData(player, EntityMetadataType.INTERACTIVE_TAG, updateEntity.getShowName());
        this.metaDataService.refreshPlayerMetadata(player);
    }

    /**
     * 关闭工具类实例
     *
     * @param player
     */
    @Override
    public void closeButton(Player player) {
        ButtonEntity removedEntity = BUTTON_OPENED.remove(player.getIdentifiedStr());

        if (removedEntity != null) {
            this.virtualEntityService.removeVirtualEntity(removedEntity.getEntity());
            this.metaDataService.setStringData(player, EntityMetadataType.INTERACTIVE_TAG, "");
            this.virtualEntityService.despawnToPlayer(removedEntity.getEntity(), player);
        }
    }

    @Override
    public boolean isExistedButton(Player player) {
        ButtonEntity updateEntity = BUTTON_OPENED.get(player.getIdentifiedStr());
        return updateEntity != null;
    }

    @Singleton
    private class LoginTest extends AbstractLevelEventHandle<PlayerJoinGameEvent> {

        @Override
        protected void onHandle(Level level, PlayerJoinGameEvent playerJoinGameEvent) {
            createButton(playerJoinGameEvent.getPlayer(), "测试按钮", (player, item) -> {
            });
        }

        @Override
        public Class getListenerEvent() {
            return PlayerJoinGameEvent.class;
        }

        @Override
        public EventRank getEventRank() {
            return EventRank.LOCAL;
        }
    }

    @Singleton
    private class ButtonUpdaterHelper extends AbstractLevelEventHandle<PlayerMoveEvent> {

        @Override
        protected void onHandle(Level level, PlayerMoveEvent playerMoveEvent) {
            Player player = playerMoveEvent.getPlayer();

            ButtonEntity buttonEntity = BUTTON_OPENED.get(player.getIdentifiedStr());

            if (buttonEntity != null) {
                Vector3f sourcePosition = positionService.getPosition(buttonEntity.getEntity());
                Vector3f targetPosition = positionService.getPosition(player);
                positionService.setPosition(buttonEntity.getEntity(), targetPosition);
                DataPacket moveEntityPacket = positionService.getMoveEntityPacket(buttonEntity.getEntity());
                if (moveEntityPacket != null) {
                    Vector3f subtract = targetPosition.subtract(sourcePosition);
                    if (subtract.lengthSquared() > MAX_TP_LENGTH_FLAG) {
                        ((MoveEntityPacket) moveEntityPacket).setTeleport(true);
                    }
                    networkManager.sendMessage(player.getClientAddress(), moveEntityPacket);
                }
            }
        }

        @Override
        public Class getListenerEvent() {
            return PlayerMoveEvent.class;
        }

        @Override
        public EventRank getEventRank() {
            return EventRank.LOCAL;
        }
    }

    @Singleton
    private class ButtonClearHelper extends AbstractLevelEventHandle<PlayerQuitGameEvent> {

        @Override
        protected void onHandle(Level level, PlayerQuitGameEvent playerQuitGameEvent) {
            closeButton(playerQuitGameEvent.getPlayer());
        }

        @Override
        public Class getListenerEvent() {
            return PlayerQuitGameEvent.class;
        }

        @Override
        public EventRank getEventRank() {
            return EventRank.LOCAL;
        }
    }
}
