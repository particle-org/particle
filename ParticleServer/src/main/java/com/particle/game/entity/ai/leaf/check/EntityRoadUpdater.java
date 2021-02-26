package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.ICondition;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

public class EntityRoadUpdater implements ICondition {

    private static final Logger logger = LoggerFactory.getLogger(EntityRoadUpdater.class);

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PositionService positionService;

    private float checkDistance = 2;
    private float traceDistance = 18;

    private int cooldown;

    @Override
    public void onInitialize() {
        this.checkDistance = this.checkDistance * this.checkDistance;
        this.traceDistance = this.traceDistance * this.traceDistance;
    }

    @Override
    public EStatus tick(Entity entity) {

        if (cooldown != 0) {
            cooldown--;
            return EStatus.SUCCESS;
        } else {
            cooldown = 3;
        }

        Vector3f entityPosition = this.positionService.getPosition(entity);

        // Step 1 : 检查是否有合法目标
        List<Vector3> roadPoints = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ROAD_POINTS, List.class);
        // 如果路径点都用完了，则直接返回
        if (roadPoints == null || roadPoints.size() == 0) {
            logFail(entityPosition, null, roadPoints, "Missing Points");
            return EStatus.FAILURE;
        }

        // Step 2 : 检查当前生物是否在路径上
        Vector3f currentTarget = new Vector3f(roadPoints.get(0)).add(0.5f, 0.5f, 0.5f);
        if (new Vector3f(currentTarget.getX() - entityPosition.getX(), 0, currentTarget.getZ() - entityPosition.getZ()).lengthSquared() > traceDistance) {
            // 路径点失效，需要重新计算
            logFail(entityPosition, currentTarget, roadPoints, "Entity fail to follow");
            return EStatus.FAILURE;
        }

        // Step 3 : 检查目标生物是否在路径上
        Entity targetEntity = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_TARGET, Entity.class);
        if (targetEntity != null) {
            Vector3f targetEntityPosition = this.positionService.getPosition(targetEntity);
            Vector3f targetEntityCachedPosition = new Vector3f(roadPoints.get(roadPoints.size() - 1)).add(0.5f, 0.5f, 0.5f);
            if (new Vector3f(targetEntityPosition.getX() - targetEntityCachedPosition.getX(), 0, targetEntityPosition.getZ() - targetEntityCachedPosition.getZ()).lengthSquared() > traceDistance) {
                // 路径点失效，需要重新计算
                logFail(targetEntityPosition, targetEntityCachedPosition, roadPoints, "Entity Target Missing");
                return EStatus.FAILURE;
            }
        }

        // Step 4 : 已经走到当前目标点
        Vector3f distance = currentTarget.subtract(entityPosition);
        distance.setY(0);
        if (distance.lengthSquared() < checkDistance) {
            roadPoints.remove(0);
        }
        // TODO: 2019/4/15 不能在走到目标点时才更新下一个点，不然有概率更新失败，原因需要调查
        if (roadPoints.size() > 0) {
            this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.POSITION_TARGET, currentTarget);
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
        if (key.equals("CheckDistance") && val instanceof Float) {
            this.checkDistance = (Float) val;
        } else if (key.equals("TraceDistance") && val instanceof Float) {
            this.traceDistance = (Float) val;
        }
    }

    private void logFail(Vector3f currentPosition, Vector3f nextPosition, List<Vector3> roadPoints, String reason) {
        logger.info("Road update fail");
        logger.info("Current position : " + currentPosition);
        logger.info("Next position : " + nextPosition);
        System.out.print("Road positions : ");
        if (roadPoints == null) {
            logger.info("Null");
        } else {
            for (Vector3 roadPoint : roadPoints) {
                System.out.print(roadPoint.toString());
                System.out.print(" ");
            }
        }

        logger.info("Reason : " + reason);
    }
}
