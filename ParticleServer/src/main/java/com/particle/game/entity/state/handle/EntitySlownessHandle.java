package com.particle.game.entity.state.handle;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.movement.module.EntityMovementModule;
import com.particle.game.entity.state.handle.effect.BaseEffectHandle;
import com.particle.model.effect.EffectBaseType;
import com.particle.model.entity.Entity;
import com.particle.model.entity.attribute.EntityAttribute;
import com.particle.model.entity.type.EntityStateRecorder;
import com.particle.model.network.packets.data.UpdateAttributesPacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EntitySlownessHandle extends BaseEffectHandle {

    private static final ECSModuleHandler<EntityMovementModule> ENTITY_MOVEMENT_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMovementModule.class);

    @Inject
    private NetworkManager networkManager;

    @Override
    public String getDisplayName() {
        return "缓慢";
    }

    @Override
    public void onStateEnabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean hasEnabled) {
        super.onStateEnabled(entity, entityStateRecorder, hasEnabled);

        EntityMovementModule entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.bindModule(entity);
        if (entityStateRecorder.getLevel() >= 7) {
            entityMovementModule.setSpeed(0.001f);
            entityMovementModule.setMaxSpeed(0.001f);
        } else {
            entityMovementModule.setSpeed(0.1f - 0.015f * entityStateRecorder.getLevel());
            entityMovementModule.setMaxSpeed(0.1f - 0.015f * entityStateRecorder.getLevel());
        }

        entityMovementModule.refreshEntityAttribute();

        if (entity instanceof Player) {
            Player player = (Player) entity;
            UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
            updateAttributesPacket.setEntityId(player.getRuntimeId());
            updateAttributesPacket.setAttributes(new EntityAttribute[]{entityMovementModule.getEntityAttribute()});
            networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
        }
    }

    @Override
    public void onStateUpdated(Entity entity, EntityStateRecorder entityStateRecorder) {

    }

    @Override
    public void onStateDisabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean needDisableState) {
        super.onStateDisabled(entity, entityStateRecorder, needDisableState);

        EntityMovementModule entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.bindModule(entity);
        entityMovementModule.setSpeed(0.1f);
        entityMovementModule.setMaxSpeed(0.2f);
        entityMovementModule.refreshEntityAttribute();

        if (entity instanceof Player) {
            Player player = (Player) entity;
            UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
            updateAttributesPacket.setEntityId(player.getRuntimeId());
            updateAttributesPacket.setAttributes(new EntityAttribute[]{entityMovementModule.getEntityAttribute()});
            networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
        }
    }

    @Override
    public EffectBaseType getEffectType() {
        return EffectBaseType.SLOWNESS;
    }
}
