package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.game.server.Server;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@CommandPermission(CommandPermissionConstant.CLEAR)
public class InventoryClearCommand extends BaseCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryClearCommand.class);

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private InventoryAPIProxy inventoryServiceProxy;

    @Inject
    private Server server;

    @SubCommand("clear")
    public void clear(CommandSource source, String playername) {
        Player player = server.getPlayer(playername);

        if (player == null) {
            String message = "玩家对象不存在!";
            source.sendError(message);
            return;
        }

        Inventory inventory = this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
        this.inventoryServiceProxy.clearAll(inventory);
        this.inventoryServiceProxy.notifyPlayerContentChanged(inventory);
    }
}
