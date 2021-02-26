package com.particle.game.entity.ai.factory;

import com.particle.game.entity.ai.model.ActionTree;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class UnderAttackTreeFactory {

    private static final Map<String, ActionTree> UNDER_ATTACK_RESPONSE = new HashMap<>();

    public static ActionTree getUnderAttackReponse(String action) {
        return UNDER_ATTACK_RESPONSE.get(action);
    }

    public static void bingUnderAttackResponse(String binder, ActionTree actionTree) {
        UNDER_ATTACK_RESPONSE.put(binder, actionTree);
    }
}
