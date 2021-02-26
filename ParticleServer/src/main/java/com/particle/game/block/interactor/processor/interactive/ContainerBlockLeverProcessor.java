package com.particle.game.block.interactor.processor.interactive;

import com.particle.api.block.IBlockInteractedProcessor;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.events.level.block.BlockLeverEvent;
import com.particle.model.math.BlockLeverFace;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ContainerBlockLeverProcessor implements IBlockInteractedProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ContainerBlockLeverProcessor.class);

    @Inject
    private LevelService levelService;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Override
    public boolean interactive(Player player, Block targetBlock, Vector3 targetPosition) {
        // 更新方块
        targetBlock.setMeta(targetBlock.getMeta() ^ 8);
        BlockLeverFace blockLeverFace = BlockLeverFace.getBlockLeverFace(targetBlock.getMeta() % 8);

        // 發送事件
        BlockLeverEvent blockLeverEvent = new BlockLeverEvent(player.getLevel());
        blockLeverEvent.setPlayer(player);
        blockLeverEvent.setBlockLeverFace(blockLeverFace);
        blockLeverEvent.setPosition(targetPosition);
        if ((targetBlock.getMeta() & 8) == 0) {
            blockLeverEvent.setLeverSwitch(false);
        } else {
            blockLeverEvent.setLeverSwitch(true);
        }

        blockLeverEvent.overrideOnEventCancelled(() -> {
            // 還原方块
            targetBlock.setMeta(targetBlock.getMeta() ^ 8);
            this.levelService.setBlockAt(player.getLevel(), targetBlock, targetPosition);
        });

        this.eventDispatcher.dispatchEvent(blockLeverEvent);

        this.levelService.setBlockAt(player.getLevel(), targetBlock, targetPosition);
        return true;
    }
}
