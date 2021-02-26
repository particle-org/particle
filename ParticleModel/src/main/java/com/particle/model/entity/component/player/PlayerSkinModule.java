package com.particle.model.entity.component.player;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.entity.model.skin.PlayerSkinData;

public class PlayerSkinModule extends BehaviorModule {
    private PlayerSkinData baseSkinData;
    private PlayerSkinData additionSkinData;

    public PlayerSkinData getBaseSkinData() {
        return baseSkinData;
    }

    public void setBaseSkinData(PlayerSkinData baseSkinData) {
        this.baseSkinData = baseSkinData;
    }

    public PlayerSkinData getAdditionSkinData() {
        return additionSkinData;
    }

    public void setAdditionSkinData(PlayerSkinData additionSkinData) {
        this.additionSkinData = additionSkinData;
    }
}
