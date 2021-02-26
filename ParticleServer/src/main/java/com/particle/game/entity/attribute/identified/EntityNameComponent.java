package com.particle.game.entity.attribute.identified;

import com.particle.core.ecs.component.ECSComponent;

public class EntityNameComponent implements ECSComponent {

    private String entityName = "";
    private String displayEntityName = "";

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getDisplayEntityName() {
        return displayEntityName;
    }

    public void setDisplayEntityName(String displayEntityName) {
        this.displayEntityName = displayEntityName;
    }

}
