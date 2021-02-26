package com.particle.model.entity.metadata.type;

public class FloatEntityData extends EntityData<Float> {
    public float data;

    public FloatEntityData(float data) {
        this.data = data;
    }

    public Float getData() {
        return data;
    }

    public void setData(Float data) {
        if (data == null) {
            this.data = 0;
        } else {
            this.data = data;
        }

    }

    @Override
    public EntityMetadataVariableType getType() {
        return EntityMetadataVariableType.FLOAT;
    }
}
