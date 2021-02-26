package com.particle.game.entity.attribute.metadata;

import com.particle.core.ecs.component.ECSComponent;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.type.EntityData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityMetaDataComponent implements ECSComponent {

    private Map<EntityMetadataType, EntityData> entityMetaData = new ConcurrentHashMap<>();

    public Map<EntityMetadataType, EntityData> getEntityMetaData() {
        return entityMetaData;
    }

    public void putEntityMetaData(EntityMetadataType type, EntityData data) {
        this.entityMetaData.put(type, data);
    }

}
