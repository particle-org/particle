package com.particle.game.entity.attribute;

import com.particle.api.entity.IEntityNBTComponentServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.NBTTagCompoundModule;
import com.particle.model.entity.Entity;
import com.particle.model.nbt.NBTTagCompound;

import javax.inject.Singleton;

@Singleton
public class EntityNBTComponentServiceProxy implements IEntityNBTComponentServiceApi {

    private static final ECSModuleHandler<NBTTagCompoundModule> NBT_TAG_COMPOUND_MODULE_HANDLER = ECSModuleHandler.buildHandler(NBTTagCompoundModule.class);

    @Override
    public NBTTagCompound getNBTData(Entity entity) {
        NBTTagCompoundModule nbtTagCompoundModule = NBT_TAG_COMPOUND_MODULE_HANDLER.getModule(entity);

        if (nbtTagCompoundModule != null) {
            return nbtTagCompoundModule.getNbtTagCompound();
        }

        return null;
    }

}
