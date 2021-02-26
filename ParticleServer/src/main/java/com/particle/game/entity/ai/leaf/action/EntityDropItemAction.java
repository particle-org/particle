package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.item.ItemDropService;
import com.particle.model.entity.Entity;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class EntityDropItemAction implements IAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityDropItemAction.class);

    private ItemPrototype itemPrototype = ItemPrototype.STONE;
    private int meta = 0;
    private int count = 1;

    @Inject
    private ItemDropService itemDropService;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        if (this.itemDropService.entityDropItem(entity, ItemStack.getItem(itemPrototype, meta, count))) {
            return EStatus.SUCCESS;
        } else {
            return EStatus.FAILURE;
        }
    }

    @Override
    public void onTicked(Entity entity, EStatus status) {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void config(String key, Object val) {
        if (key.equalsIgnoreCase("ItemPrototype") && val instanceof String) {
            ItemStack item = ItemStack.getItem((String) val);
            if (item == null) {
                LOGGER.error("Weapon config fail, {} not exist!", (String) val);
            } else {
                this.itemPrototype = item.getItemType();
            }
        } else if (key.equalsIgnoreCase("Meta") && val instanceof Integer) {
            this.meta = (Integer) val;
        } else if (key.equalsIgnoreCase("Count") && val instanceof Integer) {
            this.count = (Integer) val;
        }
    }

}
