package com.particle.game.block.interactor.processor.updater;

import com.particle.game.block.interactor.BlockOperationType;
import com.particle.model.block.Block;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

public interface IBlockWorldUpdater {

    /**
     * 当方块更新时，回调该接口
     *
     * @return
     */
    boolean onBlockUpdated(Level level, Player player, Block targetBlock, Vector3 targetPosition, BlockOperationType blockOperationType);
}
