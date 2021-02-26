package com.particle.model.inventory.data;

import com.particle.model.inventory.common.InventorySourceFlags;
import com.particle.model.inventory.common.InventorySourceType;

public class InventorySourceData {

    private InventorySourceType sourceType;

    private int containerId;

    private InventorySourceFlags bitFlags;

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public InventorySourceFlags getBitFlags() {
        return bitFlags;
    }

    public void setBitFlags(InventorySourceFlags bitFlags) {
        this.bitFlags = bitFlags;
    }

    public InventorySourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(InventorySourceType sourceType) {
        this.sourceType = sourceType;
    }

    @Override
    public String toString() {
        return "InventorySourceData{" +
                "sourceType=" + sourceType +
                ", containerId=" + containerId +
                ", bitFlags=" + bitFlags +
                '}';
    }
}
