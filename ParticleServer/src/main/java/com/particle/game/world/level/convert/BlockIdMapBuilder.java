package com.particle.game.world.level.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockIdMapBuilder {

    private Map<Integer, Integer> runtimeIdMap = new HashMap<>();
    private List<Integer> runtimeIdList = new ArrayList<>();
    private int nextIndexId = 0;

    public BlockIdMapBuilder() {
        getIndexId(0);
    }

    public int getIndexId(int runtimeId) {
        int indexId = this.runtimeIdMap.getOrDefault(runtimeId, -1);
        if (indexId == -1) {
            indexId = nextIndexId++;
            this.runtimeIdMap.put(runtimeId, indexId);
            this.runtimeIdList.add(runtimeId);
        }
        return indexId;
    }

    public int getSize() {
        return runtimeIdMap.size();
    }

    public List<Integer> getRuntimeIds() {
        return runtimeIdList;
    }

}
