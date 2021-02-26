package com.particle.model.inventory.common;

import java.util.HashMap;
import java.util.Map;

public enum PlayerUIInventorySlot {

    PLAYER_CURSOR(0, 0),
    ANVIL_CRAFT_INPUT(1, 2),
    LOOM_CRAFT_INPUT(9, 11),
    ENCHANT_CRAFT_INPUT(14, 15),
    PLAYER_CRAFT_INPUT(28, 31),
    WORKBENCH_CRAFT_INPUT(32, 40),
    CRAFT_OUTPUT(50, 50);

    private int startSlot;
    private int endSlot;

    private static final Map<Integer, PlayerUIInventorySlot> PLAYER_UI_INVENTORY_SLOT_MAP = new HashMap<Integer, PlayerUIInventorySlot>() {{
        put(0, PLAYER_CURSOR);
        put(1, ANVIL_CRAFT_INPUT);
        put(2, ANVIL_CRAFT_INPUT);
        put(9, LOOM_CRAFT_INPUT);
        put(10, LOOM_CRAFT_INPUT);
        put(11, LOOM_CRAFT_INPUT);
        put(14, ENCHANT_CRAFT_INPUT);
        put(15, ENCHANT_CRAFT_INPUT);
        put(28, PLAYER_CRAFT_INPUT);
        put(29, PLAYER_CRAFT_INPUT);
        put(30, PLAYER_CRAFT_INPUT);
        put(31, PLAYER_CRAFT_INPUT);
        put(32, WORKBENCH_CRAFT_INPUT);
        put(33, WORKBENCH_CRAFT_INPUT);
        put(34, WORKBENCH_CRAFT_INPUT);
        put(35, WORKBENCH_CRAFT_INPUT);
        put(36, WORKBENCH_CRAFT_INPUT);
        put(37, WORKBENCH_CRAFT_INPUT);
        put(38, WORKBENCH_CRAFT_INPUT);
        put(39, WORKBENCH_CRAFT_INPUT);
        put(40, WORKBENCH_CRAFT_INPUT);
        put(50, CRAFT_OUTPUT);
    }};

    public static PlayerUIInventorySlot getUIInventory(int slot) {
        return PLAYER_UI_INVENTORY_SLOT_MAP.get(slot);
    }

    PlayerUIInventorySlot(int startSlot, int endSlot) {
        this.startSlot = startSlot;
        this.endSlot = endSlot;
    }

    public int getStartSlot() {
        return startSlot;
    }

    public int getEndSlot() {
        return endSlot;
    }


}
