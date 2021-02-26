package com.particle.game.block.interactor.processor.world;

import com.particle.game.block.drops.BlockDropService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.item.ItemStack;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class BlockSlabsWorldProcessor extends BasicBlockProcessor {

    @Inject
    private LevelService levelService;

    @Inject
    private BlockDropService blockDropService;

    @Override
    public boolean handleBlockPlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        Block clickBlock = levelService.getBlockAt(level, clickPosition);

        // 若點擊位置相對上方 或是 方塊下 或是 相同半磚上
        if ((clickOffsetPosition.getY() > 0.5f && clickOffsetPosition.getY() < 1f)
                || (clickOffsetPosition.getY() == 0f && targetPosition.getY() - clickPosition.getY() < 0)
                || (clickBlock.getType() != targetBlock.getType() && clickOffsetPosition.getY() == 0.5f && targetPosition.getY() - clickPosition.getY() < 0)) {
            targetBlock.setMeta(targetBlock.getMeta() ^ 8);
        }

        // 若是同一半磚 且 點擊半磚上
        if (clickBlock.getType() == targetBlock.getType() && clickBlock.getMeta() % 8 == targetBlock.getMeta() % 8
                && clickOffsetPosition.getY() == 0.5f) {
            targetBlock = changeBlock(targetBlock);
            targetPosition = clickPosition;
        }

        return super.handleBlockPlaced(level, player, targetBlock, targetPosition, clickPosition, clickOffsetPosition);
    }

    @Override
    public boolean onBlockPreDestroy(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        // 恢復原 meta 值
        if (targetBlock.getMeta() >> 3 == 1) {
            targetBlock.setMeta(targetBlock.getMeta() ^ 8);
        }

        //方块掉落
        List<ItemStack> drops = this.blockDropService.getBlockDrops(targetBlock);

        //掉落物的处理
        if (drops != null) {
            for (ItemStack itemStack : drops) {
                ItemEntity itemEntity = this.itemEntityService.createEntity(itemStack, targetPosition, new Vector3f((float) Math.random() - 0.5f, 4f, (float) Math.random() - 0.5f));

                this.entitySpawnService.spawn(level, itemEntity);
            }
        }

        return true;
    }

    private Block changeBlock(Block block) {
        Block change = null;
        switch (block.getType()) {
            case WOODEN_SLAB:
                change = Block.getBlock(BlockPrototype.DOUBLE_WOODEN_SLAB);
                change.setMeta(block.getMeta());
                break;

            case STONE_SLAB:
                change = Block.getBlock(BlockPrototype.DOUBLE_STONE_SLAB);
                change.setMeta(block.getMeta());
                break;

            case STONE_SLAB2:
                change = Block.getBlock(BlockPrototype.DOUBLE_STONE_SLAB2);
                change.setMeta(block.getMeta());
                break;
        }

        return change;
    }
}
