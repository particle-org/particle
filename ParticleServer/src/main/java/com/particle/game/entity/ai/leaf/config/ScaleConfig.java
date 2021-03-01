package com.particle.game.entity.ai.leaf.config;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.EntityMetadataType;

import javax.inject.Inject;

public class ScaleConfig implements IAction {

    @Inject
    private MetaDataService metaDataService;

    private float appleIfScale = 1f;
    private float scale = 1f;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        if (this.metaDataService.getFloatData(entity, EntityMetadataType.SCALE) == appleIfScale) {
            this.metaDataService.setFloatData(entity, EntityMetadataType.SCALE, scale, true);
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
        if (key.equals("Scale") && val instanceof Float) {
            this.scale = (float) val;
        } else if (key.equals("AppleIfScale") && val instanceof Float) {
            this.scale = (float) val;
        }
    }
}
