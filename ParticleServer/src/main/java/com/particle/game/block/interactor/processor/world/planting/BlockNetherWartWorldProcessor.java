package com.particle.game.block.interactor.processor.world.planting;

import com.particle.game.block.interactor.processor.world.BreakableBlockProcessor;
import com.particle.game.block.planting.NetherWartService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class BlockNetherWartWorldProcessor extends BreakableBlockProcessor {

    @Inject
    private LevelService levelService;

    @Inject
    private NetherWartService netherWartService;

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        return this.netherWartService.checkPositionIllegal(level, targetPosition);
    }

    @Override
    protected List<ItemStack> onPreDrops(Level level, Player player, Block targetBlock, Vector3 targetPosition, List<ItemStack> blockDrops) {
        if (targetBlock == null) {
            return blockDrops;
        }

        int meta = targetBlock.getMeta();
        if (meta == 3) {
            int potatoCounts = (int) Math.round(Math.random() * 2) + 1;
            blockDrops.add(ItemStack.getItem(ItemPrototype.NETHER_WART_ITEM, potatoCounts));
        }

        return blockDrops;
    }
}
