package com.particle.model.events.level.player;

import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;

public class PlayerOpenMapEvent extends LevelPlayerEvent {

    private ItemStack itemMap;
    private int itemIndex;
    private boolean isRightHand;

    public PlayerOpenMapEvent(Player player, ItemStack itemMap, int itemIndex, boolean isRightHand) {
        super(player);
        this.itemMap = itemMap;
        this.itemIndex = itemIndex;
        this.isRightHand = isRightHand;
    }

    public ItemStack getItemMap() {
        return itemMap;
    }

    public boolean isRightHand() {
        return isRightHand;
    }

    public int getItemIndex() {
        return itemIndex;
    }
}
