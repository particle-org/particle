package com.particle.game.entity.link;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.ui.TextService;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.game.world.physical.modules.BoxColliderModule;
import com.particle.game.world.physical.modules.RigibodyModule;
import com.particle.model.entity.Entity;
import com.particle.model.entity.link.EntityLink;
import com.particle.model.entity.link.EntityLinkType;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.events.level.player.PlayerRideEvent;
import com.particle.model.events.level.player.PlayerUnrideEvent;
import com.particle.model.math.Vector2f;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.SetEntityLinkPacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class EntityLinkService {

    private static final ECSModuleHandler<EntityPassengerModule> ENTITY_PASSENGER_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityPassengerModule.class);
    private static final ECSModuleHandler<EntityVehicleModule> ENTITY_VEHICLE_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityVehicleModule.class);
    private static final ECSModuleHandler<BoxColliderModule> BOX_COLLIDER_MODULE_HANDLER = ECSModuleHandler.buildHandler(BoxColliderModule.class);
    private static final ECSModuleHandler<RigibodyModule> RIGIBODY_MODULE_HANDLER = ECSModuleHandler.buildHandler(RigibodyModule.class);

    @Inject
    private static BroadcastServiceProxy broadcastServiceProxy;

    @Inject
    private static NetworkManager networkManager;

    @Inject
    private static MetaDataService metaDataService;

    @Inject
    private static TextService textService;

    private static EventDispatcher eventDispatcher = EventDispatcher.getInstance();


    /**
     * 标记骑乘关系
     *
     * @param passenger
     * @param vehicle
     * @param entityPassengerModule
     * @param entityVehicleModule
     */
    public static void ridingEntity(Entity vehicle, EntityVehicleModule entityVehicleModule, Entity passenger, EntityPassengerModule entityPassengerModule) {
        if (vehicle == passenger) {
            return;
        }

        // 事件
        if (passenger instanceof Player) {
            PlayerRideEvent playerRideEvent = new PlayerRideEvent((Player) passenger);
            eventDispatcher.dispatchEvent(playerRideEvent);

            if (playerRideEvent.isCancelled()) {
                return;
            }
        }

        entityPassengerModule.setVehicle(vehicle);
        entityVehicleModule.addPassengers(passenger);
        BoxColliderModule boxColliderModule = BOX_COLLIDER_MODULE_HANDLER.getModule(vehicle);
        if (boxColliderModule != null) {
            boxColliderModule.addLinkedEntity(passenger.getRuntimeId());
        }
        RigibodyModule rigibodyModule = RIGIBODY_MODULE_HANDLER.getModule(passenger);
        if (rigibodyModule != null) {
            rigibodyModule.setLinkedEntity(vehicle);
        }

        //发包
        EntityLink entityRidedLink = new EntityLink();
        entityRidedLink.setLinkType(EntityLinkType.PASSENGER);
        entityRidedLink.setEntityUniqueIdA(vehicle.getRuntimeId());
        entityRidedLink.setEntityUniqueIdB(passenger.getRuntimeId());
        entityRidedLink.setImmediate(false);

        SetEntityLinkPacket setEntityLinkPacket = new SetEntityLinkPacket();
        setEntityLinkPacket.setEntityLink(entityRidedLink);

        broadcastServiceProxy.broadcast(passenger, setEntityLinkPacket);

        metaDataService.setDataFlag(passenger, MetadataDataFlag.DATA_FLAG_RIDING, true, true);
        metaDataService.setVector3fData(passenger, EntityMetadataType.RIDER_SEAT_POSITION, entityVehicleModule.getSitOffset(), true);

        if (passenger instanceof Player) {
            // 增加指令控制器
            entityPassengerModule.setAim(new Vector2f(0, 0));

            // 特殊发包
            entityRidedLink = new EntityLink();
            entityRidedLink.setLinkType(EntityLinkType.PASSENGER);
            entityRidedLink.setEntityUniqueIdA(vehicle.getRuntimeId());
            entityRidedLink.setEntityUniqueIdB(passenger.getRuntimeId());
            entityRidedLink.setImmediate(false);

            setEntityLinkPacket = new SetEntityLinkPacket();
            setEntityLinkPacket.setEntityLink(entityRidedLink);

            networkManager.sendMessage(((Player) passenger).getClientAddress(), setEntityLinkPacket);

            textService.sendTipMessage(((Player) passenger), "", 20);
        }
    }

    /**
     * 解除骑乘关系标记
     *
     * @param vehicle
     * @param entityVehicleModule
     * @param passenger
     */
    public static void unRidingEntity(Entity vehicle, EntityVehicleModule entityVehicleModule, Entity passenger) {
        // 事件
        if (passenger instanceof Player) {
            PlayerUnrideEvent playerUnrideEvent = new PlayerUnrideEvent((Player) passenger);
            eventDispatcher.dispatchEvent(playerUnrideEvent);

            if (playerUnrideEvent.isCancelled()) {
                return;
            }
        }

        // 清理组件
        ENTITY_PASSENGER_MODULE_HANDLER.removeModule(passenger);
        if (entityVehicleModule == null || entityVehicleModule.getPassengers().size() == 0) {
            ENTITY_VEHICLE_MODULE_HANDLER.removeModule(vehicle);
        }

        if (entityVehicleModule != null) {
            entityVehicleModule.removePassengers(passenger);
            BoxColliderModule boxColliderModule = BOX_COLLIDER_MODULE_HANDLER.getModule(vehicle);
            if (boxColliderModule != null) {
                boxColliderModule.removeLinkedEntity(passenger.getRuntimeId());
            }
            RigibodyModule rigibodyModule = RIGIBODY_MODULE_HANDLER.getModule(passenger);
            if (rigibodyModule != null) {
                rigibodyModule.setLinkedEntity(null);
            }

            if (entityVehicleModule.getPassengers().size() == 0) {
                ENTITY_VEHICLE_MODULE_HANDLER.removeModule(vehicle);
            }
        }

        // 发包
        EntityLink entityRidedLink = new EntityLink();
        entityRidedLink.setLinkType(EntityLinkType.NONE);
        entityRidedLink.setEntityUniqueIdA(vehicle.getRuntimeId());
        entityRidedLink.setEntityUniqueIdB(passenger.getRuntimeId());
        entityRidedLink.setImmediate(false);

        SetEntityLinkPacket setEntityLinkPacket = new SetEntityLinkPacket();
        setEntityLinkPacket.setEntityLink(entityRidedLink);

        broadcastServiceProxy.broadcast(vehicle, setEntityLinkPacket);

        metaDataService.setDataFlag(passenger, MetadataDataFlag.DATA_FLAG_RIDING, false, true);
    }

    /**
     * 移除骑乘关系
     *
     * @param vehicle
     */
    public static void clearPassenger(Entity vehicle, EntityVehicleModule entityVehicleModule) {
        // 获取骑乘者
        List<Entity> passengers = new ArrayList<>(entityVehicleModule.getPassengers());

        for (Entity passenger : passengers) {
            if (passenger == vehicle) {
                continue;
            }

            // 解绑
            unRidingEntity(vehicle, entityVehicleModule, passenger);
        }

        entityVehicleModule.clearPassengers();
    }

    public static DataPacket buildEntityLinkPacketForPassenger(Entity entity, EntityPassengerModule entityPassengerModule) {
        EntityLink entityRidedLink = new EntityLink();
        entityRidedLink.setLinkType(EntityLinkType.PASSENGER);
        entityRidedLink.setEntityUniqueIdA(entityPassengerModule.getVehicle().getRuntimeId());
        entityRidedLink.setEntityUniqueIdB(entity.getRuntimeId());
        entityRidedLink.setImmediate(false);

        SetEntityLinkPacket setEntityLinkPacket = new SetEntityLinkPacket();
        setEntityLinkPacket.setEntityLink(entityRidedLink);

        return setEntityLinkPacket;
    }

    public static List<DataPacket> buildEntityLinkPacketForVehicle(Entity entity, EntityVehicleModule entityVehicleModule) {
        List<DataPacket> dataPackets = new ArrayList<>(entityVehicleModule.getPassengers().size());

        for (Entity passenger : entityVehicleModule.getPassengers()) {
            EntityLink entityRidedLink = new EntityLink();
            entityRidedLink.setLinkType(EntityLinkType.PASSENGER);
            entityRidedLink.setEntityUniqueIdA(entity.getRuntimeId());
            entityRidedLink.setEntityUniqueIdB(passenger.getRuntimeId());
            entityRidedLink.setImmediate(false);

            SetEntityLinkPacket setEntityLinkPacket = new SetEntityLinkPacket();
            setEntityLinkPacket.setEntityLink(entityRidedLink);

            dataPackets.add(setEntityLinkPacket);
        }

        return dataPackets;
    }

}
