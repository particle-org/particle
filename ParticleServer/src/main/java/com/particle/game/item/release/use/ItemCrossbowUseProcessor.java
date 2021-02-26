package com.particle.game.item.release.use;

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
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.DeputyInventoryAPI;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.model.entity.component.entity.EntityPiercingModule;
import com.particle.model.entity.component.entity.WeaponCarryModule;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.inventory.DeputyInventory;
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
import java.util.Map;

@Singleton
public class ItemCrossbowUseProcessor implements IItemUseProcessor {

    private static final ECSModuleHandler<WeaponCarryModule> WEAPON_CARRY_MODULE_HANDLER = ECSModuleHandler.buildHandler(WeaponCarryModule.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    private static final ECSModuleHandler<EntityPiercingModule> ENTITY_PIERCING_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityPiercingModule.class);

    @Inject
    private PlayerInventoryAPI playerInventoryAPI;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private DeputyInventoryAPI deputyInventoryAPI;

    @Inject
    private InventoryService inventoryService;

    @Inject
    private InventoryAPI inventoryAPI;

    @Inject
    private DurabilityService durabilityService;

    @Inject
    private EntityRemoteAttackService entityRemoteAttackService;

    @Inject
    private PlayerService playerService;

    @Inject
    private MetaDataService metaDataService;

    @Override
    public void process(Player player, ItemUseInventoryData itemUseInventoryData, InventoryActionData[] inventoryActionData) {
        // 消耗物品
        if (itemUseInventoryData.getItem().getItemType() == ItemPrototype.CROSSBOW) {
            PlayerInventory playerInventory = (PlayerInventory) inventoryService.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
            ItemStack itemInHand = inventoryAPI.getItem(playerInventory, playerInventory.getItemInHandle());
            // 若無搭載物 且 在拉弓
            if ((itemInHand.getNbt() == null || itemInHand.getNbt().toString().equalsIgnoreCase("{}")) && metaDataService.getDataFlag(player, MetadataDataFlag.DATA_FLAG_ACTION)) {
                itemInHand.updateNBT(new NBTTagCompound());
                // 檢查副手物品
                DeputyInventory deputyInventory = (DeputyInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_OFFHAND);
                if (deputyInventory != null) {
                    // 获取副手物品
                    ItemStack itemMap = this.deputyInventoryAPI.getItem(deputyInventory, 0);
                    // 火箭
                    if (itemMap != null && itemMap.getItemType() == ItemPrototype.FIREWORKS) {
                        itemMap.setCount(itemMap.getCount() - 1);
                        if (itemMap.getCount() > 0) {
                            deputyInventoryAPI.setItem(deputyInventory, 0, itemMap);
                        } else {
                            deputyInventoryAPI.setItem(deputyInventory, 0, ItemStack.getItem(ItemPrototype.AIR));
                        }

                        // 弩更新 nbt
                        NBTTagCompound weaponTag = new NBTTagCompound();
                        weaponTag.setByte("Count", (byte) 1);
                        weaponTag.setShort("Damage", (short) 0);
                        weaponTag.setString("Name", itemMap.getItemType().getName());
                        weaponTag.setTag("tag", itemMap.getNbt() == null ? new NBTTagCompound() : itemMap.getNbt());
                        itemInHand.getNbt().setTag("chargedItem", weaponTag);

                        inventoryAPI.setItem(playerInventory, playerInventory.getItemInHandle(), itemInHand);

                        WeaponCarryModule weaponCarryModule = WEAPON_CARRY_MODULE_HANDLER.bindModule(player);
                        weaponCarryModule.setWeapon(ItemStack.getItem(ItemPrototype.FIREWORKS));
                        player.setOperationCrossbow(true);
                        metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_ACTION, false, true);
                        inventoryManager.notifyPlayerAllInventoryChanged(player);
                        return;
                    }
                }

                // 消耗弓箭
                this.playerInventoryAPI.notifyPlayerContentChanged(playerInventory);
                // 如果没有指定消耗的物品，则不操作
                ItemStack arrow = null;
                int arrowSlot = -1;
                for (Map.Entry<Integer, ItemStack> itemStackEntry : playerInventory.getAllSlots().entrySet()) {
                    if (itemStackEntry.getValue().getItemType() == ItemPrototype.ARROW) {
                        arrowSlot = itemStackEntry.getKey();
                        arrow = this.playerInventoryAPI.getItem(playerInventory, arrowSlot);
                        break;
                    }
                }
                // 校验物品
                if (arrow == null || arrow.getItemType() != ItemPrototype.ARROW) {
                    this.playerInventoryAPI.notifyPlayerContentChanged(playerInventory);
                    return;
                }
                arrow.setCount(arrow.getCount() - 1);
                if (arrow.getCount() > 0) {
                    this.playerInventoryAPI.setItem(playerInventory, arrowSlot, arrow);
                } else {
                    this.playerInventoryAPI.setItem(playerInventory, arrowSlot, ItemStack.getItem(ItemPrototype.AIR));
                }

                // 弩更新 nbt
                NBTTagCompound weaponTag = new NBTTagCompound();
                weaponTag.setByte("Count", (byte) 1);
                weaponTag.setShort("Damage", (short) 0);
                weaponTag.setString("Name", arrow.getItemType().getName());
                itemInHand.getNbt().setTag("chargedItem", weaponTag);
                inventoryAPI.setItem(playerInventory, playerInventory.getItemInHandle(), itemInHand);

                WeaponCarryModule weaponCarryModule = WEAPON_CARRY_MODULE_HANDLER.bindModule(player);
                weaponCarryModule.setWeapon(ItemStack.getItem(ItemPrototype.ARROW));
                metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_ACTION, false, true);
                player.setOperationCrossbow(true);
                return;
            } else {
                // 是否已裝武器
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
                    if (itemInHand.getItemType() == ItemPrototype.CROSSBOW) {
                        itemInHand.updateNBT(new NBTTagCompound());
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
}
