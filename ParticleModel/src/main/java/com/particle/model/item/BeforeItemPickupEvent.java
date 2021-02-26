package com.particle.model.item;

import com.particle.model.entity.Entity;
import com.particle.model.events.level.player.LevelPlayerEvent;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.level.Level;
import com.particle.model.player.Player;

public class BeforeItemPickupEvent extends LevelPlayerEvent {
    // item实体
    private Entity entity;
    // item类型
    private ItemPrototype itemType;

    public BeforeItemPickupEvent(Player player, Level level, Entity entity, ItemPrototype itemType) {
        super(player, level);
        this.entity = entity;
        this.itemType = itemType;
    }

    public Entity getEntity() {
        return entity;
    }

    public ItemPrototype getItemType() {
        return itemType;
    }
}