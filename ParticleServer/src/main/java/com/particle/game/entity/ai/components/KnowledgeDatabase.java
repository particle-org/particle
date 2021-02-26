package com.particle.game.entity.ai.components;


import java.util.HashMap;
import java.util.Map;

public class KnowledgeDatabase {
    private Map<String, Object> knowledges = new HashMap<>();

    /**
     * 增加知识点
     *
     * @param key
     * @param object
     * @return
     */
    public boolean addKnowledge(String key, Object object) {
        this.knowledges.put(key, object);

        return true;
    }

    /**
     * 查询知识点
     *
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public <T> T getKnowlodge(String key, Class<T> type) {
        Object object = knowledges.get(key);

        if (object != null && type.isAssignableFrom(object.getClass())) {
            return (T) object;
        }

        return null;
    }

    /**
     * 移除知识点
     *
     * @param key
     */
    public void removeKnowledge(String key) {
        this.knowledges.remove(key);
    }

    public void clear() {
        this.knowledges.clear();
    }
}
