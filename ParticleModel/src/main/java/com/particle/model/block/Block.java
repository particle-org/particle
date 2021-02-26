package com.particle.model.block;

import com.particle.model.block.types.BlockPrototype;
import com.particle.model.block.types.BlockPrototypeDictionary;

import java.util.List;

public class Block {

    private BlockPrototype type;
    private int meta;

    public static Block getBlock(BlockPrototype blockPrototype) {
        return new Block(blockPrototype);
    }

    public static Block getBlock(int id) {
        BlockPrototype blockPrototype = BlockPrototypeDictionary.map(id);

        if (blockPrototype != null)
            return new Block(blockPrototype);
        else
            return null;
    }

    public static BlockPrototype getBlockType(int id) {
        return BlockPrototypeDictionary.map(id);
    }

    public static Block getBlock(String name) {
        BlockPrototype blockPrototype = BlockPrototypeDictionary.map(name);

        if (blockPrototype != null)
            return new Block(blockPrototype);
        else
            return null;
    }

    public static BlockPrototype getBlockType(String name) {
        return BlockPrototypeDictionary.map(name);
    }

    private Block(BlockPrototype type) {
        this.type = type;
    }

    public BlockPrototype getType() {
        return type;
    }

    public int getMeta() {
        return meta;
    }

    public void setMeta(int meta) {
        if (meta >= this.type.getMaxMetadata()) {
            // 当设置超过最大值时，默认给0
            this.meta = 0;
        } else {
            this.meta = meta;
        }

    }

    /**
     * 获取runtimeId
     *
     * @return
     */
    public int getRuntimeId() {
        return type.getStartRuntimeId() + meta;
    }

    /**
     * 根據版本获取runtimeId
     *
     * @return
     */
    /**
     * 根據版本获取runtimeId
     *
     * @return
     */
    public int getRuntimeId(int version) {
        int missCount = 0;
        List<Short> missList = type.getMissMetaListMap().get(version);
        if (missList != null) {
            for (short missMeta : missList) {
                if (meta > missMeta) {
                    missCount++;
                }
            }
        }

        return type.getStartRuntimeIdMap().get(version) + meta - missCount;
    }
}
