package com.particle.game.world.physical;

import com.particle.api.physical.IPhysicalServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.world.physical.modules.RigibodyModule;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.MetadataDataFlag;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PhysicalService implements IPhysicalServiceApi {

    private static final ECSModuleHandler<RigibodyModule> RIGIBODY_MODULE_HANDLER = ECSModuleHandler.buildHandler(RigibodyModule.class);

    @Inject
    private MetaDataService metaDataService;

    /**
     * 初始化重力
     *
     * @param entity
     * @param kinematic
     * @param enableGravity
     */
    @Override
    public void initPhysicalEffects(Entity entity, boolean kinematic, boolean enableGravity) {
        RigibodyModule rigibodyModule = RIGIBODY_MODULE_HANDLER.bindModule(entity);

        rigibodyModule.setKinematic(kinematic);
        rigibodyModule.setEnableGravity(enableGravity);

        this.metaDataService.setDataFlag(entity, MetadataDataFlag.DATA_FLAG_GRAVITY, enableGravity, false);
    }

    public void initPhysicalEffects(Entity entity, boolean kinematic, boolean enableGravity, float gravity) {
        RigibodyModule rigibodyModule = RIGIBODY_MODULE_HANDLER.bindModule(entity);

        rigibodyModule.setKinematic(kinematic);
        rigibodyModule.setEnableGravity(enableGravity);
        rigibodyModule.setGravity(gravity);

        this.metaDataService.setDataFlag(entity, MetadataDataFlag.DATA_FLAG_GRAVITY, enableGravity, false);
    }

    /**
     * 设置重力
     *
     * @param entity
     * @param state
     */
    public void enableGravity(Entity entity, boolean state) {
        RigibodyModule rigibodyModule = RIGIBODY_MODULE_HANDLER.getModule(entity);
        if (rigibodyModule == null) {
            return;
        }

        rigibodyModule.setEnableGravity(state);

        this.metaDataService.setDataFlag(entity, MetadataDataFlag.DATA_FLAG_GRAVITY, state, false);
    }

    /**
     * 查询重力
     *
     * @param entity
     * @return
     */
    public float getGravity(Entity entity) {
        RigibodyModule rigibodyModule = RIGIBODY_MODULE_HANDLER.getModule(entity);
        if (rigibodyModule == null) {
            return 0;
        }

        return rigibodyModule.getGravity();
    }

    public boolean isKinematic(Entity entity) {
        RigibodyModule rigibodyModule = RIGIBODY_MODULE_HANDLER.getModule(entity);
        if (rigibodyModule != null) {
            return rigibodyModule.isKinematic();
        }

        return false;
    }
}
