package com.particle.game.block.trigger;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.block.trigger.components.EntityEnterBlockTriggerModule;
import com.particle.game.block.trigger.components.IEntityEnterBlockTrigger;
import com.particle.game.block.trigger.impl.CactusTrigger;
import com.particle.game.block.trigger.impl.FireTrigger;
import com.particle.game.block.trigger.impl.WaterTrigger;
import com.particle.game.utils.ecs.ECSComponentService;
import com.particle.game.world.physical.BlockColliderCheckService;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class EntityEnterBlockTriggerService {

    private static final ECSModuleHandler<EntityEnterBlockTriggerModule> ENTITY_ENTER_BLOCK_TRIGGER_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityEnterBlockTriggerModule.class);


    @Inject
    private ECSComponentService ecsComponentService;

    @Inject
    private BlockColliderCheckService blockColliderCheckService;

    // 注册触发器
    @Inject
    private FireTrigger fireTrigger;
    @Inject
    private WaterTrigger waterTrigger;
    @Inject
    private CactusTrigger cactusTrigger;

    public void initDetectComponent(Entity entity) {
        EntityEnterBlockTriggerModule entityEnterBlockTriggerModule = ENTITY_ENTER_BLOCK_TRIGGER_MODULE_HANDLER.bindModule(entity);
        entityEnterBlockTriggerModule.addTriger(BlockPrototype.FIRE, this.fireTrigger);
        entityEnterBlockTriggerModule.addTriger(BlockPrototype.LAVA, this.fireTrigger);
        entityEnterBlockTriggerModule.addTriger(BlockPrototype.FLOWING_LAVA, this.fireTrigger);
        entityEnterBlockTriggerModule.addTriger(BlockPrototype.WATER, this.waterTrigger);
        entityEnterBlockTriggerModule.addTriger(BlockPrototype.FLOWING_WATER, this.waterTrigger);
        entityEnterBlockTriggerModule.addTriger(BlockPrototype.CACTUS, this.cactusTrigger);
    }

    public void doDetect(Entity entity) {
        EntityEnterBlockTriggerModule entityEnterBlockTriggerModule = ENTITY_ENTER_BLOCK_TRIGGER_MODULE_HANDLER.getModule(entity);
        if (entityEnterBlockTriggerModule == null) {
            return;
        }

        // 记录时间戳
        long timestamp = System.currentTimeMillis();
        if (timestamp - entityEnterBlockTriggerModule.getLastTriggerTimestamp() < entityEnterBlockTriggerModule.getTriggerInterval()) {
            return;
        }

        // 缓存更新时间戳
        entityEnterBlockTriggerModule.setLastTriggerTimestamp(timestamp);

        // 获取碰撞方块
        Set<BlockPrototype> blockPrototypes = this.blockColliderCheckService.checkEntityColliderWithBlocks(entity);

        // 执行碰撞回调
        if (blockPrototypes != null) {
            for (BlockPrototype blockPrototype : blockPrototypes) {
                IEntityEnterBlockTrigger triger = entityEnterBlockTriggerModule.getTriger(blockPrototype);

                if (triger != null) {
                    triger.trigger(entity);
                }
            }
        }
    }

}
