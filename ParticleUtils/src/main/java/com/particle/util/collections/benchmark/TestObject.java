package com.particle.util.collections.benchmark;

public class TestObject {
    private int id;
    private int[] data = new int[10240];

    public TestObject(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int[] getData() {
        return data;
    }
}
