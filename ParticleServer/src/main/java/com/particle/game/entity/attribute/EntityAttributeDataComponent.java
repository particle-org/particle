package com.particle.game.entity.attribute;

import com.particle.core.ecs.component.ECSComponent;
import com.particle.model.entity.attribute.EntityAttribute;
import com.particle.model.entity.attribute.EntityAttributeType;

import java.util.HashMap;
import java.util.Map;

public class EntityAttributeDataComponent implements ECSComponent {

    private Map<EntityAttributeType, EntityAttribute> entityAttributeMap = new HashMap<>();

    public Map<EntityAttributeType, EntityAttribute> getEntityAttributeMap() {
        return entityAttributeMap;
    }
}
