package com.particle.model.entity.metadata.type;

public class StringEntityData extends EntityData<String> {
    public String data;

    public StringEntityData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public EntityMetadataVariableType getType() {
        return EntityMetadataVariableType.STRING;
    }

    @Override
    public String toString() {
        return data;
    }
}
