package com.particle.game.entity.ai.leaf.seeker;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.EntityService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.mob.MobEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NearEntitySeeker implements IAction {

    private static Logger logger = LoggerFactory.getLogger(NearEntitySeeker.class);

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PositionService positionService;

    @Inject
    private EntityService entityService;

    private float checkDistance = 16;

    private List<Integer> entityTypeTempList = new ArrayList<>();
    private List<Integer> entityTypeList = new ArrayList<>();

    @Override
    public void onInitialize() {
        entityTypeList.clear();
        for (int entityID : entityTypeTempList) {
            entityTypeList.add(entityID);
        }
        entityTypeTempList.clear();
    }

    @Override
    public EStatus tick(Entity entity) {
        MobEntity closestEntity = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_TARGET, MobEntity.class);
        if (closestEntity == null) {
            List<MobEntity> closestEntityList = this.entityService.getNearMobEntities(entity.getLevel(), this.positionService.getPosition(entity), checkDistance);
            if (closestEntityList == null) {
                return EStatus.FAILURE;
            } else if (entity instanceof MobEntity) {
                for (MobEntity mobEntity : closestEntityList) {
                    // 若是自己 則跳過
                    if (((MobEntity) entity).getNetworkId() == mobEntity.getNetworkId()) {
                        continue;
                    }

                    if (entityTypeList.contains(mobEntity.getNetworkId())) {
                        this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.ENTITY_TARGET, mobEntity);
                        return EStatus.SUCCESS;
                    }
                }

                return EStatus.FAILURE;
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
        if (key.equalsIgnoreCase("CheckDistance") && val.getClass() == Float.class) {
            this.checkDistance = (float) val;
        }

        if (key.equalsIgnoreCase("entityId") && val instanceof Integer) {
            Integer mobEntityID = (Integer) val;
            if (mobEntityID == null) {
                logger.error("mobEntityID config fail, {} not exist!", val);
            } else {
                entityTypeTempList.add(mobEntityID);
            }
        }
    }

}
