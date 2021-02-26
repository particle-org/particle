package com.particle.model.resources;

import java.util.Map;

public class ResourcePackInfo {

    private String id;

    private String version;

    private long size;

    private String contentKey;

    private String subPackName;

    private String contentIdentity = "";

    private String filePath;

    private int maxChunkIndex;

    private Map<Integer, byte[]> chunkData;

    private byte[] fileHash;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getContentKey() {
        return contentKey;
    }

    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
    }

    public String getSubPackName() {
        return subPackName;
    }

    public void setSubPackName(String subPackName) {
        this.subPackName = subPackName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getMaxChunkIndex() {
        return maxChunkIndex;
    }

    public void setMaxChunkIndex(int maxChunkIndex) {
        this.maxChunkIndex = maxChunkIndex;
    }

    public Map<Integer, byte[]> getChunkData() {
        return chunkData;
    }

    public void setChunkData(Map<Integer, byte[]> chunkData) {
        this.chunkData = chunkData;
    }

    public byte[] getFileHash() {
        return fileHash;
    }

    public void setFileHash(byte[] fileHash) {
        this.fileHash = fileHash;
    }

    public String getContentIdentity() {
        return contentIdentity;
    }

    public void setContentIdentity(String contentIdentity) {
        this.contentIdentity = contentIdentity;
    }
}
