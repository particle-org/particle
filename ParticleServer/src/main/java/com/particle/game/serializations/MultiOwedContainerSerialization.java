package com.particle.game.serializations;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.player.inventory.holder.EntityInventoryHolder;
import com.particle.game.player.inventory.modules.MultiOwedContainerModule;
import com.particle.model.entity.Entity;
import com.particle.model.inventory.*;
import com.particle.model.inventory.common.ContainerType;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.coder.ItemJSONObjectCoder;
import com.particle.model.player.saver.PlayerInventoryData;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class MultiOwedContainerSerialization implements IStringSerialization<MultiOwedContainerModule> {

    /**
     * 只存储玩家背包、装备、副手， 末影箱
     */
    private static final List<ContainerType> needSaveTypes = Arrays.asList(
            ContainerType.PLAYER,
            ContainerType.ARMOR,
            ContainerType.DEPUTY,
            ContainerType.ENDER_CHEST);

    @Override
    public String serialization(GameObject gameObject, MultiOwedContainerModule multiOwedContainerModule) {
        Collection<Inventory> inventories = multiOwedContainerModule.values();
        List<PlayerInventoryData> playerInventoryDatas = new ArrayList<>();
        for (Inventory inventory : inventories) {
            PlayerInventoryData data = this.getInventoryData(inventory);
            if (data != null) {
                playerInventoryDatas.add(data);
            }
        }
        return JSON.toJSONString(playerInventoryDatas);
    }

    /**
     * 只是存储玩家相关数据
     *
     * @param inventory
     * @return
     */
    private PlayerInventoryData getInventoryData(Inventory inventory) {
        if (!needSaveTypes.contains(inventory.getContainerType())) {
            return null;
        }
        PlayerInventoryData playerInventoryData = new PlayerInventoryData();
        playerInventoryData.setType(inventory.getContainerType().getDefaultTitle());
        playerInventoryData.setSortOrder(inventory.getContainerType().getSortIndex());
        if (inventory instanceof PlayerInventory) {
            playerInventoryData.setItemInHandle(((PlayerInventory) inventory).getItemInHandle());
        }

        Map<Integer, ItemStack> allSlots = inventory.getAllSlots();
        for (Map.Entry<Integer, ItemStack> entry : allSlots.entrySet()) {
            if (entry == null) {
                continue;
            }
            Integer slot = entry.getKey();
            ItemStack itemStack = entry.getValue();

            JSONObject object = ItemJSONObjectCoder.encode(itemStack);
            object.put("slot", slot);
            playerInventoryData.getAllSlotInfos().add(object);
        }
        return playerInventoryData;
    }

    @Override
    public void deserialization(GameObject gameObject, String data, MultiOwedContainerModule multiOwedContainerModule) {
        if (StringUtils.isEmpty(data)) {
            return;
        }

        if (!(gameObject instanceof Entity)) {
            return;
        }

        Entity entity = (Entity) gameObject;

        List<PlayerInventoryData> playerInventoryDatas = JSONObject.parseArray(data, PlayerInventoryData.class);
        for (PlayerInventoryData playerInventoryData : playerInventoryDatas) {
            Inventory inventory = null;
            int typeId = playerInventoryData.getSortOrder();
            if (typeId == ContainerType.PLAYER.getSortIndex()) {
                inventory = new PlayerInventory();
                inventory.setInventoryHolder(new EntityInventoryHolder(entity, inventory));
                ((PlayerInventory) inventory).setItemInHandle(playerInventoryData.getItemInHandle());
                multiOwedContainerModule.addInventory(InventoryConstants.CONTAINER_ID_PLAYER, inventory);
            } else if (typeId == ContainerType.ARMOR.getSortIndex()) {
                inventory = new ArmorInventory();
                inventory.setInventoryHolder(new EntityInventoryHolder(entity, inventory));
                multiOwedContainerModule.addInventory(InventoryConstants.CONTAINER_ID_ARMOR, inventory);
            } else if (typeId == ContainerType.DEPUTY.getSortIndex()) {
                inventory = new DeputyInventory();
                inventory.setInventoryHolder(new EntityInventoryHolder(entity, inventory));
                multiOwedContainerModule.addInventory(InventoryConstants.CONTAINER_ID_OFFHAND, inventory);
            } else if (typeId == ContainerType.ENDER_CHEST.getSortIndex()) {
                inventory = new PlayerEnderChestInventory();
                multiOwedContainerModule.addInventory(InventoryConstants.CONTAINER_ID_ENDER, inventory);
            } else {
                return;
            }
            Set<JSONObject> allSlots = playerInventoryData.getAllSlotInfos();
            if (allSlots != null && !allSlots.isEmpty()) {
                for (JSONObject entry : allSlots) {
                    if (entry == null) {
                        continue;
                    }
                    Integer slot = entry.getInteger("slot");

                    ItemStack itemStack = ItemJSONObjectCoder.decode(entry);

                    if (itemStack != null) {
                        inventory.putSlot(slot, itemStack);
                    }
                }
            }
        }
    }
}
