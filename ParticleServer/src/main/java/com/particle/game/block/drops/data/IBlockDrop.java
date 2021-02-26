package com.particle.game.block.drops.data;

import com.particle.model.item.ItemStack;

import java.util.List;

public interface IBlockDrop {

    /**
     * 只有 @{link BlockDropWithMeta} 才会根据pmeta构成掉落物
     *
     * @param pmeta
     * @return
     */
    List<ItemStack> getDrops(int pmeta);
}
