package com.particle.game.block.interactor.processor.world.direction;

import com.particle.game.block.interactor.processor.world.BasicBlockProcessor;
import com.particle.model.block.Block;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Singleton;

@Singleton
public class BlockLogWorldProcessor extends BasicBlockProcessor {

    @Override
    public boolean onBlockPreDestroy(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        // 恢復原 meta 值
        if (targetBlock.getMeta() >> 2 != 0) {
            targetBlock.setMeta(targetBlock.getMeta() % 4);
        }

        return super.onBlockPreDestroy(level, player, targetBlock, targetPosition);
    }
}
