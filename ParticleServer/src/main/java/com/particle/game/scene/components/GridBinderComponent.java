package com.particle.game.scene.components;

import com.particle.core.aoi.model.Grid;
import com.particle.core.aoi.model.Scene;
import com.particle.core.ecs.component.ECSComponent;

public class GridBinderComponent implements ECSComponent {

    // 当前所处的scene
    private Scene scene;

    // 当前所处的gride
    private Grid grid;

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }
}
