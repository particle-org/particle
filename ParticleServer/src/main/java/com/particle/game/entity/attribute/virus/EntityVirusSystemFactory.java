package com.particle.game.entity.attribute.virus;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.core.ecs.system.IntervalECSSystem;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.EntityService;
import com.particle.game.ui.TitleService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.others.MonsterEntity;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class EntityVirusSystemFactory implements ECSSystemFactory<EntityVirusSystemFactory.EntityVirusSystem> {

    @Inject
    private TitleService titleService;

    @Inject
    private EntityService entityService;

    @Inject
    private PositionService positionService;

    private static final Logger logger = LoggerFactory.getLogger(EntityVirusSystemFactory.class);

    private static final Class[] CLS = new Class[]{EntityVirusModule.class};
    private static final ECSModuleHandler<EntityVirusModule> MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityVirusModule.class);

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return CLS;
    }

    @Override
    public EntityVirusSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Player) {
            return new EntityVirusSystem((Player) gameObject);
        }
        return null;
    }

    @Override
    public Class<EntityVirusSystem> getSystemClass() {
        return EntityVirusSystem.class;
    }

    public class EntityVirusSystem extends IntervalECSSystem {
        private final Entity entity;
        private EntityVirusModule module;

        public EntityVirusSystem(Entity entity) {
            this.entity = entity;
            this.module = MODULE_HANDLER.getModule(entity);
        }

        @Override
        protected int getInterval() {
            return 5;
        }

        @Override
        protected void doTick(long deltaTime) {
            if (!module.isInfectVirous()) {
                return;
            }

            if (entity instanceof Player) {
                long curTime = System.currentTimeMillis();
                int passTime = Long.valueOf(curTime - module.getLastRefreshTime()).intValue();

                Player player = (Player) entity;

                Vector3f position = positionService.getPosition(player);
                List<MonsterEntity> monsterEntities = entityService.getNearMonsterEntities(player.getLevel(), position, module.getInfectDistance());

                // 是否摆脱感染
                boolean isRidInfect = true;

                // 检测附近有没有传染源
                for (Entity entity : monsterEntities) {
                    if (entity.getRuntimeId() == module.getSourceEntityId()) {
                        isRidInfect = false;
                        break;
                    }
                }

                if (isRidInfect) {
                    module.setInfectVirous(false);
                    module.setSourceEntityId(-1);
                    module.setInfectDistance(0);
                    module.setVirusValue(0);
                    module.setRefreshInterval(Integer.MAX_VALUE);
                    module.setInfectSpeed(0);
                    module.setLastRefreshTime(System.currentTimeMillis());
                    module.setMaxVirusValue(0);

                    // 清除之前的actionBar
                    titleService.setActionBar((Player) entity, "§2§l已解除感染", 3);

                } else {
                    if (passTime >= module.getRefreshInterval()) {
                        module.addVirusValue(module.getInfectSpeed());
                        module.setLastRefreshTime(curTime);
                    }
                    titleService.setActionBar((Player) entity, getActionBar(module.getVirusValue(), module.getMaxVirusValue()), 25);
                }
            }
        }

        private String getActionBar(int virusValue, int maxVirusValue) {
            StringBuilder strb = new StringBuilder();
            strb.append("§4【病毒感染】 §4");
            for (int i = 0; i < virusValue; i++) {
                strb.append("▌");
            }
            if (virusValue < maxVirusValue) {
                strb.append("§6");
                for (int i = virusValue; i < maxVirusValue; ++i) {
                    strb.append("▌");
                }
            }
            return strb.toString();
        }
    }
}