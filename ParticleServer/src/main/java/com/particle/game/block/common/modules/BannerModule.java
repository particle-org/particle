package com.particle.game.block.common.modules;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.component.ECSComponent;
import com.particle.core.ecs.component.ECSComponentHandler;
import com.particle.core.ecs.module.ECSModule;
import com.particle.game.block.common.components.BannerComponent;
import com.particle.game.common.components.NBTTagCompoundComponent;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.nbt.NBTTagList;

public class BannerModule extends ECSModule {

    private static final ECSComponentHandler<BannerComponent> BANNER_COMPONENT_ECS_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(BannerComponent.class);
    private static final ECSComponentHandler<NBTTagCompoundComponent> NBT_TAG_COMPOUND_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(NBTTagCompoundComponent.class);

    private BannerComponent bannerComponent;
    private NBTTagCompoundComponent nbtTagCompoundComponent;

    public int getBase() {
        return this.bannerComponent.getBase();
    }

    public NBTTagList getPatterns() {
        return this.bannerComponent.getPatterns();
    }

    public void setBase(int base) {
        this.bannerComponent.setBase(base);
        this.nbtTagCompoundComponent.getNbtTagCompound().setInteger("Base", this.bannerComponent.getBase());
    }

    public void addPatterns(int color, String pattern) {
        NBTTagList patternsTagList = bannerComponent.getPatterns();
        NBTTagCompound patternsTag = new NBTTagCompound();
        patternsTag.setString("Pattern", pattern);
        patternsTag.setInteger("Color", color);
        patternsTagList.appendTag(patternsTag);
    }

    public void pushPatterns() {
        this.nbtTagCompoundComponent.getNbtTagCompound().setTag("Patterns", bannerComponent.getPatterns());
    }

    public void setPatterns(NBTTagCompound nbtTagCompound) {
        NBTTagList patternsTagList = new NBTTagList();

        if (nbtTagCompound != null) {
            NBTTagList nbtTagList = nbtTagCompound.getTagList("Patterns", 10);
            for (int i = 0; i < nbtTagList.tagCount(); i++) {
                NBTTagCompound compound = nbtTagList.getCompoundTagAt(i);

                NBTTagCompound patternsTag = new NBTTagCompound();
                patternsTag.setString("Pattern", compound.getString("Pattern"));
                patternsTag.setInteger("Color", compound.getInteger("Color"));
                patternsTagList.appendTag(patternsTag);
            }
        }

        this.bannerComponent.setPatterns(patternsTagList);
        this.nbtTagCompoundComponent.getNbtTagCompound().setTag("Patterns", patternsTagList);
    }

    @Override
    public Class<? extends ECSComponent>[] requestComponents() {
        return new Class[]{BannerComponent.class, NBTTagCompoundComponent.class};
    }

    @Override
    protected void bindGameObject(GameObject gameObject) {
        this.bannerComponent = BANNER_COMPONENT_ECS_COMPONENT_HANDLER.getComponent(gameObject);
        this.nbtTagCompoundComponent = NBT_TAG_COMPOUND_COMPONENT_HANDLER.getComponent(gameObject);

        NBTTagCompound nbtTagCompound = this.nbtTagCompoundComponent.getNbtTagCompound();

        nbtTagCompound.setInteger("Base", this.bannerComponent.getBase());

        NBTTagList patternsTagList = new NBTTagList();
        NBTTagCompound patternsTag = new NBTTagCompound();
        patternsTag.setString("Pattern", "");
        patternsTag.setInteger("Color", 0);
        patternsTagList.appendTag(patternsTag);

        nbtTagCompound.setTag("Patterns", patternsTagList);
    }
}
