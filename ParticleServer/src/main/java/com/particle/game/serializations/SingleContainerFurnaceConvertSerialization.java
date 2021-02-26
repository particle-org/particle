package com.particle.game.serializations;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.player.inventory.holder.EntityInventoryHolder;
import com.particle.game.player.inventory.modules.SingleContainerModule;
import com.particle.model.entity.Entity;
import com.particle.model.inventory.FurnaceInventory;
import com.particle.model.inventory.Inventory;
import com.particle.model.item.ItemStack;
import com.particle.model.item.coder.ItemJSONObjectCoder;

import java.util.Map;

public class SingleContainerFurnaceConvertSerialization implements IStringSerialization<SingleContainerModule> {
    @Override
    public String serialization(GameObject gameObject, SingleContainerModule singleContainerModule) {
        JSONObject jsonObject = new JSONObject();

        //存储物品
        Map<Integer, ItemStack> allSlots = singleContainerModule.getInventory().getAllSlots();
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<Integer, ItemStack> entry : allSlots.entrySet()) {
            if (entry == null) {
                continue;
            }
            Integer slot = entry.getKey();
            ItemStack itemStack = entry.getValue();

            JSONObject object = ItemJSONObjectCoder.encode(itemStack);
            object.put("slot", slot);
            jsonArray.add(object);
        }
        jsonObject.put("data", jsonArray);
        jsonObject.put("type", "Furnace");

        return jsonObject.toJSONString();
    }

    @Override
    public void deserialization(GameObject gameObject, String data, SingleContainerModule singleContainerModule) {
        //解析数据
        JSONObject jsonObject = JSON.parseObject(data);

        //构造背包
        Inventory inventory = new FurnaceInventory();
        JSONArray allSlots = jsonObject.getJSONArray("data");
        if (allSlots != null && !allSlots.isEmpty()) {
            for (int i = 0; i < allSlots.size(); i++) {
                JSONObject entry = allSlots.getJSONObject(i);
                if (entry == null) {
                    continue;
                }
                Integer slot = entry.getInteger("slot");

                ItemStack itemStack = ItemJSONObjectCoder.decode(entry);

                if (itemStack != null)
                    inventory.putSlot(slot, itemStack);
            }
        }

        if (gameObject instanceof Entity) {
            inventory.setInventoryHolder(new EntityInventoryHolder((Entity) gameObject, inventory));
        }

        singleContainerModule.setInventory(inventory);

    }
}
