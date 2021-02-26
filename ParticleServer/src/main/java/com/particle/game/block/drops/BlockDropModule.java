package com.particle.game.block.drops;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.game.block.drops.data.IBlockDrop;

public class BlockDropModule extends BehaviorModule {

    private IBlockDrop blockDrop;
    private boolean keepMetaData;

    public void update(IBlockDrop blockDrop, boolean keepMetaData) {
        this.blockDrop = blockDrop;
        this.keepMetaData = keepMetaData;
    }

    public IBlockDrop getBlockDrop() {
        return blockDrop;
    }

    public boolean isKeepMetaData() {
        return keepMetaData;
    }
}
