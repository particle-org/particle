package com.particle.model.entity.metadata.type;

public enum EntityMetadataVariableType {
    BYTE(0),
    SHORT(1),
    INT(2),
    FLOAT(3),
    STRING(4),
    SLOT(5),
    POS(6),
    LONG(7),
    VECTORY3F(8);

    private int type;

    private EntityMetadataVariableType(int type) {
        this.type = type;
    }

    public int value() {
        return this.type;
    }

    public static EntityMetadataVariableType valueOf(int type) {
        switch (type) {
            case 0:
                return BYTE;
            case 1:
                return SHORT;
            case 2:
                return INT;
            case 3:
                return FLOAT;
            case 4:
                return STRING;
            case 5:
                return SLOT;
            case 6:
                return POS;
            case 7:
                return LONG;
            case 8:
                return VECTORY3F;
        }

        return null;
    }
}
