package com.particle.game.entity.ai.leaf.action.attack;

import com.particle.api.ai.EntityDecisionServiceApi;
import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.attack.EntityRemoteAttackService;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.spawn.AutoRemovedModule;
import com.particle.game.world.physical.PhysicalService;
import com.particle.model.entity.Entity;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.math.Vector3f;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class EntityMultiShootTarget implements IAction {

    private static Logger LOGGER = LoggerFactory.getLogger(EntityMultiShootTarget.class);

    @Inject
    private EntityDecisionServiceApi entityDecisionServiceApi;

    @Inject
    private EntityRemoteAttackService entityRemoteAttackService;

    @Inject
    private PositionService positionService;

    @Inject
    private PhysicalService physicalService;

    private static final ECSModuleHandler<AutoRemovedModule> AUTO_REMOVED_MODULE_HANDLER = ECSModuleHandler.buildHandler(AutoRemovedModule.class);

    private ItemStack weapon = ItemStack.getItem(ItemPrototype.ARROW);

    private float shootPower = 15;
    private int count = 10;

    @Override
    public void onInitialize() {
    }

    @Override
    public EStatus tick(Entity entity) {
        Entity entityTarget = this.entityDecisionServiceApi.getKnowledge(entity, "EntityTarget", Entity.class);

        if (entityTarget != null) {
            Vector3f targetPosition = this.positionService.getPosition(entityTarget).add(0, 1.5f, 0);

            Vector3f startPosition = this.positionService.getPosition(entity).add(0, 1.5f, 0);

            if (this.entityRemoteAttackService.entityRemoteAttackCheck(entity)) {
                for (int i = 0; i < count; ++i) {
                    float x = RandomUtils.nextFloat(0.0f, 6F) - 3F;
                    float y = RandomUtils.nextFloat(0.0f, 6F) - 3F;
                    float z = RandomUtils.nextFloat(0.0f, 6F) - 3F;

                    Vector3f newStartPosition = startPosition.add(x, y, z);

                    Vector3f motion = this.caculateShootDirection(newStartPosition, targetPosition, shootPower, physicalService.getGravity(entity)).clone();
                    motion.multiply(2f);

                    Entity projectileEntity = this.entityRemoteAttackService.projectileShoot(entity, weapon, motion);
                    AUTO_REMOVED_MODULE_HANDLER.bindModule(projectileEntity);
                }
            }
        }

        return EStatus.SUCCESS;
    }

    @Override
    public void onTicked(Entity entity, EStatus status) {
    }

    @Override
    public void onRelease() {
    }

    @Override
    public void config(String key, Object val) {
        if (key.equals("Weapon") && val instanceof String) {
            ItemStack item = ItemStack.getItem((String) val);
            if (item == null) {
                LOGGER.error("Weapon config fail, {} not exist!", (String) val);
            } else {
                this.weapon = item;
            }
        }
        if (key.equals("ShootPower") && val instanceof Float) {
            this.shootPower = (float) val;
        }
        if (key.equalsIgnoreCase("Count") && val instanceof Integer) {
            this.count = (int) val;
        }
    }

    /**
     * 计算抛射物射击方向
     *
     * @param start
     * @param destination
     * @param s
     * @return
     */
    private Vector3f caculateShootDirection(Vector3f start, Vector3f destination, float s, float gravity) {
        Vector3f direction = destination.subtract(start);
        double distance = direction.length();

        float offset = (float) (distance / 3 - s / 10);
        if (offset > 0)
            direction.setY(direction.getY() + offset * 2 * gravity);

        return direction.normalize().multiply(s);
    }
}
