package com.particle.model.item.coder;

import com.alibaba.fastjson.JSONObject;
import com.particle.model.item.ItemStack;
import com.particle.model.nbt.NBTBase;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.nbt.NBTToJsonObject;

public class ItemJSONObjectCoder {
    public static JSONObject encode(ItemStack itemStack) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", itemStack.getItemType().getName());
        jsonObject.put("count", itemStack.getCount());
        jsonObject.put("meta", itemStack.getMeta());

        if (itemStack.isExistedNbt()) {
            jsonObject.put("nbt", NBTToJsonObject.convertToJsonObject(itemStack.getNbt()));
        }

        return jsonObject;
    }

    public static ItemStack decode(JSONObject jsonObject) {
        ItemStack itemStack = ItemStack.getItem(jsonObject.getString("name"));

        if (itemStack == null)
            return null;

        itemStack.setCount(jsonObject.getInteger("count"));
        itemStack.setMeta(jsonObject.getInteger("meta"));

        if (jsonObject.containsKey("nbt")) {
            NBTBase nbt = NBTToJsonObject.convertToTag(jsonObject.getJSONObject("nbt"));
            if (nbt instanceof NBTTagCompound) {
                itemStack.updateNBT((NBTTagCompound) nbt);
            }
        }

        return itemStack;
    }
}
