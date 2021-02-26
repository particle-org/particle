package com.particle.game.entity.attribute.identified;

import com.particle.api.entity.attribute.EntityNameServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.entity.Entity;

import javax.inject.Singleton;

@Singleton
public class EntityNameService implements EntityNameServiceApi {

    private ECSModuleHandler<EntityNameModule> entityNameModuleHandler = ECSModuleHandler.buildHandler(EntityNameModule.class);

    @Override
    public void setEntityName(Entity entity, String entityName) {
        this.entityNameModuleHandler.getModule(entity).setEntityName(entityName);
    }

    @Override
    public String getEntityName(Entity entity) {
        return this.entityNameModuleHandler.getModule(entity).getEntityName();
    }

    @Override
    public String getDisplayEntityName(Entity entity) {
        return this.entityNameModuleHandler.getModule(entity).getDisplayEntityName();
    }

    @Override
    public void setDisplayEntityName(Entity entity, String displayEntityName) {
        this.entityNameModuleHandler.getModule(entity).setDisplayEntityName(displayEntityName);
    }
}
