package com.particle.game.serializations;

import com.alibaba.fastjson.JSON;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.item.ItemBindModule;
import com.particle.model.item.coder.ItemJSONObjectCoder;

public class ItemBindSerialization implements IStringSerialization<ItemBindModule> {
    @Override
    public String serialization(GameObject gameObject, ItemBindModule itemBindModule) {
        return ItemJSONObjectCoder.encode(itemBindModule.getItem()).toJSONString();
    }

    @Override
    public void deserialization(GameObject gameObject, String data, ItemBindModule itemBindModule) {
        itemBindModule.setItem(ItemJSONObjectCoder.decode(JSON.parseObject(data)));
    }
}
