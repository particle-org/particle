package com.particle.game.world.aoi;

import com.particle.api.aoi.IWhiteListServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.scene.module.BroadcastModule;
import com.particle.model.entity.Entity;
import com.particle.model.player.Player;

import javax.inject.Singleton;

@Singleton
public class WhiteListService implements IWhiteListServiceApi {

    private static final ECSModuleHandler<BroadcastModule> BROADCAST_MODULE_HANDLER = ECSModuleHandler.buildHandler(BroadcastModule.class);

    @Override
    public void setWhiteListState(Entity entity, boolean state) {
        BroadcastModule broadcastModule = BROADCAST_MODULE_HANDLER.getModule(entity);

        if (broadcastModule != null) {
            broadcastModule.setEnableWhiteList(state);
            broadcastModule.requestForceUpdate();
        }
    }

    @Override
    public void addWhiteList(Entity entity, Player player) {
        BroadcastModule broadcastModule = BROADCAST_MODULE_HANDLER.getModule(entity);

        if (broadcastModule != null) {
            broadcastModule.addWhiteList(player.getClientAddress());
        }
    }

    @Override
    public void removeWhiteList(Entity entity, Player player) {
        BroadcastModule broadcastModule = BROADCAST_MODULE_HANDLER.getModule(entity);

        if (broadcastModule != null) {
            broadcastModule.removeWhiteList(player.getClientAddress());
        }
    }
}
