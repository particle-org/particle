package com.particle.game.block.interactor.processor.world.tool;

import com.particle.game.block.drops.BlockDropService;
import com.particle.game.block.interactor.processor.world.BreakableBlockProcessor;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.model.item.ItemEntity;
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
import java.util.List;

/**
 * 葉子
 */
@Singleton
public class BlockLeavesWorldProcessor extends BreakableBlockProcessor {

    @Inject
    private BlockDropService blockDropService;

    @Override
    protected List<ItemStack> onPreDrops(Level level, Player player, Block targetBlock, Vector3 targetPosition, List<ItemStack> blockDrops) {
        PlayerInventory inventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
        ItemStack hand = this.playerInventoryService.getItem(inventory, inventory.getItemInHandle());
        // 剪刀破坏掉葉子
        if (hand.getItemType() == ItemPrototype.SHEARS) {
            blockDrops.clear();
            blockDrops.add(ItemStack.getItem(targetBlock.getType().getName(), targetBlock.getMeta(), 1));
        }

        return blockDrops;
    }

    @Override
    public boolean onBlockPreDestroy(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        //方块掉落
        if (targetBlock.getType() == BlockPrototype.LEAVES2) {
            targetBlock.setMeta(targetBlock.getMeta() % 4 + 4);
        }
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
}
