package com.particle.model.player;

public enum ClientPlayMode {
    NORMAL((byte) 0),
    TEASER((byte) 1),
    SCREEN((byte) 2),
    VIEWER((byte) 3),
    REALITY((byte) 4),
    PLACEMENT((byte) 5),
    LIVING_ROOM((byte) 6),
    EXIT_LEVEL((byte) 7),
    EXIT_LEVEL_LIVING_ROOM((byte) 8),
    NUM_MODES((byte) 9),

    ;

    private byte type;

    private ClientPlayMode(byte type) {
        this.type = type;
    }

    public byte value() {
        return this.type;
    }

    public static ClientPlayMode fromValue(int value) {
        switch (value) {
            case 0:
                return NORMAL;
            case 1:
                return TEASER;
            case 2:
                return SCREEN;
            case 3:
                return VIEWER;
            case 4:
                return REALITY;
            case 5:
                return PLACEMENT;
            case 6:
                return LIVING_ROOM;
            case 7:
                return EXIT_LEVEL;
            case 8:
                return EXIT_LEVEL_LIVING_ROOM;
            case 9:
                return NUM_MODES;
        }

        return null;
    }
}
