package com.particle.model.level;

import com.particle.core.ecs.system.ECSSystemManager;
import com.particle.model.entity.model.tile.TileEntity;

import java.util.ArrayList;
import java.util.List;

public class ChunkTileEntityCollection<T extends TileEntity> extends ChunkEntityCollection<T> {

    private List<T> entityTickedViewer = new ArrayList<>();

    /**
     * @return
     */
    public List<T> getTickedEntitiesViewer() {
        return this.entityTickedViewer;
    }

    /**
     * 主动同步数据
     */
    @Override
    public boolean syncEntityViewer() {
        if (super.syncEntityViewer()) {
            List<T> filterEntities = new ArrayList<>();

            for (T entity : this.getEntitiesViewer()) {
                if (ECSSystemManager.getECSSystemTickList(entity).size() > 0 || entity.getTickedSystem().size() > 0) {
                    filterEntities.add(entity);
                }
            }

            this.entityTickedViewer = filterEntities;

            return true;
        }

        return false;
    }

}
