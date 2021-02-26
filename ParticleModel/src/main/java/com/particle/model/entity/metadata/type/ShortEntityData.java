package com.particle.model.entity.metadata.type;

public class ShortEntityData extends EntityData<Short> {
    public short data;

    public ShortEntityData(short data) {
        this.data = data;
    }

    public Short getData() {
        return data;
    }

    public void setData(Short data) {
        if (data == null) {
            this.data = 0;
        } else {
            this.data = data;
        }
    }

    @Override
    public EntityMetadataVariableType getType() {
        return EntityMetadataVariableType.SHORT;
    }
}
