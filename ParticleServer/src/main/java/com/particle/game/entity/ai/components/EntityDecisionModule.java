package com.particle.game.entity.ai.components;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.game.entity.ai.model.ActionTree;

public class EntityDecisionModule extends BehaviorModule {

    private long lastDecisionTimestamp;

    private ActionTree decision;

    private ActionTree action;

    /**
     * 决策信息
     */
    private KnowledgeDatabase knowledgeDatabase = new KnowledgeDatabase();

    public long getLastDecisionTimestamp() {
        return lastDecisionTimestamp;
    }

    public void setLastDecisionTimestamp(long lastDecisionTimestamp) {
        this.lastDecisionTimestamp = lastDecisionTimestamp;
    }

    public ActionTree getDecision() {
        return decision;
    }

    public void setDecision(ActionTree decision) {
        this.decision = decision;
    }

    public ActionTree getAction() {
        return action;
    }

    public void setAction(ActionTree action) {
        this.action = action;
    }

    /**
     * 设置上下文
     *
     * @param key
     * @param data
     */
    public void addKnowledge(String key, Object data) {
        this.knowledgeDatabase.addKnowledge(key, data);
    }

    /**
     * 移除知识点
     *
     * @param key
     */
    public void removeKnowledge(String key) {
        knowledgeDatabase.removeKnowledge(key);
    }

    /**
     * 查询上下文
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getKnowledge(String key, Class<T> clazz) {
        return this.knowledgeDatabase.getKnowlodge(key, clazz);
    }

}
