package com.particle.api.ai;

import com.particle.model.entity.Entity;

public interface EntityDecisionServiceApi {
    void updateDecision(Entity entity, String decision);

    void updateResponse(Entity entity, String actionId);

    void resetActionDecision(Entity entity);

    void addKnowledge(Entity entity, String key, Object val);

    void removeKnowledge(Entity entity, String key);

    <T> T getKnowledge(Entity entity, String key, Class<T> clazz);
}
