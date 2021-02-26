package com.particle.game.entity.ai.event;

import com.particle.event.dispatcher.EventRank;
import com.particle.event.handle.AbstractLevelEventHandle;
import com.particle.game.entity.ai.service.EntityDecisionService;
import com.particle.model.entity.model.others.MonsterEntity;
import com.particle.model.events.annotation.EventHandler;
import com.particle.model.events.level.entity.EntityDeathEvent;
import com.particle.model.level.Level;

import javax.inject.Singleton;

@Singleton
@EventHandler
public class MonsterDeathEventHandle extends AbstractLevelEventHandle<EntityDeathEvent> {

    @Override
    public Class<EntityDeathEvent> getListenerEvent() {
        return EntityDeathEvent.class;
    }

    @Override
    public void onHandle(Level level, EntityDeathEvent entityDeathEvent) {
        if (!(entityDeathEvent.getEntity() instanceof MonsterEntity)) {
            return;
        }

        MonsterEntity monsterEntity = (MonsterEntity) entityDeathEvent.getEntity();

        EntityDecisionService.tickEntityDeathTree(monsterEntity, monsterEntity.getActorType());
    }

    @Override
    public EventRank getEventRank() {
        return EventRank.LOCAL;
    }
}
