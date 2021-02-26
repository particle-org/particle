package com.particle.game.ui.listener;

import com.particle.event.dispatcher.EventRank;
import com.particle.event.handle.AbstractLevelEventHandle;
import com.particle.game.ui.ChestSelectorService;
import com.particle.model.events.annotation.EventHandler;
import com.particle.model.events.level.container.InventoryTransactionLevelEvent;
import com.particle.model.level.Level;

import javax.inject.Inject;
import javax.inject.Singleton;

@EventHandler
@Singleton
public class InventoryTransactionLevelEventHandle extends AbstractLevelEventHandle<InventoryTransactionLevelEvent> {

    @Inject
    private ChestSelectorService chestSelectorService;

    @Override
    protected void onHandle(Level level, InventoryTransactionLevelEvent event) {
        boolean state = this.chestSelectorService.onSelectItem(event.getPlayer(), event.getInventoryActionData());

        if (state) {
            event.setNeedRecoveryOnCancelled(false);
            event.cancel();
        }

    }

    @Override
    public Class getListenerEvent() {
        return InventoryTransactionLevelEvent.class;
    }

    @Override
    public EventRank getEventRank() {
        return EventRank.DEFAULT;
    }
}
