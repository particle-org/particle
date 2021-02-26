package com.particle.model.events.level.player;

import com.particle.model.block.Block;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

public class PlayerInteractiveBlockEvent extends LevelPlayerEvent {
    private Block block;

    private Vector3f clickPosition;

    private Vector3 blockPosition;

    public PlayerInteractiveBlockEvent(Player player, Level level) {
        super(player, level);
    }


    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Vector3f getClickPosition() {
        return clickPosition;
    }

    public void setClickPosition(Vector3f clickPosition) {
        this.clickPosition = clickPosition;
    }

    public Vector3 getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Vector3 blockPosition) {
        this.blockPosition = blockPosition;
    }
}
