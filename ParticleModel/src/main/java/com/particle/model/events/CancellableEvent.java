package com.particle.model.events;

public class CancellableEvent extends BaseEvent {
    private Runnable onEventCancelled;

    private boolean isCancelled = false;

    public void cancel() {
        this.isCancelled = true;
    }

    public void enable() {
        this.isCancelled = false;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void overrideOnEventCancelled(Runnable onEventCancelled) {
        this.onEventCancelled = onEventCancelled;
    }

    public Runnable getOnEventCancelled() {
        return onEventCancelled;
    }
}
