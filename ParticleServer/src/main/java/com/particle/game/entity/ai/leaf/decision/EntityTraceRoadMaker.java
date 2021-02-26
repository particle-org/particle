package com.particle.game.entity.ai.leaf.decision;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.level.LevelService;
import com.particle.game.world.nav.NavMeshService;
import com.particle.game.world.nav.NavSearchService;
import com.particle.model.entity.Entity;
import com.particle.model.level.chunk.NavSquare;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;
import java.util.List;

public class EntityTraceRoadMaker implements IAction {

    @Inject
    private LevelService levelService;

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PositionService positionService;

    @Inject
    private NavSearchService navSearchService;

    @Inject
    private NavMeshService navMeshService;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        // 检查是否有合法目标
        Vector3f targetPosition = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.POSITION_TARGET, Vector3f.class);
        if (targetPosition == null) {
            return EStatus.FAILURE;
        }
        Vector3 targetFloorPosition = new Vector3(targetPosition);
        targetFloorPosition.setY(this.levelService.getTopBlockHeightBelow(entity.getLevel(), targetFloorPosition));
        NavSquare targetNav = navMeshService.findNavSquare(entity.getLevel(), targetFloorPosition);
        // 搜索不到NavSquaere
        if (targetNav == null) {
            return EStatus.FAILURE;
        }

        Vector3f currentPosition = this.positionService.getPosition(entity);
        if (currentPosition == null) {
            return EStatus.FAILURE;
        }
        Vector3 currentFloorPosition = new Vector3(currentPosition);
        currentFloorPosition.setY(this.levelService.getTopBlockHeightBelow(entity.getLevel(), currentFloorPosition));
        NavSquare currentNav = navMeshService.findNavSquare(entity.getLevel(), currentFloorPosition);
        // 搜索不到NavSquaere
        if (currentNav == null) {
            return EStatus.FAILURE;
        }

        // 搜索路径点
        List<NavSquare> search = this.navSearchService.search(currentNav, targetNav, currentFloorPosition, targetFloorPosition);
        List<Vector3> roadPoints = this.navSearchService.getRoadPoints(search, currentFloorPosition, targetFloorPosition);
        this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.ROAD_POINTS, roadPoints);

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
