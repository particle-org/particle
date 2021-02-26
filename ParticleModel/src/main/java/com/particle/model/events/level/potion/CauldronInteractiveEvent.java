package com.particle.model.events.level.potion;

import com.particle.model.block.Block;
import com.particle.model.events.level.LevelEvent;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

public class CauldronInteractiveEvent extends LevelEvent {

    private Player player;

    private Block block;

    private Vector3 position;

    public CauldronInteractiveEvent(Level level) {
        super(level);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }
}
