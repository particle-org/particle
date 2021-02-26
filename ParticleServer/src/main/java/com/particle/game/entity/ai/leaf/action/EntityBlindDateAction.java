package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.EntityService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.mob.MobEntity;

import javax.inject.Inject;
import java.util.List;

public class EntityBlindDateAction implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private EntityService entityService;

    @Inject
    private PositionService positionService;

    private float checkDistance = 16;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        List<MobEntity> mobEntityList = entityService.getNearMobEntities(entity.getLevel(), positionService.getPosition(entity), checkDistance);
        //  是否已有對象
        for (MobEntity mobEntity : mobEntityList) {
            // 若是自己則跳過
            if (mobEntity == entity) {
                continue;
            }

            Entity blindDateEntity = entityDecisionServiceProxy.getKnowledge(mobEntity, Knowledge.BLIND_DATE, Entity.class);
            if (blindDateEntity != null && blindDateEntity == entity) {
                // 移除原始目標
                entityDecisionServiceProxy.removeKnowledge(entity, Knowledge.ENTITY_TARGET);

                entityDecisionServiceProxy.addKnowledge(entity, Knowledge.BLIND_DATE, mobEntity);
                entityDecisionServiceProxy.addKnowledge(entity, Knowledge.ENTITY_TARGET, mobEntity);
                // 解除發情
                entityDecisionServiceProxy.removeKnowledge(entity, Knowledge.ESTRUS_STATUS);
                return EStatus.SUCCESS;
            }
        }

        // 尋找對象
        for (MobEntity mobEntity : mobEntityList) {
            if (mobEntity == entity) {
                continue;
            }

            if (entity instanceof MobEntity && ((MobEntity) entity).getNetworkId() == mobEntity.getNetworkId()) {
                Boolean isEstrus = entityDecisionServiceProxy.getKnowledge(mobEntity, Knowledge.ESTRUS_STATUS, Boolean.class);
                Entity blindDateEntity = entityDecisionServiceProxy.getKnowledge(mobEntity, Knowledge.BLIND_DATE, Entity.class);
                if (isEstrus != null && isEstrus && blindDateEntity == null) {
                    // 加入相親對象
                    entityDecisionServiceProxy.addKnowledge(entity, Knowledge.BLIND_DATE, mobEntity);
                    return EStatus.FAILURE;
                }
            }
        }

        entityDecisionServiceProxy.removeKnowledge(entity, Knowledge.BLIND_DATE);
        return EStatus.FAILURE;
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
    }
}