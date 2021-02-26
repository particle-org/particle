package com.particle.model.events.level.block;

import com.particle.model.item.ItemStack;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

public class BlockPlaceEvent extends LevelBlockEvent {

    /**
     * 点击的位置
     */
    private Vector3 clickPosition;

    /**
     * 玩家
     */
    private final Player player;

    /**
     * 手上拿的物品
     */
    private ItemStack itemInHand;

    public BlockPlaceEvent(Level level, Player player, ItemStack itemInHand) {
        super(level);
        this.player = player;
        this.itemInHand = itemInHand;
    }

    public Vector3 getClickPosition() {
        return clickPosition;
    }

    public void setClickPosition(Vector3 clickPosition) {
        this.clickPosition = clickPosition;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItemInHand() {
        return itemInHand;
    }

    public void setItemInHand(ItemStack itemInHand) {
        this.itemInHand = itemInHand;
    }
}
