package com.particle.game.block.planting;

import com.google.inject.Inject;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.BlockFace;
import com.particle.model.math.Vector3;

import javax.inject.Singleton;

@Singleton
public class CocoaService {

    @Inject
    private LevelService levelService;

    private int[] checkFaceIndex = new int[]{
            3, 4, 2, 5, 3, 4, 2, 5, 3, 4, 2, 5
    };

    public boolean isCocoaSeed(ItemStack itemStack) {
        return itemStack.getItemType() == ItemPrototype.DYE && itemStack.getMeta() == 3;
    }

    public boolean checkPositionIllegal(Level level, Vector3 targetPosition, Block targetBlock) {
        BlockFace face = BlockFace.fromIndex(checkFaceIndex[targetBlock.getMeta()]);
        Vector3 relyPosition = targetPosition.getSide(face);
        Block relyBlock = this.levelService.getBlockAt(level, relyPosition);

        return relyBlock.getType() == BlockPrototype.LOG || relyBlock.getMeta() == 3;
    }
}
