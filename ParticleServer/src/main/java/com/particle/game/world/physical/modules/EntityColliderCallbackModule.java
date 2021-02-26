package com.particle.game.world.physical.modules;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

import java.util.function.Consumer;

public class EntityColliderCallbackModule extends BehaviorModule {

    /**
     * 碰撞回调
     */
    private Consumer<Entity> onColliderWithEntity;
    private Consumer<Vector3f> onColliderWithBlock;

    /**
     * 获取碰撞回调
     */
    public Consumer<Entity> getOnColliderWithEntityCallback() {
        return this.onColliderWithEntity;
    }

    public Consumer<Vector3f> getOnColliderWithBlockCallback() {
        return this.onColliderWithBlock;
    }

    /**
     * 设置碰撞回调
     *
     * @param onColliderWith
     */
    public void setOnColliderWithEntityCallback(Consumer<Entity> onColliderWith) {
        this.onColliderWithEntity = onColliderWith;
    }

    public void setOnColliderWithBlockCallback(Consumer<Vector3f> onColliderWith) {
        this.onColliderWithBlock = onColliderWith;
    }

}
