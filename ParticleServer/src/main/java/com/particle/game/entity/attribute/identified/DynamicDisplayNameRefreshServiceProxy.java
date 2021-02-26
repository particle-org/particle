package com.particle.game.entity.attribute.identified;

import com.particle.api.entity.IDynamicDisplayNameRefreshServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.entity.Entity;
import com.particle.model.player.Player;

import javax.inject.Singleton;

@Singleton
public class DynamicDisplayNameRefreshServiceProxy implements IDynamicDisplayNameRefreshServiceApi {

    private static final ECSModuleHandler<DynamicDisplayNameModule> DYNAMIC_NAME_TAG_MODULE_HANDLER = ECSModuleHandler.buildHandler(DynamicDisplayNameModule.class);

    /**
     * 给生物绑定模组
     *
     * @param entity          生物
     * @param template        模板
     * @param refreshInterval 刷新间隔
     */
    @Override
    public void bindModule(Entity entity, String template, long refreshInterval) {
        DynamicDisplayNameModule dynamicDisplayNameModule = DYNAMIC_NAME_TAG_MODULE_HANDLER.bindModule(entity);
        dynamicDisplayNameModule.setTemplate(template);
        dynamicDisplayNameModule.setRefreshInterval(refreshInterval);
    }

    /**
     * 强制刷新
     *
     * @param entity
     */
    @Override
    public void requestRefresh(Entity entity) {
        DynamicDisplayNameModule module = DYNAMIC_NAME_TAG_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            // 下次tick时刷新
            module.setForceRefresh(true);
        }
    }

    /**
     * 给指定玩家刷新
     *
     * @param entity
     * @param player
     */
    @Override
    public void refreshToPlayer(Entity entity, Player player) {
        DynamicDisplayNameModule module = DYNAMIC_NAME_TAG_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            module.refresh(player);
        }
    }


}
