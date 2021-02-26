package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.MobEntityService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;

public class MobEntitySpawnAction implements IAction {

    @Inject
    private PositionService positionService;

    @Inject
    private MobEntityService mobEntityService;

    @Inject
    private MetaDataService metaDataService;

    @Inject
    private EntitySpawnService entitySpawnService;

    private Integer entityId;
    private float scaleRate = 1;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        if (entityId == null) {
            return EStatus.FAILURE;
        }

        Vector3f position = this.positionService.getPosition(entity);

        MobEntity targetEntity = this.mobEntityService.createEntity(this.entityId, position.add(0, 0.2f, 0));
        this.metaDataService.setFloatData(targetEntity, EntityMetadataType.SCALE, this.metaDataService.getFloatData(targetEntity, EntityMetadataType.SCALE) * scaleRate, true);

        this.entitySpawnService.spawnEntity(entity.getLevel(), targetEntity);

        return null;
    }

    @Override
    public void onTicked(Entity entity, EStatus status) {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void config(String key, Object val) {
        if (key.equals("NetworkId") && val instanceof Integer) {
            this.entityId = (Integer) val;
        } else if (key.equals("ScaleRate") && val instanceof Float) {
            this.scaleRate = (Float) val;
        }
    }
}
