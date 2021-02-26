package com.particle.model.ecs;


import com.particle.model.entity.Entity;
import com.particle.model.level.Level;

@Deprecated
public abstract class ECSSystem {
    public abstract int[] getRequiredComponent();

    public abstract void tick(Level level, Entity entity, long tickInterval);
}
