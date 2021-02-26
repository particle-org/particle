package com.particle.game.entity.attribute.identified;

import com.particle.core.ecs.module.BehaviorModule;

import java.util.UUID;

public class UUIDModule extends BehaviorModule {

    private UUID uuid;
    private long roleId;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }
}
