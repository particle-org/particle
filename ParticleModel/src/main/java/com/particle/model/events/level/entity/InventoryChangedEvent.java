package com.particle.model.events.level.entity;

import com.particle.model.entity.Entity;
import com.particle.model.events.level.LevelEvent;
import com.particle.model.item.ItemStack;

public class InventoryChangedEvent extends LevelEvent {

    private Entity entity;
    private int slot;
    private ItemStack from;
    private ItemStack to;
    private String operation;

    public InventoryChangedEvent(Entity entity) {
        super(entity.getLevel());

        this.entity = entity;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public ItemStack getFrom() {
        return from;
    }

    public void setFrom(ItemStack from) {
        this.from = from;
    }

    public ItemStack getTo() {
        return to;
    }

    public void setTo(ItemStack to) {
        this.to = to;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Entity getEntity() {
        return entity;
    }
}
