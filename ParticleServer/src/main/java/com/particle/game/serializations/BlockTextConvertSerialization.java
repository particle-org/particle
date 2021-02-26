package com.particle.game.serializations;

import com.alibaba.fastjson.JSONObject;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.block.common.modules.BlockTextModule;

public class BlockTextConvertSerialization implements IStringSerialization<BlockTextModule> {
    @Override
    public String serialization(GameObject gameObject, BlockTextModule blockTextModule) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Text", blockTextModule.getText());

        return jsonObject.toJSONString();
    }

    @Override
    public void deserialization(GameObject gameObject, String data, BlockTextModule blockTextModule) {
        JSONObject jsonObject = JSONObject.parseObject(data);

        blockTextModule.setText(jsonObject.getString("Text"));
    }
}
