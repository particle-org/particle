package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.MobEntityService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;

public class MobEntityCreateBabyAction implements IAction {

    @Inject
    private PositionService positionService;

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private MobEntityService mobEntityService;

    @Inject
    private MetaDataService metaDataService;

    @Inject
    private EntitySpawnService entitySpawnService;

    private float scaleRate = 1;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        // 检查是否有对象
        MobEntity mobEntity = entityDecisionServiceProxy.getKnowledge(entity, Knowledge.BLIND_DATE, MobEntity.class);
        if (mobEntity == null) {
            return EStatus.FAILURE;
        }

        // 通知对象生小孩
        EStatus status = this.entityDecisionServiceProxy.sendMessageToEntity(entity, mobEntity, mobEntity.getActorType(), "Birth");
        if (status == EStatus.FAILURE) {
            return EStatus.FAILURE;
        }

        // 确认生产，创造小宝宝
        Vector3f position = this.positionService.getPosition(entity);

        MobEntity targetEntity = this.mobEntityService.createEntity(mobEntity.getNetworkId(), position.add(0, 0.2f, 0));
        metaDataService.setDataFlag(targetEntity, MetadataDataFlag.DATA_FLAG_BABY, true, false);
        metaDataService.setFloatData(targetEntity, EntityMetadataType.SCALE, this.metaDataService.getFloatData(targetEntity, EntityMetadataType.SCALE) * scaleRate, false);

        this.entitySpawnService.spawnEntity(entity.getLevel(), targetEntity);

        entityDecisionServiceProxy.removeKnowledge(entity, Knowledge.BLIND_DATE);
        entityDecisionServiceProxy.removeKnowledge(mobEntity, Knowledge.BLIND_DATE);
        entityDecisionServiceProxy.removeKnowledge(entity, Knowledge.ENTITY_TARGET);
        entityDecisionServiceProxy.removeKnowledge(mobEntity, Knowledge.ENTITY_TARGET);
        entityDecisionServiceProxy.removeKnowledge(entity, Knowledge.POSITION_TARGET);
        entityDecisionServiceProxy.removeKnowledge(mobEntity, Knowledge.POSITION_TARGET);

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
        if (key.equals("ScaleRate") && val instanceof Float) {
            this.scaleRate = (Float) val;
        }
    }
}
