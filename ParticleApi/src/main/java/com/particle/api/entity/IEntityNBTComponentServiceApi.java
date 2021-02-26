package com.particle.api.entity;

import com.particle.model.entity.Entity;
import com.particle.model.nbt.NBTTagCompound;

public interface IEntityNBTComponentServiceApi {
    NBTTagCompound getNBTData(Entity entity);
}
