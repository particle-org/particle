package com.particle.game.world.physical.modules;

import com.google.common.collect.Sets;
import com.particle.core.ecs.module.BehaviorModule;
import com.particle.game.world.physical.collider.AABBCollider;
import com.particle.game.world.physical.module.ColliderDetectAlgorithm;
import com.particle.model.math.Vector3f;

import java.util.HashSet;
import java.util.Set;

public class BoxColliderModule extends BehaviorModule {

    private AABBCollider aabbCollider;

    // AABB碰撞之间的弹力
    private Set<Long> linkedEntity = new HashSet<>();

    // 碰撞Entity集合
    private Set<Long> colliderEntities = Sets.newHashSet();

    private ColliderDetectAlgorithm detectAlgorithm = ColliderDetectAlgorithm.SAMPLING;

    /**
     * 判断是否可以同时碰撞多个(默认为true，具体功能待实现)
     */
    private boolean canMultiCollided = true;

    public void setAABBCollider(AABBCollider aabbCollider) {
        this.aabbCollider = aabbCollider;
    }

    public AABBCollider getAABBCollider() {
        return aabbCollider;
    }

    public ColliderDetectAlgorithm getDetectAlgorithm() {
        return detectAlgorithm;
    }

    public void setDetectAlgorithm(ColliderDetectAlgorithm detectAlgorithm) {
        this.detectAlgorithm = detectAlgorithm;
    }

    public Vector3f distanceTo(BoxColliderModule to) {
        return to.getAABBCollider().getLastPosition().subtract(this.aabbCollider.getLastPosition());
    }

    /**
     * 设置碰撞产生的加速度
     *
     * @param motion
     */
    public void setColliderMotion(Vector3f motion) {
        this.aabbCollider.setColliderMotion(motion);
    }

    // --- 关节/骑乘 ---
    public void addLinkedEntity(long eid) {
        this.linkedEntity.add(eid);
    }

    public void removeLinkedEntity(long eid) {
        this.linkedEntity.remove(eid);
    }

    public boolean isLinkedWith(long eid) {
        return this.linkedEntity.contains(eid);
    }

    public boolean containsColliderEntity(long eid) {
        return colliderEntities.contains(eid);
    }

    public void addColliderEntity(long eid) {
        colliderEntities.add(eid);
    }

    public void removeColliderEntity(long eid) {
        colliderEntities.remove(eid);
    }
}
