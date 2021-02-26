package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class ContainerSetDataPacket extends DataPacket {

    public static final int PROPERTY_FURNACE_TICK_COUNT = 0;
    public static final int PROPERTY_FURNACE_LIT_TIME = 1;
    public static final int PROPERTY_FURNACE_LIT_DURATION = 2;
    public static final int PROPERTY_FURNACE_FUEL_AUX = 4;

    public static final int PROPERTY_BREWING_STAND_BREW_TIME = 0;
    public static final int PROPERTY_BREWING_STAND_FUEL_AMOUNT = 1;
    public static final int PROPERTY_BREWING_STAND_FUEL_TOTAL = 2;

    @Override
    public int pid() {
        return ProtocolInfo.CONTAINER_SET_DATA_PACKET;
    }

    private int containerId;

    private int property;

    private int value;

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public int getProperty() {
        return property;
    }

    public void setProperty(int property) {
        this.property = property;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
