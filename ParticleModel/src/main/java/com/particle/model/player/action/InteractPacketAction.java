package com.particle.model.player.action;

import java.util.HashMap;
import java.util.Map;

public enum InteractPacketAction {
    Invalid(0),
    StopRiding(3),
    InteractUpdate(4),
    NpcOpen(5),
    OpenInventory(6);

    private int action;

    private static final Map<Integer, InteractPacketAction> actions = new HashMap<>();

    static {
        for (InteractPacketAction packetAction : InteractPacketAction.values()) {
            actions.put(packetAction.action, packetAction);
        }
    }

    public static InteractPacketAction from(int action) {
        return actions.get(action);
    }

    InteractPacketAction(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }
}
