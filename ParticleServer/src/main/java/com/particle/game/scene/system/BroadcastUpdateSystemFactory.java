package com.particle.game.scene.system;

import com.particle.core.aoi.SceneManager;
import com.particle.core.aoi.model.Grid;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.scene.module.BroadcastModule;
import com.particle.game.scene.module.GridBinderModule;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class BroadcastUpdateSystemFactory implements ECSSystemFactory<BroadcastUpdateSystemFactory.GridBinderSystem> {

    private static final ECSModuleHandler<GridBinderModule> GRID_BINDER_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(GridBinderModule.class);
    private static final ECSModuleHandler<BroadcastModule> BROADCAST_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(BroadcastModule.class);


    private static final long FORCE_UPDATE_INTERVAL = 5000;

    public class GridBinderSystem implements ECSSystem {

        private GridBinderModule gridBinderModule;
        private BroadcastModule broadcastModule;

        private GameObject gameObject;

        private long forceRefreshTtl = FORCE_UPDATE_INTERVAL;

        public GridBinderSystem(GameObject gameObject) {
            this.gridBinderModule = GRID_BINDER_MODULE_ECS_MODULE_HANDLER.getModule(gameObject);
            this.broadcastModule = BROADCAST_MODULE_ECS_MODULE_HANDLER.getModule(gameObject);

            this.gameObject = gameObject;
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
            if (this.broadcastModule.getCurrentGrid() == null || this.broadcastModule.getCurrentGrid() != currentGrid || this.forceRefreshTtl < 1 || this.broadcastModule.isForceUpdate()) {
                Set<InetSocketAddress> subscribers = new HashSet<>(currentGrid.getSubscribers());

                // 移除自己
                if (broadcastModule.getAddress() != null) {
                    subscribers.remove(broadcastModule.getAddress());
                }

                // 确认不会订阅自己
                subscribers.remove(this.broadcastModule.getAddress());

                this.broadcastModule.setCurrentGrid(currentGrid);
                sceneManager.refreshSubscribers(this.broadcastModule.getBroadcaster(), subscribers);
                this.forceRefreshTtl = FORCE_UPDATE_INTERVAL;

                // 关闭强制刷新
                this.broadcastModule.cancelForceUpdate();
            } else {
                this.forceRefreshTtl -= deltaTime;
            }
        }
    }


    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{GridBinderModule.class, BroadcastModule.class};
    }

    @Override
    public GridBinderSystem buildECSSystem(GameObject gameObject) {
        return new GridBinderSystem(gameObject);
    }

    @Override
    public Class<GridBinderSystem> getSystemClass() {
        return GridBinderSystem.class;
    }
}
