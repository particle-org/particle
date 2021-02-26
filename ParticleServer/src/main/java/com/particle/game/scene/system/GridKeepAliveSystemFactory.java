package com.particle.game.scene.system;

import com.particle.core.aoi.SceneManager;
import com.particle.core.aoi.model.Grid;
import com.particle.core.aoi.model.Scene;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.scene.module.GridKeepAliveModule;

public class GridKeepAliveSystemFactory implements ECSSystemFactory<GridKeepAliveSystemFactory.GridKeepAliveSystem> {

    private static final ECSModuleHandler<GridKeepAliveModule> GRID_KEEP_ALIVE_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(GridKeepAliveModule.class);

    public class GridKeepAliveSystem implements ECSSystem {

        private GridKeepAliveModule gridKeepAliveModule;

        public GridKeepAliveSystem(GameObject gameObject) {
            this.gridKeepAliveModule = GRID_KEEP_ALIVE_MODULE_ECS_MODULE_HANDLER.getModule(gameObject);
        }

        @Override
        public void tick(long deltaTime) {
            boolean state = this.gridKeepAliveModule.ttl(deltaTime);

            if (!state) {
                Grid grid = this.gridKeepAliveModule.getGrid();
                Scene scene = grid.getScene();

                SceneManager.getInstance().recycleGridNode(scene, grid.getX(), grid.getZ());
            }
        }
    }

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{GridKeepAliveModule.class};
    }

    @Override
    public GridKeepAliveSystem buildECSSystem(GameObject gameObject) {
        return new GridKeepAliveSystem(gameObject);
    }

    @Override
    public Class<GridKeepAliveSystem> getSystemClass() {
        return GridKeepAliveSystem.class;
    }
}
