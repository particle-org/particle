package com.particle.api.entity;

import com.particle.model.entity.Entity;
import com.particle.model.entity.model.others.MonsterEntity;
import com.particle.model.math.Vector3f;

import java.util.UUID;

public interface MonsterEntityServiceApi {
    /**
     * 创建 monster entity
     *
     * @return
     */
    MonsterEntity createEntity(String actorType, Vector3f position);


    UUID getMonsterEntityUuid(MonsterEntity monsterEntity);

    public void setDeathExperience(Entity entity, int amount);
}
