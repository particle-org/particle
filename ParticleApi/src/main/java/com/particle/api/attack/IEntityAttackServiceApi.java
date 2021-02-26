package com.particle.api.attack;

import com.particle.model.entity.Entity;

public interface IEntityAttackServiceApi {
    void initEntityAttackComponent(Entity entity);

    void initEntityAttackComponent(Entity entity, float baseDamage, long attackInterval);

    boolean canAttack(Entity entity);

    boolean entityCloseAttack(Entity source, Entity victim);

}
