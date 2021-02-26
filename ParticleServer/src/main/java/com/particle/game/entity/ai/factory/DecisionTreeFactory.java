package com.particle.game.entity.ai.factory;

import com.particle.game.entity.ai.model.ActionTree;

import java.util.HashMap;
import java.util.Map;

public class DecisionTreeFactory {

    private static final Map<String, ActionTree> DECISIONS_MAP = new HashMap<>();

    /**
     * 获取行为树
     *
     * @param action
     * @return
     */
    public static ActionTree getDecision(String action) {
        return DECISIONS_MAP.get(action);
    }

    public static void bindDecision(String binder, ActionTree actionTree) {
        DECISIONS_MAP.put(binder, actionTree);
    }
}
