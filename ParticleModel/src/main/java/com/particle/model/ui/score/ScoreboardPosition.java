package com.particle.model.ui.score;

import java.util.HashMap;
import java.util.Map;

public enum ScoreboardPosition {
    LIST("list"),
    SIDEBAR("sidebar"),
    SIDEBAR_TEAM_RED("sidebar.team.red"),
    BELOW_NAME("belowName");

    private String position;

    private static final Map<String, ScoreboardPosition> types = new HashMap<>();

    static {
        for (ScoreboardPosition item : ScoreboardPosition.values()) {
            types.put(item.getPosition(), item);
        }
    }

    ScoreboardPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public static ScoreboardPosition from(String position) {
        return types.get(position);
    }
}
