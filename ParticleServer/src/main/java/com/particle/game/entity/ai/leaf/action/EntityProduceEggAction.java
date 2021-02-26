package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.ItemEntityService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;

public class EntityProduceEggAction implements IAction {
    @Inject
    private ItemEntityService itemEntityService;

    @Inject
    private PositionService positionService;

    @Inject
    private EntitySpawnService entitySpawnService;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        ItemStack itemStack = ItemStack.getItem(ItemPrototype.EGG, 1);
        ItemEntity itemEntity = this.itemEntityService.createEntity(itemStack, positionService.getPosition(entity), new Vector3f((float) Math.random() - 0.5f, 4f, (float) Math.random() - 0.5f));
        this.entitySpawnService.spawn(entity.getLevel(), itemEntity);

        return EStatus.SUCCESS;
    }

    @Override
    public void onTicked(Entity entity, EStatus status) {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void config(String key, Object val) {

    }
}