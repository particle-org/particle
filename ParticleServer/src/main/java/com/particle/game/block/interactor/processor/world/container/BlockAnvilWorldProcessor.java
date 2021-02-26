package com.particle.game.block.interactor.processor.world.container;

import com.particle.game.block.interactor.processor.world.BreakableBlockProcessor;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.level.BlockService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.item.ItemStack;
import com.particle.model.level.Level;
import com.particle.model.math.BlockFace;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class BlockAnvilWorldProcessor extends BreakableBlockProcessor {

    @Inject
    private LevelService levelService;

    @Inject
    private PositionService positionService;

    @Inject
    private BlockService blockService;

    private int[] faces = {1, 2, 3, 0};

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        BlockPrototype clickBlock = this.levelService.getBlockTypeAt(level, clickPosition);
        if (clickBlock == null) {
            return false;
        }
        int originMeta = targetBlock.getMeta();
        // 后两位
        originMeta = originMeta & 0x03;
        // 左移动两位
        originMeta <<= 2;
        BlockFace playerDirection = positionService.getFaceDirection(player);
        int meta = 0;
        if (playerDirection != null) {
            int hIndex = playerDirection.getHorizontalIndex();
            meta = this.faces[hIndex > 0 ? hIndex : 0];
        }
        originMeta = meta | originMeta;
        targetBlock.setMeta(originMeta);

        return true;
    }

    @Override
    protected List<ItemStack> onPreDrops(Level level, Player player, Block targetBlock, Vector3 targetPosition, List<ItemStack> blockDrops) {
        for (ItemStack dropItem : blockDrops) {
            int blockMeta = targetBlock.getMeta();
            // 右移动两位
            blockMeta = blockMeta >> 2;
            // 保留后两位
            blockMeta = blockMeta & 0x03;
            dropItem.setMeta(blockMeta);
        }
        return blockDrops;
    }
}
