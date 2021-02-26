package com.particle.game.block.common.modules;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.component.ECSComponent;
import com.particle.core.ecs.component.ECSComponentHandler;
import com.particle.core.ecs.module.ECSModule;
import com.particle.game.block.common.components.SkullComponent;
import com.particle.game.common.components.NBTTagCompoundComponent;
import com.particle.model.nbt.NBTTagCompound;

public class SkullModule extends ECSModule {

    private static final ECSComponentHandler<SkullComponent> SKULL_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(SkullComponent.class);
    private static final ECSComponentHandler<NBTTagCompoundComponent> NBT_TAG_COMPOUND_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(NBTTagCompoundComponent.class);

    private SkullComponent skullComponent;
    private NBTTagCompoundComponent nbtTagCompoundComponent;

    public byte getRot() {
        return this.skullComponent.getRot();
    }

    public void setRot(byte rot) {
        this.skullComponent.setRot(rot);
        this.nbtTagCompoundComponent.getNbtTagCompound().setByte("Rot", this.skullComponent.getRot());
    }

    public byte getSkullType() {
        return this.skullComponent.getSkullType();
    }

    public void setSkullType(byte skullType) {
        this.skullComponent.setSkullType(skullType);
        this.nbtTagCompoundComponent.getNbtTagCompound().setByte("SkullType", this.skullComponent.getSkullType());
    }

    @Override
    public Class<? extends ECSComponent>[] requestComponents() {
        return new Class[]{SkullComponent.class, NBTTagCompoundComponent.class};
    }

    @Override
    protected void bindGameObject(GameObject gameObject) {
        this.skullComponent = SKULL_COMPONENT_HANDLER.getComponent(gameObject);
        this.nbtTagCompoundComponent = NBT_TAG_COMPOUND_COMPONENT_HANDLER.getComponent(gameObject);

        NBTTagCompound nbtTagCompound = this.nbtTagCompoundComponent.getNbtTagCompound();
        nbtTagCompound.setByte("Rot", this.skullComponent.getRot());
        nbtTagCompound.setByte("SkullType", this.skullComponent.getSkullType());
    }
}
