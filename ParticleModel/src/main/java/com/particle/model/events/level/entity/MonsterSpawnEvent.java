package com.particle.model.events.level.entity;

import com.particle.model.entity.model.others.MonsterEntity;
import com.particle.model.events.level.LevelEvent;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3f;

public class MonsterSpawnEvent extends LevelEvent {
    private Vector3f position;
    private MonsterEntity monsterEntity;

    public MonsterSpawnEvent(Level level) {
        super(level);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public MonsterEntity getMonsterEntity() {
        return monsterEntity;
    }

    public void setMonsterEntity(MonsterEntity monsterEntity) {
        this.monsterEntity = monsterEntity;
    }
}
