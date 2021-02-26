package com.particle.model.events.level.player;

import com.particle.model.item.ItemStack;
import com.particle.model.math.BlockFace;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

public class PlayerInteractEvent extends LevelPlayerEvent {

    public PlayerInteractEvent(Player player, ItemStack itemInHand, Vector3 targetPosition,
                               BlockFace blockFace, Action action) {
        super(player);
        this.itemInHand = itemInHand;
        this.targetPosition = targetPosition;
        this.blockFace = blockFace;
        this.action = action;
    }

    public PlayerInteractEvent(Player player, ItemStack itemInHand, Vector3 targetPosition,
                               BlockFace blockFace) {
        super(player);
        this.itemInHand = itemInHand;
        this.targetPosition = targetPosition;
        this.blockFace = blockFace;
    }

    private ItemStack itemInHand;

    private Vector3 targetPosition;

    private BlockFace blockFace;

    private Action action;

    public ItemStack getItemInHand() {
        return itemInHand;
    }

    public Vector3 getTargetPosition() {
        return targetPosition;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    public Action getAction() {
        return action;
    }

    public enum Action {
        LEFT_CLICK_BLOCK,
        RIGHT_CLICK_BLOCK,
        LEFT_CLICK_AIR,
        RIGHT_CLICK_AIR,
        PHYSICAL
    }
}
