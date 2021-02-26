package com.particle.model.entity.link;

public class EntityLink {

    private long entityUniqueIdA;

    private long entityUniqueIdB;

    private EntityLinkType linkType;

    private byte unknownByte;

    private boolean immediate;

    public long getEntityUniqueIdA() {
        return entityUniqueIdA;
    }

    public void setEntityUniqueIdA(long entityUniqueIdA) {
        this.entityUniqueIdA = entityUniqueIdA;
    }

    public long getEntityUniqueIdB() {
        return entityUniqueIdB;
    }

    public void setEntityUniqueIdB(long entityUniqueIdB) {
        this.entityUniqueIdB = entityUniqueIdB;
    }

    public EntityLinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(EntityLinkType linkType) {
        this.linkType = linkType;
    }

    public boolean isImmediate() {
        return immediate;
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }

    public byte getUnknownByte() {
        return unknownByte;
    }

    public void setUnknownByte(byte unknownByte) {
        this.unknownByte = unknownByte;
    }
}
