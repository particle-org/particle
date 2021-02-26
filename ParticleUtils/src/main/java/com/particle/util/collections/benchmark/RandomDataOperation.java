package com.particle.util.collections.benchmark;

import java.util.Iterator;
import java.util.Map;

public class RandomDataOperation {

    private TestObject[] dataSet;

    public RandomDataOperation(int dataSetSize) {
        this.dataSet = new TestObject[dataSetSize];

        for (int i = 0; i < dataSetSize; i++) {
            int id = (int) (Math.random() * Integer.MAX_VALUE);
            this.dataSet[i] = new TestObject(id);
        }
    }

    public void add(Map<Integer, TestObject> map) {
        for (TestObject object : this.dataSet) {
            map.put(object.getId(), object);
        }
    }

    public void delete(Map<Integer, TestObject> map) {
        for (TestObject object : this.dataSet) {
            map.remove(object.getId());
        }
    }

    public void get(Map<Integer, TestObject> map) {
        for (TestObject object : this.dataSet) {
            map.get(object.getId());
        }
    }

    public void iterator(Map<Integer, TestObject> map) {
        Iterator<Map.Entry<Integer, TestObject>> iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            iterator.next();
        }
    }

    public void forEachIterator(Map<Integer, TestObject> map) {
        for (Map.Entry<Integer, TestObject> integerIntegerEntry : map.entrySet()) {
        }
    }

    public void lambadIterator(Map<Integer, TestObject> map) {
        map.forEach((key, val) -> {
        });
    }
}
