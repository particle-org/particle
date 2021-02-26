package com.particle.model.player;

public enum PositionMode {
    NORMAL((byte) 0),
    RESET((byte) 1),
    TELEPORT((byte) 2),
    PITCH((byte) 3);

    private byte type;

    private PositionMode(byte type) {
        this.type = type;
    }

    public byte value() {
        return this.type;
    }

    public static PositionMode fromValue(byte value) {
        switch (value) {
            case 0:
                return NORMAL;
            case 1:
                return RESET;
            case 2:
                return TELEPORT;
            case 3:
                return PITCH;
        }

        return null;
    }
}
