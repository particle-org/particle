package com.particle.model.resources;

public enum ResourcePackType {
    INVALID((byte) 0),
    ADDON((byte) 1),
    CACHED((byte) 2),
    COPY_PROTECTED((byte) 3),
    BEHAVIOR((byte) 4),
    PERSONA_PIECE((byte) 5),
    RESOURCES((byte) 6),
    SKINS((byte) 7),
    WORLD_TEMPLATE((byte) 8),
    COUNT((byte) 9);


    private byte id;

    ResourcePackType(byte id) {
        this.id = id;
    }

    public byte getId() {
        return id;
    }

    public byte getIdV361() {
        switch (this) {
            case ADDON:
                return 4;
            case CACHED:
                return 6;
            case COPY_PROTECTED:
                return 7;
            case BEHAVIOR:
                return 2;
            case RESOURCES:
                return 1;
            case SKINS:
                return 5;
            case WORLD_TEMPLATE:
                return 3;
            default:
                return 0;
        }
    }

    public static ResourcePackType getType(byte id) {
        switch (id) {
            case 0:
                return INVALID;
            case 1:
                return ADDON;
            case 2:
                return CACHED;
            case 3:
                return COPY_PROTECTED;
            case 4:
                return BEHAVIOR;
            case 5:
                return PERSONA_PIECE;
            case 6:
                return RESOURCES;
            case 7:
                return SKINS;
            case 8:
                return WORLD_TEMPLATE;
            case 9:
                return COUNT;
        }

        return null;
    }

    public static ResourcePackType getTypeFromV361(byte id) {
        switch (id) {
            case 0:
                return INVALID;
            case 1:
                return RESOURCES;
            case 2:
                return BEHAVIOR;
            case 3:
                return WORLD_TEMPLATE;
            case 4:
                return ADDON;
            case 5:
                return SKINS;
            case 6:
                return CACHED;
            case 7:
                return COPY_PROTECTED;
        }

        return null;
    }

}
