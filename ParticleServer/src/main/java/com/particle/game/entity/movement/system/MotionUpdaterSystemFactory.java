package com.particle.game.entity.movement.system;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.core.ecs.system.IntervalECSSystem;
import com.particle.game.entity.movement.module.EntityMovementModule;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.data.SetEntityMotionPacket;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MotionUpdaterSystemFactory implements ECSSystemFactory<MotionUpdaterSystemFactory.MotionUpdaterSystem> {

    private static final ECSModuleHandler<EntityMovementModule> ENTITY_MOVEMENT_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMovementModule.class);

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{EntityMovementModule.class};
    }

    @Override
    public MotionUpdaterSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new MotionUpdaterSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<MotionUpdaterSystem> getSystemClass() {
        return MotionUpdaterSystem.class;
    }

    public class MotionUpdaterSystem extends IntervalECSSystem {

        private Entity entity;
        private EntityMovementModule entityMovementModule;

        public MotionUpdaterSystem(Entity entity) {
            this.entity = entity;
            this.entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.getModule(entity);
        }

        @Override
        protected int getInterval() {
            return 1;
        }

        @Override
        protected void doTick(long deltaTime) {
            Vector3f motion = this.entityMovementModule.getMotion();

            if (!this.entityMovementModule.getLastMotion().equals(motion)) {
                SetEntityMotionPacket setEntityMotionPacket = new SetEntityMotionPacket();
                setEntityMotionPacket.setEid(entity.getRuntimeId());
                setEntityMotionPacket.setMotionX(motion.getX() / 20);
                setEntityMotionPacket.setMotionY(motion.getY() / 20);
                setEntityMotionPacket.setMotionZ(motion.getZ() / 20);

                broadcastServiceProxy.broadcast(entity, setEntityMotionPacket, true);

                this.entityMovementModule.setLastMotion(motion);
            }
        }
    }
}
