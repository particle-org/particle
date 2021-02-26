package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.attack.EntityAttackService;
import com.particle.game.entity.attack.component.EntityAttackModule;
import com.particle.game.entity.attribute.health.HealthServiceProxy;
import com.particle.model.entity.Entity;
import com.particle.model.events.level.entity.EntityDamageType;
import com.particle.model.player.Player;

import javax.inject.Inject;

public class EntityTurnHurtAction implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private EntityAttackService entityAttackService;

    @Inject
    private HealthServiceProxy healthServiceProxy;

    private static final ECSModuleHandler<EntityAttackModule> ENTITY_ATTACK_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityAttackModule.class);

    private float multiple = 1.0F;

    @Override
    public void onInitialize() {
    }

    @Override
    public EStatus tick(Entity entity) {
        Entity entityTarget = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_CRIMINAL, Entity.class);
        if (entityTarget != null && entityTarget instanceof Player) {
            EntityAttackModule entityAttackModule = ENTITY_ATTACK_MODULE_HANDLER.bindModule(entityTarget);

            float baseDamage = entityAttackModule.getBaseDamage();
            float damage = baseDamage * multiple;

            this.healthServiceProxy.damageEntity(entityTarget, damage, EntityDamageType.EntityAttack, entity);
        }

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
        if (key.equalsIgnoreCase("Multiple") && val instanceof Float) {
            this.multiple = (Float) val;
        }
    }
}
