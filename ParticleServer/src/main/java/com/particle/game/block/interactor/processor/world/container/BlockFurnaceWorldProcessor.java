package com.particle.game.block.interactor.processor.world.container;

import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

public class BlockFurnaceWorldProcessor extends ContainerBlockProcessor {
    @Override
    public boolean onBlockPreDestroy(Level level, Player player, Block targetBlock, Vector3 position) {
        targetBlock = Block.getBlock(BlockPrototype.FURNACE);

        return super.onBlockPreDestroy(level, player, targetBlock, position);
    }
}
