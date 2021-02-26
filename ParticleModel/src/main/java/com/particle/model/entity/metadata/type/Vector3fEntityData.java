package com.particle.model.entity.metadata.type;

import com.particle.model.math.Vector3f;

public class Vector3fEntityData extends EntityData<Vector3f> {

    private float x;
    private float y;
    private float z;

    public Vector3fEntityData(Vector3f vector3f) {
        this.setData(vector3f);
    }

    @Override
    public EntityMetadataVariableType getType() {
        return EntityMetadataVariableType.VECTORY3F;
    }

    @Override
    public Vector3f getData() {
        return new Vector3f(x, y, z);
    }

    @Override
    public void setData(Vector3f data) {
        this.x = data.getX();
        this.y = data.getY();
        this.z = data.getZ();
    }
}
