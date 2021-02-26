package com.particle.game.item.release.release;

import com.particle.api.item.IItemReleaseProcessor;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemReleaseInventoryData;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemCrossbowReleaseProcessor implements IItemReleaseProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemCrossbowReleaseProcessor.class);

    @Inject
    private MetaDataService metaDataService;

    @Override
    public void process(Player player, ItemReleaseInventoryData itemReleaseInventoryData, InventoryActionData[] inventoryActionData) {
        // 取消動作
        metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_ACTION, false, true);
    }
}
