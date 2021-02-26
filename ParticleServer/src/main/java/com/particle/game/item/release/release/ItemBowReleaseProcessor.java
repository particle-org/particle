package com.particle.game.item.release.release;

import com.particle.api.item.IItemReleaseProcessor;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.attack.EntityRemoteAttackService;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.spawn.AutoRemovedModule;
import com.particle.game.item.DurabilityService;
import com.particle.game.item.ItemAttributeService;
import com.particle.game.item.ItemBindModule;
import com.particle.game.item.PickableModule;
import com.particle.game.player.PlayerService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemReleaseInventoryData;
import com.particle.model.item.ItemStack;
import com.particle.model.item.enchantment.Enchantments;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.player.GameMode;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemBowReleaseProcessor implements IItemReleaseProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemBowReleaseProcessor.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    private static final ECSModuleHandler<PickableModule> PICKABLE_MODULE_HANDLER = ECSModuleHandler.buildHandler(PickableModule.class);

    private static final ECSModuleHandler<AutoRemovedModule> AUTO_REMOVED_MODULE_HANDLER = ECSModuleHandler.buildHandler(AutoRemovedModule.class);

    private static final ECSModuleHandler<ItemBindModule> ITEM_BIND_MODULE_HANDLER = ECSModuleHandler.buildHandler(ItemBindModule.class);

    @Inject
    private DurabilityService durabilityService;

    @Inject
    private PlayerInventoryAPI playerInventoryAPI;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private EntityRemoteAttackService entityRemoteAttackService;

    @Inject
    private MetaDataService metaDataService;

    @Inject
    private PlayerService playerService;

    @Override
    public void process(Player player, ItemReleaseInventoryData itemReleaseInventoryData, InventoryActionData[] inventoryActionData) {
        boolean isInfinity = true;

        // 生存模式的话计算耐久
        if (this.playerService.getGameMode(player) == GameMode.SURVIVE) {
            if (!this.durabilityService.consumptionItem(player)) {
                return;
            }

            // 動作復原
            metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_ACTION, false, true);

            PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);

            // 检查无限附魔
            ItemStack bow = this.playerInventoryAPI.getItem(playerInventory, playerInventory.getItemInHandle());
            if (bow.getItemType() != ItemPrototype.BOW) {
                return;
            }
            isInfinity = ItemAttributeService.getEnchantment(bow, Enchantments.INFINITY) != null;

            // 消耗物品
            if (!isInfinity) {
                // 查找客户端希望扣除的弓箭位置
                ItemStack arrow = null;
                int arrowSlot = -1;
                for (InventoryActionData inventoryActionDatum : inventoryActionData) {
                    if (inventoryActionDatum.getFromItem().getItemType() == ItemPrototype.ARROW) {
                        arrowSlot = inventoryActionDatum.getSlot();
                        arrow = this.playerInventoryAPI.getItem(playerInventory, arrowSlot);
                    }
                }
                // 校验物品
                if (arrow == null || arrow.getItemType() != ItemPrototype.ARROW) {
                    this.playerInventoryAPI.notifyPlayerContentChanged(playerInventory);
                    return;
                }
                // 执行扣除操作
                arrow.setCount(arrow.getCount() - 1);
                if (arrow.getCount() > 0) {
                    this.playerInventoryAPI.setItem(playerInventory, arrowSlot, arrow);
                } else {
                    this.playerInventoryAPI.setItem(playerInventory, arrowSlot, ItemStack.getItem(ItemPrototype.AIR));
                }
            }
        }

        // 计算拉力
        long power = (System.currentTimeMillis() - player.getOperationBowTime()) >> 1;
        if (power > 500) {
            power = 500;
        }

        //射出抛射物
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(player);

        Entity entity = this.entityRemoteAttackService.projectileShoot(player, ItemStack.getItem(ItemPrototype.ARROW), transformModule.getDirection().getDirectionVector().multiply(power / 7f));
        AUTO_REMOVED_MODULE_HANDLER.bindModule(entity);

        if (!isInfinity) {
            PickableModule pickableModule = PICKABLE_MODULE_HANDLER.bindModule(entity);
            pickableModule.setBinderEntity(player.getRuntimeId());
            ItemBindModule itemBindModule = ITEM_BIND_MODULE_HANDLER.bindModule(entity);
            itemBindModule.setItem(ItemStack.getItem(ItemPrototype.ARROW));
        }
    }
}
