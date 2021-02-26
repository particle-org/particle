package com.particle.game.entity.attribute.satisfaction;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.entity.attribute.health.HealthServiceProxy;
import com.particle.game.entity.state.EntityStateModule;
import com.particle.game.entity.state.model.EntityStateType;
import com.particle.game.player.PlayerService;
import com.particle.model.entity.Entity;
import com.particle.model.network.packets.data.UpdateAttributesPacket;
import com.particle.model.player.GameMode;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HungerSystemFactory implements ECSSystemFactory<HungerSystemFactory.HungerSystem> {

    private static final ECSModuleHandler<EntityStateModule> ENTITY_STATE_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityStateModule.class);

    @Inject
    private PlayerService playerService;
    @Inject
    private HealthServiceProxy healthServiceProxy;
    @Inject
    private HungerService hungerService;

    @Inject
    private NetworkManager networkManager;

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{EntitySatisfactionModule.class, EntityStateModule.class};
    }

    @Override
    public HungerSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new HungerSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<HungerSystem> getSystemClass() {
        return HungerSystem.class;
    }

    public class HungerSystem implements ECSSystem {

        private Entity entity;
        private EntityStateModule entityStateModule;

        public HungerSystem(Entity entity) {
            this.entity = entity;
            this.entityStateModule = ENTITY_STATE_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            if (entity instanceof Player) {
                GameMode gameMode = playerService.getGameMode((Player) entity);
                // 创造模式或者观察者模式
                if (gameMode != null && gameMode == GameMode.CREATIVE || gameMode == GameMode.SURVIVAL_VIEWER || gameMode == GameMode.CREATIVE_VIEWER) {
                    return;
                }
            }

            if (!healthServiceProxy.isAlive(entity)) {
                return;
            }

            float foodExhaustionLevel = hungerService.getFoodExhuastionLevel(entity);
            int footLevel = hungerService.getFoodLevel(entity);
            float foodSaturationLevel = hungerService.getFoodSaturationLevel(entity);

            // 当饥饿等级达到4.0的时候，会改变饥饿度或者食物饱和度
            if (foodExhaustionLevel >= EntitySatisfactionModule.HUNGRY_CHANGE_THREASHOLD) {
                if (foodSaturationLevel > 0) {
                    foodSaturationLevel = foodSaturationLevel > 1 ? foodSaturationLevel - 1 : 0;
                    // 通过这个API，会下发至客户端
                    hungerService.setFoodSaturationLevel(entity, foodSaturationLevel);
                } else {
                    footLevel = footLevel > 1 ? footLevel - 1 : 0;
                    // 通过这个API，会下发至客户端
                    hungerService.setFoodLevel(entity, footLevel);
                }

                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
                    updateAttributesPacket.setEntityId(player.getRuntimeId());
                    updateAttributesPacket.setAttributes(hungerService.getHungerAttributes(player));

                    networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
                }

                foodExhaustionLevel = 0;
            }

            // Buffer效果
            if (this.entityStateModule.hasState(EntityStateType.HUNGER)) {
                foodExhaustionLevel += 0.02;
            }

            hungerService.setFoodExhuastionLevel(entity, foodExhaustionLevel);
        }
    }
}
