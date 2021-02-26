package com.particle.api.block;

import com.particle.model.block.Block;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

public interface IBlockInteractedProcessor {

    /**
     * 当目标方块被交互
     *
     * @param player
     * @param targetBlock
     * @param targetPosition
     * @return true表示可以继续交互，false表示交互取消
     */
    boolean interactive(Player player, Block targetBlock, Vector3 targetPosition);

}
