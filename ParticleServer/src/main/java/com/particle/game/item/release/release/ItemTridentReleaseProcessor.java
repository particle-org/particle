package com.particle.game.item.release.release;

import com.particle.api.item.IItemReleaseProcessor;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.attack.EntityRemoteAttackService;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.item.DurabilityService;
import com.particle.game.player.PlayerService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemReleaseInventoryData;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemTridentReleaseProcessor implements IItemReleaseProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemTridentReleaseProcessor.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    @Inject
    private DurabilityService durabilityService;

    @Inject
    private PlayerInventoryAPI playerInventoryAPI;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private EntityRemoteAttackService entityRemoteAttackService;

    @Inject
    private PlayerService playerService;

    @Inject
    private MetaDataService metaDataService;

    @Override
    public void process(Player player, ItemReleaseInventoryData itemReleaseInventoryData, InventoryActionData[] inventoryActionData) {
        // 動作復原
        metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_ACTION, false, true);

        // 计算拉力
        long power = System.currentTimeMillis() - player.getOperationBowTime();
        if (power < 450) {
            return;
        }

        if (power > 450) {
            power = 450;
        }

        ItemStack trident = null;
        // 生存模式的话计算耐久
        if (!this.durabilityService.consumptionItem(player)) {
            return;
        }

        // 消耗物品
        Inventory playerInventory = this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
        // 如果没有指定消耗的物品，则不操作
        int tridentSlot = -1;
        for (InventoryActionData inventoryActionDatum : inventoryActionData) {
            if (inventoryActionDatum.getFromItem().getItemType() == ItemPrototype.TRIDENT) {
                tridentSlot = inventoryActionDatum.getSlot();
                trident = this.playerInventoryAPI.getItem(playerInventory, tridentSlot);
            }
        }
        // 校验物品
        if (trident == null || trident.getItemType() != ItemPrototype.TRIDENT) {
            this.playerInventoryAPI.notifyPlayerContentChanged(playerInventory);
            return;
        }
        trident.setCount(trident.getCount() - 1);
        if (trident.getCount() > 0) {
            this.playerInventoryAPI.setItem(playerInventory, tridentSlot, trident);
        } else {
            this.playerInventoryAPI.setItem(playerInventory, tridentSlot, ItemStack.getItem(ItemPrototype.AIR));
        }

        //射出抛射物
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(player);

        this.entityRemoteAttackService.projectileShoot(player, trident, transformModule.getDirection().getDirectionVector().multiply(power / 7f));
    }
}
