package com.particle.game.entity.movement.module;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.component.ECSComponent;
import com.particle.core.ecs.component.ECSComponentHandler;
import com.particle.core.ecs.module.ECSModule;
import com.particle.game.entity.attribute.EntityAttributeDataComponent;
import com.particle.game.entity.movement.component.EntityMotionComponent;
import com.particle.game.entity.movement.component.EntitySpeedComponent;
import com.particle.model.entity.attribute.EntityAttribute;
import com.particle.model.entity.attribute.EntityAttributeType;
import com.particle.model.math.Vector3f;

import java.util.Map;

public class EntityMovementModule extends ECSModule {

    private static final ECSComponentHandler<EntityAttributeDataComponent> ENTITY_ATTRIBUTE_DATA_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(EntityAttributeDataComponent.class);
    private static final ECSComponentHandler<EntitySpeedComponent> ENTITY_MOVEMENT_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(EntitySpeedComponent.class);
    private static final ECSComponentHandler<EntityMotionComponent> ENTITY_MOTION_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(EntityMotionComponent.class);

    private static final Class[] REQUIRE_CLASSES = new Class[]{EntityAttributeDataComponent.class, EntitySpeedComponent.class, EntityMotionComponent.class};

    private EntityAttributeDataComponent entityAttributeDataComponent;
    private EntitySpeedComponent entitySpeedComponent;
    private EntityMotionComponent entityMotionComponent;

    private Vector3f lastMotion = new Vector3f(0, 0, 0);

    public float getSpeed() {
        return this.entitySpeedComponent.getSpeed();
    }

    public void setSpeed(float speed) {
        this.entitySpeedComponent.setSpeed(speed);

        this.refreshEntityAttribute();
    }

    public float getMaxSpeed() {
        return this.entitySpeedComponent.getMaxSpeed();
    }

    public void setMaxSpeed(float maxSpeed) {
        this.entitySpeedComponent.setMaxSpeed(maxSpeed);

        this.refreshEntityAttribute();
    }

    public boolean isRunning() {
        return this.entitySpeedComponent.isRunning();
    }

    public void setRunning(boolean state) {
        this.entitySpeedComponent.setRunning(state);
    }

    public Vector3f getMotion() {
        return new Vector3f(this.entityMotionComponent.getMotionX(), this.entityMotionComponent.getMotionY(), this.entityMotionComponent.getMotionZ());
    }

    public void setMotion(Vector3f vector3f) {
        this.entityMotionComponent.setMotionX(vector3f.getX());
        this.entityMotionComponent.setMotionY(vector3f.getY());
        this.entityMotionComponent.setMotionZ(vector3f.getZ());
    }

    public void setMotionX(float motionX) {
        this.entityMotionComponent.setMotionX(motionX);
    }

    public void setMotionY(float motionY) {
        this.entityMotionComponent.setMotionY(motionY);
    }

    public void setMotionZ(float motionZ) {
        this.entityMotionComponent.setMotionZ(motionZ);
    }

    public float getMotionX() {
        return this.entityMotionComponent.getMotionX();
    }

    public float getMotionY() {
        return this.entityMotionComponent.getMotionY();
    }

    public float getMotionZ() {
        return this.entityMotionComponent.getMotionZ();
    }


    public Vector3f getLastMotion() {
        return lastMotion;
    }

    public void setLastMotion(Vector3f lastMotion) {
        this.lastMotion = lastMotion;
    }

    public EntityAttribute getEntityAttribute() {
        EntityAttribute entityAttribute = this.entityAttributeDataComponent.getEntityAttributeMap().get(EntityAttributeType.MOVEMENT_SPEED);
        if (entityAttribute == null) {
            entityAttribute = refreshEntityAttribute();
        }

        return entityAttribute;
    }

    public EntityAttribute refreshEntityAttribute() {
        Map<EntityAttributeType, EntityAttribute> entityAttributeMap = this.entityAttributeDataComponent.getEntityAttributeMap();
        EntityAttribute entityAttribute = entityAttributeMap.get(EntityAttributeType.MOVEMENT_SPEED);
        if (entityAttribute == null) {
            entityAttribute = new EntityAttribute(EntityAttributeType.MOVEMENT_SPEED,
                    0,
                    this.entitySpeedComponent.getMaxSpeed(),
                    this.entitySpeedComponent.getSpeed(),
                    this.entitySpeedComponent.getSpeed());

            entityAttributeMap.put(EntityAttributeType.MOVEMENT_SPEED, entityAttribute);
        }
        entityAttribute.setCurrentValue(this.entitySpeedComponent.getSpeed());
        entityAttribute.setMaxValue(this.entitySpeedComponent.getMaxSpeed());

        return entityAttribute;
    }


    @Override
    public Class<? extends ECSComponent>[] requestComponents() {
        return REQUIRE_CLASSES;
    }

    @Override
    protected void bindGameObject(GameObject gameObject) {
        this.entityAttributeDataComponent = ENTITY_ATTRIBUTE_DATA_COMPONENT_HANDLER.getComponent(gameObject);
        this.entitySpeedComponent = ENTITY_MOVEMENT_COMPONENT_HANDLER.getComponent(gameObject);
        this.entityMotionComponent = ENTITY_MOTION_COMPONENT_HANDLER.getComponent(gameObject);
    }
}
