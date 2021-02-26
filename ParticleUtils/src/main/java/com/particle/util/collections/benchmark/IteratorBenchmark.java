package com.particle.util.collections.benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class IteratorBenchmark {

    private Map<Integer, TestObject> hashMap = new HashMap<>();
    private Map<Integer, TestObject> linkedHashMap = new LinkedHashMap<>();
    private Map<Integer, TestObject> concurrentHashMap = new ConcurrentHashMap<>();

    private RandomDataOperation randomDataOperation = new RandomDataOperation(1000);

    @Setup
    public void prepare() {
        this.randomDataOperation.add(hashMap);
        this.randomDataOperation.add(linkedHashMap);
        this.randomDataOperation.add(concurrentHashMap);
    }

    @Benchmark
    public void hashMapIterator() {
        this.randomDataOperation.iterator(hashMap);
    }

    @Benchmark
    public void hashMapForEachIterator() {
        this.randomDataOperation.forEachIterator(hashMap);
    }

    @Benchmark
    public void hashMapLambadIterator() {
        this.randomDataOperation.lambadIterator(hashMap);
    }

    @Benchmark
    public void linkedHashMapIterator() {
        this.randomDataOperation.iterator(linkedHashMap);
    }

    @Benchmark
    public void linkedHashMapForEachIterator() {
        this.randomDataOperation.forEachIterator(linkedHashMap);
    }

    @Benchmark
    public void linkedHashMapLambadIterator() {
        this.randomDataOperation.lambadIterator(linkedHashMap);
    }

    @Benchmark
    public void concurrentHashMapIterator() {
        this.randomDataOperation.iterator(concurrentHashMap);
    }

    @Benchmark
    public void concurrentHashMapForEachIterator() {
        this.randomDataOperation.forEachIterator(concurrentHashMap);
    }

    @Benchmark
    public void concurrentHashMapLambadIterator() {
        this.randomDataOperation.lambadIterator(concurrentHashMap);
    }
}
