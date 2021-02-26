package com.particle.game.entity.ai.factory;

import com.particle.game.entity.ai.model.ActionTree;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class MessageTreeFactory {

    private static final Map<String, ActionTree> MESSAGE_RESPONSE = new HashMap<>();

    public static ActionTree getMessageResponse(String action, String messageId) {
        return MESSAGE_RESPONSE.get(String.format("%s:%s", action, messageId));
    }

    public static void bindMessageResponse(String binder, String messageId, ActionTree actionTree) {
        MESSAGE_RESPONSE.put(String.format("%s:%s", binder, messageId), actionTree);
    }
}
