package com.particle.game.entity.ai.leaf.decision;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.model.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class ClearEntityKnowledge implements IAction {

    private static final Logger logger = LoggerFactory.getLogger(ClearEntityKnowledge.class);

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    private Knowledge knowledge;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        entityDecisionServiceProxy.removeKnowledge(entity, knowledge);
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
        if (key.equalsIgnoreCase("knowledge")) {
            if (val instanceof String) {
                this.knowledge = Knowledge.parse((String) val);
                if (this.knowledge == null) {
                    logger.error(String.format("knowledge = %s not exist !", val));
                }
            } else {
                logger.error(String.format("knowledge type is Error !"));
            }
        }
    }
}