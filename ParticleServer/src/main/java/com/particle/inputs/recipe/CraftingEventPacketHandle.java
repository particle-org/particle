package com.particle.inputs.recipe;

import com.particle.game.player.craft.CraftService;
import com.particle.game.player.inventory.transaction.TransactionManager;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.CraftingEventPacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CraftingEventPacketHandle extends PlayerPacketHandle<CraftingEventPacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CraftingEventPacketHandle.class);

    @Inject
    private CraftService craftService;

    @Inject
    private TransactionManager transactionManager;

    @Override
    protected void handlePacket(Player player, CraftingEventPacket packet) {
        boolean state = this.craftService.processCrafting(player, packet.getType());
        if (!state) {
            LOGGER.warn("Player {} try to process illegal crafting! craft data : {}", player.getIdentifiedStr(), packet.toString());

            // 如果合成失败，则清除数据
            // 补充返还物品的逻辑 2020-11-11
            this.transactionManager.clearCache(player);
            this.craftService.resetCraftData(player);
        } else {
            // 切到主线程
            player.getLevel().getLevelSchedule().scheduleSimpleTask("CraftingEventPacketHandle task", () -> {
                transactionManager.handleCraftTransactionAfterCrafting(player);
            });
        }
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.CRAFTING_EVENT_PACKET;
    }
}
