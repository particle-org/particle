package com.particle.game.entity.attribute.fossilization;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.entity.Entity;

import javax.inject.Singleton;

@Singleton
public class EntityFossilizationServiceProxy {

    private static final ECSModuleHandler<EntityFossilizationModule> MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityFossilizationModule.class);

    public boolean isFossilization(Entity entity) {
        EntityFossilizationModule module = MODULE_HANDLER.bindModule(entity);
        return module.isFossilization();
    }


    public void fossilization(Entity entity, long fossilizationEndTime) {
        EntityFossilizationModule module = MODULE_HANDLER.bindModule(entity);
        module.setFossilization(true);
        module.setFossilizationEndTime(fossilizationEndTime);
    }

    public void relieveFossilization(Entity entity) {
        EntityFossilizationModule module = MODULE_HANDLER.bindModule(entity);
        module.setFossilization(false);
        module.setFossilizationEndTime(-1);
    }
}