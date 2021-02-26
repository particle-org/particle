package com.particle.game.scene.system;

import com.particle.core.aoi.SceneManager;
import com.particle.core.aoi.model.Grid;
import com.particle.core.aoi.model.Scene;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.scene.module.GridBinderModule;

public class GridBinderUpdateSystemFactory implements ECSSystemFactory<GridBinderUpdateSystemFactory.InnerSystem> {

    private static final ECSModuleHandler<GridBinderModule> GRID_BINDER_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(GridBinderModule.class);
    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);


    public class InnerSystem implements ECSSystem {

        private GridBinderModule gridBinderModule;
        private TransformModule transformModule;

        public InnerSystem(GameObject gameObject) {
            this.gridBinderModule = GRID_BINDER_MODULE_ECS_MODULE_HANDLER.getModule(gameObject);
            this.transformModule = TRANSFORM_MODULE_ECS_MODULE_HANDLER.getModule(gameObject);
        }

        @Override
        public void tick(long deltaTime) {
            // 查询当前的节点
            Grid currentGrid = this.gridBinderModule.getGrid();

            // 获取scene
            Scene scene = this.gridBinderModule.getScene();

            // 获取SceneManager
            SceneManager sceneManager = SceneManager.getInstance();

            // 计算index
            int indexX = (this.transformModule.getFloorX()) / Grid.GRID_WIDTH;
            int indexZ = (this.transformModule.getFloorZ()) / Grid.GRID_WIDTH;

            // 检查是否发生移动
            if (currentGrid == null || currentGrid.getX() != indexX || currentGrid.getZ() != indexZ) {
                // 未发生移动，直接返回
                currentGrid = sceneManager.getGridNode(scene, indexX, indexZ, true);
                this.gridBinderModule.updateGrid(currentGrid);
            }
        }
    }


    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{GridBinderModule.class, TransformModule.class};
    }

    @Override
    public InnerSystem buildECSSystem(GameObject gameObject) {
        return new InnerSystem(gameObject);
    }

    @Override
    public Class<InnerSystem> getSystemClass() {
        return InnerSystem.class;
    }
}
