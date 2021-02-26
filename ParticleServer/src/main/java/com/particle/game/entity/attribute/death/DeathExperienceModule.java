package com.particle.game.entity.attribute.death;

import com.particle.core.ecs.module.BehaviorModule;

public class DeathExperienceModule extends BehaviorModule {

    private int experience;

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
