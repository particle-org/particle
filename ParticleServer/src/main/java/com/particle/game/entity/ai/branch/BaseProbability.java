package com.particle.game.entity.ai.branch;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IBehaviour;
import com.particle.api.ai.behavior.IProbability;
import com.particle.model.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class BaseProbability implements IProbability {

    private EStatus status = EStatus.INITIALIZATION;

    /**
     * 子节点列表
     */
    protected ArrayList<IBehaviour> children = new ArrayList<>();

    @Override
    public void addChild(IBehaviour child, int weight) {
        this.children.add(child);
    }

    @Override
    public void removeChild(IBehaviour child) {
        this.children.remove(child);
    }

    @Override
    public void clearChild() {
        this.children.clear();
    }

    @Override
    public List<IBehaviour> getChildren() {
        return new BehaviorList<IBehaviour>(this.children);
    }

    @Override
    public void onInitialize() {
        for (IBehaviour behaviour : this.children) {
            behaviour.onInitialize();
        }
    }

    @Override
    public EStatus tick(Entity entity) {
        int index = (int) (Math.random() * this.children.size());

        this.status = this.children.get(index).tick(entity);

        return this.status;
    }

    @Override
    public void onTicked(Entity entity, EStatus status) {
        for (IBehaviour behaviour : this.children) {
            behaviour.onTicked(entity, status);
        }
    }

    @Override
    public void onRelease() {
        for (IBehaviour behaviour : this.children) {
            behaviour.onRelease();
        }
    }

    @Override
    public void config(String key, Object val) {

    }
}
