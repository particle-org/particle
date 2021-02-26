package com.particle.model.entity.component.player;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.permission.NodeTree;
import com.particle.model.permission.Tristate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PermissionModule extends BehaviorModule {
    /**
     * 当前玩家的权限
     */
    private NodeTree permissions;

    /**
     * 当前玩家是否op玩家
     */
    private boolean isOp = false;

    /**
     * 获取玩家权限
     *
     * @return
     */
    public NodeTree getPermissions() {
        return permissions;
    }

    /**
     * 设置玩家权限
     *
     * @param permissions
     */
    public void setPermissions(NodeTree permissions) {
        this.permissions = permissions;
    }

    public void addPermission(String permission) {
        Map<String, Boolean> permissionMap = permissions.asMap();
        permissionMap.put(permission, true);
        permissions = NodeTree.of(permissionMap);
    }

    public void removePermission(String permission) {
        Map<String, Boolean> permissionMap = permissions.asMap();
        permissionMap.remove(permission);
        permissions = NodeTree.of(permissionMap);
    }

    public void clearPermission() {
        permissions = NodeTree.of(new HashMap<>());
    }

    /**
     * 判断是否有权限
     *
     * @param permission
     * @return
     */
    public boolean hasPermission(String permission) {
        Tristate res = this.permissions.get(permission);
        if (res == Tristate.TRUE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否有权限
     *
     * @param permissions
     * @return
     */
    public boolean hasAllPermission(Set<String> permissions) {
        if (permissions == null) {
            return true;
        }
        for (String permission : permissions) {
            if (!this.hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    public boolean isOp() {
        return isOp;
    }

    public void setOp(boolean op) {
        isOp = op;
    }
}
