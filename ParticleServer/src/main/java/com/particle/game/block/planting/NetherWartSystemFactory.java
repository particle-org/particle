package com.particle.game.block.planting;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.farming.NetherWartModule;
import com.particle.model.entity.component.farming.PlantGrowUpProgressModule;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NetherWartSystemFactory implements ECSSystemFactory<NetherWartSystemFactory.NetherWartSystem> {

    private static final Logger logger = LoggerFactory.getLogger(NetherWartSystemFactory.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);
    private static final ECSModuleHandler<PlantGrowUpProgressModule> PLANT_GROW_UP_PROGRESS_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlantGrowUpProgressModule.class);
    private static final ECSModuleHandler<NetherWartModule> NETHER_WART_MODULE_HANDLER = ECSModuleHandler.buildHandler(NetherWartModule.class);


    @Inject
    private LevelService levelService;

    @Inject
    private EntitySpawnService entitySpawnService;

    public class NetherWartSystem implements ECSSystem {

        private Entity entity;
        private PlantGrowUpProgressModule plantGrowupProgressModule;
        private NetherWartModule netherWartModule;
        private TransformModule transformModule;

        public NetherWartSystem(Entity entity) {
            this.entity = entity;
            this.plantGrowupProgressModule = PLANT_GROW_UP_PROGRESS_MODULE_HANDLER.getModule(entity);
            this.netherWartModule = NETHER_WART_MODULE_HANDLER.getModule(entity);
            this.transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            long now = System.currentTimeMillis();
            if (plantGrowupProgressModule.getFutureUpdateTime() < now) {
                Vector3 targetPosition = transformModule.getFloorPosition();
                Block targetBlock = levelService.getBlockAt(entity.getLevel(), targetPosition);

                if (targetBlock.getMeta() < netherWartModule.getMaxGrowMeta()) {
                    targetBlock.setMeta(targetBlock.getMeta() + 1);
                    levelService.setBlockAt(entity.getLevel(), targetBlock, targetPosition);
                }

                if (targetBlock.getMeta() >= netherWartModule.getMaxGrowMeta()
                        && netherWartModule.isRemoveTileEntityAfterMature()) {
                    entity.getLevel().getLevelSchedule().scheduleSimpleTask("netherWart_remove", () -> this.removeCropTileEntity(entity.getLevel(), targetPosition));
                    return;
                }

                // 设置下一次更新时间
                plantGrowupProgressModule.randomFutureUpdateTime();
            }
        }

        /**
         * 去除掉tileEntity
         *
         * @param level
         * @param targetPosition
         */
        private void removeCropTileEntity(Level level, Vector3 targetPosition) {
            // 去除其对应的tileEntity
            entitySpawnService.despawnTileEntity(level, targetPosition);
        }
    }


    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{
                NetherWartModule.class,
                PlantGrowUpProgressModule.class,
                TransformModule.class
        };
    }

    @Override
    public NetherWartSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new NetherWartSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<NetherWartSystem> getSystemClass() {
        return NetherWartSystem.class;
    }
}
