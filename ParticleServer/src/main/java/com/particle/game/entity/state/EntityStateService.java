package com.particle.game.entity.state;

import com.google.inject.Injector;
import com.particle.api.entity.IEntityStateServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.state.handle.*;
import com.particle.game.entity.state.handle.effect.CommonEffectHandle;
import com.particle.game.entity.state.handle.effect.InvisibilityEffectHandle;
import com.particle.game.entity.state.handle.particle.CrucibleReactParticleStateHandle;
import com.particle.game.entity.state.model.EntityStateType;
import com.particle.model.effect.EffectBaseType;
import com.particle.model.entity.Entity;
import com.particle.model.entity.type.EntityStateRecorder;
import com.particle.model.math.Vector3;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Singleton
public class EntityStateService implements IEntityStateServiceApi {

    private static final ECSModuleHandler<EntityStateModule> ENTITY_STATE_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityStateModule.class);

    private static final long DEFAULT_UPDATE_INTERVAL = 500;
    private static final long DEFAULT_TTL = -1;

    @Inject
    private EmptyHandle emptyHandle;

    public void init(Injector injector) {
        // 初始化生物状态处理器映射关系

        // ------------------- 生物的基础状态 -------------------------

        // ------------------- 生物AI的状态 -------------------------
        EntityStateRecorderService.register(EntityStateType.ESTRUS_STATUS.getName(), injector.getInstance(EstrusStatusHandle.class));

        // ------------------- 生物MetaData的状态 -------------------------
        EntityStateRecorderService.register(EntityStateType.SHEARED.getName(), injector.getInstance(EntityShearedHandle.class));
        EntityStateRecorderService.register(EntityStateType.ON_FIRE.getName(), injector.getInstance(EntityOnFireStatusHandle.class));

        EntityStateRecorderService.register(OnFireDamageEntityHandle.KEY, injector.getInstance(OnFireDamageEntityHandle.class));
        EntityStateRecorderService.register(EntitySmallScaleHandle.KEY, injector.getInstance(EntitySmallScaleHandle.class));
        EntityStateRecorderService.register(EntityBigScaleHandle.KEY, injector.getInstance(EntityBigScaleHandle.class));

        // ------------------- 粒子效果 ------------------------
        EntityStateRecorderService.register("EntityDustParticle", injector.getInstance(CrucibleReactParticleStateHandle.class));

        // ------------------- 药水 --------------------------
        EntityStateRecorderService.register(EntityStateType.SPEED.getName(), injector.getInstance(EntitySpeedHandle.class));
        EntityStateRecorderService.register(EntityStateType.SLOWNESS.getName(), injector.getInstance(EntitySlownessHandle.class));
        EntityStateRecorderService.register(EntityStateType.HASTE.getName(), CommonEffectHandle.newCommonEffect(injector, EffectBaseType.HASTE, "急迫"));
        EntityStateRecorderService.register(EntityStateType.FATIGUE.getName(), CommonEffectHandle.newCommonEffect(injector, EffectBaseType.FATIGUE, "疲劳"));
        EntityStateRecorderService.register(EntityStateType.STRENGTH.getName(), CommonEffectHandle.newCommonEffect(injector, EffectBaseType.STRENGTH, "力量"));
        EntityStateRecorderService.register(EntityStateType.LEAPING.getName(), CommonEffectHandle.newCommonEffect(injector, EffectBaseType.LEAPING, "跳跃"));
        EntityStateRecorderService.register(EntityStateType.NAUSEA.getName(), CommonEffectHandle.newCommonEffect(injector, EffectBaseType.NAUSEA, "恶心"));
        EntityStateRecorderService.register(EntityStateType.REGENERATION.getName(), CommonEffectHandle.newCommonEffect(injector, EffectBaseType.REGENERATION, "生命恢复"));
        EntityStateRecorderService.register(EntityStateType.DAMAGE_RESISTANCE.getName(), CommonEffectHandle.newCommonEffect(injector, EffectBaseType.DAMAGE_RESISTANCE, "伤害抗性"));
        EntityStateRecorderService.register(EntityStateType.FIRE_RESISTANCE.getName(), CommonEffectHandle.newCommonEffect(injector, EffectBaseType.FIRE_RESISTANCE, "火焰抗性"));
        EntityStateRecorderService.register(EntityStateType.WATER_BREATHING.getName(), CommonEffectHandle.newCommonEffect(injector, EffectBaseType.WATER_BREATHING, "水下呼吸"));
        EntityStateRecorderService.register(EntityStateType.INVISIBILITY.getName(), injector.getInstance(InvisibilityEffectHandle.class));
        EntityStateRecorderService.register(EntityStateType.BLINDNESS.getName(), CommonEffectHandle.newCommonEffect(injector, EffectBaseType.BLINDNESS, "致盲"));
        EntityStateRecorderService.register(EntityStateType.NIGHT_VISION.getName(), CommonEffectHandle.newCommonEffect(injector, EffectBaseType.NIGHT_VISION, "夜视"));
        EntityStateRecorderService.register(EntityStateType.HUNGER.getName(), CommonEffectHandle.newCommonEffect(injector, EffectBaseType.HUNGER, "饥饿"));
        EntityStateRecorderService.register(EntityStateType.WEAKNESS.getName(), CommonEffectHandle.newCommonEffect(injector, EffectBaseType.WEAKNESS, "虚弱"));
        EntityStateRecorderService.register(EntityStateType.POISON.getName(), CommonEffectHandle.newCommonEffect(injector, EffectBaseType.POISON, "中毒"));
        EntityStateRecorderService.register(EntityStateType.WITHER.getName(), CommonEffectHandle.newCommonEffect(injector, EffectBaseType.WITHER, "凋零"));
        EntityStateRecorderService.register(EntityStateType.HEALTH_BOOST.getName(), CommonEffectHandle.newCommonEffect(injector, EffectBaseType.HEALTH_BOOST, "生命恢复"));
        EntityStateRecorderService.register(EntityStateType.ABSORPTION.getName(), CommonEffectHandle.newCommonEffect(injector, EffectBaseType.ABSORPTION, "伤害吸收"));
    }

    /**
     * 初始化生物的状态组件
     *
     * @param entity
     */
    public void initEntityStateComponent(Entity entity) {
        ENTITY_STATE_MODULE_HANDLER.bindModule(entity);
    }

    /**
     * 设置生物状态（使用预制的状态）
     *
     * @param entity
     * @param name
     */
    public void enableState(Entity entity, String name) {
        this.enableState(entity, name, 0, DEFAULT_UPDATE_INTERVAL, DEFAULT_TTL);
    }

    public void enableState(Entity entity, String name, int level) {
        this.enableState(entity, name, level, DEFAULT_UPDATE_INTERVAL, DEFAULT_TTL);
    }

    public void enableState(Entity entity, String name, long updateInterval, long ttl) {
        this.enableState(entity, name, 0, updateInterval, ttl);
    }

    public void enableState(Entity source, Entity entity, String name, long updateInterval, long ttl) {
        this.enableState(source, entity, name, 0, updateInterval, ttl);
    }

    /**
     * 设置生物状态（使用自定义状态）
     *
     * @param entity
     * @param name
     */
    @Override
    public void enableState(Entity entity, String name, int level, long updateInterval, long ttl) {
        // 查找Component
        EntityStateModule entityStateModule = ENTITY_STATE_MODULE_HANDLER.getModule(entity);
        if (entityStateModule == null) {
            entityStateModule = ENTITY_STATE_MODULE_HANDLER.bindModule(entity);
        }

        EntityStateHandle entityStateHandle = EntityStateRecorderService.get(name);
        if (entityStateHandle != null) {
            boolean isEnable = true;
            EntityStateRecorder entityStateRecorder = entityStateModule.getState(name);
            if (entityStateRecorder == null) {
                // 设置状态
                entityStateRecorder = new EntityStateRecorder();
                isEnable = false;
            }

            entityStateRecorder.setName(name);
            entityStateRecorder.setLevel(level);
            entityStateRecorder.setEnableTimestamp(System.currentTimeMillis());
            entityStateRecorder.setUpdateInterval(updateInterval);
            entityStateRecorder.setTimeToLive(ttl);
            entityStateRecorder.setEnabledCount(1);
            entityStateModule.setEntityState(name, entityStateRecorder);

            // tick组件
            entityStateHandle.onStateEnabled(entity, entityStateRecorder, isEnable || entityStateModule.hasBindState(name));
        }
    }

    /**
     * 设置生物状态含來源（使用自定义状态）
     *
     * @param entity
     * @param name
     */
    @Override
    public void enableState(Entity source, Entity entity, String name, int level, long updateInterval, long ttl) {
        // 查找Component
        EntityStateModule entityStateModule = ENTITY_STATE_MODULE_HANDLER.getModule(entity);
        if (entityStateModule == null) {
            entityStateModule = ENTITY_STATE_MODULE_HANDLER.bindModule(entity);
        }

        EntityStateHandle entityStateHandle = EntityStateRecorderService.get(name);
        if (entityStateHandle != null) {
            boolean isEnable = true;
            EntityStateRecorder entityStateRecorder = entityStateModule.getState(name);
            if (entityStateRecorder == null) {
                // 设置状态
                entityStateRecorder = new EntityStateRecorder();
                isEnable = false;
            }

            entityStateRecorder.setName(name);
            entityStateRecorder.setLevel(level);
            entityStateRecorder.setEnableTimestamp(System.currentTimeMillis());
            entityStateRecorder.setUpdateInterval(updateInterval);
            entityStateRecorder.setTimeToLive(ttl);
            entityStateRecorder.setEnabledCount(1);
            entityStateModule.setEntityState(name, entityStateRecorder);

            // tick组件
            entityStateHandle.onStateEnabled(source, entity, entityStateRecorder, isEnable || entityStateModule.hasBindState(name));
        }
    }

    public void bindState(Entity entity, String name) {
        // 查找Component
        EntityStateModule entityStateModule = ENTITY_STATE_MODULE_HANDLER.getModule(entity);
        if (entityStateModule == null) {
            entityStateModule = ENTITY_STATE_MODULE_HANDLER.bindModule(entity);
        }

        EntityStateHandle entityStateHandle = EntityStateRecorderService.get(name);
        if (entityStateHandle != null) {
            // 设置状态
            EntityStateRecorder entityStateRecorder = entityStateModule.bindState(name, 1);
            // tick组件
            entityStateHandle.onStateEnabled(entity, entityStateRecorder, entityStateModule.hasState(name));
        }
    }

    /**
     * 取消生物状态
     *
     * @param entity
     * @param name
     */
    @Override
    public void disableState(Entity entity, String name) {
        // 查找Component
        EntityStateModule entityStateModule = ENTITY_STATE_MODULE_HANDLER.getModule(entity);
        if (entityStateModule == null) {
            return;
        }

        // 取消生物状态
        EntityStateRecorder entityStateRecorder = entityStateModule.removeEntityState(name);

        // tick组件
        if (entityStateRecorder != null) {
            EntityStateRecorderService.get(entityStateRecorder.getName()).onStateDisabled(entity, entityStateRecorder, false);
        }
    }

    public void unbindState(Entity entity, String name) {
        // 查找Component
        EntityStateModule entityStateModule = ENTITY_STATE_MODULE_HANDLER.getModule(entity);
        if (entityStateModule == null) {
            return;
        }

        // 取消生物状态
        EntityStateRecorder entityStateRecorder = entityStateModule.unbindState(name, 1);

        // tick组件
        if (entityStateRecorder != null) {
            EntityStateHandle entityStateHandle = EntityStateRecorderService.get(entityStateRecorder.getName());
            if (entityStateHandle != null) {
                entityStateHandle.onStateDisabled(entity, entityStateRecorder, entityStateModule.hasState(name));
            }
        }
    }

    public void disableAllState(Entity entity) {
        // 查找Component
        EntityStateModule entityStateModule = ENTITY_STATE_MODULE_HANDLER.getModule(entity);
        if (entityStateModule == null) {
            return;
        }

        // 取消生物状态
        for (EntityStateRecorder entityStateRecorder : entityStateModule.clearEntityStateRecorders()) {
            EntityStateRecorderService.get(entityStateRecorder.getName()).onStateDisabled(entity, entityStateRecorder, false);
        }
    }

    /**
     * 判断生物是否有指定状态
     *
     * @param entity
     * @param name
     * @return
     */
    @Override
    public boolean hasState(Entity entity, String name) {
        // 查找Component
        EntityStateModule entityStateModule = ENTITY_STATE_MODULE_HANDLER.getModule(entity);
        if (entityStateModule == null) {
            return false;
        }

        return entityStateModule.hasState(name);
    }

    public EntityStateRecorder getState(Entity entity, String name) {
        // 查找Component
        EntityStateModule entityStateModule = ENTITY_STATE_MODULE_HANDLER.getModule(entity);
        if (entityStateModule == null) {
            return null;
        }

        return entityStateModule.getState(name);
    }

    /**
     * 状态应用在方块上
     *
     * @param entity
     * @param position
     * @param name
     */
    public void enableStateOnBlock(Entity entity, Vector3 position, String name) {
        EntityStateRecorderService.get(name).onStateHitBlock(entity, position);
    }

    /**
     * Tick 生物组件
     *
     * @param entity
     */
    public void tickEntityState(Entity entity, EntityStateModule entityStateModule) {
        // 缓存时间戳
        long timestamp = System.currentTimeMillis();

        // 检查是否Module第一次加载，需要启用
        if (!entityStateModule.hasFirstEnabled()) {
            entityStateModule.enableFirstTick();

            for (Map.Entry<String, EntityStateRecorder> entityStateRecorderEntry : entityStateModule.getEntityState().entrySet()) {
                EntityStateRecorder entityStateRecorder = entityStateRecorderEntry.getValue();
                EntityStateRecorderService.get(entityStateRecorder.getName()).onStateEnabled(entity, entityStateRecorder, false);
            }

            for (Map.Entry<String, EntityStateRecorder> entityStateRecorderEntry : entityStateModule.getBindState().entrySet()) {
                EntityStateRecorder entityStateRecorder = entityStateRecorderEntry.getValue();
                int count = entityStateRecorder.getEnabledCount();
                for (int i = 0; i < count; i++) {
                    entityStateRecorder.setEnabledCount(i + 1);
                    EntityStateRecorderService.get(entityStateRecorder.getName()).onStateEnabled(entity, entityStateRecorder, false);
                }

            }
        }

        // Tick组件
        List<EntityStateRecorder> removedRecorder = null;
        for (Map.Entry<String, EntityStateRecorder> entityStateRecorderEntry : entityStateModule.getEntityState().entrySet()) {
            EntityStateRecorder entityStateRecorder = entityStateRecorderEntry.getValue();

            // 检查组件有效性
            if (entityStateRecorder.getTimeToLive() != -1) {
                if (timestamp - entityStateRecorder.getEnableTimestamp() > entityStateRecorder.getTimeToLive()) {
                    if (removedRecorder == null) {
                        removedRecorder = new LinkedList<>();
                    }

                    entityStateRecorder.setEnabledCount(0);
                    removedRecorder.add(entityStateRecorder);
                    continue;
                }
            }

            // 执行tick操作
            if (entityStateRecorder.getUpdateInterval() != -1 && timestamp - entityStateRecorder.getLastUpdatedTimestamp() > entityStateRecorder.getUpdateInterval()) {
                EntityStateRecorderService.get(entityStateRecorder.getName()).onStateUpdated(entity, entityStateRecorder);
                entityStateRecorder.setLastUpdatedTimestamp(timestamp);
            }
        }

        // 清除过期组件
        if (removedRecorder != null) {
            for (EntityStateRecorder entityStateRecorder : removedRecorder) {
                this.disableState(entity, entityStateRecorder.getName());
            }
        }

        // Tick绑定的数据
        for (Map.Entry<String, EntityStateRecorder> entityStateRecorderEntry : entityStateModule.getBindState().entrySet()) {
            EntityStateRecorder entityStateRecorder = entityStateRecorderEntry.getValue();

            // 执行tick操作
            if (entityStateRecorder.getUpdateInterval() != -1 && timestamp - entityStateRecorder.getLastUpdatedTimestamp() > entityStateRecorder.getUpdateInterval()) {
                EntityStateRecorderService.get(entityStateRecorder.getName()).onStateUpdated(entity, entityStateRecorder);
                entityStateRecorder.setLastUpdatedTimestamp(timestamp);
            }
        }
    }

}
