package com.particle.game.entity.attribute.identified;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.component.ECSComponent;
import com.particle.core.ecs.component.ECSComponentHandler;
import com.particle.core.ecs.module.ECSModule;
import com.particle.game.entity.attribute.metadata.EntityMetaDataComponent;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.type.StringEntityData;

public class EntityNameModule extends ECSModule {

    private static final ECSComponentHandler<EntityMetaDataComponent> ENTITY_META_DATA_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(EntityMetaDataComponent.class);
    private static final ECSComponentHandler<EntityNameComponent> ENTITY_NAME_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(EntityNameComponent.class);

    private EntityMetaDataComponent entityMetaDataComponent;
    private EntityNameComponent entityNameComponent;

    /**
     * 查询生物名
     *
     * @return 生物名称
     */
    public String getEntityName() {
        return entityNameComponent.getEntityName();
    }

    /**
     * 设置生物名称
     *
     * @param entityName 生物名称
     */
    public void setEntityName(String entityName) {
        entityNameComponent.setEntityName(entityName);

        setDisplayEntityName(entityName);
    }

    /**
     * 查询生物显示名称
     *
     * @return 生物显示名称
     */
    public String getDisplayEntityName() {
        return entityNameComponent.getDisplayEntityName();
    }

    /**
     * 设置生物显示名称
     */
    public void setDisplayEntityName(String displayEntityName) {
        entityNameComponent.setDisplayEntityName(displayEntityName);

        entityMetaDataComponent.putEntityMetaData(EntityMetadataType.NAMETAG, new StringEntityData(this.getDisplayEntityName()));
    }

    @Override
    public Class<? extends ECSComponent>[] requestComponents() {
        return new Class[]{EntityMetaDataComponent.class, EntityNameComponent.class};
    }

    @Override
    protected void bindGameObject(GameObject gameObject) {
        this.entityMetaDataComponent = ENTITY_META_DATA_COMPONENT_HANDLER.getComponent(gameObject);
        this.entityNameComponent = ENTITY_NAME_COMPONENT_HANDLER.getComponent(gameObject);
    }
}
