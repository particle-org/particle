package com.particle.game.world.aoi;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.core.ecs.system.IntervalECSSystem;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.scene.module.SubscriberModule;
import com.particle.game.world.aoi.components.PlayerAutoChunkSubscriberModule;

public class PlayerAutoChunkSubscriberRadiusUpdateSystemFactory implements ECSSystemFactory<PlayerAutoChunkSubscriberRadiusUpdateSystemFactory.PlayerAutoChunkSubscriberRadiusUpdateSystem> {

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);
    private static final ECSModuleHandler<SubscriberModule> SUBSCRIBER_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(SubscriberModule.class);
    private static final ECSModuleHandler<PlayerAutoChunkSubscriberModule> PLAYER_AUTO_CHUNK_SUBSCRIBER_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlayerAutoChunkSubscriberModule.class);

    public class PlayerAutoChunkSubscriberRadiusUpdateSystem extends IntervalECSSystem {

        private TransformModule transformModule;
        private SubscriberModule subscriberModule;
        private PlayerAutoChunkSubscriberModule playerAutoChunkSubscriberModule;

        public PlayerAutoChunkSubscriberRadiusUpdateSystem(GameObject gameObject) {
            this.transformModule = TRANSFORM_MODULE_ECS_MODULE_HANDLER.getModule(gameObject);
            this.subscriberModule = SUBSCRIBER_MODULE_ECS_MODULE_HANDLER.getModule(gameObject);
            this.playerAutoChunkSubscriberModule = PLAYER_AUTO_CHUNK_SUBSCRIBER_MODULE_ECS_MODULE_HANDLER.getModule(gameObject);
        }

        @Override
        protected int getInterval() {
            return 39;
        }

        @Override
        protected void doTick(long deltaTime) {
            boolean needUpdate = this.playerAutoChunkSubscriberModule.checkForUpdate(this.transformModule.getPosition());
            if (needUpdate) {
                PlayerNetworkChunkPublisherUpdateService.updateRadius(this.transformModule, this.subscriberModule);
            }
        }
    }


    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{TransformModule.class, SubscriberModule.class, PlayerAutoChunkSubscriberModule.class};
    }

    @Override
    public PlayerAutoChunkSubscriberRadiusUpdateSystem buildECSSystem(GameObject gameObject) {
        return new PlayerAutoChunkSubscriberRadiusUpdateSystem(gameObject);
    }

    @Override
    public Class<PlayerAutoChunkSubscriberRadiusUpdateSystem> getSystemClass() {
        return PlayerAutoChunkSubscriberRadiusUpdateSystem.class;
    }
}
