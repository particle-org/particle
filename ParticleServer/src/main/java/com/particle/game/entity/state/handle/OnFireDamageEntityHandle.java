package com.particle.game.entity.state.handle;

import com.particle.game.entity.attack.EntityAttackedHandleService;
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
public class OnFireDamageEntityHandle implements EntityStateHandle {

    private static final String DISPLAY_NAME = "炽热";

    public static final String KEY = "Magic:OnFire";

    @Inject
    private MetaDataService metaDataService;

    @Inject
    private HealthServiceProxy healthServiceProxy;

    @Inject
    private EntityStateService entityStateService;

    @Inject
    private EntityAttackedHandleService entityAttackedHandleService;

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public void onStateEnabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean hasEnabled) {

    }

    @Override
    public void onStateEnabled(Entity source, Entity entity, EntityStateRecorder entityStateRecorder, boolean hasEnabled) {
        // 通知起火
        if (!(entity instanceof Player)) {
            entityAttackedHandleService.entityAttackedByEntity(source, entity);
            this.metaDataService.setDataFlag(entity, MetadataDataFlag.DATA_FLAG_ONFIRE, true, true);
        }
    }

    @Override
    public void onStateUpdated(Entity entity, EntityStateRecorder entityStateRecorder) {
        if (!entityStateService.hasState(entity, EffectBaseType.FIRE_RESISTANCE.getName()) && !(entity instanceof Player)) {
            this.healthServiceProxy.damageEntity(entity, 1, EntityDamageType.Fire, null);
        }
    }

    @Override
    public void onStateDisabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean needDisableState) {
        // 清除起火
        if (!(entity instanceof Player)) {
            this.metaDataService.setDataFlag(entity, MetadataDataFlag.DATA_FLAG_ONFIRE, false, true);
        }
    }
}
