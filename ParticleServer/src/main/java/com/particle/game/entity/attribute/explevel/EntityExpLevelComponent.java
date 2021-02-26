package com.particle.game.entity.attribute.explevel;

import com.particle.core.ecs.component.ECSComponent;

public class EntityExpLevelComponent implements ECSComponent {

    private int level = 0;

    private int maxLevel = 24791;

    private int minLevel = 0;

    private int lastSpendLevel = 0;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }

    public int getLastSpendLevel() {
        return lastSpendLevel;
    }

    public void setLastSpendLevel(int lastSpendLevel) {
        this.lastSpendLevel = lastSpendLevel;
    }
}
