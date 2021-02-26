package com.particle.game.player.save;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.MongoWriteException;
import com.particle.api.entity.IPlayerDataServiceApi;
import com.particle.core.ecs.serialization.SerializationTool;
import com.particle.game.entity.attribute.identified.EntityNameService;
import com.particle.game.player.PlayerService;
import com.particle.game.player.save.loader.PlayerDatabaseProvider;
import com.particle.game.utils.config.ServerConfigService;
import com.particle.game.utils.ecs.ECSComponentManager;
import com.particle.model.entity.component.ECSComponent;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class PlayerDataService implements IPlayerDataServiceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerDataService.class);

    @Inject
    private PlayerService playerService;

    @Inject
    private EntityNameService entityNameService;

    @Inject
    private ECSComponentManager ecsComponentManager;

    @Inject
    private ServerConfigService serverConfigService;

    @Inject
    private PlayerDatabaseProvider playerDatabaseProvider;

    /**
     * 保存玩家数据
     *
     * @param player
     */
    @Override
    public void save(Player player) {
        this.save(player, false, null);
    }

    public void save(Player player, boolean release, IPlayerDataSaveCallback callback) {
        String uuid = playerService.getPlayerUUID(player).toString();
        // 若玩家沒有完整登入，則取消存檔，避免空存檔
        if (!player.isSaveLoaded()) {
            if (release) {
                this.playerDatabaseProvider.get().releaseLock(uuid);
            }
            return;
        }

        // 若配置不給存檔
        if (!serverConfigService.isPlayerSave()) {
            if (release) {
                this.playerDatabaseProvider.get().releaseLock(uuid);
            }
            return;
        }

        // 保存数据
        // 导出所有组件持久化数据
        Map<String, String> componentsData = new HashMap<>();
        for (ECSComponent component : player.getComponents()) {
            if (component != null) {
                String data = this.ecsComponentManager.exportComponent(component);

                if (data != null) {
                    componentsData.put(component.getName(), data);
                }
            }
        }
        Map<String, String> ecsData = SerializationTool.exportModuleData(player);
        if (serverConfigService.isSaveOldData()) {
            // 將緩存的存檔寫入
            Map<Long, Map<String, String>> tempSaveData = SerializationTool.getTempSaveData();
            if (tempSaveData != null) {
                Map<String, String> saveData = tempSaveData.get(player.ID);
                if (saveData != null) {
                    ecsData.putAll(saveData);
                }
            }
        }

        UUID playerUUID = this.playerService.getPlayerUUID(player);
        if (playerUUID != null) {
            try {
                this.playerDatabaseProvider.get().savePlayerData(playerUUID.toString(), this.entityNameService.getEntityName(player), componentsData, ecsData, release);
                if (release) {
                    // 釋放
                    SerializationTool.getTempSaveData().remove(player.ID);
                }

                if (callback != null) {
                    callback.handle(PlayerDataOperateState.SUCCESS);
                }
            } catch (MongoWriteException e) {
                LOGGER.error("Fail to save player {} data, because of lock missing!", player.getIdentifiedStr(), e);

                if (callback != null) {
                    callback.handle(PlayerDataOperateState.EXCEPTION);
                }
            } catch (Exception e) {
                LOGGER.error("Fail to save player {} data.", player.getIdentifiedStr(), e);

                if (callback != null) {
                    callback.handle(PlayerDataOperateState.EXCEPTION);
                }
            }
        }
    }


    public void loadPlayerData(Player player, IPlayerDataSaveCallback callback) {
        // 若不讀存檔
        if (!serverConfigService.isPlayerLoad()) {
            // 回调
            if (callback != null) {
                callback.handle(PlayerDataOperateState.SUCCESS);
            }

            // 标记存档读取情况
            player.setSaveLoaded(true);
            return;
        }

        boolean hasData = false;

        UUID playerUUID = this.playerService.getPlayerUUID(player);
        // 启动读取数据的任务
        Map<String, String> componentsData = null;
        try {
            // 异步读取数据
            componentsData = this.playerDatabaseProvider.get().loadPlayerDataByUUID(playerUUID.toString());

            // 解析组件
            if (componentsData != null && !componentsData.isEmpty()) {
                for (Map.Entry<String, String> dataEntry : componentsData.entrySet()) {
                    this.ecsComponentManager.importECSComponent(player, dataEntry.getValue());

                    if (ecsComponentManager.isDepressed(dataEntry.getKey())) {
                        this.playerDatabaseProvider.get().removePlayerData(playerUUID.toString(), dataEntry.getKey());
                    }

                }

                hasData = true;
            }

        } catch (Exception e) {
            LOGGER.error("Fail to load player {} data.", player.getIdentifiedStr(), e);

            if (callback != null) {
                callback.handle(PlayerDataOperateState.EXCEPTION);
            }
            return;
        }

        try {
            // 异步读取数据
            componentsData = this.playerDatabaseProvider.get().loadPlayerECSDataByUUID(playerUUID.toString());

            // 解析组件
            if (componentsData != null && !componentsData.isEmpty()) {
                for (Map.Entry<String, String> dataEntry : componentsData.entrySet()) {
                    SerializationTool.importModuleData(player, dataEntry.getKey(), dataEntry.getValue());
                }

                hasData = true;
            }

        } catch (Exception e) {
            LOGGER.error("Fail to load player {} data.", player.getIdentifiedStr(), e);

            if (callback != null) {
                callback.handle(PlayerDataOperateState.EXCEPTION);
            }
            return;
        }

        // 回调
        if (callback != null) {
            callback.handle(hasData ? PlayerDataOperateState.SUCCESS : PlayerDataOperateState.NOT_EXIST);
        }

        // 标记存档读取情况
        player.setSaveLoaded(true);
    }
}
