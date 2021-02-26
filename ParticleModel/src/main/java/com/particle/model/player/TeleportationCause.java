package com.particle.model.player;

public enum TeleportationCause {
    UNKNOWN(0),
    PROJECTILE(1),
    CHORUS_FRUIT(2),
    COMMAND(3),
    BEHAVIOR(4),
    TELEPORTATION_CAUSE_COUNT(5);

    private int type;

    private TeleportationCause(int type) {
        this.type = type;
    }

    public int value() {
        return this.type;
    }

    public static TeleportationCause fromValue(int value) {
        switch (value) {
            case 0:
                return UNKNOWN;
            case 1:
                return PROJECTILE;
            case 2:
                return CHORUS_FRUIT;
            case 3:
                return COMMAND;
            case 4:
                return BEHAVIOR;
            case 5:
                return TELEPORTATION_CAUSE_COUNT;
        }

        return null;
    }
}
