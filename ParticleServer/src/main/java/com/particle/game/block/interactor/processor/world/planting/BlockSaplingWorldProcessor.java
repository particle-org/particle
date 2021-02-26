package com.particle.game.block.interactor.processor.world.planting;

import com.particle.game.block.interactor.processor.world.BasicBlockProcessor;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockSaplingWorldProcessor extends BasicBlockProcessor {

    @Inject
    private LevelService levelService;

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        int meta = targetBlock.getMeta();
        if (targetBlock.getType() == BlockPrototype.SAPLING) {
            switch (meta) {
                case 6:
                case 7:
                case 8:
                    targetBlock.setMeta(0);
                    break;
                case 9:
                    targetBlock.setMeta(1);
                    break;
                case 10:
                    targetBlock.setMeta(2);
                    break;
                case 11:
                    targetBlock.setMeta(3);
                    break;
                case 12:
                    targetBlock.setMeta(4);
                    break;
                case 13:
                    targetBlock.setMeta(5);
                    break;
            }
        }

        Vector3 down = targetPosition.down(1);
        BlockPrototype downBlock = levelService.getBlockTypeAt(level, down);
        // 樹苗，只能种在泥土, 草方块, 灰化土上
        if (downBlock == null || (downBlock != BlockPrototype.PODZOL
                && downBlock != BlockPrototype.DIRT
                && downBlock != BlockPrototype.GRASS
                && downBlock != BlockPrototype.FARMLAND)) {
            return false;
        }

        return true;
    }
}
