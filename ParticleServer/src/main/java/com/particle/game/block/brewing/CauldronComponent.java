package com.particle.game.block.brewing;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CauldronComponent extends BehaviorModule {

    public static final int DEFAULT_VALUE = 0xFFFFFF;

    public static final int DEFAULT_ARROW_COUNTS = 16;

    /**
     * 药水Id，如为0xFFFFFF，表示没有装药水
     */
    private int potionId = DEFAULT_VALUE;

    /**
     * 当存储了染了色的水的时候，存储的颜色的值
     */
    private int customColor = DEFAULT_VALUE;

    /**
     * 是否水被染色
     */
    private boolean isExistCustomColor = false;

    /**
     * 是否是喷溅药水
     */
    private boolean isSplashPotion = false;

    /**
     * 是否能被活塞推动
     */
    private boolean isMovable = true;

    /**
     * 弓箭的次数
     */
    private int arrowPotionCounts = DEFAULT_ARROW_COUNTS;

    /**
     * 当前容器内的物品列表，wiki上说，该标签的用途未被确定
     */
    private List<ItemStack> items = new ArrayList<>();

    public int getPotionId() {
        return potionId;
    }

    public void setPotionId(int potionId) {
        this.potionId = potionId;
    }

    public boolean isSplashPotion() {
        return isSplashPotion;
    }

    public void setSplashPotion(boolean splashPotion) {
        isSplashPotion = splashPotion;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public void setMovable(boolean movable) {
        isMovable = movable;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void addItem(ItemStack itemStack) {
        this.items.add(itemStack);
    }

    public void removeItem(ItemStack itemStack) {
        this.items.remove(itemStack);
    }

    public int getCustomColor() {
        return customColor;
    }

    public void setCustomColor(int customColor) {
        this.customColor = customColor;
    }

    public boolean isExistCustomColor() {
        return isExistCustomColor;
    }

    public void setExistCustomColor(boolean existCustomColor) {
        isExistCustomColor = existCustomColor;
    }

    public int getArrowPotionCounts() {
        return arrowPotionCounts;
    }

    public void setArrowPotionCounts(int arrowPotionCounts) {
        this.arrowPotionCounts = arrowPotionCounts;
    }

    public void reset() {
        potionId = 0xFFFFFF;
        customColor = 0xFFFFFF;
        isExistCustomColor = false;
        isSplashPotion = false;
        isMovable = true;
        arrowPotionCounts = DEFAULT_ARROW_COUNTS;
    }


}
