package com.particle.model.nbt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class NBTToJsonObject {
    private static final Logger log = LoggerFactory.getLogger(NBTToJsonObject.class);

    public static JSONObject convertToJsonObject(NBTBase tag) {
        JSONObject document = new JSONObject();

        document.put("type", tag.getId());
        switch (tag.getId()) {
            case 1:
                document.put("data", (((NBTBase.NBTPrimitive) tag).getByte()));
                break;
            case 6:
                document.put("data", (((NBTBase.NBTPrimitive) tag).getDouble()));
                break;
            case 5:
                document.put("data", (((NBTBase.NBTPrimitive) tag).getFloat()));
                break;
            case 3:
                document.put("data", (((NBTBase.NBTPrimitive) tag).getInt()));
                break;
            case 4:
                document.put("data", (((NBTBase.NBTPrimitive) tag).getLong()));
                break;
            case 2:
                document.put("data", (((NBTBase.NBTPrimitive) tag).getShort()));
                break;
            case 8:
                document.put("data", (((NBTBase) tag).getString()));
                break;
            case 7:
                List<Byte> bytes = new LinkedList<>();
                for (byte b : ((NBTTagByteArray) tag).getByteArray()) {
                    bytes.add(b);
                }
                document.put("data", bytes);
                break;
            case 11:
                List<Integer> integers = new LinkedList<>();
                for (int i : ((NBTTagIntArray) tag).getIntArray()) {
                    integers.add(i);
                }
                document.put("data", integers);
                break;
            case 0:
                document.put("data", "END");
                break;
            case 9:
                List<JSONObject> tags = new LinkedList<>();
                NBTTagList tagList = (NBTTagList) tag;
                for (int i = 0; i < tagList.tagCount(); i++) {
                    if (tagList.get(i) instanceof NBTTagCompound) {
                        tags.add(convertToJsonObject(tagList.getCompoundTagAt(i)));
                    } else if (tagList.get(i) instanceof NBTTagString) {
                        tags.add(convertToJsonObject(tagList.getStringValAt(i)));
                    }
                }

                document.put("data", tags);
                break;
            case 10:
                JSONObject child = new JSONObject();
                NBTTagCompound tagCompound = (NBTTagCompound) tag;

                tagCompound.getKeySet().forEach(key -> {
                    child.put(key, convertToJsonObject(tagCompound.getTag(key)));
                });

                document.put("data", child);
                break;
        }

        return document;
    }

    public static NBTBase convertToTag(JSONObject document) {
        int type = document.getInteger("type");
        String name = document.getString("name");
        switch (type) {
            case 1:
                return new NBTTagByte(document.getInteger("data").byteValue());
            case 2:
                return new NBTTagShort(document.getInteger("data").shortValue());
            case 3:
                return new NBTTagInt(document.getInteger("data"));
            case 4:
                return new NBTTagLong(document.getLong("data"));
            case 5:
                return new NBTTagFloat(document.getDouble("data").floatValue());
            case 6:
                return new NBTTagDouble(document.getDouble("data"));
            case 8:
                return new NBTTagString(document.getString("data"));
            case 7:
                JSONArray byteDataList = document.getJSONArray("data");
                byte[] bytes = new byte[byteDataList.size()];
                for (int i = 0; i < byteDataList.size(); i++) {
                    bytes[i] = byteDataList.getByte(i);
                }
                return new NBTTagByteArray(bytes);
            case 11:
                JSONArray intDataList = document.getJSONArray("data");
                int[] ints = new int[intDataList.size()];
                for (int i = 0; i < intDataList.size(); i++) {
                    ints[i] = intDataList.getInteger(i);
                }
                return new NBTTagIntArray(ints);
            case 9:
                JSONArray tagList = document.getJSONArray("data");
                NBTTagList listTag = new NBTTagList();
                for (Object tagObject : tagList) {
                    listTag.appendTag(convertToTag((JSONObject) tagObject));
                }
                return listTag;
            case 10:
                NBTTagCompound compoundTag = new NBTTagCompound();

                JSONObject mapObjects = (JSONObject) document.get("data");

                mapObjects.forEach((key, value) -> compoundTag.setTag(key, convertToTag((JSONObject) value)));

                return compoundTag;
        }

        return null;
    }
}
