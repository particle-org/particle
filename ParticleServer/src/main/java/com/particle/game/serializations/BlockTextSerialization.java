package com.particle.game.serializations;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.block.common.modules.BlockTextModule;

public class BlockTextSerialization implements IStringSerialization<BlockTextModule> {
    @Override
    public String serialization(GameObject gameObject, BlockTextModule blockTextModule) {
        return blockTextModule.getText();
    }

    @Override
    public void deserialization(GameObject gameObject, String data, BlockTextModule blockTextModule) {
        blockTextModule.setText(data);
    }
}
