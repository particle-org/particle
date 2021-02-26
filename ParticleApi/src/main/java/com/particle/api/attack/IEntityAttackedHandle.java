package com.particle.api.attack;

import com.particle.model.entity.Entity;

/**
 * 生物被攻击处理器
 */
@FunctionalInterface
public interface IEntityAttackedHandle {
    void handle(Entity attacker);
}