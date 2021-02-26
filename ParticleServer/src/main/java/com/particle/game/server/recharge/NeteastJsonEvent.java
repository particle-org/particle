package com.particle.game.server.recharge;

public enum NeteastJsonEvent {
    ON_OPEN_STORE("ON_OPEN_STORE"),
    ON_CONTROL_STORE_ENTRANCE("ON_CONTROL_STORE_ENTRANCE");

    private String event;

    NeteastJsonEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }
}
