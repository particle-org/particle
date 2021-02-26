package com.particle.api.block;

import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

public interface BlockColliderDetectServiceApi {
    /**
     * 检测是否在地面上
     *
     * @param entity
     * @return
     */
    public boolean isStandOnBlock(Entity entity, Vector3f position);
}
