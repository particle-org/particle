package com.particle.game.world.level.convert;

import com.particle.model.level.chunk.ChunkSection;
import com.particle.model.nbt.NBTTagCompound;

public class ChunkSection2NBTTagCompound {

    /**
     * 将ChunkSection转换成适用于存储的NBT
     *
     * @param section
     * @return
     */
    public static NBTTagCompound toNBT(ChunkSection section) {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setByte("Y", section.getY());

        // 填充Block信息
        short[] blocks = section.getBlocks();
        byte[] savedBlocks = new byte[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            savedBlocks[i] = (byte) blocks[i];
        }
        nbtTagCompound.setByteArray("Blocks", savedBlocks);

        // 填充Meta信息
        byte[] meta = section.getData();
        byte[] savedMeta = new byte[meta.length >> 1];
        for (int i = 0; i < savedMeta.length; i++) {
            savedMeta[i] = (byte) ((meta[(i << 1) + 1] << 4) | meta[i << 1]);
        }
        nbtTagCompound.setByteArray("Data", savedMeta);

        // 填充BlockLight信息
        byte[] blockLight = section.getBlockLight();
        byte[] savedBlockLight = new byte[blockLight.length >> 1];
        for (int i = 0; i < savedBlockLight.length; i++) {
            savedBlockLight[i] = (byte) ((blockLight[(i << 1) + 1] << 4) | blockLight[i << 1]);
        }
        nbtTagCompound.setByteArray("BlockLight", savedBlockLight);

        // 填充SkyLight信息
        byte[] skyLight = section.getBlockLight();
        byte[] savedSkyLight = new byte[skyLight.length >> 1];
        for (int i = 0; i < savedSkyLight.length; i++) {
            savedSkyLight[i] = (byte) ((skyLight[(i << 1) + 1] << 4) | skyLight[i << 1]);
        }
        nbtTagCompound.setByteArray("SkyLight", savedSkyLight);

        return nbtTagCompound;
    }


    public static ChunkSection fromNBT(NBTTagCompound data) {
        ChunkSection chunkSection = new ChunkSection(data.getByte("Y"));

        // 解析Block信息
        byte[] savedBlocks = data.getByteArray("Blocks");
        short[] blocks = new short[savedBlocks.length];
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = (short) (savedBlocks[i] & 0xff);
        }
        chunkSection.setBlocks(blocks);

        // 填充Meta信息
        byte[] savedMeta = data.getByteArray("Data");
        byte[] meta = new byte[savedMeta.length << 1];
        for (int i = 0; i < savedMeta.length; i++) {
            byte temp = savedMeta[i];
            meta[(i << 1) + 1] = (byte) ((temp >>> 4) & 0xf);
            meta[i << 1] = (byte) (temp & 0xf);
        }
        chunkSection.setData(meta);

        // 填充BlockLight信息
        byte[] savedBlockLight = data.getByteArray("BlockLight");
        byte[] blockLight = new byte[savedBlockLight.length << 1];
        for (int i = 0; i < savedBlockLight.length; i++) {
            byte temp = savedBlockLight[i];
            blockLight[(i << 1) + 1] = (byte) ((temp >>> 4) & 0xf);
            blockLight[i << 1] = (byte) (temp & 0xf);
        }
        chunkSection.setBlockLight(blockLight);

        // 填充SkyLight信息
        byte[] savedSkyLight = data.getByteArray("SkyLight");
        byte[] skyLight = new byte[savedSkyLight.length << 1];
        for (int i = 0; i < savedSkyLight.length; i++) {
            byte temp = savedSkyLight[i];
            skyLight[(i << 1) + 1] = (byte) ((temp >>> 4) & 0xf);
            skyLight[i << 1] = (byte) (temp & 0xf);
        }
        chunkSection.setSkyLight(skyLight);

        return chunkSection;
    }

}
