package com.particle.game.block.interactor.processor.world.planting;

import com.particle.game.block.interactor.processor.world.BreakableBlockProcessor;
import com.particle.game.block.planting.CocoaService;
import com.particle.model.block.Block;
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
public class BlockCocoaPlaced extends BreakableBlockProcessor {

    @Inject
    private CocoaService cocoaService;

    @Inject
    private Random random;

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        return this.cocoaService.checkPositionIllegal(level, targetPosition, targetBlock);
    }

    @Override
    protected List<ItemStack> onPreDrops(Level level, Player player, Block targetBlock, Vector3 targetPosition, List<ItemStack> blockDrops) {
        List<ItemStack> itemStackList = new ArrayList<>();
        if (targetBlock.getMeta() < 8) {
            itemStackList.add(ItemStack.getItem(ItemPrototype.DYE, 3, 1));
        } else {
            int amount = random.nextInt(1) + 2;
            itemStackList.add(ItemStack.getItem(ItemPrototype.DYE, 3, amount));
        }

        return itemStackList;
    }
}
