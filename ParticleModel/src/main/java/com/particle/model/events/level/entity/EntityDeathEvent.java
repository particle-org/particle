package com.particle.model.events.level.entity;

import com.particle.model.entity.Entity;

public class EntityDeathEvent extends LevelEntityEvent {
    // 击杀者,可能为null，因为玩家可能环境会给其造成伤害，远程伤害算投掷者击杀
    private final Entity killer;

    public EntityDeathEvent(Entity victim, Entity killer) {
        super(victim);
        this.killer = killer;
    }

    public Entity getKiller() {
        return killer;
    }
}
