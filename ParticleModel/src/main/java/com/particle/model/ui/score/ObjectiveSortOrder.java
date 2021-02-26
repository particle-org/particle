package com.particle.model.ui.score;

import java.util.HashMap;
import java.util.Map;

public enum ObjectiveSortOrder {
    Ascending(0),
    Descending(1);

    private static final Map<Integer, ObjectiveSortOrder> orders = new HashMap<>();

    static {
        for (ObjectiveSortOrder item : ObjectiveSortOrder.values()) {
            orders.put(item.getOrder(), item);
        }
    }

    private int order;

    ObjectiveSortOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public static ObjectiveSortOrder from(int order) {
        return orders.get(order);
    }
}
