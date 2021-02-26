package com.particle.game.server.command.defaults.test;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.api.inventory.InventoryAPI;
import com.particle.game.entity.state.model.EntityStateType;
import com.particle.game.item.ItemAttributeService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.ParentCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.lore.Lore;
import com.particle.model.item.types.DyeType;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@ParentCommand("testItem")
@CommandPermission(CommandPermissionConstant.TEST)
public class ItemTestCommand extends BaseCommand {

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private InventoryAPI inventoryAPI;


    @SubCommand("enchance")
    public void test(CommandSource source) {
        Player player = source.getPlayer();

        if (player == null) {
            return;
        }

        PlayerInventory inventory = (PlayerInventory) inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
        ItemStack item = inventoryAPI.getItem(inventory, inventory.getItemInHandle());

        item.setLore(new Lore("§3已强化§r"));
        ItemAttributeService.addBindState(item, EntityStateType.ESTRUS_STATUS.getName());

        inventoryAPI.setItem(inventory, inventory.getItemInHandle(), item);
    }

    @SubCommand("enchance")
    public void test(CommandSource source, String type) {
        Player player = source.getPlayer();

        if (player == null) {
            return;
        }

        PlayerInventory inventory = (PlayerInventory) inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
        ItemStack item = inventoryAPI.getItem(inventory, inventory.getItemInHandle());

        item.setLore(new Lore("§3已强化§r"));
        ItemAttributeService.addBindState(item, type);

        inventoryAPI.setItem(inventory, inventory.getItemInHandle(), item);
    }

    @SubCommand("color")
    public void color(CommandSource source) {
        Player player = source.getPlayer();

        if (player == null) {
            return;
        }

        PlayerInventory inventory = (PlayerInventory) inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
        ItemStack item = inventoryAPI.getItem(inventory, inventory.getItemInHandle());

        ItemAttributeService.setColor(item, DyeType.blue.getColorHex());

        inventoryAPI.setItem(inventory, inventory.getItemInHandle(), item);
    }
}
