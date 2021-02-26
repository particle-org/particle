package com.particle.game.entity.service.network;

import com.particle.api.entity.function.IAddEntityPacketBuilder;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.attribute.identified.EntityNameService;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.link.EntityLinkService;
import com.particle.game.entity.link.EntityPassengerModule;
import com.particle.game.entity.link.EntityVehicleModule;
import com.particle.game.entity.service.NpcService;
import com.particle.game.player.PlayerSkinService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.ArmorInventoryAPI;
import com.particle.game.player.inventory.service.impl.DeputyInventoryAPI;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.model.entity.model.others.NpcEntity;
import com.particle.model.inventory.ArmorInventory;
import com.particle.model.inventory.DeputyInventory;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.AddPlayerPacket;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class NpcPacketBuilder extends PlayerPacketBuilder {

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    private static final ECSModuleHandler<EntityPassengerModule> ENTITY_PASSENGER_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityPassengerModule.class);

    private static final ECSModuleHandler<EntityVehicleModule> ENTITY_VEHICLE_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityVehicleModule.class);


    // ----- 玩家业务相关service -----
    @Inject
    private PlayerSkinService playerSkinService;
    @Inject
    private EntityNameService entityNameService;
    @Inject
    private MetaDataService metaDataService;
    @Inject
    private NpcService npcService;
    @Inject
    private PlayerInventoryAPI playerInventoryService;
    @Inject
    private ArmorInventoryAPI armorInventoryService;
    @Inject
    private DeputyInventoryAPI deputyInventoryService;
    @Inject
    private InventoryManager inventoryManager;

    public IAddEntityPacketBuilder getAddPacketBuilder(NpcEntity entity) {
        return new AddPacketBuilder(entity);
    }

    private class AddPacketBuilder implements IAddEntityPacketBuilder {

        private NpcEntity npcEntity;
        private TransformModule transformModule;

        public AddPacketBuilder(NpcEntity npcEntity) {
            this.npcEntity = npcEntity;
            this.transformModule = TRANSFORM_MODULE_HANDLER.getModule(npcEntity);
        }

        @Override
        public DataPacket[] buildPacket() {
            return this.buildAddPacket();
        }

        private DataPacket[] buildAddPacket() {
            //添加玩家
            AddPlayerPacket addPlayerPacket = new AddPlayerPacket();
            addPlayerPacket.setUuid(npcService.getNpcUuid(npcEntity));
            addPlayerPacket.setUserName(entityNameService.getDisplayEntityName(npcEntity));
            addPlayerPacket.setThirdPartyName("");
            addPlayerPacket.setPlatformId(0);
            addPlayerPacket.setPlatformChatId("");
            addPlayerPacket.setEntityUniqueId(npcEntity.getRuntimeId());
            addPlayerPacket.setEntityRuntimeId(npcEntity.getRuntimeId());
            addPlayerPacket.setSpeedX(0);
            addPlayerPacket.setSpeedY(0);
            addPlayerPacket.setSpeedZ(0);

            //配置位置
            addPlayerPacket.setDirection(transformModule.getDirection());
            addPlayerPacket.setPosition(transformModule.getPosition());

            PlayerInventory playerInventory = (PlayerInventory) inventoryManager.getInventoryByContainerId(npcEntity, InventoryConstants.CONTAINER_ID_PLAYER);
            if (playerInventory != null) {
                addPlayerPacket.setItem(playerInventoryService.getItem(playerInventory, playerInventory.getItemInHandle()));
            }
            addPlayerPacket.setMetadata(metaDataService.getEntityMetaData(npcEntity));
            List<DataPacket> dataPackets = new ArrayList<>();
            dataPackets.add(addPlayerPacket);
            //发送皮肤
            DataPacket[] skinPacket = playerSkinService.getRefreshPacket(npcEntity);
            if (skinPacket != null) {
                for (DataPacket dataPacket : skinPacket) {
                    dataPackets.add(dataPacket);
                }
            }

            // 区块内npc的装备数据同步给该玩家
            ArmorInventory armorInventory = (ArmorInventory) inventoryManager.getInventoryByContainerId(npcEntity, InventoryConstants.CONTAINER_ID_ARMOR);
            if (armorInventory != null) {
                dataPackets.add(armorInventoryService.constructEquipmentPacket(armorInventory));
            }

            // 区块内npc的手执物品同步给该玩家
            if (playerInventory != null) {
                dataPackets.add(playerInventoryService.constructEquipmentPacket(playerInventory));
            }

            // 区块内的npc的副手物品同步给该玩家
            DeputyInventory deputyInventory = (DeputyInventory) inventoryManager.getInventoryByContainerId(npcEntity, InventoryConstants.CONTAINER_ID_OFFHAND);
            if (deputyInventory != null) {
                dataPackets.add(deputyInventoryService.constructEquipmentPacket(deputyInventory));
            }

            DataPacket linkPacket = buildLinkPacket();
            if (linkPacket != null) {
                dataPackets.add(linkPacket);
            }

            List<DataPacket> vehiclePacket = buildVehiclePacket();
            if (vehiclePacket != null) {
                dataPackets.addAll(vehiclePacket);
            }

            // 构造数据包
            return dataPackets.toArray(new DataPacket[0]);
        }

        private DataPacket buildLinkPacket() {
            // 可能是之后才绑定的module，所以实时获取
            EntityPassengerModule entityPassengerModule = ENTITY_PASSENGER_MODULE_HANDLER.getModule(npcEntity);
            if (entityPassengerModule != null) {
                return EntityLinkService.buildEntityLinkPacketForPassenger(npcEntity, entityPassengerModule);
            }

            return null;
        }

        private List<DataPacket> buildVehiclePacket() {
            // 可能是之后才绑定的module，所以实时获取
            EntityVehicleModule entityVehicleModule = ENTITY_VEHICLE_MODULE_HANDLER.getModule(npcEntity);
            if (entityVehicleModule != null) {
                return EntityLinkService.buildEntityLinkPacketForVehicle(npcEntity, entityVehicleModule);
            }

            return null;
        }
    }
}

