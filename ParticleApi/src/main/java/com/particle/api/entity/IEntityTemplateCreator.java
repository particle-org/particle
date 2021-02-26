package com.particle.api.entity;

import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

public interface IEntityTemplateCreator {

    Entity getEntity(Vector3f position);

}
