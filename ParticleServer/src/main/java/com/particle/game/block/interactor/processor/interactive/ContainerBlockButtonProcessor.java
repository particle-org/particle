package com.particle.game.block.interactor.processor.interactive;

import com.particle.api.block.IBlockInteractedProcessor;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ContainerBlockButtonProcessor implements IBlockInteractedProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ContainerBlockButtonProcessor.class);

    @Inject
    private LevelService levelService;

    @Override
    public boolean interactive(Player player, Block targetBlock, Vector3 targetPosition) {
        // 更新方块
        this.levelService.setBlockAt(player.getLevel(), targetBlock, targetPosition);
        return true;
    }
}
