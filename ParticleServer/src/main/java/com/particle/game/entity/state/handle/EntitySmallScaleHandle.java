package com.particle.game.entity.state.handle;

import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.particle.ParticleService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.type.EntityStateRecorder;
import com.particle.model.math.Vector3f;
import com.particle.model.particle.CustomParticleType;

import javax.inject.Inject;

public class EntitySmallScaleHandle implements EntityStateHandle {

    public static final String KEY = "SmallScale";

    @Inject
    private MetaDataService metaDataService;

    @Inject
    private ParticleService particleService;

    @Inject
    private PositionService positionService;

    @Override
    public String getDisplayName() {
        return "量子化";
    }

    @Override
    public void onStateEnabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean hasEnabled) {
        if (entityStateRecorder.getEnabledCount() == 1 && !hasEnabled) {
            // 变化是依据当前尺寸情况，以更好处理巨大和缩小叠加的问题
            float scale = metaDataService.getFloatData(entity, EntityMetadataType.SCALE);
            if (scale > 0.4) {
                metaDataService.setFloatData(entity, EntityMetadataType.SCALE, scale * 0.7f, false);
                metaDataService.refreshMetadata(entity);
            }

            Vector3f position = this.positionService.getPosition(entity);
            this.particleService.playParticle(entity.getLevel(), CustomParticleType.LARGE_EXPLOSION, position);
        }
    }

    @Override
    public void onStateEnabled(Entity source, Entity entity, EntityStateRecorder entityStateRecorder, boolean hasEnabled) {
        onStateEnabled(entity, entityStateRecorder, hasEnabled);
    }

    @Override
    public void onStateUpdated(Entity entity, EntityStateRecorder entityStateRecorder) {

    }

    @Override
    public void onStateDisabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean needDisableState) {
        if (entityStateRecorder.getEnabledCount() == 0) {
            // 变化是依据当前尺寸情况，以更好处理巨大和缩小叠加的问题
            float scale = metaDataService.getFloatData(entity, EntityMetadataType.SCALE);
            if (scale < 4.5) {
                metaDataService.setFloatData(entity, EntityMetadataType.SCALE, (scale * 10f) / 7f, false);
                metaDataService.refreshMetadata(entity);
            }
        }
    }
}
