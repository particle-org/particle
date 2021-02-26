package com.particle.model.ui.score;

import java.util.HashMap;
import java.util.Map;

public enum IdentityDefinitionType {
    Invalid(0),
    Player(1),
    Entity(2),
    FakePlayer(3);

    private int type;

    private static final Map<Integer, IdentityDefinitionType> types = new HashMap<>();

    static {
        for (IdentityDefinitionType item : IdentityDefinitionType.values()) {
            types.put(item.getType(), item);
        }
    }

    IdentityDefinitionType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static IdentityDefinitionType from(int type) {
        return types.get(type);
    }
}
