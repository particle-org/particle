package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.attribute.health.HealthServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.EntityService;
import com.particle.game.entity.state.EntityStateService;
import com.particle.game.entity.state.model.EntityStateType;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.mob.MobEntity;

import javax.inject.Inject;
import java.util.List;

public class EntityBlindDateAction2 implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private EntityStateService entityStateService;

    @Inject
    private EntityService entityService;

    @Inject
    private PositionService positionService;

    @Inject
    private HealthServiceProxy healthServiceProxy;

    private float checkDistance = 16;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        // 检查当前绑定的对象
        Entity currentBlindEntity = entityDecisionServiceProxy.getKnowledge(entity, Knowledge.BLIND_DATE, Entity.class);
        if (currentBlindEntity != null) {
            // 如果当前绑定的生物仍然存活有效，则直接返回。否则清空绑定生物，重新搜索。
            if (this.healthServiceProxy.isAlive(currentBlindEntity) && this.entityStateService.hasState(currentBlindEntity, EntityStateType.ESTRUS_STATUS.getName())) {
                return EStatus.SUCCESS;
            } else {
                entityDecisionServiceProxy.removeKnowledge(entity, Knowledge.BLIND_DATE);
            }
        }

        // 搜索附近的生物
        List<MobEntity> mobEntityList = entityService.getNearMobEntities(entity.getLevel(), positionService.getPosition(entity), checkDistance);

        // 尋找對象
        for (MobEntity mobEntity : mobEntityList) {
            // 跳过自己
            if (mobEntity == entity) {
                continue;
            }

            // 目标生物检查
            if (entity instanceof MobEntity && ((MobEntity) entity).getNetworkId() == mobEntity.getNetworkId()) {
                // 目标生物是否处于发情状态
                if (this.entityStateService.hasState(mobEntity, EntityStateType.ESTRUS_STATUS.getName())) {
                    // 检查该生物是否已经有对象
                    Entity blindDateEntity = entityDecisionServiceProxy.getKnowledge(mobEntity, Knowledge.BLIND_DATE, Entity.class);
                    if (blindDateEntity != null) {
                        continue;
                    }

                    // 通知目标
                    EStatus status = this.entityDecisionServiceProxy.sendMessageToEntity(entity, mobEntity, mobEntity.getActorType(), "Blind");
                    if (status == EStatus.FAILURE) {
                        continue;
                    }

                    // 绑定关联关系
                    this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.BLIND_DATE, mobEntity);

                    return EStatus.SUCCESS;
                }
            }
        }

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