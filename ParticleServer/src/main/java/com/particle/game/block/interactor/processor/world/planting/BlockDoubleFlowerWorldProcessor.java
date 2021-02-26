package com.particle.game.block.interactor.processor.world.planting;

import com.particle.game.block.interactor.processor.world.BreakableBlockProcessor;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Singleton
public class BlockDoubleFlowerWorldProcessor extends BreakableBlockProcessor {

    @Inject
    private LevelService levelService;

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        Vector3 upPosition = targetPosition.up();
        BlockPrototype upBlock = this.levelService.getBlockTypeAt(level, upPosition);
        if (upBlock == null || upBlock == BlockPrototype.AIR) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onBlockPlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        Block upBlock = Block.getBlock(targetBlock.getType());
        upBlock.setMeta(0x08);
        this.levelService.setBlockAt(level, upBlock, targetPosition.up());
        return true;
    }

    @Override
    public boolean onBlockPreDestroy(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        return super.onBlockPreDestroy(level, player, targetBlock, targetPosition);
    }

    @Override
    public boolean onBlockDestroyed(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        return super.onBlockDestroyed(level, player, targetBlock, targetPosition);
    }

    @Override
    protected List<ItemStack> onPreDrops(Level level, Player player, Block targetBlock, Vector3 targetPosition, List<ItemStack> blockDrops) {
        int meta = targetBlock.getMeta();
        if ((meta & 0x08) != 0x08) {
            switch (meta & 0x07) {
                case 2:
                case 3:
                    PlayerInventory inventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
                    ItemStack hand = this.playerInventoryService.getItem(inventory, inventory.getItemInHandle());
                    // 用剪刀会掉落物品形式，对于双草丛和大型厥，会掉落两个
                    if (hand.getItemType() == ItemPrototype.SHEARS) {
                        for (ItemStack itemStack : blockDrops) {
                            itemStack.setCount(2);
                        }
                        return blockDrops;
                    } else {
                        // 其他方式，只能掉落种子
                        boolean dropSeeds = new Random().nextInt(23) == 0;
                        if (dropSeeds) {
                            ItemStack wheatSeeds = ItemStack.getItem(ItemPrototype.WHEAT_SEEDS, 1);
                            List<ItemStack> results = new ArrayList<>();
                            results.add(wheatSeeds);
                            return results;
                        } else {
                            return new ArrayList<>();
                        }
                    }

            }
            return blockDrops;
        }
        return new ArrayList<>();
    }
}
