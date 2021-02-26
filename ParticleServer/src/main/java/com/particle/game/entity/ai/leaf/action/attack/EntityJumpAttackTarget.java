package com.particle.game.entity.ai.leaf.action.attack;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.attack.EntityAttackedHandleService;
import com.particle.game.entity.attack.component.EntityAttackModule;
import com.particle.game.entity.movement.PositionService;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;

public class EntityJumpAttackTarget implements IAction {

    private static final ECSModuleHandler<EntityAttackModule> ENTITY_ATTACK_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityAttackModule.class);

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private EntityAttackedHandleService entityAttackedHandleService;

    @Inject
    private PositionService positionService;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Entity entityTarget = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_TARGET, Entity.class);

        if (entityTarget != null) {
            Vector3f targetPosition = this.positionService.getPosition(entityTarget);
            Vector3f entityPosition = this.positionService.getPosition(entity);

            Vector3f jumpVector = targetPosition.subtract(entityPosition);
            jumpVector.setY(0);
            jumpVector = jumpVector.normalize().multiply(1);
            jumpVector.setY(1f);

            // 这里和Attacked会做两次检查
            EntityAttackModule entityAttackModule = ENTITY_ATTACK_MODULE_HANDLER.getModule(entity);
            if (entityAttackModule != null && entityAttackModule.canAttack()) {
                this.positionService.setPosition(entity, entityPosition.add(jumpVector));

                this.entityAttackedHandleService.entityAttackedByEntity(entity, entityTarget);
            }

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

    }
}
