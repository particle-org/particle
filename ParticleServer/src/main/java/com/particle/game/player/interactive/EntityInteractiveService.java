package com.particle.game.player.interactive;

import com.particle.api.entity.IEntityInteractiveServiceApi;
import com.particle.api.entity.IEntityInteractivedHandle;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.player.service.EntityInteractedModule;
import com.particle.model.entity.Entity;
import com.particle.model.events.level.player.PlayerInteractiveEntityEvent;
import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;

import javax.inject.Singleton;

/**
 * Description: 玩家与生物交互时触发，更具玩家手持的物品路由
 */
@Singleton
public class EntityInteractiveService implements IEntityInteractiveServiceApi {

    private static final ECSModuleHandler<EntityInteractedModule> ENTITY_INTERACTED_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityInteractedModule.class);

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    /**
     * 初始化生物被交互的组件
     *
     * @param entity
     * @param handle
     */
    @Override
    public void initEntityInteractived(Entity entity, IEntityInteractivedHandle handle) {
        EntityInteractedModule entityInteractedModule = ENTITY_INTERACTED_MODULE_HANDLER.bindModule(entity);
        entityInteractedModule.setEntityInteractivedHandle(handle);
    }

    /**
     * 生物被交互时执行的逻辑
     *
     * @param entity
     * @param interactor
     * @param itemOnHand
     * @return
     */
    public boolean onEntityInteractived(Entity entity, Player interactor, ItemStack itemOnHand) {
        PlayerInteractiveEntityEvent playerInteractiveEntityEvent = new PlayerInteractiveEntityEvent(interactor, interactor.getLevel(), itemOnHand);
        playerInteractiveEntityEvent.setEntity(entity);
        this.eventDispatcher.dispatchEvent(playerInteractiveEntityEvent);

        if (playerInteractiveEntityEvent.isCancelled()) {
            return false;
        }

        // 执行被交互者的相关操作
        EntityInteractedModule entityInteractedModule = ENTITY_INTERACTED_MODULE_HANDLER.getModule(entity);
        if (entityInteractedModule != null) {
            entityInteractedModule.getEntityInteractivedHandle().handle(interactor, itemOnHand);
            return true;
        }

        return false;
    }

}
