package com.particle.game.ui.listener;

import com.particle.event.dispatcher.EventRank;
import com.particle.event.handle.AbstractLevelEventHandle;
import com.particle.game.ui.ChestSelectorService;
import com.particle.game.ui.EnderBagService;
import com.particle.model.events.annotation.EventHandler;
import com.particle.model.events.level.player.PlayerCloseCustomContainerEvent;
import com.particle.model.level.Level;

import javax.inject.Inject;
import javax.inject.Singleton;

@EventHandler
@Singleton
public class PlayerCloseFakeChestListener extends AbstractLevelEventHandle<PlayerCloseCustomContainerEvent> {

    @Inject
    private ChestSelectorService chestSelectorService;

    @Inject
    private EnderBagService enderBagService;

    @Override
    protected void onHandle(Level level, PlayerCloseCustomContainerEvent playerCloseCustomContainerEvent) {
        boolean result =
                this.chestSelectorService.onCloseHandleIfPlayerOpened(playerCloseCustomContainerEvent.getPlayer())
                        || enderBagService.onCloseHandleIfPlayerOpened(playerCloseCustomContainerEvent.getPlayer());
    }

    @Override
    public Class getListenerEvent() {
        return PlayerCloseCustomContainerEvent.class;
    }

    @Override
    public EventRank getEventRank() {
        return EventRank.DEFAULT;
    }
}
