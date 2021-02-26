package com.particle.game.entity.service;

import com.particle.api.entity.VirtualEntityServiceApi;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.model.entity.Entity;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class VirtualEntityService implements VirtualEntityServiceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(VirtualEntityService.class);

    @Inject
    private NetworkManager networkManager;

    @Inject
    private EntitySpawnService entitySpawnService;

    private Map<Long, Entity> virtualEntities = new ConcurrentHashMap<>();

    /**
     * 注册虚拟生物，虚拟生物不spawn进世界，每个玩家看到的都不一样
     *
     * @param entity
     */
    @Override
    public void registerVirtualEntity(Entity entity) {
        this.virtualEntities.put(entity.getRuntimeId(), entity);
    }

    /**
     * 去掉生物缓存，该操作会导致生物无法被交互
     *
     * @param entity
     * @return
     */
    @Override
    public boolean removeVirtualEntity(Entity entity) {
        return this.virtualEntities.remove(entity.getRuntimeId()) != null;
    }

    /**
     * 查询虚拟生物
     *
     * @param entityId
     * @return
     */
    public Entity getVirtualEntity(long entityId) {
        return this.virtualEntities.get(entityId);
    }

    /**
     * 将生物刷新给玩家
     *
     * @param entity
     * @param player
     */
    @Override
    public void spawnToPlayer(Entity entity, Player player) {
        //只有虚拟NPC才允许这么操作
        if (entity.getLevel() == null) {
            this.networkManager.sendMessage(player.getClientAddress(), this.entitySpawnService.getEntitySpawnPacket(entity));
        }
    }

    /**
     * 将生物移除
     *
     * @param entity
     * @param player
     */
    @Override
    public void despawnToPlayer(Entity entity, Player player) {
        //只有虚拟NPC才允许这么操作
        if (entity.getLevel() == null) {
            this.networkManager.sendMessage(player.getClientAddress(), this.entitySpawnService.getEntityDespawnPacket(entity));
        }
    }
}
