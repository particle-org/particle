package com.particle.model.events.level.magic;

import com.particle.model.events.level.LevelEvent;
import com.particle.model.item.ItemStack;
import com.particle.model.level.Level;
import com.particle.model.player.Player;

public class MagicWandUseEvent extends LevelEvent {
    /**
     * 玩家
     */
    private final Player player;

    /**
     * 手上拿的物品
     */
    private final ItemStack itemInHand;

    public MagicWandUseEvent(Level level, Player player, ItemStack itemInHand) {
        super(level);
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
