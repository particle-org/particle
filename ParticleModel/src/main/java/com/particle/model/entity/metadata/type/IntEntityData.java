package com.particle.model.entity.metadata.type;

public class IntEntityData extends EntityData<Integer> {
    public int data;

    public IntEntityData(int data) {
        this.data = data;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        if (data == null) {
            this.data = 0;
        } else {
            this.data = data;
        }
    }

    @Override
    public EntityMetadataVariableType getType() {
        return EntityMetadataVariableType.INT;
    }
}
