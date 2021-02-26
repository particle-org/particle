package com.particle.game.block.common.modules;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.component.ECSComponent;
import com.particle.core.ecs.component.ECSComponentHandler;
import com.particle.core.ecs.module.ECSModule;
import com.particle.game.block.brewing.BrewingFuelComponent;
import com.particle.game.block.common.components.CookComponent;
import com.particle.game.common.components.NBTTagCompoundComponent;
import com.particle.model.nbt.NBTTagCompound;

public class CookModule extends ECSModule {

    private static final ECSComponentHandler<CookComponent> BREWING_COMPONENT_ECS_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(CookComponent.class);
    private static final ECSComponentHandler<NBTTagCompoundComponent> NBT_TAG_COMPOUND_COMPONENT_ECS_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(NBTTagCompoundComponent.class);

    private CookComponent cookComponent;
    private NBTTagCompoundComponent nbtTagCompoundComponent;

    public int getCookTime() {
        return this.cookComponent.getCookTime();
    }

    public void setCookTime(int cookTime) {
        this.cookComponent.setCookTime(cookTime);

        NBTTagCompound nbtTagCompound = this.nbtTagCompoundComponent.getNbtTagCompound();
        nbtTagCompound.setShort("CookTime", (short) this.cookComponent.getCookTime());
    }

    public int getStatus() {
        return this.cookComponent.getStatus();
    }

    public void setStatus(int status) {
        if (status != CookComponent.STATUS_RUNNING) {
            this.cookComponent.setStatus(CookComponent.MAX_BREWING_TIME);
        }

        this.cookComponent.setStatus(status);
    }


    @Override
    public Class<? extends ECSComponent>[] requestComponents() {
        return new Class[]{BrewingFuelComponent.class, CookComponent.class, NBTTagCompoundComponent.class};
    }

    @Override
    protected void bindGameObject(GameObject gameObject) {
        this.cookComponent = BREWING_COMPONENT_ECS_COMPONENT_HANDLER.getComponent(gameObject);
        this.nbtTagCompoundComponent = NBT_TAG_COMPOUND_COMPONENT_ECS_COMPONENT_HANDLER.getComponent(gameObject);

        NBTTagCompound nbtTagCompound = this.nbtTagCompoundComponent.getNbtTagCompound();
        nbtTagCompound.setShort("CookTime", (short) this.cookComponent.getCookTime());
    }
}
