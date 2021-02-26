package com.particle.game.block.common.components;

import com.particle.core.ecs.component.ECSComponent;

public class SkullComponent implements ECSComponent {

    private byte rot = 0;
    private byte skullType = 0;

    public byte getRot() {
        return rot;
    }

    public void setRot(byte rot) {
        this.rot = rot;
    }

    public byte getSkullType() {
        return skullType;
    }

    public void setSkullType(byte skullType) {
        this.skullType = skullType;
    }
}
