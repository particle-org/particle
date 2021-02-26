package com.particle.game.block.common.components;

import com.particle.core.ecs.component.ECSComponent;

public class BlockTextComponent implements ECSComponent {

    private String text = "";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
