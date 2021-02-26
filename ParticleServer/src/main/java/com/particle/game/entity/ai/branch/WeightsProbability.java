package com.particle.game.entity.ai.branch;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IBehaviour;
import com.particle.api.ai.behavior.IProbability;
import com.particle.model.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class WeightsProbability implements IProbability {

    private EStatus status = EStatus.INITIALIZATION;

    /**
     * 子节点列表
     */
    private ArrayList<IBehaviour> children = new ArrayList<>();
    private ArrayList<Integer> weights = new ArrayList<>();
    private int maxWeight = 0;

    @Override
    public void addChild(IBehaviour child, int weight) {
        this.children.add(child);
        this.weights.add(weight);
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
        if (this.children.size() != this.weights.size()) {
            throw new RuntimeException("Weight size miss match!");
        }
        this.maxWeight = 0;

        for (int i = 0; i < this.children.size(); i++) {
            this.children.get(i).onInitialize();
            this.maxWeight += this.weights.get(i);
        }
    }

    @Override
    public EStatus tick(Entity entity) {
        int selectIndex = (int) (Math.random() * maxWeight);

        for (int i = 0; i < this.weights.size(); i++) {
            selectIndex -= this.weights.get(i);

            if (selectIndex <= 0) {
                this.status = this.children.get(i).tick(entity);
                return this.status;
            }
        }

        this.status = EStatus.FAILURE;
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
