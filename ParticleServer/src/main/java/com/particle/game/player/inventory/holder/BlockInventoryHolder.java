package com.particle.game.player.inventory.holder;

import com.particle.model.entity.Entity;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.api.InventoryHolder;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3f;

public class BlockInventoryHolder implements InventoryHolder {

    private Inventory inventory;
    private Vector3f position;

    public BlockInventoryHolder(Inventory inventory, Vector3f position) {
        this.inventory = inventory;
        this.position = position;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public Vector3f getPosition() {
        return this.position;
    }

    @Override
    public Level getLevel() {
        return null;
    }

    @Override
    public long getRuntimeId() {
        return -1;
    }

    @Override
    public Entity getOwn() {
        return null;
    }
}
