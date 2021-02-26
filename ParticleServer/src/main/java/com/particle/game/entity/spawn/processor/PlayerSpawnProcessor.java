package com.particle.game.entity.spawn.processor;

import com.particle.api.entity.function.ISpawnEntityProcessor;
import com.particle.core.aoi.SceneManager;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.player.PlayerService;
import com.particle.game.scene.module.SubscriberModule;
import com.particle.game.server.Server;
import com.particle.game.world.level.LevelService;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.level.settings.LevelSettings;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;
import com.particle.model.player.PlayerState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlayerSpawnProcessor extends EntitySpawnProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerSpawnProcessor.class);

    private static final ECSModuleHandler<SubscriberModule> SUBSCRIBER_MODULE_HANDLER = ECSModuleHandler.buildHandler(SubscriberModule.class);


    // ----- 核心Service -----
    @Inject
    private Server server;
    @Inject
    private PositionService positionService;

    // ----- 世界相关service -----
    @Inject
    private LevelService levelService;

    // ----- 玩家业务相关service -----
    @Inject
    private PlayerService playerService;

    public ISpawnEntityProcessor getEntitySpawnProcessor(Player entity) {
        return new SpawnProcess(entity);
    }

    private class SpawnProcess implements ISpawnEntityProcessor {

        private Player player;

        public SpawnProcess(Player player) {
            this.player = player;
        }

        /**
         * 将玩家spawn到世界中
         * 处理内容包括：
         * 1) 注册区块监听
         * 2) 发送区块数据
         * 3) 区块内玩家之间相互spawn
         */
        @Override
        public void spawn(Level level, Chunk chunk) {
            LOGGER.debug("Spawn player {} at chunk({},{})", player.getIdentifiedStr(), chunk.getxPos(), chunk.getzPos());

            // 将玩家注册进入区块
            if (!chunk.getPlayersCollection().registerEntity(player)) {
                // 玩家注册失败
                server.close(player, "duplicated spawn");
                return;
            }

            // 初始化模块
            SubscriberModule subscriberModule = SUBSCRIBER_MODULE_HANDLER.bindModule(player);
            subscriberModule.setClientVersion(player.getProtocolVersion());
            LevelSettings levelSettings = level.getLevelSettings();

            // 计算安全地点
            level.getLevelSchedule().scheduleDelayTask("PlayerSpawnPositionCheck", () -> {
                if (player.getPlayerState() == PlayerState.SPAWNED) {
                    Vector3f position = positionService.getPosition(player);

                    if (chunk.isRunning()) {
                        float safeHeight = levelService.getSafePositionAbove(level, position);
                        position.setY(safeHeight);

                        playerService.teleport(player, position);
                    }
                }
            }, 500);

            // 订阅模块要延迟一些启动，不然区块发早了，客户端不认
            level.getLevelSchedule().scheduleDelayTask("PlayerSubscriptModuleInit", () -> {
                if (player.getPlayerState() == PlayerState.SPAWNED) {
                    // 初始化模块
                    subscriberModule.changeScene(level.getScene(), levelSettings.getChunkLoadRadius(), levelSettings.getChunkUnloadRadius(), player.getClientAddress());
                }
            }, 1000);
        }

        @Override
        public void respawn(Level level, Chunk from, Chunk to) {
            from.getPlayersCollection().removeEntity(player.getRuntimeId());

            if (!to.getPlayersCollection().registerEntity(player)) {
                LOGGER.error("Fail to respawn entity {}", player.getRuntimeId());
            }
        }

        /**
         * 将玩家从世界中移除
         * 处理内容包括：
         * 1) 取消玩家注册区块
         * 2) 清空订阅列表
         */
        @Override
        public void despawn(Chunk chunk) {
            chunk.getPlayersCollection().removeEntity(player.getRuntimeId());

            SubscriberModule subscriberModule = SUBSCRIBER_MODULE_HANDLER.getModule(player);
            if (subscriberModule != null) {
                SceneManager.getInstance().clearSubscriptData(subscriberModule.getSubscriber());
                subscriberModule.leaveScene();
            }
        }
    }

}
