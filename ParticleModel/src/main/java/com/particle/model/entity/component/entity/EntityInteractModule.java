package com.particle.model.entity.component.entity;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.entity.Entity;
import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;

public class EntityInteractModule extends BehaviorModule {

    private EntityInteractInterface interactInterface;
    private boolean cancelAfterProcess = true;

    public EntityInteractModule(EntityInteractInterface interactInterface) {
        this.interactInterface = interactInterface;
    }

    public void onInteractive(Player player, Entity entity, ItemStack itemStack) {
        if (interactInterface != null) {
            interactInterface.onEntityInteract(player, entity, itemStack);
        }
    }

    public boolean isCancelAfterProcess() {
        return cancelAfterProcess;
    }

    public void setCancelAfterProcess(boolean cancelAfterProcess) {
        this.cancelAfterProcess = cancelAfterProcess;
    }

    /**
     * 内部交互类
     */
    public interface EntityInteractInterface {

        /**
         * entity交互方法
         * 返回 true 表示拦截交互
         * 返回 false 表示继续下面的交互
         *
         * @param player
         * @param entity
         * @param itemStack 用什么武器攻击
         * @return
         */
        boolean onEntityInteract(Player player, Entity entity, ItemStack itemStack);
    }
}
