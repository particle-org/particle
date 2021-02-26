package com.particle.model.inventory;

import com.particle.model.inventory.common.ContainerType;

public class BrewingInventory extends Inventory {

    /**
     * 成分的index
     */
    public static final int IngredientIndex = 0;

    /**
     * 燃料的index
     */
    public static final int FuelIndex = 4;

    /**
     * 药水槽1
     */
    public static final int POTION1 = 1;

    /**
     * 药水槽2
     */
    public static final int POTION2 = 2;

    /**
     * 药水槽3
     */
    public static final int POTION3 = 3;

    public BrewingInventory() {
        this.setContainerType(ContainerType.BREWING);
        this.setSize(ContainerType.BREWING.getDefaultSize());
    }
}
