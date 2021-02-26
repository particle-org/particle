package com.particle.model.entity.metadata.type;

public class LongEntityData extends EntityData<Long> {
    public long data;

    public LongEntityData(long data) {
        this.data = data;
    }

    public Long getData() {
        return data;
    }

    public void setData(Long data) {
        this.data = data;
    }

    @Override
    public EntityMetadataVariableType getType() {
        return EntityMetadataVariableType.LONG;
    }
}
