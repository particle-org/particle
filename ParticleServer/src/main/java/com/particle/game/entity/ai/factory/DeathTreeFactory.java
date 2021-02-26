package com.particle.game.entity.ai.factory;

import com.particle.game.entity.ai.model.ActionTree;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class DeathTreeFactory {

    private static final Map<String, ActionTree> DEATH_RESPONSE = new HashMap<>();

    public static ActionTree getDeathResponse(String action) {
        return DEATH_RESPONSE.get(action);
    }

    public static void bingDeathResponse(String binder, ActionTree actionTree) {
        DEATH_RESPONSE.put(binder, actionTree);
    }
}
