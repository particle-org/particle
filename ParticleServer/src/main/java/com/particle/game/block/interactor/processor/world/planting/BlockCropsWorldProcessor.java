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
public class BlockCropsWorldProcessor extends BreakableBlockProcessor {

    @Inject
    private LevelService levelService;

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        Vector3 down = targetPosition.down(1);
        BlockPrototype block = levelService.getBlockTypeAt(level, targetPosition);
        BlockPrototype downBlock = levelService.getBlockTypeAt(level, down);
        if (downBlock == null || downBlock != BlockPrototype.FARMLAND || block != BlockPrototype.AIR) {
            return false;
        }
        return true;
    }

    @Override
    public boolean handleBlockPlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        // 若不是頂部
        if (clickOffsetPosition.getY() < 1f) {
            return false;
        }

        return super.handleBlockPlaced(level, player, targetBlock, targetPosition, clickPosition, clickOffsetPosition);
    }

    @Override
    protected List<ItemStack> onPreDrops(Level level, Player player, Block targetBlock, Vector3 targetPosition, List<ItemStack> blockDrops) {
        if (targetBlock == null) {
            return blockDrops;
        }
        switch (targetBlock.getType()) {
            case WHEAT:
                return this.getWheatDrops(targetBlock, blockDrops);
            case POTATOES:
                return this.getPotatoesDrops(targetBlock, blockDrops);
            case CARROTS:
                return this.getCarrotsDrops(targetBlock, blockDrops);
            case BEETROOT:
                return this.getBeetrootDrops(targetBlock, blockDrops);
            case TALLGRASS:
                return this.getTallGrassDrops(player, targetBlock, blockDrops);
        }
        return blockDrops;
    }

    /**
     * 小麦
     *
     * @param targetBlock
     * @param blockDrops
     * @return
     */
    private List<ItemStack> getWheatDrops(Block targetBlock, List<ItemStack> blockDrops) {
        int meta = targetBlock.getMeta();
        if (meta == 7) {
            blockDrops.add(ItemStack.getItem(ItemPrototype.WHEAT_ITEM));
            int seedCount = (int) Math.floor(Math.random() * 2.5) + 1;
            if (seedCount != 0) {
                blockDrops.add(ItemStack.getItem(ItemPrototype.WHEAT_SEEDS, seedCount));
            }
        } else {
            blockDrops.add(ItemStack.getItem(ItemPrototype.WHEAT_SEEDS, 1));
        }
        return blockDrops;
    }

    /**
     * 获取马铃薯
     *
     * @param targetBlock
     * @param blockDrops
     * @return
     */
    private List<ItemStack> getPotatoesDrops(Block targetBlock, List<ItemStack> blockDrops) {
        int meta = targetBlock.getMeta();
        if (meta == 7) {
            int potatoCounts = (int) Math.round(Math.random() * 4);
            blockDrops.add(ItemStack.getItem(ItemPrototype.POTATO, potatoCounts));
            if (potatoCounts < 4 && Math.random() <= 0.02) {
                blockDrops.add(ItemStack.getItem(ItemPrototype.POISONOUS_POTATO));
            }
        } else {
            blockDrops.add(ItemStack.getItem(ItemPrototype.POTATO, 1));
        }
        return blockDrops;
    }

    /**
     * 获取胡萝卜
     *
     * @param targetBlock
     * @param blockDrops
     * @return
     */
    private List<ItemStack> getCarrotsDrops(Block targetBlock, List<ItemStack> blockDrops) {
        int meta = targetBlock.getMeta();
        if (meta == 7) {
            int potatoCounts = (int) Math.round(Math.random() * 4);
            blockDrops.add(ItemStack.getItem(ItemPrototype.CARROT, potatoCounts));
        } else {
            blockDrops.add(ItemStack.getItem(ItemPrototype.CARROT, 1));
        }
        return blockDrops;
    }

    /**
     * 获取甜菜根
     *
     * @param targetBlock
     * @param blockDrops
     * @return
     */
    private List<ItemStack> getBeetrootDrops(Block targetBlock, List<ItemStack> blockDrops) {
        int meta = targetBlock.getMeta();
        if (meta == 4) {
            blockDrops.add(ItemStack.getItem(ItemPrototype.BEETROOT_ITEM));
            int potatoCounts = (int) Math.round(Math.random() * 3);
            blockDrops.add(ItemStack.getItem(ItemPrototype.BEETROOT_SEEDS, potatoCounts));
        } else {
            blockDrops.add(ItemStack.getItem(ItemPrototype.BEETROOT_SEEDS, 1));
        }
        return blockDrops;
    }

    /**
     * 获取小麦
     *
     * @param targetBlock
     * @param blockDrops
     * @return
     */
    private List<ItemStack> getTallGrassDrops(Player player, Block targetBlock, List<ItemStack> blockDrops) {
        if (player != null) {
            PlayerInventory inventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
            ItemStack hand = this.playerInventoryService.getItem(inventory, inventory.getItemInHandle());
            if (hand.getItemType() == ItemPrototype.SHEARS) {
                return blockDrops;
            }
        }
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
