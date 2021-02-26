package com.particle.game.item.use.use;

import com.particle.api.inventory.InventoryAPI;
import com.particle.api.inventory.InventoryService;
import com.particle.api.item.IItemUseProcessor;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.attack.EntityRemoteAttackService;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.item.DurabilityService;
import com.particle.game.item.ItemAttributeService;
import com.particle.game.player.PlayerService;
import com.particle.model.entity.component.entity.EntityPiercingModule;
import com.particle.model.entity.component.entity.WeaponCarryModule;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemUseInventoryData;
import com.particle.model.item.ItemStack;
import com.particle.model.item.enchantment.Enchantments;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.player.GameMode;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemCrossbowPreUseProcessor implements IItemUseProcessor {

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    private static final ECSModuleHandler<WeaponCarryModule> WEAPON_CARRY_MODULE_HANDLER = ECSModuleHandler.buildHandler(WeaponCarryModule.class);

    private static final ECSModuleHandler<EntityPiercingModule> ENTITY_PIERCING_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityPiercingModule.class);


    @Inject
    private DurabilityService durabilityService;

    @Inject
    private MetaDataService metaDataService;

    @Inject
    private InventoryService inventoryService;

    @Inject
    private InventoryAPI inventoryAPI;

    @Inject
    private EntityRemoteAttackService entityRemoteAttackService;

    @Inject
    private PlayerService playerService;

    @Override
    public void process(Player player, ItemUseInventoryData itemUseInventoryData, InventoryActionData[] inventoryActionData) {
        // 开始使用弩
        if (itemUseInventoryData.getItem().getItemType() == ItemPrototype.CROSSBOW) {
            if (player.isOperationCrossbow()) {
                player.setOperationCrossbow(false);
                // 计算拉力
                long power = 1300;

                // 生存模式的话计算耐久
                if (this.playerService.getGameMode(player) == GameMode.SURVIVE) {
                    if (!this.durabilityService.consumptionItem(player)) {
                        return;
                    }
                }

                // 移除弩的搭載物
                PlayerInventory playerInventory = (PlayerInventory) inventoryService.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
                ItemStack itemInHand = inventoryAPI.getItem(playerInventory, playerInventory.getItemInHandle());
                if (itemInHand.getItemType() == ItemPrototype.CROSSBOW) {
                    NBTTagCompound nbtTagCompound = itemInHand.getNbt();
                    nbtTagCompound.setString("chargedItem", "");
                    inventoryAPI.setItem(playerInventory, playerInventory.getItemInHandle(), itemInHand);

                    // 若有附魔
                    if (ItemAttributeService.hasEnchantment(itemInHand, Enchantments.PIERCING)) {
                        EntityPiercingModule entityPiercingModule = ENTITY_PIERCING_MODULE_HANDLER.bindModule(player);

                        entityPiercingModule.setPiercingCount(0);
                        entityPiercingModule.setMaxCount(ItemAttributeService.getEnchantment(itemInHand, Enchantments.PIERCING).getLevel());
                    } else {
                        ENTITY_PIERCING_MODULE_HANDLER.removeModule(player);
                    }
                }


                //射出抛射物
                TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(player);
                // 取得搭載武器
                WeaponCarryModule weaponCarryModule = WEAPON_CARRY_MODULE_HANDLER.getModule(player);

                float speed = power / 14f;

                if (weaponCarryModule.getWeapon().getItemType() == ItemPrototype.FIREWORKS) {
                    speed = speed / 3f;
                }

                this.entityRemoteAttackService.projectileShoot(player, weaponCarryModule.getWeapon(), transformModule.getDirection().getDirectionVector().multiply(speed));
            } else {
                // 拉弓動作
                metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_ACTION, true, true);
            }
        }
    }
}
