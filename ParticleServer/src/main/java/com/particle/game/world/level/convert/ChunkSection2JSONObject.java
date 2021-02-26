package com.particle.game.world.level.convert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.particle.model.level.chunk.ChunkSection;

public class ChunkSection2JSONObject {

    /**
     * 将ChunkSection转换成适用于存储的JSONObject
     *
     * @param section
     * @return
     */
    public static JSONObject toJsonObject(ChunkSection section) {
        JSONObject nbtTagCompound = new JSONObject();
        nbtTagCompound.put("Y", section.getY());

        // 填充Block信息
        short[] blocks = section.getBlocks();
        byte[] savedBlocks = new byte[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            savedBlocks[i] = (byte) blocks[i];
        }
        nbtTagCompound.put("Blocks", savedBlocks);

        // 填充Meta信息
        byte[] meta = section.getData();
        byte[] savedMeta = new byte[meta.length >> 1];
        for (int i = 0; i < savedMeta.length; i++) {
            savedMeta[i] = (byte) ((meta[(i << 1) + 1] << 4) | meta[i << 1]);
        }
        nbtTagCompound.put("Data", savedMeta);

        // 填充BlockLight信息
        byte[] blockLight = section.getBlockLight();
        byte[] savedBlockLight = new byte[blockLight.length >> 1];
        for (int i = 0; i < savedBlockLight.length; i++) {
            savedBlockLight[i] = (byte) ((blockLight[(i << 1) + 1] << 4) | blockLight[i << 1]);
        }
        nbtTagCompound.put("BlockLight", savedBlockLight);

        // 填充SkyLight信息
        byte[] skyLight = section.getBlockLight();
        byte[] savedSkyLight = new byte[skyLight.length >> 1];
        for (int i = 0; i < savedSkyLight.length; i++) {
            savedSkyLight[i] = (byte) ((skyLight[(i << 1) + 1] << 4) | skyLight[i << 1]);
        }
        nbtTagCompound.put("SkyLight", savedSkyLight);

        return nbtTagCompound;
    }


    public static ChunkSection fromJsonObject(JSONObject data) {
        ChunkSection chunkSection = new ChunkSection(data.getByte("Y"));

        // 解析Block信息
        JSONArray savedBlocks = data.getJSONArray("Blocks");
        short[] blocks = new short[savedBlocks.size()];
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = (short) (savedBlocks.getByteValue(i) & 0xff);
        }
        chunkSection.setBlocks(blocks);

        // 填充Meta信息
        JSONArray savedMeta = data.getJSONArray("Data");
        byte[] meta = new byte[savedMeta.size() << 1];
        for (int i = 0; i < meta.length; i++) {
            byte temp = savedMeta.getByteValue(i);
            meta[(i << 1) + 1] = (byte) ((temp >>> 4) & 0xf);
            meta[i << 1] = (byte) (temp & 0xf);
        }
        chunkSection.setData(meta);

        // 填充BlockLight信息
        JSONArray savedBlockLight = data.getJSONArray("BlockLight");
        byte[] blockLight = new byte[savedBlockLight.size() << 1];
        for (int i = 0; i < blockLight.length; i++) {
            byte temp = savedBlockLight.getByteValue(i);
            blockLight[(i << 1) + 1] = (byte) ((temp >>> 4) & 0xf);
            blockLight[i << 1] = (byte) (temp & 0xf);
        }
        chunkSection.setBlockLight(blockLight);

        // 填充SkyLight信息
        JSONArray savedSkyLight = data.getJSONArray("SkyLight");
        byte[] skyLight = new byte[savedSkyLight.size() << 1];
        for (int i = 0; i < skyLight.length; i++) {
            byte temp = savedSkyLight.getByteValue(i);
            skyLight[(i << 1) + 1] = (byte) ((temp >>> 4) & 0xf);
            skyLight[i << 1] = (byte) (temp & 0xf);
        }
        chunkSection.setSkyLight(skyLight);

        return chunkSection;
    }

}
