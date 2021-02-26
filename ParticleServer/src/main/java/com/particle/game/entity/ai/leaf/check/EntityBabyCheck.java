package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.ICondition;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.MetadataDataFlag;

import javax.inject.Inject;

public class EntityBabyCheck implements ICondition {

    @Inject
    private MetaDataService metaDataService;

    @Override
    public void onInitialize() {
    }

    @Override
    public EStatus tick(Entity entity) {

        boolean isBaby = metaDataService.getDataFlag(entity, MetadataDataFlag.DATA_FLAG_BABY);

        if (isBaby) {
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

    }
}
