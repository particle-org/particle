package com.particle.game.entity.service.network;

import com.particle.api.entity.function.IAddEntityPacketBuilder;
import com.particle.api.entity.function.IRemoveEntityPacketBuilder;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.attribute.identified.EntityNameService;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.link.EntityLinkService;
import com.particle.game.entity.link.EntityLinkServiceProxy;
import com.particle.game.entity.link.EntityPassengerModule;
import com.particle.game.entity.link.EntityVehicleModule;
import com.particle.game.player.PlayerService;
import com.particle.game.player.PlayerSkinService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.ArmorInventoryAPI;
import com.particle.game.player.inventory.service.impl.DeputyInventoryAPI;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.position.IMoveEntityPacketBuilder;
import com.particle.model.inventory.ArmorInventory;
import com.particle.model.inventory.DeputyInventory;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.AddPlayerPacket;
import com.particle.model.network.packets.data.MovePlayerPacket;
import com.particle.model.network.packets.data.RemoveEntityPacket;
import com.particle.model.player.Player;
import com.particle.model.player.PositionMode;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class PlayerPacketBuilder extends AliveEntityPacketBuilder {

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);
    private static final ECSModuleHandler<EntityPassengerModule> ENTITY_PASSENGER_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityPassengerModule.class);
    private static final ECSModuleHandler<EntityVehicleModule> ENTITY_VEHICLE_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityVehicleModule.class);

    // ----- 玩家业务相关service -----
    @Inject
    private PlayerInventoryAPI playerInventoryService;
    @Inject
    private PlayerSkinService playerSkinService;
    @Inject
    private EntityNameService entityNameService;
    @Inject
    private ArmorInventoryAPI armorInventoryService;
    @Inject
    private DeputyInventoryAPI deputyInventoryService;
    @Inject
    private MetaDataService metaDataService;
    @Inject
    private PlayerService playerService;
    @Inject
    private InventoryManager inventoryManager;
    @Inject
    private EntityLinkServiceProxy entityLinkServiceProxy;

    public IAddEntityPacketBuilder getAddPacketBuilder(Player entity) {
        return new AddPacketBuilder(entity);
    }

    @Override
    public IMoveEntityPacketBuilder getMovePacketBuilder(Entity entity) {
        return new PlayerPacketBuilder.MovePacketBuilder(entity);
    }

    @Override
    public IRemoveEntityPacketBuilder getRemovePacketBuilder(Entity entity) {
        return new PlayerPacketBuilder.RemovePacketBuilder(entity);
    }

    private class AddPacketBuilder implements IAddEntityPacketBuilder {

        private Player player;

        public AddPacketBuilder(Player player) {
            this.player = player;
        }

        @Override
        public DataPacket[] buildPacket() {
            return this.buildAddPacket();
        }

        private DataPacket[] buildAddPacket() {
            List<DataPacket> packets = new LinkedList<>();

            //添加玩家
            AddPlayerPacket addPlayerPacket = new AddPlayerPacket();
            addPlayerPacket.setUuid(playerService.getPlayerUUID(player));
            addPlayerPacket.setUserName(entityNameService.getDisplayEntityName(player));
            addPlayerPacket.setThirdPartyName("");
            addPlayerPacket.setPlatformId(0);
            addPlayerPacket.setPlatformChatId("");
            addPlayerPacket.setEntityUniqueId(player.getRuntimeId());
            addPlayerPacket.setEntityRuntimeId(player.getRuntimeId());
            addPlayerPacket.setSpeedX(0);
            addPlayerPacket.setSpeedY(0);
            addPlayerPacket.setSpeedZ(0);

            //配置位置
            TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(player);
            addPlayerPacket.setDirection(transformModule.getDirection());
            addPlayerPacket.setPosition(transformModule.getPosition());

            PlayerInventory playerInventory = (PlayerInventory) inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);

            addPlayerPacket.setItem(playerInventoryService.getItem(playerInventory, playerInventory.getItemInHandle()));
            addPlayerPacket.setMetadata(metaDataService.getEntityMetaData(player));

            //发送皮肤
            DataPacket[] skinPacket = playerSkinService.getRefreshPacket(player);

            // 区块内玩家的装备数据同步给该玩家
            ArmorInventory armorInventory = (ArmorInventory) inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_ARMOR);
            DataPacket mobArmorEquipmentPacket = armorInventoryService.constructEquipmentPacket(armorInventory);

            // 区块内玩家的手执物品同步给该玩家
            DataPacket mobEquipmentPacket = playerInventoryService.constructEquipmentPacket(playerInventory);

            // 区块内的玩家的副手物品同步给该玩家
            DeputyInventory deputyInventory = (DeputyInventory) inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_OFFHAND);
            DataPacket deputyMobEquipmentPacket = deputyInventoryService.constructEquipmentPacket(deputyInventory);

            // 骑乘数据
            DataPacket entityLinkPacket = buildLinkPacket();
            List<DataPacket> vehiclePacket = buildVehiclePacket();

            packets.add(addPlayerPacket);
            packets.add(mobArmorEquipmentPacket);
            packets.add(mobEquipmentPacket);
            packets.add(deputyMobEquipmentPacket);
            if (skinPacket != null) {
                for (DataPacket dataPacket : skinPacket) {
                    packets.add(dataPacket);
                }
            }
            if (entityLinkPacket != null) {
                packets.add(entityLinkPacket);
            }
            if (vehiclePacket != null) {
                packets.addAll(vehiclePacket);
            }

            // 构造数据包
            return packets.toArray(new DataPacket[0]);
        }

        private DataPacket buildLinkPacket() {
            // 可能是之后才绑定的module，所以实时获取
            EntityPassengerModule entityPassengerModule = ENTITY_PASSENGER_MODULE_HANDLER.getModule(player);
            if (entityPassengerModule != null) {
                return EntityLinkService.buildEntityLinkPacketForPassenger(player, entityPassengerModule);
            }

            return null;
        }

        private List<DataPacket> buildVehiclePacket() {
            // 可能是之后才绑定的module，所以实时获取
            EntityVehicleModule entityVehicleModule = ENTITY_VEHICLE_MODULE_HANDLER.getModule(player);
            if (entityVehicleModule != null) {
                return EntityLinkService.buildEntityLinkPacketForVehicle(player, entityVehicleModule);
            }

            return null;
        }
    }

    private class MovePacketBuilder implements IMoveEntityPacketBuilder {

        private Entity player;
        private TransformModule transformModule;

        public MovePacketBuilder(Entity entity) {
            this.player = entity;
            transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public DataPacket build() {
            MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
            movePlayerPacket.setEntityId(this.player.getRuntimeId());
            movePlayerPacket.setVector3f(this.transformModule.getPosition().add(0, 1.62f, 0));
            movePlayerPacket.setDirection(this.transformModule.getDirection());
            movePlayerPacket.setMode(PositionMode.NORMAL);
            movePlayerPacket.setOnGround(this.transformModule.isOnGround());
            movePlayerPacket.setRidingEntityId(0);

            return movePlayerPacket;
        }
    }

    private class RemovePacketBuilder implements IRemoveEntityPacketBuilder {

        private Entity player;

        public RemovePacketBuilder(Entity player) {
            this.player = player;
        }

        @Override
        public DataPacket[] buildPacket() {
            return this.buildRemovePacket();
        }

        private DataPacket[] buildRemovePacket() {
            // 移除玩家
            RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
            removeEntityPacket.setEntityUniqueId(this.player.getRuntimeId());

            // 移除皮肤
            DataPacket skinPacket = playerSkinService.getRemovePacket(player);

            // 构造数据包
            if (skinPacket != null) {
                return new DataPacket[]{removeEntityPacket, skinPacket};
            } else {
                return new DataPacket[]{removeEntityPacket};
            }
        }
    }
}
