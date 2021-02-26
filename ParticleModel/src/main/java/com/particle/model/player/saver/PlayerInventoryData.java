package com.particle.model.player.saver;

import com.alibaba.fastjson.JSONObject;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class PlayerInventoryData {

    /**
     * 类型
     */
    private String type;

    /**
     * 箱子类型
     */
    private int sortOrder;

    /**
     * 背包里所有的槽集合
     */
    private Set<JSONObject> allSlotInfos = new CopyOnWriteArraySet<>();

    /**
     * 玩家背包中手握的index
     */
    private int itemInHandle;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getItemInHandle() {

        return itemInHandle;
    }

    public void setItemInHandle(int itemInHandle) {
        this.itemInHandle = itemInHandle;
    }

    public Set<JSONObject> getAllSlotInfos() {
        return allSlotInfos;
    }

    public void setAllSlotInfos(Set<JSONObject> allSlotInfos) {
        this.allSlotInfos = allSlotInfos;
    }
}
