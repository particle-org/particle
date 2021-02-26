package com.particle.model.entity.metadata.type;

public class ByteEntityData extends EntityData<Byte> {
    protected byte data;

    public ByteEntityData(byte data) {
        this.data = data;
    }

    public Byte getData() {
        return data;
    }

    public void setData(Byte data) {
        if (data == null) {
            this.data = 0;
        } else {
            this.data = data;
        }
    }

    @Override
    public EntityMetadataVariableType getType() {
        return EntityMetadataVariableType.BYTE;
    }
}
