package com.particle.game.entity.link;

import com.particle.api.entity.attribute.EntityLinkServiceAPI;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class EntityLinkServiceProxy implements EntityLinkServiceAPI {

    private static final ECSModuleHandler<EntityPassengerModule> ENTITY_PASSENGER_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityPassengerModule.class);
    private static final ECSModuleHandler<EntityVehicleModule> ENTITY_VEHICLE_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityVehicleModule.class);

    @Override
    public void ridingEntity(Entity vehicle, Entity passenger) {
        // 检查组件
        EntityVehicleModule entityVehicleModule = ENTITY_VEHICLE_MODULE_HANDLER.bindModule(vehicle);
        EntityPassengerModule entityPassengerModule = ENTITY_PASSENGER_MODULE_HANDLER.bindModule(passenger);

        EntityLinkService.ridingEntity(vehicle, entityVehicleModule, passenger, entityPassengerModule);
    }

    @Override
    public void ridingEntity(Entity vehicle, Entity passenger, Vector3f sitOffset) {
        // 检查组件
        EntityVehicleModule entityVehicleModule = ENTITY_VEHICLE_MODULE_HANDLER.bindModule(vehicle);
        entityVehicleModule.setSitOffset(sitOffset);
        EntityPassengerModule entityPassengerModule = ENTITY_PASSENGER_MODULE_HANDLER.bindModule(passenger);

        EntityLinkService.ridingEntity(vehicle, entityVehicleModule, passenger, entityPassengerModule);
    }

    @Override
    public void unMountEntity(Entity vehicle) {
        // 获取关联组件
        EntityVehicleModule entityVehicleModule = ENTITY_VEHICLE_MODULE_HANDLER.getModule(vehicle);
        if (entityVehicleModule == null) {
            return;
        }

        EntityLinkService.clearPassenger(vehicle, entityVehicleModule);
    }

    @Override
    public void unRideEntity(Entity passenger) {
        // 获取关联组件
        EntityPassengerModule entityPassengerModule = ENTITY_PASSENGER_MODULE_HANDLER.getModule(passenger);
        if (entityPassengerModule == null || entityPassengerModule.getVehicle() == null) {
            return;
        }

        EntityVehicleModule entityVehicleModule = ENTITY_VEHICLE_MODULE_HANDLER.getModule(entityPassengerModule.getVehicle());

        // 解绑
        EntityLinkService.unRidingEntity(entityPassengerModule.getVehicle(), entityVehicleModule, passenger);
    }

    @Override
    public long getPlayerRidingEntityId(Player player) {
        EntityPassengerModule entityPassengerModule = ENTITY_PASSENGER_MODULE_HANDLER.getModule(player);
        if (entityPassengerModule == null || entityPassengerModule.getVehicle() == null) {
            return 0;
        }

        return entityPassengerModule.getVehicle().getRuntimeId();
    }

    @Override
    public Entity getVehicle(Entity entity) {
        EntityPassengerModule entityPassengerModule = ENTITY_PASSENGER_MODULE_HANDLER.getModule(entity);
        if (entityPassengerModule == null || entityPassengerModule.getVehicle() == null) {
            return null;
        }

        return entityPassengerModule.getVehicle();
    }

    public boolean hasPassenger(Entity entity) {
        EntityVehicleModule entityVehicleModule = ENTITY_VEHICLE_MODULE_HANDLER.getModule(entity);

        return entityVehicleModule != null && entityVehicleModule.getPassengers().size() > 0;
    }

    public boolean hasPassenger(Entity entity, long passengerId) {
        EntityVehicleModule entityVehicleModule = ENTITY_VEHICLE_MODULE_HANDLER.getModule(entity);
        if (entityVehicleModule == null) {
            return false;
        }

        for (Entity passenger : entityVehicleModule.getPassengers()) {
            if (passenger.getRuntimeId() == passengerId) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Entity getRiderEntity(Entity entity) {
        EntityVehicleModule entityVehicleModule = ENTITY_VEHICLE_MODULE_HANDLER.getModule(entity);
        if (entityVehicleModule == null) {
            return null;
        }

        List<Entity> passengers = entityVehicleModule.getPassengers();
        if (passengers.isEmpty()) {
            return null;
        }

        return passengers.get(0);
    }

    @Override
    public List<Entity> getRiderEntities(Entity entity) {
        EntityVehicleModule entityVehicleModule = ENTITY_VEHICLE_MODULE_HANDLER.getModule(entity);
        if (entityVehicleModule == null) {
            return null;
        }

        return entityVehicleModule.getPassengers();
    }
}
