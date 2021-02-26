package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.ItemEntityService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.game.server.Server;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.math.Vector3f;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@RegisterCommand
@Singleton
@CommandPermission(CommandPermissionConstant.GIVE)
public class ItemGiveCommand extends BaseCommand {

    private static final Logger logger = LoggerFactory.getLogger(ItemGiveCommand.class);

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private InventoryAPIProxy inventoryServiceProxy;

    @Inject
    private Server server;

    @Inject
    private PositionService positionService;

    @Inject
    private ItemEntityService itemEntityService;

    @Inject
    private EntitySpawnService entitySpawnService;

    @SubCommand("give")
    public void give(CommandSource source, String itemInfo, String counts) {
        Player player = null;
        if (!source.isConsole()) {
            player = source.getPlayer();
        }

        if (player == null) {
            String message = "玩家对象不存在!";
            source.sendError(message);
            return;
        }
        this.give(source, player, itemInfo, counts);
    }

    @SubCommand("give")
    public void give(CommandSource source, String itemInfo) {
        this.give(source, itemInfo, "1");
    }


    @SubCommand("give")
    @CommandPermission(CommandPermissionConstant.CONSOLE)
    public void give(CommandSource source, String playerName, String itemInfo, String counts) {
        Player player = this.server.getPlayer(playerName);
        if (player == null) {
            source.sendMessage("玩家不存在");
            return;
        }
        this.give(source, player, itemInfo, counts);
    }

    private void give(CommandSource source, Player player, String itemInfo, String counts) {
        if (StringUtils.isEmpty(itemInfo)) {
            source.sendError("item对象为空！");
            return;
        }

        itemInfo = itemInfo.toLowerCase();
        String metaVa = null;
        String itemName = null;
        if (itemInfo.contains(":")) {
            String[] valueArray = itemInfo.split(":");
            if (valueArray.length != 2) {
                source.sendError("命令格式不对");
                return;
            }
            itemName = valueArray[0];
            metaVa = valueArray[1];
        } else {
            itemName = itemInfo;
        }

        if (StringUtils.isEmpty(counts)) {
            counts = "1";
        }

        int meta = 0;
        int itemCounts = 1;
        int itemId = -1;

        try {
            itemCounts = Integer.parseInt(counts);
            if (!StringUtils.isEmpty(metaVa)) {
                meta = Integer.parseInt(metaVa);
            }
            if (NumberUtils.isDigits(itemName)) {
                itemId = Integer.parseInt(itemName);
            }
        } catch (NumberFormatException nf) {
            source.sendError("命令格式不对！");
            return;
        }
        ItemStack itemStack = null;
        if (itemId != -1) {
            itemStack = ItemStack.getItem(itemId, meta, itemCounts);
        } else {
            itemName = String.format("minecraft:%s", itemName);
            itemStack = ItemStack.getItem(itemName, meta, itemCounts);
        }

        if (itemStack == null || itemStack.getItemType() == ItemPrototype.AIR) {
            source.sendError("请输入正确的物品！");
            return;
        }
        Inventory inventory = this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
        List<ItemStack> results = this.inventoryServiceProxy.addItem(inventory, itemStack);
        if (results.isEmpty()) {
            source.sendMessage(String.format("玩家[%s]获取物品[%s]", player.getIdentifiedStr(), itemStack.getItemType().getName()));
        } else {
            source.sendError("玩家背包已满！");
            ItemEntity itemEntity = this.itemEntityService.createEntity(itemStack, this.positionService.getPosition(player), new Vector3f((float) Math.random() - 0.5f, 6f, (float) Math.random() - 0.5f));
            this.entitySpawnService.spawnEntity(player.getLevel(), itemEntity);
        }
    }
}
