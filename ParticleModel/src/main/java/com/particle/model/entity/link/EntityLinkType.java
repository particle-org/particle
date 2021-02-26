package com.particle.model.entity.link;

import java.util.HashMap;
import java.util.Map;

public enum EntityLinkType {
    NONE(0, "none"),
    RIDING(1, "riding"),
    PASSENGER(2, "passenger");


    private static final Map<Integer, EntityLinkType> dict = new HashMap<>();

    static {
        for (EntityLinkType item : EntityLinkType.values()) {
            dict.put(item.getId(), item);
        }
    }

    public static EntityLinkType valueOf(int key) {
        return dict.get(key);
    }

    EntityLinkType(int id, String toast) {
        this.id = id;
        this.toast = toast;
    }

    private int id;

    private String toast;

    public int getId() {
        return id;
    }

    public String getToast() {
        return toast;
    }
}
