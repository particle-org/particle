package com.particle.game.entity.link;

import com.particle.api.entity.IEntityMountControlServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.model.entity.Entity;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector2f;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EntityMountControlService implements IEntityMountControlServiceApi {

    private static final ECSModuleHandler<EntityPassengerModule> ENTITY_PASSENGER_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityPassengerModule.class);
    private static final ECSModuleHandler<EntityVehicleModule> ENTITY_VEHICLE_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityVehicleModule.class);
    private static final ECSModuleHandler<MountModule> MOUNT_COMPONENT_HANDLER = ECSModuleHandler.buildHandler(MountModule.class);


    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private EntityLinkServiceProxy entityLinkServiceProxy;

    @Inject
    private PositionService positionService;

    /**
     * 初始化可骑乘数据的属性数据
     *
     * @param entity
     */
    @Override
    public void boundMountEntity(Player player, Entity entity) {
        // 初始化组件
        MountModule mountModule = MOUNT_COMPONENT_HANDLER.bindModule(entity);
        mountModule.setOwn(player);
    }

    /**
     * 设置生物的目标
     *
     * @param player
     * @param aim
     */
    public void setAim(Player player, Vector2f aim) {
        EntityPassengerModule entityPassengerModule = ENTITY_PASSENGER_MODULE_HANDLER.getModule(player);
        if (entityPassengerModule != null) {
            entityPassengerModule.setAim(aim);
        }
    }

    /**
     * 获取骑乘者的目标
     *
     * @param entity
     * @return
     */
    public Direction getRiderAim(Entity entity) {
        // 获取骑乘者
        Entity riderEntity = this.entityLinkServiceProxy.getRiderEntity(entity);

        // 获取骑乘者的指令
        EntityPassengerModule entityPassengerModule = ENTITY_PASSENGER_MODULE_HANDLER.getModule(riderEntity);

        if (entityPassengerModule != null) {
            Vector2f aim = entityPassengerModule.getAim();

            // 没有移动操作
            if (aim.getY() == 0 && aim.getX() == 0) {
                return null;
            }

            // 获取骑乘者的朝向
            Direction direction = this.positionService.getDirection(riderEntity);

            // 计算目标位移
            float x = aim.getX();
            float y = aim.getY();
            if (y == 1) {
                return direction;
            } else if (y == -1) {
                // 这里策划要求往后需要平移，而不是朝正后方移动，所以有了以下代码
                direction = direction.backend();
                direction.setPitch(0);
                return direction;
            } else if (x == -1) {
                return direction.left(90);
            } else if (x == 1) {
                return direction.right(90);
            } else if (y > 0 && x > 0) {
                return direction.right(45);
            } else if (y > 0 && x < 0) {
                return direction.left(45);
            }
        }

        return null;
    }

    /**
     * 获取骑乘者
     *
     * @param entity
     * @return
     */
    public Entity getOwner(Entity entity) {
        MountModule mountModule = MOUNT_COMPONENT_HANDLER.getModule(entity);

        if (mountModule != null) {
            return mountModule.getOwn();
        }

        return null;
    }

}
