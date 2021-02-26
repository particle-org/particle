package com.particle.game.entity.state.handle.effect;

import com.google.inject.Injector;
import com.particle.model.effect.EffectBaseType;
import com.particle.model.entity.Entity;
import com.particle.model.entity.type.EntityStateRecorder;

public class CommonEffectHandle extends BaseEffectHandle {

    public static BaseEffectHandle newCommonEffect(Injector injector, EffectBaseType effectBaseType, String name) {
        CommonEffectHandle commonEffectHandle = injector.getInstance(CommonEffectHandle.class);
        commonEffectHandle.effectType = effectBaseType;
        commonEffectHandle.name = name;

        return commonEffectHandle;
    }

    private EffectBaseType effectType;
    private String name;

    @Override
    protected EffectBaseType getEffectType() {
        return effectType;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public void onStateUpdated(Entity entity, EntityStateRecorder entityStateRecorder) {
        super.onStateUpdated(entity, entityStateRecorder);
    }
}
