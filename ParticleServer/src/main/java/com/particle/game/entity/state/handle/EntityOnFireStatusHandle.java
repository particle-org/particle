package com.particle.game.entity.state.handle;

import com.particle.game.entity.attribute.health.HealthServiceProxy;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.state.EntityStateService;
import com.particle.model.effect.EffectBaseType;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.entity.type.EntityStateRecorder;
import com.particle.model.events.level.entity.EntityDamageType;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EntityOnFireStatusHandle implements EntityStateHandle {

    @Inject
    private MetaDataService metaDataService;

    @Inject
    private HealthServiceProxy healthServiceProxy;

    @Inject
    private EntityStateService entityStateService;

    @Override
    public String getDisplayName() {
        return "炽热";
    }

    @Override
    public void onStateEnabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean hasEnabled) {
        // 通知起火
        this.metaDataService.setDataFlag(entity, MetadataDataFlag.DATA_FLAG_ONFIRE, true, true);
        if (entity instanceof Player) {
            this.metaDataService.refreshPlayerMetadata((Player) entity);
        }
    }

    @Override
    public void onStateUpdated(Entity entity, EntityStateRecorder entityStateRecorder) {
        if (!entityStateService.hasState(entity, EffectBaseType.FIRE_RESISTANCE.getName())) {
            this.healthServiceProxy.damageEntity(entity, 1, EntityDamageType.Fire, null);
        }
    }

    @Override
    public void onStateDisabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean needDisableState) {
        // 清除起火
        this.metaDataService.setDataFlag(entity, MetadataDataFlag.DATA_FLAG_ONFIRE, false, true);
        if (entity instanceof Player) {
            this.metaDataService.refreshPlayerMetadata((Player) entity);
        }
    }
}
