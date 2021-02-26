package com.particle.game.item.use.use;

import com.particle.api.item.IItemUseProcessor;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemUseInventoryData;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemTridentPreUseProcessor implements IItemUseProcessor {

    @Inject
    private MetaDataService metaDataService;

    @Override
    public void process(Player player, ItemUseInventoryData itemUseInventoryData, InventoryActionData[] inventoryActionData) {
        // 开始使用三叉戟
        if (itemUseInventoryData.getItem().getItemType() == ItemPrototype.TRIDENT) {
            player.setOperationBowTime(System.currentTimeMillis());

            // 拉弓動作
            metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_ACTION, true, true);
        }
    }
}
