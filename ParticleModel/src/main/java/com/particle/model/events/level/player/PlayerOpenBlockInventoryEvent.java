package com.particle.model.events.level.player;

import com.particle.model.block.Block;
import com.particle.model.inventory.Inventory;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

public class PlayerOpenBlockInventoryEvent extends LevelPlayerEvent {
    public PlayerOpenBlockInventoryEvent(Player player, Level level) {
        super(player, level);
    }

    private Vector3 blockPosition;

    private Block block;

    private Inventory inventory;

    public Vector3 getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Vector3 blockPosition) {
        this.blockPosition = blockPosition;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
