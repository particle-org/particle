package com.particle.game.entity.attribute.fossilization;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.core.ecs.system.IntervalECSSystem;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.EntityService;
import com.particle.game.ui.TitleService;
import com.particle.model.entity.Entity;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EntityFossilizationSystemFactory implements ECSSystemFactory<EntityFossilizationSystemFactory.EntityFossilizationSystem> {

    @Inject
    private TitleService titleService;

    @Inject
    private EntityService entityService;

    @Inject
    private PositionService positionService;

    private static final Logger logger = LoggerFactory.getLogger(EntityFossilizationSystemFactory.class);

    private static final Class[] CLS = new Class[]{EntityFossilizationModule.class};

    private static final ECSModuleHandler<EntityFossilizationModule> MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityFossilizationModule.class);

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return CLS;
    }

    @Override
    public EntityFossilizationSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Player) {
            return new EntityFossilizationSystem((Player) gameObject);
        }
        return null;
    }

    @Override
    public Class<EntityFossilizationSystem> getSystemClass() {
        return EntityFossilizationSystem.class;
    }

    public class EntityFossilizationSystem extends IntervalECSSystem {
        private final Entity entity;
        private EntityFossilizationModule module;

        public EntityFossilizationSystem(Entity entity) {
            this.entity = entity;
            this.module = MODULE_HANDLER.getModule(entity);
        }

        @Override
        protected int getInterval() {
            return 5;
        }

        @Override
        protected void doTick(long deltaTime) {
            if (!module.isFossilization()) {
                return;
            }
            if (module.isEndFossilization()) {
                module.relieveFossilization();
                // 清除之前的actionBar
                titleService.setActionBar((Player) entity, "§a解除石化", 3);
                return;
            }

            long fossilizationEndTime = module.getFossilizationEndTime();
            long curTime = System.currentTimeMillis();

            long leftTime = curTime - fossilizationEndTime;

            String actionBar = "";
            if (leftTime % 1000 < 333) {
                actionBar = "§c石化中...";
            } else if (leftTime % 1000 < 666) {
                actionBar = "§c石化中..";
            } else {
                actionBar = "§c石化中.";
            }
            titleService.setActionBar((Player) entity, actionBar, 3);
        }
    }
}