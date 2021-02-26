package com.particle.game.entity.service.network;

import com.particle.api.entity.function.IAddEntityPacketBuilder;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.attribute.metadata.EntityMetaDataModule;
import com.particle.game.entity.link.EntityLinkService;
import com.particle.game.entity.link.EntityPassengerModule;
import com.particle.game.entity.link.EntityVehicleModule;
import com.particle.game.entity.movement.module.EntityMovementModule;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.ArmorInventoryAPI;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.model.entity.model.others.MonsterEntity;
import com.particle.model.inventory.ArmorInventory;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.AddEntityPacket;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MonsterEntityPacketBuilder extends AliveEntityPacketBuilder {

    private static final ECSModuleHandler<EntityMetaDataModule> ENTITY_META_DATA_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMetaDataModule.class);

    private static final ECSModuleHandler<EntityMovementModule> ENTITY_MOVEMENT_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMovementModule.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    private static final ECSModuleHandler<EntityPassengerModule> ENTITY_PASSENGER_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityPassengerModule.class);

    private static final ECSModuleHandler<EntityVehicleModule> ENTITY_VEHICLE_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityVehicleModule.class);


    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private PlayerInventoryAPI playerInventoryService;

    @Inject
    private ArmorInventoryAPI armorInventoryService;

    public IAddEntityPacketBuilder getAddPacketBuilder(MonsterEntity entity) {
        return new MonsterEntityPacketBuilder.AddPacketBuilder(entity);
    }

    private class AddPacketBuilder implements IAddEntityPacketBuilder {

        private MonsterEntity entity;
        private EntityMovementModule entityMovementModule;
        private EntityMetaDataModule entityMetaDataModule;
        private TransformModule transformModule;

        public AddPacketBuilder(MonsterEntity entity) {
            this.entity = entity;
            this.entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.bindModule(entity);
            this.entityMetaDataModule = ENTITY_META_DATA_MODULE_HANDLER.bindModule(entity);
            this.transformModule = TRANSFORM_MODULE_HANDLER.bindModule(entity);
        }

        @Override
        public DataPacket[] buildPacket() {
            List<DataPacket> dataPackets = new ArrayList<>();
            dataPackets.add(this.buildAddPacket());

            PlayerInventory playerInventory = (PlayerInventory) inventoryManager.getInventoryByContainerId(entity, InventoryConstants.CONTAINER_ID_PLAYER);
            if (playerInventory != null) {
                // 区块内玩家的手执物品同步给该玩家
                DataPacket MonsterEquipmentPacket = playerInventoryService.constructEquipmentPacket(playerInventory);
                dataPackets.add(MonsterEquipmentPacket);
            }

            // 区块内玩家的装备数据同步给该玩家
            ArmorInventory armorInventory = (ArmorInventory) inventoryManager.getInventoryByContainerId(entity, InventoryConstants.CONTAINER_ID_ARMOR);
            if (armorInventory != null) {
                DataPacket MonsterArmorEquipmentPacket = armorInventoryService.constructEquipmentPacket(armorInventory);
                dataPackets.add(MonsterArmorEquipmentPacket);
            }

            DataPacket linkPacket = buildLinkPacket();
            if (linkPacket != null) {
                dataPackets.add(linkPacket);
            }

            List<DataPacket> vehiclePacket = buildVehiclePacket();
            if (vehiclePacket != null) {
                dataPackets.addAll(vehiclePacket);
            }

            return dataPackets.toArray(new DataPacket[dataPackets.size()]);
        }

        private DataPacket buildAddPacket() {
            AddEntityPacket addEntityPacket = new AddEntityPacket();

            addEntityPacket.setActorType(entity.getActorType());

            addEntityPacket.setEntityUniqueId(entity.getRuntimeId());
            addEntityPacket.setEntityRuntimeId(entity.getRuntimeId());

            addEntityPacket.setPosition(this.transformModule.getPosition());
            addEntityPacket.setDirection(this.transformModule.getDirection());

            addEntityPacket.setSpeedX(this.entityMovementModule.getMotionX());
            addEntityPacket.setSpeedY(this.entityMovementModule.getMotionY());
            addEntityPacket.setSpeedZ(this.entityMovementModule.getMotionZ());

            addEntityPacket.setMetadata(this.entityMetaDataModule.getEntityMetaData());

            return addEntityPacket;
        }

        private DataPacket buildLinkPacket() {
            // 可能是之后才绑定的module，所以实时获取
            EntityPassengerModule entityPassengerModule = ENTITY_PASSENGER_MODULE_HANDLER.getModule(entity);
            if (entityPassengerModule != null) {
                return EntityLinkService.buildEntityLinkPacketForPassenger(entity, entityPassengerModule);
            }

            return null;
        }

        private List<DataPacket> buildVehiclePacket() {
            // 可能是之后才绑定的module，所以实时获取
            EntityVehicleModule entityVehicleModule = ENTITY_VEHICLE_MODULE_HANDLER.getModule(entity);
            if (entityVehicleModule != null) {
                return EntityLinkService.buildEntityLinkPacketForVehicle(entity, entityVehicleModule);
            }

            return null;
        }
    }
}
