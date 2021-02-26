package com.particle.api.entity;

import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

public interface IEntityTemplateServiceApi {
    void registerEntity(String id, IEntityTemplateCreator template);

    Entity createEntityFromTemplate(String id, Vector3f position);
}
