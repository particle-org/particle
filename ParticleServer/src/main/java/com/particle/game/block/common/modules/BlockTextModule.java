package com.particle.game.block.common.modules;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.component.ECSComponent;
import com.particle.core.ecs.component.ECSComponentHandler;
import com.particle.core.ecs.module.ECSModule;
import com.particle.game.block.common.components.BlockTextComponent;
import com.particle.game.common.components.NBTTagCompoundComponent;
import com.particle.model.nbt.NBTTagCompound;

public class BlockTextModule extends ECSModule {

    private static final ECSComponentHandler<BlockTextComponent> ENTITY_TEXT_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(BlockTextComponent.class);
    private static final ECSComponentHandler<NBTTagCompoundComponent> NBT_TAG_COMPOUND_COMPONENT_ECS_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(NBTTagCompoundComponent.class);

    private BlockTextComponent blockTextComponent;
    private NBTTagCompoundComponent nbtTagCompoundComponent;

    private static final Class[] REQUIRE_CLASSES = new Class[]{BlockTextComponent.class, NBTTagCompoundComponent.class};

    public String getText() {
        return this.blockTextComponent.getText();
    }

    public void setText(String text) {
        this.blockTextComponent.setText(text);

        NBTTagCompound nbtTagCompound = this.nbtTagCompoundComponent.getNbtTagCompound();
        nbtTagCompound.setString("Text", text);
    }


    @Override
    public Class<? extends ECSComponent>[] requestComponents() {
        return REQUIRE_CLASSES;
    }

    @Override
    protected void bindGameObject(GameObject gameObject) {
        this.blockTextComponent = ENTITY_TEXT_COMPONENT_HANDLER.getComponent(gameObject);
        this.nbtTagCompoundComponent = NBT_TAG_COMPOUND_COMPONENT_ECS_COMPONENT_HANDLER.getComponent(gameObject);

        NBTTagCompound nbtTagCompound = this.nbtTagCompoundComponent.getNbtTagCompound();
        nbtTagCompound.setString("Text", this.blockTextComponent.getText());
    }
}
