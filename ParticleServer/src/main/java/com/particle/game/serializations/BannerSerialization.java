package com.particle.game.serializations;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.block.common.modules.BannerModule;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.nbt.NBTTagList;

import java.util.Iterator;

public class BannerSerialization implements IStringSerialization<BannerModule> {
    @Override
    public String serialization(GameObject gameObject, BannerModule bannerModule) {
        NBTTagList nbtTagList = bannerModule.getPatterns();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < nbtTagList.tagCount(); i++) {
            JSONObject jsonObject = new JSONObject();
            NBTTagCompound compound = nbtTagList.getCompoundTagAt(i);
            jsonObject.put("Pattern", compound.getString("Pattern"));
            jsonObject.put("Color", compound.getInteger("Color"));
            jsonArray.add(jsonObject);
        }

        return bannerModule.getBase() + "|" + jsonArray.toString();
    }

    @Override
    public void deserialization(GameObject gameObject, String data, BannerModule bannerModule) {
        String[] split = data.split("\\|");
        if (split.length == 2) {
            bannerModule.setBase(Integer.valueOf(split[0]));

            JSONArray jsonArray = JSONArray.parseArray(split[1]);
            Iterator<Object> jsonObjectIterator = jsonArray.iterator();
            while (jsonObjectIterator.hasNext()) {
                JSONObject jsonObject = JSONObject.parseObject("" + jsonObjectIterator.next());
                bannerModule.addPatterns(jsonObject.getInteger("Color"), jsonObject.getString("Pattern"));
            }

            bannerModule.pushPatterns();
        }
    }
}
