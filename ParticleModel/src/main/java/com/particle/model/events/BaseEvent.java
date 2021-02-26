package com.particle.model.events;

public abstract class BaseEvent {
    private Runnable onEventNotExecuted;
    private Runnable afterEventExecuted;

    public void overrideOnEventNotExecuted(Runnable onEventNotExecuted) {
        this.onEventNotExecuted = onEventNotExecuted;
    }

    public void overrideAfterEventExecuted(Runnable afterEventExecuted) {
        this.afterEventExecuted = afterEventExecuted;
    }

    public Runnable getOnEventNotExecuted() {
        return onEventNotExecuted;
    }

    public Runnable getAfterEventExecuted() {
        return afterEventExecuted;
    }
}
