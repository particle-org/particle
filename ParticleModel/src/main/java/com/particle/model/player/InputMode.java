package com.particle.model.player;

public enum InputMode {
    UNDEFINED((byte) 0),
    MOUSE((byte) 1),
    TOUCH((byte) 2),
    GAME_PAD((byte) 3),
    MOTION_CONTROLLER((byte) 4),
    COUNT((byte) 5),

    ;

    private byte type;

    private InputMode(byte type) {
        this.type = type;
    }

    public byte value() {
        return this.type;
    }

    public static InputMode fromValue(int value) {
        switch (value) {
            case 0:
                return UNDEFINED;
            case 1:
                return MOUSE;
            case 2:
                return TOUCH;
            case 3:
                return GAME_PAD;
            case 4:
                return MOTION_CONTROLLER;
            case 5:
                return COUNT;
        }

        return null;
    }
}
