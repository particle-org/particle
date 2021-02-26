package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.EntityDecisionServiceApi;
import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.attack.component.EntityAttackModule;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class EntitySetAttackIntervalAction implements IAction {

    @Inject
    private EntityDecisionServiceApi entityDecisionServiceApi;


    private static final ECSModuleHandler<EntityAttackModule> ENTITY_ATTACK_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityAttackModule.class);

    /**
     * 冷却时间，时间单位:ms
     */
    private long attackInterval = 1000L;

    @Override
    public void onInitialize() {
    }

    @Override
    public EStatus tick(Entity entity) {
        EntityAttackModule entityAttackModule = ENTITY_ATTACK_MODULE_HANDLER.bindModule(entity);
        entityAttackModule.setAttackInterval(attackInterval);

        return EStatus.SUCCESS;
    }

    @Override
    public void onTicked(Entity entity, EStatus status) {
    }

    @Override
    public void onRelease() {
    }

    @Override
    public void config(String key, Object val) {
        if (key.equalsIgnoreCase("AttackInterval") && val instanceof Long) {
            this.attackInterval = (Long) val;
        }
    }
}
