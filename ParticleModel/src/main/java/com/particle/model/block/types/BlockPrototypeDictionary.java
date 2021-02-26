package com.particle.model.block.types;

import java.util.HashMap;
import java.util.Map;

public class BlockPrototypeDictionary {

    // block index偏移，处理block id为负数的情况
    private static final int INDEX_OFFSET = 512;
    // 字典大小
    private static final int DICTIONARY_SIZE = 2048;
    // 字典校验掩码
    private static final int DICTIONARY_SIZE_MASK = 2047;

    private static Map<String, BlockPrototype> dictionaryName = new HashMap<>();
    private static BlockPrototype[] dictionaryId = new BlockPrototype[DICTIONARY_SIZE];

    static {
        for (BlockPrototype blockPrototype : BlockPrototype.values()) {
            dictionaryName.put(blockPrototype.getName(), blockPrototype);

            // 考虑id为负数的方块，整体位移512
            int index = blockPrototype.getId() + INDEX_OFFSET;
            // 如果发生数据覆盖情况，则报异常
            if (dictionaryId[index] != null) {
                throw new RuntimeException("Id error!");
            }

            dictionaryId[index] = blockPrototype;
        }
    }

    public static BlockPrototype map(String name) {
        BlockPrototype blockPrototype = dictionaryName.get(name);

        return blockPrototype == null ? BlockPrototype.AIR : blockPrototype;
    }

    public static BlockPrototype map(int id) {
        int offSeteId = id + INDEX_OFFSET;
        if ((offSeteId & DICTIONARY_SIZE_MASK) == offSeteId) {
            BlockPrototype blockPrototype = dictionaryId[id + INDEX_OFFSET];

            return blockPrototype == null ? BlockPrototype.AIR : blockPrototype;
        }

        return BlockPrototype.AIR;
    }
}
