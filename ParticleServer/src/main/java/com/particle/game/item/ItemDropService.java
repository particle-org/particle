package com.particle.game.item;

import com.particle.api.inventory.IItemDropServiceApi;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.ItemEntityService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.events.level.player.PlayerDropItemEvent;
import com.particle.model.item.ItemStack;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Description: 物品放置操作处理业务
 */
@Singleton
public class ItemDropService implements IItemDropServiceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemDropService.class);

    @Inject
    private ItemEntityService itemEntityService;

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private PositionService positionService;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Override
    public boolean playerDropItem(Player player, List<ItemStack> itemStacks) {
        if (itemStacks == null || itemStacks.isEmpty()) {
            return false;
        }

        PlayerDropItemEvent playerDropItemEvent = new PlayerDropItemEvent(player, itemStacks);
        this.eventDispatcher.dispatchEvent(playerDropItemEvent);

        if (playerDropItemEvent.isCancelled()) {
            return false;
        }

        //计算掉落位置
        Vector3f position = this.positionService.getPosition(player);
        Vector3f directionVector = this.positionService.getDirection(player).getAheadDirectionVector().multiply(4);
        directionVector.setY(2f);

        for (ItemStack itemStack : itemStacks) {

            Vector3f newPosition = position.add(0, 1, 0);
            Vector3f speed = directionVector.clone();

            //配置掉落物
            ItemEntity itemEntity = itemEntityService.createEntity(itemStack, newPosition, speed);

            LOGGER.debug("Player {} drop block {} at {},{},{} to {},{},{}",
                    player.getIdentifiedStr(),
                    itemStack.getItemType().getName(),
                    position.getX(),
                    position.getY(),
                    position.getZ(),
                    newPosition.getX(),
                    newPosition.getY(),
                    newPosition.getZ());

            this.entitySpawnService.spawnEntity(player.getLevel(), itemEntity);
        }

        return true;
    }

    public boolean entityDropItem(Entity entity, ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }

        //计算掉落位置
        Vector3f position = this.positionService.getPosition(entity);

        Vector3f newPosition = position.add(0, 1, 0);

        //配置掉落物
        ItemEntity itemEntity = itemEntityService.createEntity(itemStack, newPosition);

        LOGGER.debug("Entity {} drop block {} at {},{},{} to {},{},{}",
                entity.getRuntimeId(),
                itemStack.getItemType().getName(),
                position.getX(),
                position.getY(),
                position.getZ(),
                newPosition.getX(),
                newPosition.getY(),
                newPosition.getZ());

        this.entitySpawnService.spawnEntity(entity.getLevel(), itemEntity);

        return true;
    }

    public boolean entityDropItem(Entity entity, List<ItemStack> itemStacks) {
        if (itemStacks == null || itemStacks.isEmpty()) {
            return false;
        }

        //计算掉落位置
        Vector3f position = this.positionService.getPosition(entity);

        for (ItemStack itemStack : itemStacks) {

            Vector3f newPosition = position.add(0, 1, 0);

            //配置掉落物
            ItemEntity itemEntity = itemEntityService.createEntity(itemStack, newPosition);

            LOGGER.debug("Entity {} drop block {} at {},{},{} to {},{},{}",
                    entity.getRuntimeId(),
                    itemStack.getItemType().getName(),
                    position.getX(),
                    position.getY(),
                    position.getZ(),
                    newPosition.getX(),
                    newPosition.getY(),
                    newPosition.getZ());

            this.entitySpawnService.spawnEntity(entity.getLevel(), itemEntity);
        }

        return true;
    }
}
