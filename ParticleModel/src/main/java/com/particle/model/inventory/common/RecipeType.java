package com.particle.model.inventory.common;

public interface RecipeType {

    /**
     * 无形状的合成配方
     */
    public static final int ShapelessRecipe = 0;

    /**
     * 有形状的合成配方
     */
    public static final int ShapedRecipe = 1;

    /**
     * 熔炉配方
     */
    public static final int FurnaceRecipe = 2;

    /**
     * 带属性的熔炉配方
     */
    public static final int FurnaceAuxRecipe = 3;

    /**
     * 附魔配方
     */
    public static final int MultiRecipe = 4;

    /**
     * 潜影盒
     */
    public static final int ShulkerBoxRecipe = 5;

    /**
     * 无形的化学食谱
     */
    public static final int ShapelessChemistryRecipe = 6;

    /**
     * 有形的化学食谱
     */
    public static final int ShapedChemistryRecipe = 7;
}
