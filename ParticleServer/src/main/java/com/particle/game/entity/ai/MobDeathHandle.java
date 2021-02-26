package com.particle.game.entity.ai;

import com.particle.event.dispatcher.EventRank;
import com.particle.event.handle.AbstractLevelEventHandle;
import com.particle.game.entity.ai.service.EntityDecisionService;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.events.annotation.EventHandler;
import com.particle.model.events.level.entity.EntityDeathEvent;
import com.particle.model.level.Level;

import javax.inject.Singleton;

@Singleton
@EventHandler
public class MobDeathHandle extends AbstractLevelEventHandle<EntityDeathEvent> {

    @Override
    public Class<EntityDeathEvent> getListenerEvent() {
        return EntityDeathEvent.class;
    }

    @Override
    public void onHandle(Level level, EntityDeathEvent entityDeathEvent) {
        if (!(entityDeathEvent.getEntity() instanceof MobEntity)) {
            return;
        }

        MobEntity mobEntity = (MobEntity) entityDeathEvent.getEntity();

        EntityDecisionService.tickEntityDeathTree(mobEntity, mobEntity.getActorType());
    }

    @Override
    public EventRank getEventRank() {
        return EventRank.LOCAL;
    }
}
