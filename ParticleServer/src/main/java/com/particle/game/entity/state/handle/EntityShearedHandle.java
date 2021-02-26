package com.particle.game.entity.state.handle;

import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.entity.type.EntityStateRecorder;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EntityShearedHandle implements EntityStateHandle {

    @Inject
    private MetaDataService metaDataService;

    @Override
    public String getDisplayName() {
        return "";
    }

    @Override
    public void onStateEnabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean hasEnabled) {
        this.metaDataService.setDataFlag(entity, MetadataDataFlag.DATA_FLAG_SHEARED, true, true);
    }

    @Override
    public void onStateUpdated(Entity entity, EntityStateRecorder entityStateRecorder) {
    }

    @Override
    public void onStateDisabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean needDisableState) {
        this.metaDataService.setDataFlag(entity, MetadataDataFlag.DATA_FLAG_SHEARED, false, true);
    }
}
