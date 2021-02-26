package com.particle.game.block.interactor;

import com.particle.model.block.Block;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

public interface IBlockWorldProcessor {

    /**
     * 当方块被放置时，做一层拦截，可在方块被放置时候更改其属性
     *
     * @param player              该值可能为空
     * @param targetBlock
     * @param targetPosition
     * @param clickOffsetPosition
     * @return
     */
    boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition);

    /**
     * 處理放置邏輯
     *
     * @param player              该值可能为空
     * @param targetBlock
     * @param targetPosition
     * @param clickOffsetPosition
     * @return
     */
    boolean handleBlockPlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition);

    /**
     * 当方块放置的时候，初始化对应的entity
     *
     * @param player         该值可能为空
     * @param targetBlock
     * @param targetPosition
     */
    boolean onBlockPlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition);

    /**
     * 当方块被破坏前，回调该接口
     * 在该接口中，可根据目标block更改item的meta值
     * 可根据手中的item，判断是否存在掉落物
     *
     * @return
     */
    boolean onBlockPreDestroy(Level level, Player player, Block targetBlock, Vector3 targetPosition);

    /**
     * 当方块掉落后，回调该接口
     * 在该接口中，可根据目标block更改item的meta值
     * 可根据手中的item，判断是否存在掉落物
     *
     * @return
     */
    boolean onBlockDestroyed(Level level, Player player, Block targetBlock, Vector3 targetPosition);
}
