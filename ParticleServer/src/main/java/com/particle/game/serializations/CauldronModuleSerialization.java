package com.particle.game.serializations;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.block.brewing.CauldronModule;
import com.particle.model.item.ItemStack;
import com.particle.model.item.coder.ItemJSONObjectCoder;

public class CauldronModuleSerialization implements IStringSerialization<CauldronModule> {
    @Override
    public String serialization(GameObject gameObject, CauldronModule cauldronModule) {
        JSONObject jsonObject = new JSONObject();

        // 物品
        JSONArray jsonArray = new JSONArray();
        for (ItemStack itemStack : cauldronModule.getItems()) {
            jsonArray.add(ItemJSONObjectCoder.encode(itemStack));
        }

        jsonObject.put("items", jsonArray);

        //存储坐标
        jsonObject.put("potionId", cauldronModule.getPotionId());
        jsonObject.put("customColor", cauldronModule.getCustomColor());
        jsonObject.put("isExistCustomColor", cauldronModule.isExistCustomColor());
        jsonObject.put("isSplashPotion", cauldronModule.isSplashPotion());
        jsonObject.put("isMovable", cauldronModule.isMovable());
        jsonObject.put("arrowPotionCounts", cauldronModule.getArrowPotionCounts());

        return jsonObject.toJSONString();
    }

    @Override
    public void deserialization(GameObject gameObject, String data, CauldronModule cauldronModule) {
        //解析数据
        JSONObject jsonObject = JSON.parseObject(data);
        JSONArray jsonArrays = jsonObject.getJSONArray("items");
        if (jsonArrays != null && !jsonArrays.isEmpty()) {
            for (int i = 0; i < jsonArrays.size(); i++) {
                JSONObject entry = jsonArrays.getJSONObject(i);
                if (entry == null) {
                    continue;
                }
                ItemStack itemStack = ItemJSONObjectCoder.decode(entry);
                if (itemStack != null) {
                    cauldronModule.getItems().add(itemStack);
                }
            }
        }

        cauldronModule.setPotionId(jsonObject.getInteger("potionId"));
        cauldronModule.setCustomColor(jsonObject.getInteger("customColor"));
        cauldronModule.setExistCustomColor(jsonObject.getBoolean("isExistCustomColor"));
        cauldronModule.setSplashPotion(jsonObject.getBoolean("isSplashPotion"));
        cauldronModule.setMovable(jsonObject.getBoolean("isMovable"));
        cauldronModule.setArrowPotionCounts(jsonObject.getInteger("arrowPotionCounts"));
    }
}
