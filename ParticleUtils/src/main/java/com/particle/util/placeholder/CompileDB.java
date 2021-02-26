package com.particle.util.placeholder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CompileDB {

    private static final Map<String, CompileInterface> DB = new ConcurrentHashMap<>();

    public static void register(String namespace, CompileInterface compileInterface) {
        DB.put(namespace, compileInterface);
    }

    public static CompileInterface get(String namespace) {
        return DB.get(namespace);
    }
}
