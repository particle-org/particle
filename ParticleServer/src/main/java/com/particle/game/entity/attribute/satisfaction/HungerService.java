package com.particle.game.entity.attribute.satisfaction;

import com.particle.api.entity.attribute.IHungerServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.state.EntityStateService;
import com.particle.model.effect.EffectBaseData;
import com.particle.model.effect.EffectBaseType;
import com.particle.model.entity.Entity;
import com.particle.model.entity.attribute.EntityAttribute;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.food.FoodEffectTable;
import com.particle.model.food.FoodEffectType;
import com.particle.model.item.ItemStack;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.data.UpdateAttributesPacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class HungerService implements IHungerServiceApi {

    private static final ECSModuleHandler<EntitySatisfactionModule> ENTITY_SATISFACTION_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntitySatisfactionModule.class);


    @Inject
    private EntityStateService entityStateService;
    @Inject
    private PositionService positionService;
    @Inject
    private MetaDataService metaDataService;

    @Inject
    private NetworkManager networkManager;

    public void initHungerComponent(Entity entity) {
        ENTITY_SATISFACTION_MODULE_HANDLER.bindModule(entity);
    }

    public void onPlayerMoveConsumption(Entity entity, Vector3f nextPosition) {
        Vector3f position = this.positionService.getPosition(entity);

        if (nextPosition.getX() != position.getX() || nextPosition.getZ() != position.getZ()) {
            float foodExhuastionLevel = 0;

            if (this.metaDataService.getDataFlag(entity, MetadataDataFlag.DATA_FLAG_SPRINTING)) {
                foodExhuastionLevel = 0.1f;
            } else {
                foodExhuastionLevel = 0.01f;
            }

            if (this.entityStateService.hasState(entity, EffectBaseType.HUNGER.getName())) {
                foodExhuastionLevel *= 4;
            }

            this.addFoodExhuastionLevel(entity, foodExhuastionLevel);
        }
    }

    public boolean isFull(Entity entity) {
        EntitySatisfactionModule module = ENTITY_SATISFACTION_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            return module.getFoodLevel() < module.getMaxFoodLevel();
        }

        return true;
    }

    /**
     * 获取当前玩家的饥饿度
     *
     * @param entity
     * @return
     */
    @Override
    public int getFoodLevel(Entity entity) {
        EntitySatisfactionModule module = ENTITY_SATISFACTION_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            return module.getFoodLevel();
        }

        return 0;
    }

    /**
     * 设置玩家的饥饿度
     *
     * @param entity
     * @param foodLevel
     */
    @Override
    public void setFoodLevel(Entity entity, int foodLevel) {
        EntitySatisfactionModule satisfactionModule = ENTITY_SATISFACTION_MODULE_HANDLER.getModule(entity);
        if (satisfactionModule != null) {
            satisfactionModule.setFoodLevel(foodLevel);

            if (entity instanceof Player) {
                Player player = (Player) entity;
                UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
                updateAttributesPacket.setEntityId(player.getRuntimeId());
                updateAttributesPacket.setAttributes(new EntityAttribute[]{satisfactionModule.getEntityHungerAttribute(), satisfactionModule.getEntitySaturationAttribute()});
                this.networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
            }
        }
    }

    @Override
    public void resetFoodLevel(Entity entity) {
        EntitySatisfactionModule satisfactionModule = ENTITY_SATISFACTION_MODULE_HANDLER.getModule(entity);
        if (satisfactionModule != null) {
            satisfactionModule.setFoodLevel(satisfactionModule.getMaxFoodLevel());
            satisfactionModule.setFoodSaturationLevel(satisfactionModule.getMaxFoodSaturationLevel());

            if (entity instanceof Player) {
                Player player = (Player) entity;
                UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
                updateAttributesPacket.setEntityId(player.getRuntimeId());
                updateAttributesPacket.setAttributes(new EntityAttribute[]{satisfactionModule.getEntityHungerAttribute(), satisfactionModule.getEntitySaturationAttribute()});
                this.networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
            }
        }
    }

    /**
     * 增加玩家的饥饿度
     *
     * @param entity
     * @param foodLevel
     */
    @Override
    public void addFoodLevel(Entity entity, int foodLevel) {
        EntitySatisfactionModule satisfactionModule = ENTITY_SATISFACTION_MODULE_HANDLER.getModule(entity);
        if (satisfactionModule != null) {
            satisfactionModule.setFoodLevel(satisfactionModule.getFoodLevel() + foodLevel);

            if (entity instanceof Player) {
                Player player = (Player) entity;
                UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
                updateAttributesPacket.setEntityId(player.getRuntimeId());
                updateAttributesPacket.setAttributes(new EntityAttribute[]{satisfactionModule.getEntityHungerAttribute(), satisfactionModule.getEntitySaturationAttribute()});
                this.networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
            }
        }

    }

    /**
     * 获取当前玩家的食物饱和度
     *
     * @param entity
     * @return
     */
    @Override
    public float getFoodSaturationLevel(Entity entity) {
        EntitySatisfactionModule satisfactionModule = ENTITY_SATISFACTION_MODULE_HANDLER.getModule(entity);
        if (satisfactionModule != null) {
            return satisfactionModule.getFoodSaturationLevel();
        }

        return 0;
    }

    /**
     * 增加玩家的食物饱和度
     *
     * @param entity
     * @param foodSaturationLevel
     */
    @Override
    public void addFoodSaturationLevel(Entity entity, float foodSaturationLevel) {
        EntitySatisfactionModule satisfactionModule = ENTITY_SATISFACTION_MODULE_HANDLER.getModule(entity);
        if (satisfactionModule != null) {
            satisfactionModule.setFoodSaturationLevel(satisfactionModule.getFoodSaturationLevel() + foodSaturationLevel);

            if (entity instanceof Player) {
                Player player = (Player) entity;
                UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
                updateAttributesPacket.setEntityId(player.getRuntimeId());
                updateAttributesPacket.setAttributes(new EntityAttribute[]{satisfactionModule.getEntityHungerAttribute(), satisfactionModule.getEntitySaturationAttribute()});
                this.networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
            }
        }
    }

    /**
     * 设置玩家的食物饱和度
     *
     * @param entity
     * @param foodSaturationLevel
     */
    @Override
    public void setFoodSaturationLevel(Entity entity, float foodSaturationLevel) {
        EntitySatisfactionModule satisfactionModule = ENTITY_SATISFACTION_MODULE_HANDLER.getModule(entity);
        if (satisfactionModule != null) {
            satisfactionModule.setFoodSaturationLevel(foodSaturationLevel);

            if (entity instanceof Player) {
                Player player = (Player) entity;
                UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
                updateAttributesPacket.setEntityId(player.getRuntimeId());
                updateAttributesPacket.setAttributes(new EntityAttribute[]{satisfactionModule.getEntityHungerAttribute(), satisfactionModule.getEntitySaturationAttribute()});
                this.networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
            }
        }
    }

    /**
     * 获取饱食度水平
     *
     * @param entity
     * @return
     */
    @Override
    public float getFoodExhuastionLevel(Entity entity) {
        EntitySatisfactionModule satisfactionModule = ENTITY_SATISFACTION_MODULE_HANDLER.getModule(entity);
        if (satisfactionModule != null) {
            return satisfactionModule.getFoodExhaustionLevel();
        }

        return 0;
    }

    /**
     * 设置饱食度水平
     *
     * @param entity
     * @param foodSaturationLevel
     */
    @Override
    public void setFoodExhuastionLevel(Entity entity, float foodSaturationLevel) {
        EntitySatisfactionModule satisfactionModule = ENTITY_SATISFACTION_MODULE_HANDLER.getModule(entity);
        if (satisfactionModule != null) {
            satisfactionModule.setFoodExhaustionLevel(foodSaturationLevel);

            if (entity instanceof Player) {
                Player player = (Player) entity;
                UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
                updateAttributesPacket.setEntityId(player.getRuntimeId());
                updateAttributesPacket.setAttributes(new EntityAttribute[]{satisfactionModule.getEntityHungerAttribute(), satisfactionModule.getEntitySaturationAttribute()});
                this.networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
            }
        }
    }

    /**
     * 玩家移动、跳跃时会触发
     * 增加玩家的饥饿等级
     *
     * @param entity
     * @param foodExhuastionLevel
     */
    @Override
    public void addFoodExhuastionLevel(Entity entity, float foodExhuastionLevel) {
        EntitySatisfactionModule satisfactionModule = ENTITY_SATISFACTION_MODULE_HANDLER.getModule(entity);
        if (satisfactionModule != null) {
            satisfactionModule.addFoodExhaustionLevel(foodExhuastionLevel);

            if (entity instanceof Player) {
                Player player = (Player) entity;
                UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
                updateAttributesPacket.setEntityId(player.getRuntimeId());
                updateAttributesPacket.setAttributes(new EntityAttribute[]{satisfactionModule.getEntityHungerAttribute(), satisfactionModule.getEntitySaturationAttribute()});
                this.networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
            }
        }
    }

    /**
     * 吃单个食物
     *
     * @param entity
     * @param foodItem
     */
    public void eatFood(Entity entity, ItemStack foodItem) {
        FoodEffectTable foodEffect = FoodEffectTable.get(foodItem.getItemType());
        if (foodEffect == null) {
            return;
        }

        EntitySatisfactionModule satisfactionModule = ENTITY_SATISFACTION_MODULE_HANDLER.getModule(entity);
        if (satisfactionModule != null) {
            // 先填充饥饿度
            satisfactionModule.setFoodLevel(satisfactionModule.getFoodLevel() + foodEffect.getFoodLevel());
            // 再填充食物饱和度
            satisfactionModule.setFoodSaturationLevel(satisfactionModule.getFoodSaturationLevel() + foodEffect.getFoodSaturationLevel());

            if (entity instanceof Player) {
                Player player = (Player) entity;
                UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
                updateAttributesPacket.setEntityId(player.getRuntimeId());
                updateAttributesPacket.setAttributes(new EntityAttribute[]{satisfactionModule.getEntityHungerAttribute(), satisfactionModule.getEntitySaturationAttribute()});
                this.networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
            }
        }

        // 设置状态效果
        List<FoodEffectType> foodEffectTypes = foodEffect.getEffectTypes();
        for (FoodEffectType foodEffectType : foodEffectTypes) {
            if (foodEffectType.isValid()) {
                EffectBaseData effectBaseData = foodEffectType.getEffectBaseData();
                this.entityStateService.enableState(entity, effectBaseData.getEffectType().getName(), 1, -1, effectBaseData.getDuration() * 1000);
            }
        }

    }

    /**
     * 获取属性
     *
     * @param entity
     * @return
     */
    public EntityAttribute[] getHungerAttributes(Entity entity) {
        EntitySatisfactionModule satisfactionModule = ENTITY_SATISFACTION_MODULE_HANDLER.getModule(entity);
        if (satisfactionModule != null) {
            return new EntityAttribute[]{satisfactionModule.getEntityHungerAttribute(), satisfactionModule.getEntitySaturationAttribute()};
        }
        return new EntityAttribute[0];
    }
}
