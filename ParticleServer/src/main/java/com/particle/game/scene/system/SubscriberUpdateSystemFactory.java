package com.particle.game.scene.system;

import com.particle.core.aoi.SceneManager;
import com.particle.core.aoi.model.Grid;
import com.particle.core.aoi.model.Subscriber;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.scene.module.GridBinderModule;
import com.particle.game.scene.module.SubscriberModule;

public class SubscriberUpdateSystemFactory implements ECSSystemFactory<SubscriberUpdateSystemFactory.SubscriberUpdateSystem> {

    private static final ECSModuleHandler<GridBinderModule> GRID_BINDER_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(GridBinderModule.class);
    private static final ECSModuleHandler<SubscriberModule> SUBSCRIBER_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(SubscriberModule.class);

    public class SubscriberUpdateSystem implements ECSSystem {

        private GridBinderModule gridBinderModule;
        private SubscriberModule subscriberModule;

        public SubscriberUpdateSystem(GameObject gameObject) {
            this.gridBinderModule = GRID_BINDER_MODULE_ECS_MODULE_HANDLER.getModule(gameObject);
            this.subscriberModule = SUBSCRIBER_MODULE_ECS_MODULE_HANDLER.getModule(gameObject);
        }

        @Override
        public void tick(long deltaTime) {
            // 查询当前的节点
            Grid currentGrid = this.gridBinderModule.getGrid();

            // 节点不存在
            if (currentGrid == null) {
                return;
            }

            // 获取SceneManager
            SceneManager sceneManager = SceneManager.getInstance();

            // 如果节点变化，则刷新aoi
            Subscriber subscriber = this.subscriberModule.getSubscriber();
            if (subscriber.getBaseGrid() == null || subscriber.getBaseGrid() != currentGrid) {
                sceneManager.refreshSubscriptGrids(subscriber, currentGrid);
            }
        }
    }


    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{GridBinderModule.class, SubscriberModule.class};
    }

    @Override
    public SubscriberUpdateSystem buildECSSystem(GameObject gameObject) {
        return new SubscriberUpdateSystem(gameObject);
    }

    @Override
    public Class<SubscriberUpdateSystem> getSystemClass() {
        return SubscriberUpdateSystem.class;
    }
}
