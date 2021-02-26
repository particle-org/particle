package com.particle.game.entity.attribute.virus;

import com.particle.api.entity.attribute.EntityVirusServiceAPI;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.entity.Entity;

public class EntityVirusServiceProxy implements EntityVirusServiceAPI {

    private static final ECSModuleHandler<EntityVirusModule> ENTITY_VIRUS_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityVirusModule.class);


    @Override
    public boolean isInfectVirus(Entity entity) {
        EntityVirusModule module = ENTITY_VIRUS_MODULE_HANDLER.bindModule(entity);
        return module.isInfectVirous();
    }

    @Override
    public void infectVirus(Entity entity, long sourceEntityId, float infectDistance, int initVirusValue, int refreshInterval, int infectSpeed, int maxVirusValue) {
        EntityVirusModule module = ENTITY_VIRUS_MODULE_HANDLER.bindModule(entity);
        module.setInfectVirous(true);
        module.setSourceEntityId(sourceEntityId);
        module.setInfectDistance(infectDistance);
        module.setVirusValue(Math.min(initVirusValue, maxVirusValue));
        module.setRefreshInterval(refreshInterval);
        module.setInfectSpeed(infectSpeed);
        module.setLastRefreshTime(System.currentTimeMillis());
        module.setMaxVirusValue(maxVirusValue);
    }

    @Override
    public void clearVirus(Entity entity) {
        EntityVirusModule module = ENTITY_VIRUS_MODULE_HANDLER.bindModule(entity);
        module.setInfectVirous(false);
        module.setSourceEntityId(-1);
        module.setInfectDistance(0);
        module.setVirusValue(0);
        module.setRefreshInterval(Integer.MAX_VALUE);
        module.setInfectSpeed(0);
        module.setLastRefreshTime(System.currentTimeMillis());
        module.setMaxVirusValue(0);
    }

    @Override
    public int getVirusValue(Entity entity) {
        EntityVirusModule module = ENTITY_VIRUS_MODULE_HANDLER.bindModule(entity);
        return module.getVirusValue();
    }
}