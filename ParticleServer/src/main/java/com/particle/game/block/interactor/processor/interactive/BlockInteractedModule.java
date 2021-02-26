package com.particle.game.block.interactor.processor.interactive;

import com.particle.api.block.IBlockInteractedProcessor;
import com.particle.core.ecs.module.BehaviorModule;

public class BlockInteractedModule extends BehaviorModule {

    /**
     * 方块交互的回调
     */
    private IBlockInteractedProcessor blockInteractedProcessor;

    private boolean hasTileEntity;

    public IBlockInteractedProcessor getBlockInteractedProcessor() {
        return blockInteractedProcessor;
    }

    public void setBlockInteractedProcessor(IBlockInteractedProcessor blockInteractedProcessor) {
        this.blockInteractedProcessor = blockInteractedProcessor;
    }

    public boolean isHasTileEntity() {
        return hasTileEntity;
    }

    public void setHasTileEntity(boolean hasTileEntity) {
        this.hasTileEntity = hasTileEntity;
    }
}
