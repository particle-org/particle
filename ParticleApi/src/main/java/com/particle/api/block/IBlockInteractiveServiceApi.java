package com.particle.api.block;

import com.particle.model.block.types.BlockPrototype;

public interface IBlockInteractiveServiceApi {
    void registerBlockInteractive(BlockPrototype blockPrototype, IBlockInteractedProcessor blockInteractedProcessor);
}
