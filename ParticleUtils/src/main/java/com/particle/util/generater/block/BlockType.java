package com.particle.util.generater.block;


public class BlockType {


    private int id;
    private String name;
    private int startRuntimeId;
    private int maxMetadata;

    public BlockType(int id, String name, int startRuntimeId, int maxMetadata) {
        this.id = id;
        this.name = name;
        this.startRuntimeId = startRuntimeId;
        this.maxMetadata = maxMetadata;
    }

    /**
     * 方块ID.
     *
     * @return 获取方块ID
     */
    public int getId() {
        return id;
    }

    /**
     * 方块名称.
     *
     * @return 获取方块名称
     */
    public String getName() {
        return name;
    }

    public int getStartRuntimeId() {
        return startRuntimeId;
    }

    public int getMaxMetadata() {
        return maxMetadata;
    }
}
