package com.particle.game.player.inventory.transaction.processor.action;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.game.player.state.EntityGameModeModule;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.player.GameMode;
import com.particle.model.player.Player;

public class CreativeAction extends InventoryAction {

    private static final ECSModuleHandler<EntityGameModeModule> ENTITY_GAME_MODE_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityGameModeModule.class);

    /**
     * 当为创造模式的时候，slot表示类型，为0表示丢弃
     */
    private static final int CREATIVE_SLOT_TYPE_DELETE_ITEM = 0;

    /**
     * 当为创造模式的时候，slot表示类型，为1表示生成
     */
    private static final int CREATIVE_SLOT_TYPE_CREATE_ITEM = 1;

    public CreativeAction(InventoryActionData inventoryActionData) {
        super(inventoryActionData);
    }

    @Override
    public boolean isValid(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        // todo 添加slot type判断
        return ENTITY_GAME_MODE_MODULE_HANDLER.getModule(player).getGameMode() == GameMode.CREATIVE;
    }

    @Override
    public boolean execute(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        return true;
    }

    @Override
    public void onSuccess(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {

    }

    @Override
    public void onFailed(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {

    }
}
