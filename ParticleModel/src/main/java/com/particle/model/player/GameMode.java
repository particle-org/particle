package com.particle.model.player;

public enum GameMode {
    SURVIVE(0, "survival_mode"),
    CREATIVE(1, "creative_mode"),
    ADVENTURE(2, "adventure_mode"),
    SURVIVAL_VIEWER(3, "survival_viewer"),
    CREATIVE_VIEWER(4, "creative_viewer");

    private int mode;

    private String description;

    private static final GameMode[] allModes = GameMode.values();

    GameMode(int mode, String description) {
        this.mode = mode;
        this.description = description;
    }

    public int getMode() {
        return mode;
    }

    public String getDescription() {
        return description;
    }

    public static GameMode valueOf(int mode) {
        return allModes[mode % allModes.length];
    }
}
