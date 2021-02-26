package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.ICondition;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.EntityMetadataType;

import javax.inject.Inject;

public class EntitySizeCheck implements ICondition {


    @Inject
    private MetaDataService metaDataService;

    private float checkScale = 1;

    @Override
    public void onInitialize() {
    }

    @Override
    public EStatus tick(Entity entity) {
        float scale = this.metaDataService.getFloatData(entity, EntityMetadataType.SCALE);

        if (scale > checkScale) {
            return EStatus.SUCCESS;
        } else {
            return EStatus.FAILURE;
        }
    }

    @Override
    public void onTicked(Entity entity, EStatus status) {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void config(String key, Object val) {
        if (key.equals("CheckScale") && val instanceof Float) {
            this.checkScale = (Float) val;
        }
    }
}
