package com.particle.game.entity.ai.leaf.mark;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.particle.AdvanceParticleService;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.particle.ParticleType;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class EntityRoadPointsMark implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PositionService positionService;

    @Inject
    private AdvanceParticleService particleService;

    private int cooldown;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        if (cooldown != 0) {
            cooldown--;
            return EStatus.SUCCESS;
        } else {
            cooldown = 1;
        }

        // 检查是否有合法目标
        List<Vector3> roadPoints = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ROAD_POINTS, List.class);

        if (roadPoints != null) {
            List<Vector3f> lines = new ArrayList<>(roadPoints.size());
            lines.add(0, this.positionService.getPosition(entity));
            for (Vector3 point : roadPoints) {
                lines.add(new Vector3f(point).add(0.5f, 1, 0.5f));
                this.particleService.markLines(entity.getLevel(), lines, ParticleType.TYPE_REDSTONE);
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
