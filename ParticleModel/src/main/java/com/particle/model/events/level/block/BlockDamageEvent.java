package com.particle.model.events.level.block;


import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;

public class BlockDamageEvent extends LevelBlockEvent {

    private final Player player;

    private final ItemStack itemInHand;

    public BlockDamageEvent(Player player, ItemStack itemInHand) {
        super(player.getLevel());
        this.player = player;
        this.itemInHand = itemInHand;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItemInHand() {
        return itemInHand;
    }
}
