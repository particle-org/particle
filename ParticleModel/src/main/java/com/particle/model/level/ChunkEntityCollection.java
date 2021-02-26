package com.particle.model.level;

import com.particle.model.entity.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChunkEntityCollection<T extends Entity> {

    private List<T> entityViewer = new ArrayList<>();
    private Map<Long, T> entities = new ConcurrentHashMap<>();
    private boolean updated = false;

    /**
     * 注册生物
     *
     * @param entity
     * @return
     */
    public boolean registerEntity(T entity) {
        T old = this.entities.put(entity.getRuntimeId(), entity);

        if (old != null && old.isSame(entity)) {
            return false;
        } else {
            this.updated = true;

            return true;
        }
    }

    /**
     * 注册生物列表
     *
     * @param entities
     * @return
     */
    public boolean registerEntities(List<T> entities) {
        boolean changed = false;

        for (T entity : entities) {
            T old = this.entities.put(entity.getRuntimeId(), entity);

            if (old == null || !old.isSame(entity)) {
                changed = true;
            }
        }

        if (changed) {
            this.updated = true;
        }

        return changed;
    }

    /**
     * 移除生物
     *
     * @param entityId
     * @return
     */
    public boolean removeEntity(long entityId) {
        boolean changed = this.entities.remove(entityId) != null;

        if (changed) {
            this.updated = true;
        }

        return changed;
    }

    /**
     * @param entityId
     * @return 查找生物
     */
    public T findEntity(long entityId) {
        return this.entities.get(entityId);
    }

    /**
     * 查询生物视图
     *
     * @return
     */
    public List<T> getEntitiesViewer() {
        // 懒刷新，避免在tick中调用出现不同步的情况
        this.syncEntityViewer();

        return entityViewer;
    }

    public List<T> getTickedEntitiesViewer() {
        return this.getEntitiesViewer();
    }

    /**
     * 获取生物hash列表
     *
     * @return
     */
    public Map<Long, T> getEntities() {
        return Collections.unmodifiableMap(this.entities);
    }

    /**
     * 主动同步数据
     */
    public boolean syncEntityViewer() {
        if (this.updated) {
            // 先重置标志位，防止在并发过程中，entityView生成过程中entities内容发生变化
            this.updated = false;

            this.entityViewer = Collections.unmodifiableList(new ArrayList<>(this.entities.values()));

            return true;
        }

        return false;
    }

    public void requestUpdate() {
        this.updated = true;
    }

}
