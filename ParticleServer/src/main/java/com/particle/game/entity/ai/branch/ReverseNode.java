package com.particle.game.entity.ai.branch;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IBehaviour;
import com.particle.api.ai.behavior.IBranchNode;
import com.particle.model.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class ReverseNode implements IBranchNode {

    private IBehaviour child;

    @Override
    public void addChild(IBehaviour child, int weight) {
        this.child = child;
    }

    @Override
    public void removeChild(IBehaviour child) {
        this.child = null;
    }

    @Override
    public void clearChild() {
        this.child = null;
    }

    @Override
    public List<IBehaviour> getChildren() {
        return new ArrayList<IBehaviour>() {{
            add(child);
        }};
    }

    @Override
    public void onInitialize() {
        this.child.onInitialize();
    }

    @Override
    public EStatus tick(Entity entity) {
        EStatus status = this.child.tick(entity);

        if (status == EStatus.SUCCESS) return EStatus.FAILURE;
        else if (status == EStatus.FAILURE) return EStatus.SUCCESS;
        else return status;
    }

    @Override
    public void onTicked(Entity entity, EStatus status) {
        this.child.onTicked(entity, status);
    }

    @Override
    public void onRelease() {
        this.child.onRelease();
    }

    @Override
    public void config(String key, Object val) {

    }
}
