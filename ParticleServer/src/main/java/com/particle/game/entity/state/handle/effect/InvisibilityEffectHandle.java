package com.particle.game.entity.state.handle.effect;

import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.model.effect.EffectBaseType;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.entity.type.EntityStateRecorder;

import javax.inject.Inject;

public class InvisibilityEffectHandle extends BaseEffectHandle {

    @Inject
    private MetaDataService metaDataService;

    @Override
    protected EffectBaseType getEffectType() {
        return EffectBaseType.INVISIBILITY;
    }

    @Override
    public String getDisplayName() {
        return "隐身";
    }

    @Override
    public void onStateEnabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean hasEnabled) {
        super.onStateEnabled(entity, entityStateRecorder, hasEnabled);

        metaDataService.setDataFlag(entity, MetadataDataFlag.DATA_FLAG_INVISIBLE, true, false);
        metaDataService.refreshMetadata(entity);
    }

    @Override
    public void onStateDisabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean needDisableState) {
        super.onStateDisabled(entity, entityStateRecorder, needDisableState);

        metaDataService.setDataFlag(entity, MetadataDataFlag.DATA_FLAG_INVISIBLE, false, false);
        metaDataService.refreshMetadata(entity);
    }

    @Override
    public void onStateUpdated(Entity entity, EntityStateRecorder entityStateRecorder) {

    }
}
