package com.particle.model.events.level.player;

import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;

import java.util.List;

public class PlayerDropItemEvent extends LevelPlayerEvent {

    private List<ItemStack> itemStacks;

    public PlayerDropItemEvent(Player player, List<ItemStack> itemStacks) {
        super(player);
        this.itemStacks = itemStacks;
    }

    public List<ItemStack> getItemStacks() {
        return itemStacks;
    }

    public void setItemStacks(List<ItemStack> itemStacks) {
        this.itemStacks = itemStacks;
    }
}
