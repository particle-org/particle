package com.particle.game.block.brewing;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.component.ECSComponent;
import com.particle.core.ecs.component.ECSComponentHandler;
import com.particle.core.ecs.module.ECSModule;
import com.particle.game.common.components.NBTTagCompoundComponent;
import com.particle.model.item.ItemStack;
import com.particle.model.nbt.NBTTagCompound;

import java.util.List;

public class CauldronModule extends ECSModule {

    private static final ECSComponentHandler<CauldronComponent> CAULDRON_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(CauldronComponent.class);
    private static final ECSComponentHandler<NBTTagCompoundComponent> NBT_TAG_COMPOUND_COMPONENT_ECS_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(NBTTagCompoundComponent.class);

    private CauldronComponent cauldronComponent;
    private NBTTagCompoundComponent nbtTagCompoundComponent;

    private static final Class[] REQUIRE_CLASSES = new Class[]{CauldronComponent.class, NBTTagCompoundComponent.class};

    public int getPotionId() {
        return this.cauldronComponent.getPotionId();
    }

    public void setPotionId(int potionId) {
        this.cauldronComponent.setPotionId(potionId);

        NBTTagCompound nbtTagCompound = this.nbtTagCompoundComponent.getNbtTagCompound();
        nbtTagCompound.setShort("PotionId", (short) potionId);
    }

    public boolean isSplashPotion() {
        return this.cauldronComponent.isSplashPotion();
    }

    public void setSplashPotion(boolean splashPotion) {
        this.cauldronComponent.setSplashPotion(splashPotion);

        NBTTagCompound nbtTagCompound = this.nbtTagCompoundComponent.getNbtTagCompound();
        nbtTagCompound.setBoolean("SplashPotion", splashPotion);
    }

    public boolean isMovable() {
        return this.cauldronComponent.isMovable();
    }

    public void setMovable(boolean movable) {
        this.cauldronComponent.setMovable(movable);

        NBTTagCompound nbtTagCompound = this.nbtTagCompoundComponent.getNbtTagCompound();
        nbtTagCompound.setBoolean("isMovable", movable);
    }

    public List<ItemStack> getItems() {
        return this.cauldronComponent.getItems();
    }

    public void addItem(ItemStack itemStack) {
        this.cauldronComponent.getItems().add(itemStack);
    }

    public void removeItem(ItemStack itemStack) {
        this.cauldronComponent.getItems().remove(itemStack);
    }

    public int getCustomColor() {
        return this.cauldronComponent.getCustomColor();
    }

    public void setCustomColor(int customColor) {
        this.cauldronComponent.setCustomColor(customColor);

        NBTTagCompound nbtTagCompound = this.nbtTagCompoundComponent.getNbtTagCompound();
        nbtTagCompound.setShort("CustomColor", (short) customColor);
    }

    public boolean isExistCustomColor() {
        return this.cauldronComponent.isExistCustomColor();
    }

    public void setExistCustomColor(boolean existCustomColor) {
        this.cauldronComponent.setExistCustomColor(existCustomColor);
    }

    public int getArrowPotionCounts() {
        return this.cauldronComponent.getArrowPotionCounts();
    }

    public void setArrowPotionCounts(int arrowPotionCounts) {
        this.cauldronComponent.setArrowPotionCounts(arrowPotionCounts);
    }

    public void reset() {
        this.cauldronComponent.setPotionId(0xFFFFFF);
        this.cauldronComponent.setCustomColor(0xFFFFFF);
        this.cauldronComponent.setExistCustomColor(false);
        this.cauldronComponent.setSplashPotion(false);
        this.cauldronComponent.setMovable(true);
        this.cauldronComponent.setArrowPotionCounts(CauldronComponent.DEFAULT_ARROW_COUNTS);

        NBTTagCompound nbtTagCompound = this.nbtTagCompoundComponent.getNbtTagCompound();
        nbtTagCompound.removeTag("CustomColor");
        nbtTagCompound.setShort("PotionId", (short) 0xFFFFFF);
        nbtTagCompound.setBoolean("SplashPotion", false);
        nbtTagCompound.setBoolean("isMovable", true);
    }

    @Override
    public Class<? extends ECSComponent>[] requestComponents() {
        return REQUIRE_CLASSES;
    }

    @Override
    protected void bindGameObject(GameObject gameObject) {
        this.cauldronComponent = CAULDRON_COMPONENT_HANDLER.getComponent(gameObject);
        this.nbtTagCompoundComponent = NBT_TAG_COMPOUND_COMPONENT_ECS_COMPONENT_HANDLER.getComponent(gameObject);

        NBTTagCompound nbtTagCompound = this.nbtTagCompoundComponent.getNbtTagCompound();
        if (this.cauldronComponent.isExistCustomColor()) {
            nbtTagCompound.setShort("CustomColor", (short) this.cauldronComponent.getCustomColor());
        }
        nbtTagCompound.setShort("PotionId", (short) this.cauldronComponent.getPotionId());
        nbtTagCompound.setBoolean("SplashPotion", this.cauldronComponent.isSplashPotion());
        nbtTagCompound.setBoolean("isMovable", this.cauldronComponent.isMovable());
    }
}
