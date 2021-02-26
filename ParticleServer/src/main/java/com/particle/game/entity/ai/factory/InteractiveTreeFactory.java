package com.particle.game.entity.ai.factory;

import com.particle.game.entity.ai.model.ActionTree;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class InteractiveTreeFactory {

    private static final Map<String, ActionTree> INTERACTIVE_RESPONSE = new HashMap<>();

    public static ActionTree getInteractiveReponse(String action) {
        return INTERACTIVE_RESPONSE.get(action);
    }

    public static void bingInteractiveResponse(String binder, ActionTree actionTree) {
        INTERACTIVE_RESPONSE.put(binder, actionTree);
    }
}
