package com.particle.game.block.interactor.processor.interactive;

import com.particle.api.block.IBlockInteractedProcessor;
import com.particle.game.entity.attribute.satisfaction.HungerService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockCakeInteracted implements IBlockInteractedProcessor {

    @Inject
    private HungerService hungerService;

    @Inject
    private LevelService levelService;

    @Override
    public boolean interactive(Player player, Block targetBlock, Vector3 targetPosition) {
        synchronized (BlockCakeInteracted.class) {
            int meta = targetBlock.getMeta();
            if (meta <= 0x06) {
                meta++;
            }
            if (meta >= 0x07) {
                this.hungerService.eatFood(player, ItemStack.getItem(ItemPrototype.CAKE, 1));
                this.levelService.setBlockAt(player.getLevel(), Block.getBlock(BlockPrototype.AIR), targetPosition);
            } else {
                this.hungerService.eatFood(player, ItemStack.getItem(ItemPrototype.CAKE, 1));
                targetBlock.setMeta(meta);
                this.levelService.setBlockAt(player.getLevel(), targetBlock, targetPosition);
            }

            return true;
        }
    }
}
