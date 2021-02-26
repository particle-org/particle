package com.particle.game.block.planting;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.farming.CocoaModule;
import com.particle.model.math.Vector3;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Random;

@Singleton
public class CocoaSystemFactory implements ECSSystemFactory<CocoaSystemFactory.CocoaSystem> {

    private static final ECSModuleHandler<CocoaModule> COCOA_MODULE_HANDLER = ECSModuleHandler.buildHandler(CocoaModule.class);

    @Inject
    private LevelService levelService;

    @Inject
    private PositionService positionService;

    public class CocoaSystem implements ECSSystem {

        private Entity entity;
        private CocoaModule cocoaModule;

        public CocoaSystem(Entity entity) {
            this.entity = entity;
            this.cocoaModule = COCOA_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            Vector3 targetPosition = new Vector3(positionService.getPosition(entity));
            Block targetBlock = levelService.getBlockAt(entity.getLevel(), targetPosition);
            int meta = targetBlock.getMeta();
            if (meta >= 8) {
                return;
            }
            long now = System.currentTimeMillis();
            long duration = now - this.cocoaModule.getLastGrowTime();
            if (duration > 30 * 1000) {
                if (new Random().nextInt(2) == 1) {
                    meta += 4;
                    targetBlock.setMeta(meta);
                    levelService.setBlockAt(entity.getLevel(), targetBlock, targetPosition);
                }
                this.cocoaModule.setLastGrowTime(now);
            }
        }
    }

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{CocoaModule.class};
    }

    @Override
    public CocoaSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new CocoaSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<CocoaSystem> getSystemClass() {
        return CocoaSystem.class;
    }
}
