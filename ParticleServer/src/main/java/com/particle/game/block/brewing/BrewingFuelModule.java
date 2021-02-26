package com.particle.game.block.brewing;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.component.ECSComponent;
import com.particle.core.ecs.component.ECSComponentHandler;
import com.particle.core.ecs.module.ECSModule;
import com.particle.game.block.common.components.CookComponent;
import com.particle.game.common.components.NBTTagCompoundComponent;
import com.particle.model.nbt.NBTTagCompound;

public class BrewingFuelModule extends ECSModule {

    private static final ECSComponentHandler<BrewingFuelComponent> BREWING_FUEL_COMPONENT_ECS_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(BrewingFuelComponent.class);
    private static final ECSComponentHandler<NBTTagCompoundComponent> NBT_TAG_COMPOUND_COMPONENT_ECS_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(NBTTagCompoundComponent.class);

    private BrewingFuelComponent brewingFuelComponent;
    private NBTTagCompoundComponent nbtTagCompoundComponent;

    public int getFuelTotal() {
        return this.brewingFuelComponent.getFuelTotal();
    }

    public int getFuelAmount() {
        return this.brewingFuelComponent.getFuelAmount();
    }

    public void setFuelAmount(int fuelAmount) {
        this.brewingFuelComponent.setFuelAmount(fuelAmount);

        NBTTagCompound nbtTagCompound = this.nbtTagCompoundComponent.getNbtTagCompound();
        nbtTagCompound.setShort("FuelAmount", (short) fuelAmount);
    }

    @Override
    public Class<? extends ECSComponent>[] requestComponents() {
        return new Class[]{BrewingFuelComponent.class, CookComponent.class, NBTTagCompoundComponent.class};
    }

    @Override
    protected void bindGameObject(GameObject gameObject) {
        this.brewingFuelComponent = BREWING_FUEL_COMPONENT_ECS_COMPONENT_HANDLER.getComponent(gameObject);
        this.nbtTagCompoundComponent = NBT_TAG_COMPOUND_COMPONENT_ECS_COMPONENT_HANDLER.getComponent(gameObject);

        NBTTagCompound nbtTagCompound = this.nbtTagCompoundComponent.getNbtTagCompound();
        nbtTagCompound.setShort("FuelTotal", (short) this.brewingFuelComponent.getFuelTotal());
        nbtTagCompound.setShort("FuelAmount", (short) this.brewingFuelComponent.getFuelAmount());
    }
}
