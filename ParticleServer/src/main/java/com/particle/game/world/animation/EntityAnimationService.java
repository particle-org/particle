package com.particle.game.world.animation;

import com.particle.api.animation.EntityAnimationServiceApi;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.item.ItemStack;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.data.AddItemEntityPacket;
import com.particle.model.network.packets.data.EntityEventPacket;
import com.particle.model.network.packets.data.RemoveEntityPacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EntityAnimationService implements EntityAnimationServiceApi {

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;
    @Inject
    private NetworkManager networkManager;

    @Inject
    private MetaDataService metaDataService;

    @Override
    public void hurt(Entity entity) {
        sendAnimation(entity, EntityEventPacket.HURT);
    }

    @Override
    // 苦力怕爆炸前動畫
    public void exploding(Entity entity, boolean isExploding) {
        // 99 tick
        metaDataService.setIntegerData(entity, EntityMetadataType.FUSE_LENGTH, 99);
        metaDataService.setIntegerData(entity, EntityMetadataType.TARGET_EID, Integer.MAX_VALUE);
        metaDataService.setDataFlag(entity, MetadataDataFlag.DATA_FLAG_IGNITED, isExploding, false);

        metaDataService.refreshMetadata(entity);
    }

    public long throwItem(Entity sender, ItemStack itemStack, Vector3f position, Vector3f motion, long ttl) {
        long itemId = ItemEntity.requestId();

        this.throwItem(sender, itemId, itemStack, position, motion, ttl);

        return itemId;
    }

    public void throwItem(Entity sender, long itemId, ItemStack itemStack, Vector3f position, Vector3f motion, long ttl) {
        AddItemEntityPacket addItemEntityPacket = new AddItemEntityPacket();
        addItemEntityPacket.setEntityRuntimeId(itemId);
        addItemEntityPacket.setEntityUniqueId(itemId);
        addItemEntityPacket.setItemStack(itemStack);
        addItemEntityPacket.setPosition(position);
        addItemEntityPacket.setSpeedX(motion.getX());
        addItemEntityPacket.setSpeedY(motion.getY());
        addItemEntityPacket.setSpeedZ(motion.getZ());

        broadcastServiceProxy.broadcast(sender, addItemEntityPacket);

        if (ttl > 0) {
            sender.getLevel().getLevelSchedule().scheduleDelayTask("ThrowItemAnimate", () -> {
                RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
                removeEntityPacket.setEntityUniqueId(itemId);

                broadcastServiceProxy.broadcast(sender, removeEntityPacket);
            }, ttl);
        }
    }

    public long throwItemFor(Player receiver, ItemStack itemStack, Vector3f position, Vector3f motion, long ttl) {
        long itemId = ItemEntity.requestId();

        this.throwItemFor(receiver, itemId, itemStack, position, motion, ttl);

        return itemId;
    }

    public void throwItemFor(Player receiver, long itemId, ItemStack itemStack, Vector3f position, Vector3f motion, long ttl) {
        AddItemEntityPacket addItemEntityPacket = new AddItemEntityPacket();
        addItemEntityPacket.setEntityRuntimeId(itemId);
        addItemEntityPacket.setEntityUniqueId(itemId);
        addItemEntityPacket.setItemStack(itemStack);
        addItemEntityPacket.setPosition(position);
        addItemEntityPacket.setSpeedX(motion.getX());
        addItemEntityPacket.setSpeedY(motion.getY());
        addItemEntityPacket.setSpeedZ(motion.getZ());

        networkManager.sendMessage(receiver.getClientAddress(), addItemEntityPacket);

        if (ttl > 0) {
            receiver.getLevel().getLevelSchedule().scheduleDelayTask("ThrowItemAnimate", () -> {
                RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
                removeEntityPacket.setEntityUniqueId(itemId);

                networkManager.sendMessage(receiver.getClientAddress(), removeEntityPacket);
            }, ttl);
        }
    }

    public void removeThrowItem(Entity sender, long itemId) {
        RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
        removeEntityPacket.setEntityUniqueId(itemId);

        broadcastServiceProxy.broadcast(sender, removeEntityPacket);
    }

    @Override
    public void sendAnimation(Entity entity, int eventId) {
        EntityEventPacket entityEventPacket = new EntityEventPacket();
        entityEventPacket.setEntityRuntimeId(entity.getRuntimeId());
        entityEventPacket.setEventId(eventId);

        this.broadcastServiceProxy.broadcast(entity, entityEventPacket, true);
    }
}
