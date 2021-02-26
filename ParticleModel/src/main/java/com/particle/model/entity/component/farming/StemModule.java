package com.particle.model.entity.component.farming;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.block.types.BlockPrototype;

public class StemModule extends BehaviorModule {

    private BlockPrototype stemType;
    private BlockPrototype fruitType;

    public StemModule(BlockPrototype stemType, BlockPrototype fruitType) {
        this.stemType = stemType;
        this.fruitType = fruitType;
    }

    public StemModule() {

    }

    public void setStemType(BlockPrototype stemType) {
        this.stemType = stemType;
    }

    public void setFruitType(BlockPrototype fruitType) {
        this.fruitType = fruitType;
    }

    public BlockPrototype getStemType() {
        return stemType;
    }

    public BlockPrototype getFruitType() {
        return fruitType;
    }
}
