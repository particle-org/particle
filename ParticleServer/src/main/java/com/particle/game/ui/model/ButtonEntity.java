package com.particle.game.ui.model;

import com.particle.model.entity.model.mob.MobEntity;

public class ButtonEntity {

    private MobEntity entity;

    private String showName;

    public ButtonEntity(MobEntity entity, String showName) {
        this.entity = entity;
        this.showName = showName;
    }

    public MobEntity getEntity() {
        return entity;
    }

    public String getShowName() {
        return showName;
    }
}
