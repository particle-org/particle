package com.particle.game.player.inventory.holder;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.TransformModule;
import com.particle.model.entity.Entity;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.api.InventoryHolder;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3f;

public class EntityInventoryHolder implements InventoryHolder {

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    private Entity entity;
    private Inventory inventory;
    private TransformModule transformModule;
    private Level level;
    private long runtimeId;


    public EntityInventoryHolder(Entity entity, Inventory inventory) {
        this.entity = entity;
        this.inventory = inventory;
        this.transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
        this.level = entity.getLevel();
        this.runtimeId = entity.getRuntimeId();
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public Vector3f getPosition() {
        return this.transformModule.getPosition();
    }

    @Override
    public Level getLevel() {
        return this.entity.getLevel();
    }

    @Override
    public long getRuntimeId() {
        return this.runtimeId;
    }

    @Override
    public Entity getOwn() {
        return entity;
    }
}
