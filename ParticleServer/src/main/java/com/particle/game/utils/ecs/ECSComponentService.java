package com.particle.game.utils.ecs;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.particle.api.entity.ECSComponentServiceAPI;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.ECSComponent;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ECSComponentService implements ECSComponentServiceAPI {

    @Inject
    private ECSComponentManager ecsComponentManager;

    private static Map<Integer, Integer> componentClassMap = new HashMap<>();

    @Override
    public boolean hasComponent(Entity entity, int id) {
        return entity.hasComponent(id);
    }

    @Override
    public void setComponents(Entity entity, ECSComponent[] ecsComponents) {
        for (ECSComponent ecsComponent : ecsComponents) {
            entity.setComponent(ecsComponent);
        }

        //配置system
        this.ecsComponentManager.filterTickedSystem(entity);
    }

    @Override
    public void setComponent(Entity entity, ECSComponent ecsComponent) {
        entity.setComponent(ecsComponent);

        //配置system
        this.ecsComponentManager.filterTickedSystem(entity);
    }

    /**
     * 去除component
     *
     * @param entity
     * @param id
     */
    @Override
    public void removeComponent(Entity entity, int id) {
        entity.resetComponent(id);
        //配置system
        ecsComponentManager.filterTickedSystem(entity);
    }

    public ECSComponent getComponent(Entity entity, int id) {
        return entity.getComponent(id);
    }

    /**
     * 获取Component
     *
     * @param entity
     * @param id
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends ECSComponent> T getComponent(Entity entity, int id, Class<T> clazz) {
        ECSComponent component = entity.getComponent(id);

        if (component == null) {
            return null;
        }

        try {
            return (T) component;
        } catch (Exception e) {
            throw new RuntimeException("Component not matched!", e);
        }
    }

    public <T extends ECSComponent> T getComponent(Entity entity, Class<T> clazz) {
        Integer componentId = componentClassMap.get(clazz.hashCode());

        if (componentId == null) {
            Field idField;
            try {
                idField = clazz.getField("ID");
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Class structure error");
            }

            try {
                componentId = idField.getInt(clazz);

                componentClassMap.put(clazz.hashCode(), componentId);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Class structure error");
            }
        }

        return this.getComponent(entity, componentId, clazz);
    }

}
