package com.particle.model.events.level.player;

import com.particle.model.entity.Entity;
import com.particle.model.item.ItemStack;
import com.particle.model.level.Level;
import com.particle.model.player.Player;

public class PlayerInteractiveEntityEvent extends LevelPlayerEvent {
    private Entity entity;
    private ItemStack itemOnHand;

    public PlayerInteractiveEntityEvent(Player player, Level level, ItemStack itemOnHand) {
        super(player, level);
        this.itemOnHand = itemOnHand;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public ItemStack getItemOnHand() {
        return itemOnHand;
    }
}
