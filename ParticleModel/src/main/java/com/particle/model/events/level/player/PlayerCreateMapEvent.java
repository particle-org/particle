package com.particle.model.events.level.player;

import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;

public class PlayerCreateMapEvent extends LevelPlayerEvent {

    private ItemStack itemMap;
    private ItemStack itemSrc;

    public PlayerCreateMapEvent(Player player, ItemStack itemMap, ItemStack itemSrc) {
        super(player);
        this.itemMap = itemMap;
        this.itemSrc = itemSrc;
    }

    public ItemStack getItemMap() {
        return itemMap;
    }

    public ItemStack getItemSrc() {
        return itemSrc;
    }
}
