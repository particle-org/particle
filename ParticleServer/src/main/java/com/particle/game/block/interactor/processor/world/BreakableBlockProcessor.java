package com.particle.game.block.interactor.processor.world;

import com.particle.game.block.drops.BlockDropService;
import com.particle.game.item.ItemAttributeService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.model.block.Block;
import com.particle.model.block.element.BlockElement;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.enchantment.Enchantments;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.item.types.ItemTag;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public abstract class BreakableBlockProcessor extends BasicBlockProcessor {

    @Inject
    protected PlayerInventoryAPI playerInventoryService;

    @Inject
    protected InventoryManager inventoryManager;

    @Inject
    private BlockDropService blockDropService;

    @Override
    public boolean onBlockPreDestroy(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        if (player != null) {
            //硬度判定
            BlockElement blockElement = targetBlock.getType().getBlockElement();

            PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player,
                    InventoryConstants.CONTAINER_ID_PLAYER);

            int digLevel = 0;
            ItemPrototype holdItemType = this.playerInventoryService.getItem(playerInventory, playerInventory.getItemInHandle()).getItemType();
            if (holdItemType.hasTag(ItemTag.MATERIAL_WOOD)) {
                digLevel = 1;
            } else if (holdItemType.hasTag(ItemTag.MATERIAL_STONE)) {
                digLevel = 2;
            } else if (holdItemType.hasTag(ItemTag.MATERIAL_IRON)) {
                digLevel = 3;
            } else if (holdItemType.hasTag(ItemTag.MATERIAL_GOLD)) {
                digLevel = 4;
            } else if (holdItemType.hasTag(ItemTag.MATERIAL_DIAMOND)) {
                digLevel = 5;
            }
            if (holdItemType.hasTag(ItemTag.MATERIAL_MAGIC)) {
                digLevel = 5;
            }

            if (digLevel < blockElement.getLevel()) {
                return true;
            }

            //精准附魔的处理
            ItemStack holdItem = this.playerInventoryService.getItem(playerInventory, playerInventory.getItemInHandle());
            if (ItemAttributeService.hasEnchantment(holdItem, Enchantments.SILK_TOUCH)) {
                // 是否為過濾物
                boolean isFilter = isFilterSilkTouch(targetBlock);

                if (!isFilter) {
                    return super.onBlockPreDestroy(level, player, targetBlock, targetPosition);
                }
            }
        }

        //方块掉落
        List<ItemStack> drops = this.blockDropService.getBlockDrops(targetBlock);

        //有变化的掉落物的处理
        List<ItemStack> blockDrops = this.onPreDrops(level, player, targetBlock, targetPosition, drops);
        if (blockDrops != null) {
            for (ItemStack itemStack : blockDrops) {
                ItemEntity itemEntity = this.itemEntityService.createEntity(itemStack, targetPosition, new Vector3f((float) Math.random() - 0.5f, 4f, (float) Math.random() - 0.5f));

                this.entitySpawnService.spawn(level, itemEntity);
            }
        }

        return true;
    }

    /**
     * 处理额外的掉落物
     *
     * @param level
     * @param player
     * @param targetBlock
     * @param targetPosition
     * @param blockDrops
     * @return
     */
    protected List<ItemStack> onPreDrops(Level level, Player player, Block targetBlock, Vector3 targetPosition, List<ItemStack> blockDrops) {
        return blockDrops;
    }

    private boolean isFilterSilkTouch(Block targetBlock) {
        switch (targetBlock.getType()) {
            // 告示牌
            case WALL_SIGN:
            case ACACIA_WALL_SIGN:
            case SPRUCE_WALL_SIGN:
            case BIRCH_WALL_SIGN:
            case DARKOAK_WALL_SIGN:
            case JUNGLE_WALL_SIGN:
            case STANDING_SIGN:
            case ACACIA_STANDING_SIGN:
            case SPRUCE_STANDING_SIGN:
            case BIRCH_STANDING_SIGN:
            case DARKOAK_STANDING_SIGN:
            case JUNGLE_STANDING_SIGN:

                // 種子
            case WHEAT:
            case PUMPKIN_STEM:
            case MELON_STEM:
            case POTATOES:
            case CARROTS:
            case BEETROOT:

                // 可可豆
            case COCOA:

            case DOUBLE_PLANT:

                return true;
        }

        return false;
    }
}
