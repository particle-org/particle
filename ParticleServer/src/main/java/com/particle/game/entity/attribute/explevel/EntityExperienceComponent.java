package com.particle.game.entity.attribute.explevel;

import com.particle.core.ecs.component.ECSComponent;

public class EntityExperienceComponent implements ECSComponent {

    private int experience = 0;

    private int minExperience = 0;
    private int maxExperience = 100;

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getMinExperience() {
        return minExperience;
    }

    public void setMinExperience(int minExperience) {
        this.minExperience = minExperience;
    }

    public int getMaxExperience() {
        return maxExperience;
    }

    public void setMaxExperience(int maxExperience) {
        this.maxExperience = maxExperience;
    }
}
