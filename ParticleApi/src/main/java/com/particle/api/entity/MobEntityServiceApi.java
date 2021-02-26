package com.particle.api.entity;

import com.particle.model.entity.Entity;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.math.Vector3f;

public interface MobEntityServiceApi {

    /**
     * 创建mob entity
     *
     * @param networkId
     * @param position
     * @return
     */
    MobEntity createEntity(int networkId, Vector3f position);

    MobEntity createEntity(String actorType, Vector3f position);

    void setDeathExperience(Entity entity, int amount);
}

