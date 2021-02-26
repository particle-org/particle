package com.particle.game.common.components;

import com.particle.core.ecs.component.ECSComponent;
import com.particle.model.nbt.NBTTagCompound;

public class NBTTagCompoundComponent implements ECSComponent {
    private NBTTagCompound nbtTagCompound = new NBTTagCompound();

    public NBTTagCompound getNbtTagCompound() {
        return nbtTagCompound;
    }

    public void setNbtTagCompound(NBTTagCompound nbtTagCompound) {
        this.nbtTagCompound = nbtTagCompound;
    }
}
