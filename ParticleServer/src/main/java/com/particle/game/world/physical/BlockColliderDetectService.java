package com.particle.game.world.physical;

import com.particle.api.block.BlockColliderDetectServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.world.physical.modules.BoxColliderModule;
import com.particle.game.world.physical.modules.PointColliderModule;
import com.particle.game.world.physical.service.BlockColliderDetectTool;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

import javax.inject.Singleton;

@Singleton
public class BlockColliderDetectService implements BlockColliderDetectServiceApi {

    private static final ECSModuleHandler<BoxColliderModule> BOX_COLLIDER_MODULE_HANDLER = ECSModuleHandler.buildHandler(BoxColliderModule.class);
    private static final ECSModuleHandler<PointColliderModule> POINT_COLLIDER_MODULE_HANDLER = ECSModuleHandler.buildHandler(PointColliderModule.class);

    public boolean checkEntityPosition(Entity entity, Vector3f targetPosition) {
        // 防止精度計算問題
        targetPosition.setY(targetPosition.getY() + 0.0001f);

        BoxColliderModule boxColliderModule = BOX_COLLIDER_MODULE_HANDLER.getModule(entity);
        if (boxColliderModule != null) {
            return BlockColliderDetectTool.checkPosition(entity.getLevel(), targetPosition, boxColliderModule.getAABBCollider());
        }

        PointColliderModule pointCollider = POINT_COLLIDER_MODULE_HANDLER.getModule(entity);
        if (pointCollider != null) {
            return BlockColliderDetectTool.checkPosition(entity.getLevel(), targetPosition, pointCollider.getPointCollider()) != null;
        }

        return false;
    }


    /**
     * 检测是否在地面上
     *
     * @param entity
     * @return
     */
    public boolean isStandOnBlock(Entity entity, Vector3f position) {
        BoxColliderModule boxColliderModule = BOX_COLLIDER_MODULE_HANDLER.getModule(entity);
        if (boxColliderModule != null) {
            return BlockColliderDetectTool.isStandOnBlock(entity, position, boxColliderModule.getAABBCollider());
        }

        PointColliderModule pointCollider = POINT_COLLIDER_MODULE_HANDLER.getModule(entity);
        if (pointCollider != null) {
            return BlockColliderDetectTool.isStandOnBlock(entity, position, pointCollider.getPointCollider());
        }

        return true;
    }
}
