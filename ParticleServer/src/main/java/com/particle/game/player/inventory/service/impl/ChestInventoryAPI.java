package com.particle.game.player.inventory.service.impl;

import com.particle.game.sound.SoundService;
import com.particle.game.world.animation.InventoryAnimationService;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.api.InventoryHolder;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;
import com.particle.model.sound.SoundType;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChestInventoryAPI extends ContainerInventoryAPI {

    @Inject
    private InventoryAnimationService inventoryAnimationService;

    @Inject
    private SoundService soundService;

    @Override
    public void addView(Player player, Inventory inventory) {
        super.addView(player, inventory);
        this.openChest(player, inventory);
    }

    @Override
    public void onClose(Player player, Inventory inventory) {
        this.closeChest(player, inventory);
        super.onClose(player, inventory);
    }

    /**
     * 打开胸包
     *
     * @param player
     * @param inventory
     */
    private void openChest(Player player, Inventory inventory) {
        if (inventory.getViewers().size() == 1) {
            InventoryHolder inventoryHolder = inventory.getInventoryHolder();
            Vector3 position = new Vector3(inventoryHolder.getPosition());
            this.inventoryAnimationService.sendOpenInventoryPacket(player.getLevel(), position);
            this.soundService.broadcastPlaySound(player.getLevel(), SoundType.RANDOM_CHESTOPEN, position);
        }
    }

    /**
     * 关闭胸包
     *
     * @param player
     * @param inventory
     */
    private void closeChest(Player player, Inventory inventory) {
        if (inventory.getViewers().size() == 1) {
            InventoryHolder inventoryHolder = inventory.getInventoryHolder();
            Vector3 position = new Vector3(inventoryHolder.getPosition());
            this.inventoryAnimationService.sendCloseInventoryPacket(player.getLevel(), position);
            this.soundService.broadcastPlaySound(player.getLevel(), SoundType.RANDOM_CHESTCLOSED, position);
        }
    }

}
