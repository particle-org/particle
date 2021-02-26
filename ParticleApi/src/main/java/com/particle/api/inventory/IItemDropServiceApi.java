package com.particle.api.inventory;

import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;

import java.util.List;

public interface IItemDropServiceApi {
    boolean playerDropItem(Player player, List<ItemStack> itemStacks);
}
