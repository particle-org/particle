package com.particle.game.entity.attribute.health;

import com.particle.api.entity.attribute.HealthServiceAPI;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.entity.Entity;
import com.particle.model.entity.attribute.EntityAttribute;
import com.particle.model.events.level.entity.EntityDamageType;

import javax.inject.Singleton;

@Singleton
public class HealthServiceProxy implements HealthServiceAPI {

    private static final ECSModuleHandler<EntityHealthModule> ENTITY_HEALTH_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityHealthModule.class);

    @Override
    public void initHealthComponent(Entity entity, float health) {
        EntityHealthModule healthModule = ENTITY_HEALTH_MODULE_HANDLER.bindModule(entity);
        healthModule.setHealth(health);
    }

    @Override
    public void initHealthComponent(Entity entity, float health, long minDamageInterval) {
        EntityHealthModule healthModule = ENTITY_HEALTH_MODULE_HANDLER.bindModule(entity);
        healthModule.setHealth(health);
        healthModule.updateMinDamageInterval(minDamageInterval);
    }

    @Override
    public float getHealth(Entity entity) {
        EntityHealthModule module = ENTITY_HEALTH_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            return module.getHealth();
        }

        return 0;
    }

    @Override
    public float getMaxHealth(Entity entity) {
        EntityHealthModule module = ENTITY_HEALTH_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            return module.getMaxHealth();
        }

        return 0;
    }

    @Override
    public void setHealth(Entity entity, float health) {
        EntityHealthModule module = ENTITY_HEALTH_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            module.setHealth(health);
        }
    }

    @Override
    public void setMaxHealth(Entity entity, float health) {
        EntityHealthModule module = ENTITY_HEALTH_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            module.setMaxHealth(health);
        }
    }

    @Override
    public float getAbsorption(Entity entity) {
        EntityHealthModule module = ENTITY_HEALTH_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            return module.getAbsorption();
        }
        return 0f;
    }

    @Override
    public void setAbsorption(Entity entity, float absorption) {
        EntityHealthModule module = ENTITY_HEALTH_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            module.setAbsorption(absorption);
        }
    }

    @Override
    public float getMaxAbsorption(Entity entity) {
        EntityHealthModule module = ENTITY_HEALTH_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            return module.getMaxAbsorption();
        }
        return 0f;
    }

    @Override
    public void setMaxAbsorption(Entity entity, float maxAbsorption) {
        EntityHealthModule module = ENTITY_HEALTH_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            module.setMaxAbsorption(maxAbsorption);
        }
    }

    @Override
    public boolean isAlive(Entity entity) {
        EntityHealthModule module = ENTITY_HEALTH_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            return module.isAlive();
        }

        return false;
    }

    public EntityAttribute getEntityHealthAttribute(Entity entity) {
        EntityHealthModule module = ENTITY_HEALTH_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            return module.getEntityHealthAttribute();
        }
        return null;
    }

    public EntityAttribute getEntityAbsorptionAttribute(Entity entity) {
        EntityHealthModule module = ENTITY_HEALTH_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            return module.getEntityAbsorptionAttribute();
        }
        return null;
    }


    /**
     * 对生物造成伤害
     *
     * @param entity
     * @param damage
     */
    @Override
    public void damageEntity(Entity entity, float damage, EntityDamageType damageType, Entity damager) {
        EntityHealthModule healthModule = ENTITY_HEALTH_MODULE_HANDLER.getModule(entity);
        if (healthModule == null || !healthModule.isAlive()) {
            return;
        }

        healthModule.damageEntity(damage, damageType, damager);
    }

    @Override
    public void killEntity(Entity entity, Entity damager) {
        EntityHealthModule.killEntity(entity, damager);
    }

    @Override
    public void resetHealth(Entity entity) {
        EntityHealthModule healthModule = ENTITY_HEALTH_MODULE_HANDLER.getModule(entity);
        if (healthModule == null) {
            return;
        }

        healthModule.resetHealth();
    }

    @Override
    public void healing(Entity entity, float health) {
        EntityHealthModule healthModule = ENTITY_HEALTH_MODULE_HANDLER.getModule(entity);
        if (healthModule == null) {
            return;
        }

        healthModule.healing(health);
    }
}
