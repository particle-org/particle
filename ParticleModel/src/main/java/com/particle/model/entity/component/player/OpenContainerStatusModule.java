package com.particle.model.entity.component.player;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.inventory.common.ContainerType;

public class OpenContainerStatusModule extends BehaviorModule {

    private ContainerType currentOpenContainer = ContainerType.PLAYER;

    public ContainerType getCurrentOpenContainer() {
        return currentOpenContainer;
    }

    public void setCurrentOpenContainer(ContainerType currentOpenContainer) {
        this.currentOpenContainer = currentOpenContainer;
    }
}
