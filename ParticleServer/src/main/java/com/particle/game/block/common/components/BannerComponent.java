package com.particle.game.block.common.components;

import com.particle.core.ecs.component.ECSComponent;
import com.particle.model.nbt.NBTTagList;

public class BannerComponent implements ECSComponent {

    private int base = 0;
    private NBTTagList patterns = new NBTTagList();

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public NBTTagList getPatterns() {
        return patterns;
    }

    public void setPatterns(NBTTagList patterns) {
        this.patterns = patterns;
    }
}
