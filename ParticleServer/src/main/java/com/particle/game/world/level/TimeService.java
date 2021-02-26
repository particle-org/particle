package com.particle.game.world.level;

import com.particle.api.level.TimeServiceApi;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.model.events.level.global.SetTimeLevelEvent;
import com.particle.model.level.Level;
import com.particle.model.network.packets.data.SetTimePacket;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TimeService implements TimeServiceApi {

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Override
    public void setTime(Level level, long time, boolean notify) {
        SetTimeLevelEvent setTimeLevelEvent = new SetTimeLevelEvent(level);
        setTimeLevelEvent.setTime(time);
        setTimeLevelEvent.setNotify(notify);

        this.eventDispatcher.dispatchEvent(setTimeLevelEvent);

        if (!setTimeLevelEvent.isCancelled()) {
            level.setTime(time);
            if (notify) {
                SetTimePacket setTimePacket = new SetTimePacket();
                setTimePacket.setTime((int) level.getTime());
                this.broadcastServiceProxy.broadcast(level, setTimePacket);
            }
        }
    }
}
