package com.particle.game.common.modules;

import com.particle.core.ecs.module.ECSModuleBindSingleComponent;
import com.particle.game.common.components.NBTTagCompoundComponent;
import com.particle.model.nbt.NBTTagCompound;

public class NBTTagCompoundModule extends ECSModuleBindSingleComponent<NBTTagCompoundComponent> {

    public NBTTagCompound getNbtTagCompound() {
        return this.component.getNbtTagCompound();
    }

    @Override
    protected Class<NBTTagCompoundComponent> getTypeClass() {
        return NBTTagCompoundComponent.class;
    }
}
