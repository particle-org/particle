package com.particle.game.entity.ai.leaf.action.attack;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.attack.EntityRemoteAttackService;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.spawn.AutoRemovedModule;
import com.particle.game.world.physical.PhysicalService;
import com.particle.model.entity.Entity;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.math.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class EntityShootTarget implements IAction {

    private static Logger LOGGER = LoggerFactory.getLogger(EntityShootTarget.class);

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private EntityRemoteAttackService entityRemoteAttackService;

    @Inject
    private PositionService positionService;

    @Inject
    private PhysicalService physicalService;

    private static final ECSModuleHandler<AutoRemovedModule> AUTO_REMOVED_MODULE_HANDLER = ECSModuleHandler.buildHandler(AutoRemovedModule.class);

    private ItemStack weapon = ItemStack.getItem(ItemPrototype.ARROW);
    private float shootPower = 15;
    private float powerGenerate = 0.5f;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Entity entityTarget = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_TARGET, Entity.class);

        if (entityTarget != null) {
            Float power = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.REMOTE_ATTACK_POWER, Float.class);
            if (power == null) power = 0f;

            // 检测蓄力状态
            if (power < shootPower) {
                power += powerGenerate;
                this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.REMOTE_ATTACK_POWER, power);

                return EStatus.SUCCESS;
            }

            Vector3f startPosition = this.positionService.getPosition(entity).add(0, 1.5f, 0);
            Vector3f targetPosition = this.positionService.getPosition(entityTarget).add(0, 1.5f, 0);

            if (this.entityRemoteAttackService.entityRemoteAttackCheck(entity)) {
                Entity projectileEntity = this.entityRemoteAttackService.projectileShoot(entity, weapon, this.caculateShootDirection(startPosition, targetPosition, shootPower, this.physicalService.getGravity(entity)));
                AUTO_REMOVED_MODULE_HANDLER.bindModule(projectileEntity);
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
        } else if (key.equals("ShootPower") && val instanceof Float) {
            this.shootPower = (float) val;
        } else if (key.equals("PowerGenerate") && val instanceof Float) {
            this.powerGenerate = (float) val;
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
