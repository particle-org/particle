package com.particle.game.entity.state;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.game.entity.state.model.EntityStateType;
import com.particle.model.entity.type.EntityStateRecorder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EntityStateModule extends BehaviorModule {
    private Map<String, EntityStateRecorder> entityState = new HashMap<>();

    // 登录时，需要启用缓存中的state
    private boolean hasFirstEnabled = false;

    private Map<String, EntityStateRecorder> bindState = new HashMap<>();

    public EntityStateRecorder setEntityState(String name, EntityStateRecorder recorder) {
        return this.entityState.put(name, recorder);
    }

    public Map<String, EntityStateRecorder> getEntityState() {
        return entityState;
    }

    public Collection<EntityStateRecorder> clearEntityStateRecorders() {
        Collection<EntityStateRecorder> values = this.entityState.values();

        this.entityState.clear();

        return values;
    }

    public boolean hasState(EntityStateType entityStateType) {
        if (entityStateType == null) {
            return false;
        }
        return hasState(entityStateType.getName());
    }

    public boolean hasState(String name) {
        return this.entityState.containsKey(name);
    }

    public boolean hasBindState(String name) {
        return this.bindState.containsKey(name);
    }

    public EntityStateRecorder getState(String name) {
        return this.entityState.get(name);
    }

    public EntityStateRecorder removeEntityState(String name) {
        return this.entityState.remove(name);
    }

    public void resetEntityState() {
        this.entityState = new HashMap<>();
    }

    public boolean hasFirstEnabled() {
        return hasFirstEnabled;
    }

    public void enableFirstTick() {
        this.hasFirstEnabled = true;
    }

    public EntityStateRecorder bindState(String name, int level) {
        String key = name + "_" + level;

        EntityStateRecorder entityStateRecorder = this.bindState.get(key);
        if (entityStateRecorder == null) {
            entityStateRecorder = new EntityStateRecorder();
            entityStateRecorder.setName(name);
            entityStateRecorder.setLevel(level);
            entityStateRecorder.setEnableTimestamp(System.currentTimeMillis());
            entityStateRecorder.setUpdateInterval(3000);
            entityStateRecorder.setTimeToLive(-1);
            entityStateRecorder.setEnabledCount(1);

            this.bindState.put(key, entityStateRecorder);
        } else {
            entityStateRecorder.setEnabledCount(entityStateRecorder.getEnabledCount() + 1);
        }

        return entityStateRecorder;
    }

    public EntityStateRecorder unbindState(String name, int level) {
        String key = name + "_" + level;

        EntityStateRecorder entityStateRecorder = this.bindState.get(key);
        if (entityStateRecorder != null) {
            int enabledCount = entityStateRecorder.getEnabledCount();
            entityStateRecorder.setEnabledCount(enabledCount - 1);

            if (enabledCount == 1) {
                this.bindState.remove(key);
            }
        }

        return entityStateRecorder;
    }

    public EntityStateRecorder importBindState(String name, EntityStateRecorder recorder) {
        return this.bindState.put(name, recorder);
    }

    public Map<String, EntityStateRecorder> getBindState() {
        return bindState;
    }

}
