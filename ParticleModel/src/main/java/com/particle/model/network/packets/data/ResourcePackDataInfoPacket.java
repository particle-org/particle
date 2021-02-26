package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.resources.ResourcePackType;

public class ResourcePackDataInfoPacket extends DataPacket {

    private String id;

    private int chunkSize;

    private int chunkCounts;

    private long fileSize;

    private byte[] fileHash;

    private boolean isPremium = false;

    private ResourcePackType packType;

    @Override
    public int pid() {
        return ProtocolInfo.RESOURCE_PACK_DATA_INFO_PACKET;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public int getChunkCounts() {
        return chunkCounts;
    }

    public void setChunkCounts(int chunkCounts) {
        this.chunkCounts = chunkCounts;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getFileHash() {
        return fileHash;
    }

    public void setFileHash(byte[] fileHash) {
        this.fileHash = fileHash;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public ResourcePackType getPackType() {
        return packType;
    }

    public void setPackType(ResourcePackType packType) {
        this.packType = packType;
    }
}
