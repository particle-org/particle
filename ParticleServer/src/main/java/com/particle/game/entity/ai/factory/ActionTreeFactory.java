package com.particle.game.entity.ai.factory;

import com.particle.game.entity.ai.model.ActionTree;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ActionTreeFactory {

    private static final Map<String, ActionTree> ACTION_RECORDER = new HashMap<>();

    /**
     * 获取行为树
     *
     * @param action
     * @return
     */
    public static ActionTree getActionTree(String action) {
        return ACTION_RECORDER.get(action);
    }

    public static void bindAction(String binder, ActionTree actionTree) {
        ACTION_RECORDER.put(binder, actionTree);
    }
}
