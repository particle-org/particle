package com.particle.event.handle;


import com.particle.event.dispatcher.EventRank;
import com.particle.model.events.BaseEvent;
import com.particle.model.level.Level;

public interface LevelEventHandle<T extends BaseEvent> extends EventHandle {
    Class<T> getListenerEvent();

    void handle(Level level, T t);

    EventRank getEventRank();
}
