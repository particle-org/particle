package com.particle.api.entity;

import com.particle.model.ecs.ECSSystem;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.ECSComponent;

public interface IECSComponentManagerApi {

    int getVersion();

    void filterTickedSystem(Entity entity);

    boolean importECSComponent(Entity entity, String data);

    String exportComponent(ECSComponent ecsComponent);

    void depress(String key);

    void registerCustomSystem(ECSSystem system);
}
