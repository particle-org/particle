package com.particle.model.player.spawn;

public enum PlayerRespawnState {

    SEARCHING_FOR_SPAWN((byte) 0),
    READY_TO_SPAWN((byte) 1),
    CLIENT_READY_TO_SPAWN((byte) 2);

    private byte state;

    public static PlayerRespawnState fromValue(byte state) {
        switch (state) {
            case 0:
                return SEARCHING_FOR_SPAWN;
            case 1:
                return READY_TO_SPAWN;
            case 2:
                return CLIENT_READY_TO_SPAWN;
        }

        return null;
    }

    PlayerRespawnState(byte state) {
        this.state = state;
    }

    public byte state() {
        return state;
    }
}
