package com.particle.util.placeholder;

public interface CompileInterface {

    /**
     * 传入key和playerName，根据实现逻辑返回这个key对应的值
     */
    String compile(String key, String playerName);
}
