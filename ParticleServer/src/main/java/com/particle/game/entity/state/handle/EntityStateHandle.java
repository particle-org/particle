package com.particle.game.entity.state.handle;

import com.particle.model.entity.Entity;
import com.particle.model.entity.type.EntityStateRecorder;
import com.particle.model.math.Vector3;

public interface EntityStateHandle {

    String getDisplayName();

    void onStateEnabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean hasEnabled);

    /**
     * 部分状态需要來源
     */
    default void onStateEnabled(Entity source, Entity entity, EntityStateRecorder entityStateRecorder, boolean hasEnabled) {
    }

    void onStateUpdated(Entity entity, EntityStateRecorder entityStateRecorder);

    void onStateDisabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean needDisableState);

    /**
     * 部分状态允许对方块施加一个瞬时效果
     *
     * @param hitPosition
     */
    default void onStateHitBlock(Entity entity, Vector3 hitPosition) {
    }

}
