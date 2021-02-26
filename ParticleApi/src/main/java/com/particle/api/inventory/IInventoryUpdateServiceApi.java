package com.particle.api.inventory;

import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

public interface IInventoryUpdateServiceApi {
    void updateSingleContainerComponentPosition(Entity entity, Vector3f position);
}
