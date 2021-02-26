package com.particle.game.entity.ai;

import com.particle.api.ai.behavior.EStatus;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.entity.ai.components.EntityDecisionModule;
import com.particle.game.entity.ai.service.EntityDecisionService;
import com.particle.game.entity.attribute.health.EntityHealthModule;
import com.particle.model.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ActionTreeSystemFactory implements ECSSystemFactory<ActionTreeSystemFactory.ActionTreeSystem> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionTreeSystem.class);

    private static final ECSModuleHandler<EntityDecisionModule> ENTITY_DECISION_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityDecisionModule.class);
    private static final ECSModuleHandler<EntityHealthModule> ENTITY_HEALTH_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityHealthModule.class);


    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{EntityDecisionModule.class};
    }

    @Override
    public ActionTreeSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new ActionTreeSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<ActionTreeSystem> getSystemClass() {
        return ActionTreeSystem.class;
    }

    public class ActionTreeSystem implements ECSSystem {


        private Entity entity;
        private EntityDecisionModule entityDecisionModule;

        public ActionTreeSystem(Entity entity) {
            this.entity = entity;
            this.entityDecisionModule = ENTITY_DECISION_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            EntityHealthModule entityHealthModule = ENTITY_HEALTH_MODULE_HANDLER.getModule(this.entity);
            if (entityHealthModule != null && entityHealthModule.isAlive()) {
                EStatus state = EntityDecisionService.makeActionDecision(entity, entityDecisionModule);

                if (state == EStatus.ABORTED) {
                    EntityDecisionService.resetActionDecision(entity, entityDecisionModule);
                    EntityDecisionService.makeDecision(entity, entityDecisionModule, false);
                }
            }
        }
    }
}
