package com.particle.game.entity.ai.leaf.mark;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.world.particle.ParticleService;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;
import com.particle.model.particle.ParticleType;

import javax.inject.Inject;

public class EntityTargetMark implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private ParticleService particleService;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        // 检查是否有合法目标
        Vector3f targetPosition = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.POSITION_TARGET, Vector3f.class);
        if (targetPosition != null) {
            this.particleService.playParticle(entity.getLevel(), ParticleType.TYPE_SMOKE, new Vector3f(targetPosition));
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
