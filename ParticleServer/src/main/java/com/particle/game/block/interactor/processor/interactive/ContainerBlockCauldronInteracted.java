package com.particle.game.block.interactor.processor.interactive;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.block.brewing.CauldronModule;
import com.particle.game.block.potion.CauldronService;
import com.particle.game.block.tile.TileEntityService;
import com.particle.model.block.Block;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.events.level.potion.CauldronInteractiveEvent;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class ContainerBlockCauldronInteracted extends BaseContainerBlockInteractedProcessor {

    private static final ECSModuleHandler<CauldronModule> CAULDRON_MODULE_HANDLER = ECSModuleHandler.buildHandler(CauldronModule.class);

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    private TileEntityService tileEntityService;

    @Inject
    private CauldronService cauldronService;

    private static Set<ItemPrototype> interactiveItems = new HashSet<ItemPrototype>();

    @Inject
    public void init() {
        // 桶
        interactiveItems.add(ItemPrototype.BUCKET);
        // 染料
        interactiveItems.add(ItemPrototype.DYE);
        // 皮革护甲
        interactiveItems.add(ItemPrototype.LEATHER_BOOTS);
        interactiveItems.add(ItemPrototype.LEATHER_LEGGINGS);
        interactiveItems.add(ItemPrototype.LEATHER_HELMET);
        interactiveItems.add(ItemPrototype.LEATHER_CHESTPLATE);
        // 药瓶
        interactiveItems.add(ItemPrototype.POTION);
        // 玻璃瓶
        interactiveItems.add(ItemPrototype.GLASS_BOTTLE);
        // 旗帜
        interactiveItems.add(ItemPrototype.BANNER);
        // 弓箭
        interactiveItems.add(ItemPrototype.ARROW);
    }

    @Override
    public boolean interactive(Player player, Block targetBlock, Vector3 targetPosition) {
        PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
        ItemStack hand = this.inventoryServiceProxy.getItem(playerInventory, playerInventory.getItemInHandle());
        if (interactiveItems.contains(hand.getItemType())) {
            this.onHandle(player, targetBlock, targetPosition);
        }

        return true;
    }

    private void onHandle(Player player, Block block, Vector3 position) {
        CauldronInteractiveEvent cauldronInteractiveEvent = new CauldronInteractiveEvent(player.getLevel());
        cauldronInteractiveEvent.setPlayer(player);
        cauldronInteractiveEvent.setBlock(block);
        cauldronInteractiveEvent.setPosition(position);
        this.eventDispatcher.dispatchEvent(cauldronInteractiveEvent);

        if (cauldronInteractiveEvent.isCancelled()) {
            return;
        }

        PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
        ItemStack hand = this.inventoryServiceProxy.getItem(playerInventory, playerInventory.getItemInHandle());

        TileEntity tileEntity = tileEntityService.getEntityAt(player.getLevel(), position);
        if (tileEntity == null) {
            return;
        }
        CauldronModule cauldronModule = CAULDRON_MODULE_HANDLER.getModule(tileEntity);
        if (cauldronModule == null) {
            return;
        }
        ItemStack newHand = null;
        switch (hand.getItemType()) {
            case BUCKET:
                newHand = this.cauldronService.handleBucket(player, block, tileEntity, hand);
                break;
            case DYE:
                newHand = this.cauldronService.handleDye(player, block, tileEntity, hand);
                break;
            case LEATHER_BOOTS:
            case LEATHER_CHESTPLATE:
            case LEATHER_HELMET:
            case LEATHER_LEGGINGS:
                newHand = this.cauldronService.handleLeather(player, block, tileEntity, hand);
                break;
            case POTION:
                newHand = this.cauldronService.handlePotion(player, block, tileEntity, hand);
                break;
            case GLASS_BOTTLE:
                newHand = this.cauldronService.handleGlassBottle(player, block, tileEntity, hand);
                break;
            case ARROW:
                newHand = this.cauldronService.handleArrow(player, block, tileEntity, hand);
                break;
        }

        //判断是否需要更新原物品
        if (hand.getCount() == 0) {
            this.inventoryServiceProxy.setItem(playerInventory, playerInventory.getItemInHandle(), ItemStack.getItem(ItemPrototype.AIR));
        } else {
            this.inventoryServiceProxy.setItem(playerInventory, playerInventory.getItemInHandle(), hand);
        }

        if (newHand != null) {
            this.inventoryServiceProxy.addItem(playerInventory, newHand);
        }

    }
}
