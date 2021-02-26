package com.particle.game.entity.ai.leaf.seeker;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.player.PlayerService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.model.entity.Entity;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class SearchItemInHandSeeker implements IAction {

    private static Logger logger = LoggerFactory.getLogger(SearchItemInHandSeeker.class);

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PlayerService playerService;

    @Inject
    private PositionService positionService;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private PlayerInventoryAPI playerInventoryAPI;

    private float checkDistance = 16;
    private float distanceSquared = 16;

    private List<ItemPrototype> itemStackTempList = new ArrayList<>();
    private List<ItemPrototype> itemStackList = new ArrayList<>();

    @Override
    public void onInitialize() {
        distanceSquared = checkDistance * checkDistance;
        itemStackList.clear();
        for (ItemPrototype itemPrototype : itemStackTempList) {
            itemStackList.add(itemPrototype);
        }
        itemStackTempList.clear();
    }

    @Override
    public EStatus tick(Entity entity) {
        List<Player> playerList = this.playerService.getNearPlayers(entity.getLevel(), this.positionService.getPosition(entity), checkDistance);
        if (playerList == null || playerList.size() <= 0) {
            return EStatus.FAILURE;
        } else {
            for (Player player : playerList) {
                PlayerInventory inventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
                if (inventory == null) {
                    continue;
                }

                ItemStack itemStack = this.playerInventoryAPI.getItem(inventory, inventory.getItemInHandle());
                if (positionService.getPosition(entity).subtract(positionService.getPosition(player)).lengthSquared() <= distanceSquared
                        && itemStackList.contains(itemStack.getItemType())) {
                    this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.ENTITY_TARGET, player);
                    this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.ITEM_IN_HAND_CACHE, itemStack.getItemType().getId());
                    return EStatus.SUCCESS;
                }
            }
        }

        return EStatus.FAILURE;
    }

    @Override
    public void onTicked(Entity entity, EStatus status) {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void config(String key, Object val) {
        if (key.equalsIgnoreCase("CheckDistance") && val.getClass() == Float.class) {
            this.checkDistance = (float) val;
        }

        if (key.equalsIgnoreCase("itemInHand") && val instanceof String) {
            ItemStack item = ItemStack.getItem((String) val);
            if (item == null) {
                logger.error("itemInHand config fail, {} not exist!", (String) val);
            } else {
                itemStackTempList.add(item.getItemType());
            }
        }
    }

}
