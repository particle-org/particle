package com.particle.game.scene.module;

import com.particle.core.aoi.model.Grid;
import com.particle.core.aoi.model.Scene;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.component.ECSComponent;
import com.particle.core.ecs.component.ECSComponentHandler;
import com.particle.core.ecs.module.ECSModule;
import com.particle.game.scene.components.GridBinderComponent;

public class GridBinderModule extends ECSModule {

    private GridBinderComponent gridBinderComponent;

    /**
     * 将gameobject从scene中移除
     */
    public void unregisterGameObject() {
        // 移除组件
        this.gridBinderComponent.setGrid(null);
    }

    /**
     * 查询GameObject所在的node
     *
     * @return
     */
    public Grid getGrid() {
        return this.gridBinderComponent.getGrid();
    }

    /**
     * 更新Grid
     */
    public void updateGrid(Grid grid) {
        this.gridBinderComponent.setGrid(grid);
    }

    /**
     * 查询GameObject所在的Scene
     *
     * @return
     */
    public Scene getScene() {
        return this.gridBinderComponent.getScene();
    }

    /**
     * 将GameObject注册到Scene中
     *
     * @param scene
     */
    public void updateScene(Scene scene) {
        // 配置组件
        this.gridBinderComponent.setScene(scene);
    }


    @Override
    public Class<? extends ECSComponent>[] requestComponents() {
        return new Class[]{GridBinderComponent.class};
    }

    @Override
    protected void bindGameObject(GameObject gameObject) {
        this.gridBinderComponent = ECSComponentHandler.buildHandler(GridBinderComponent.class).getOrCreateComponent(gameObject);
    }
}
