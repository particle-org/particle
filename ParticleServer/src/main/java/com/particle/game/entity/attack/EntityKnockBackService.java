package com.particle.game.entity.attack;

import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.item.ItemAttributeService;
import com.particle.game.world.physical.ForbidColliderService;
import com.particle.model.entity.Entity;
import com.particle.model.item.ItemStack;
import com.particle.model.item.enchantment.Enchantments;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EntityKnockBackService {

    @Inject
    private PositionService positionService;

    @Inject
    private MetaDataService metaDataService;

    @Inject
    private MovementServiceProxy movementServiceProxy;

    @Inject
    private ForbidColliderService colliderConditionService;

    /**
     * 计算击退
     *
     * @param entity
     * @return
     */
    public Vector3f getCloseCombatKnockback(Entity entity, Entity victim, ItemStack weapon) {
        if (colliderConditionService.isForbidKinckback(victim)) {
            return new Vector3f(0f, 0f, 0f);
        }
        // 计算攻击者朝向
        Direction direction = this.positionService.getDirection(entity);

        // 计算基础击退距离
        Vector3f knockback = direction.getAheadDirectionVector().multiply(6f).add(0, 6f, 0);

        // 计算疾跑攻击加成
        boolean isSprinting = this.movementServiceProxy.isRunning(entity);
        if (isSprinting) {
            knockback = knockback.add(new Vector3f(1f, 0, 1f));
        }

        // 计算击退附魔加成
        if (ItemAttributeService.hasEnchantment(weapon, Enchantments.KNOCKBACK)) {
            knockback = knockback.add(new Vector3f(1f, 0.5f, 1f).multiply(ItemAttributeService.getEnchantment(weapon, Enchantments.KNOCKBACK).getLevel()));
        }

        return knockback;
    }

    /**
     * 计算远程攻击击退
     *
     * @param projectile
     * @param weapon
     * @return
     */
    public Vector3f getProjectileKnockback(Entity projectile, Entity victim, ItemStack weapon) {
        if (colliderConditionService.isForbidKinckback(victim)) {
            return new Vector3f(0f, 0f, 0f);
        }
        // 计算攻击者朝向
        Direction direction = this.positionService.getDirection(projectile);

        // 计算基础击退距离
        Vector3f knockback = direction.getAheadDirectionVector().multiply(2f).add(0, 5f, 0);

        // 计算击退附魔加成
        if (ItemAttributeService.hasEnchantment(weapon, Enchantments.PUNCH)) {
            knockback = knockback.add(new Vector3f(1f, 0.5f, 1f).multiply(ItemAttributeService.getEnchantment(weapon, Enchantments.PUNCH).getLevel()));
        }

        return knockback;
    }

    /**
     * 计算爆炸击退距离
     *
     * @param explosionPosition
     * @param position
     * @param power
     * @return
     */
    public Vector3f getExplosionKnockback(Vector3f explosionPosition, Vector3f position, float power) {
        // 计算爆心距离
        Vector3f distance = position.subtract(explosionPosition);

        float effectPower = power * power - distance.lengthSquared();
        if (effectPower > 0) {
            return distance.normalize().multiply(5).multiply(effectPower / power);
        }

        return null;
    }
}
