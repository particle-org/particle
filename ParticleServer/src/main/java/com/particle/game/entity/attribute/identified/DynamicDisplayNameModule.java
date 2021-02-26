package com.particle.game.entity.attribute.identified;

import com.particle.api.inject.RequestStaticInject;
import com.particle.core.ecs.module.BehaviorModule;
import com.particle.game.utils.placeholder.ThreadBindCompiledService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.type.EntityData;
import com.particle.model.entity.metadata.type.StringEntityData;
import com.particle.model.network.packets.data.SetEntityDataPacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@RequestStaticInject
public class DynamicDisplayNameModule extends BehaviorModule {

    @Inject
    private static ThreadBindCompiledService threadBindCompiledService;

    @Inject
    private static NetworkManager networkManager;

    private String template = "";
    private long refreshInterval = -1;
    private long lastRefreshTimestamp;
    public boolean forceRefresh = false;

    public void refresh(Player player) {
        if (getOwn() instanceof Entity) {
            String compiledName = threadBindCompiledService.compile(player, this.getTemplate());

            SetEntityDataPacket setEntityDataPacket = new SetEntityDataPacket();
            setEntityDataPacket.setEid(((Entity) getOwn()).getRuntimeId());
            Map<EntityMetadataType, EntityData> entityMetadataTypeEntityDataMap = new HashMap<>();
            entityMetadataTypeEntityDataMap.put(EntityMetadataType.NAMETAG, new StringEntityData(compiledName));
            setEntityDataPacket.setMetaData(entityMetadataTypeEntityDataMap);

            networkManager.sendMessage(player.getClientAddress(), setEntityDataPacket);
        }
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public long getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(long refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public long getLastRefreshTimestamp() {
        return lastRefreshTimestamp;
    }

    public void setLastRefreshTimestamp(long lastRefreshTimestamp) {
        this.lastRefreshTimestamp = lastRefreshTimestamp;
    }

    public boolean isForceRefresh() {
        return forceRefresh;
    }

    public void setForceRefresh(boolean forceRefresh) {
        this.forceRefresh = forceRefresh;
    }
}
