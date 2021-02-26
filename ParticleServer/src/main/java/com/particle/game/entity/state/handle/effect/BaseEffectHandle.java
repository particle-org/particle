package com.particle.game.entity.state.handle.effect;

import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.state.handle.EntityStateHandle;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.model.effect.EffectBaseType;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.type.EntityStateRecorder;
import com.particle.model.network.packets.data.MobEffectPacket;

import javax.inject.Inject;

public abstract class BaseEffectHandle implements EntityStateHandle {

    @Inject
    private BroadcastServiceProxy broadcastService;

    @Inject
    private MetaDataService metaDataService;

    @Override
    public void onStateEnabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean hasEnabled) {
        EffectBaseType effectType = this.getEffectType();

        //发送更新数据包
        MobEffectPacket mobEffectPacket = new MobEffectPacket();
        mobEffectPacket.setEntityId(entity.getRuntimeId());
        mobEffectPacket.setEventId(hasEnabled ? MobEffectPacket.EVENT_MODIFY : MobEffectPacket.EVENT_ADD);
        mobEffectPacket.setDuration((int) (entityStateRecorder.getTimeToLive() < 0 ? 600 : entityStateRecorder.getTimeToLive() / 50));
        mobEffectPacket.setParticles(true);
        mobEffectPacket.setAmplifier(0);
        mobEffectPacket.setEffectId(effectType.getId());

        //广播给所有的订阅者
        this.broadcastService.broadcast(entity, mobEffectPacket, true);

        this.metaDataService.setIntegerData(entity, EntityMetadataType.POTION_COLOR, effectType.getColor());
        this.metaDataService.setBooleanData(entity, EntityMetadataType.POTION_AMBIENT, true);
        this.metaDataService.refreshMetadata(entity);
    }

    @Override
    public void onStateUpdated(Entity entity, EntityStateRecorder entityStateRecorder) {
        EffectBaseType effectType = this.getEffectType();

        //发送更新数据包
        MobEffectPacket mobEffectPacket = new MobEffectPacket();
        mobEffectPacket.setEntityId(entity.getRuntimeId());
        mobEffectPacket.setEventId(MobEffectPacket.EVENT_MODIFY);
        mobEffectPacket.setDuration((int) (entityStateRecorder.getTimeToLive() < 0 ? 600 : (entityStateRecorder.getTimeToLive() - (System.currentTimeMillis() - entityStateRecorder.getEnableTimestamp())) / 50));
        mobEffectPacket.setParticles(true);
        mobEffectPacket.setAmplifier(0);
        mobEffectPacket.setEffectId(effectType.getId());

        //广播给所有的订阅者
        this.broadcastService.broadcast(entity, mobEffectPacket, true);
    }

    @Override
    public void onStateDisabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean needDisableState) {
        //发送更新数据包
        MobEffectPacket mobEffectPacket = new MobEffectPacket();
        mobEffectPacket.setEntityId(entity.getRuntimeId());
        mobEffectPacket.setEventId(MobEffectPacket.EVENT_REMOVE);
        mobEffectPacket.setEffectId(this.getEffectType().getId());
        mobEffectPacket.setParticles(false);

        this.broadcastService.broadcast(entity, mobEffectPacket, true);

        this.metaDataService.setIntegerData(entity, EntityMetadataType.POTION_COLOR, 0);
        this.metaDataService.setBooleanData(entity, EntityMetadataType.POTION_AMBIENT, false);
        this.metaDataService.refreshMetadata(entity);
    }

    protected abstract EffectBaseType getEffectType();
}
