package com.particle.game.entity.spawn;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.entity.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelBindEntityModule extends BehaviorModule {

    /**
     * spawn进世界的生物缓存
     */
    private Map<Long, List<Entity>> chunkToEntitiesCache = new HashMap<>();

    public Map<Long, List<Entity>> getChunkToEntitiesCache() {
        return chunkToEntitiesCache;
    }

    public void setChunkToEntitiesCache(Map<Long, List<Entity>> chunkToEntitiesCache) {
        this.chunkToEntitiesCache = chunkToEntitiesCache;
    }
}
