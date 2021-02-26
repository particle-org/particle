package com.particle.api.permission;

import com.particle.model.permission.NodeTree;
import com.particle.model.player.Player;

import java.util.Set;

public interface PermissionApi {

    /**
     * 获取某个玩家的权限
     *
     * @param player 玩家
     * @return 返回结果
     */
    NodeTree getPermissions(Player player);

    /**
     * 玩家添加权限
     *
     * @param player     玩家
     * @param permission 权限名
     * @return 是否添加成功
     */
    boolean addPermissionToPlayer(Player player, String permission);

    /**
     * 删除某玩家的权限
     *
     * @param player     玩家
     * @param permission 权限名
     * @return 是否删除成功
     */
    boolean removePermissionFromPlayer(Player player, String permission);

    /**
     * 强制加载指定玩家的权限
     *
     * @param player 玩家
     */
    void reload(Player player);

    /**
     * 强制加载所有玩家和控制台权限数据
     */
    void reloadAll();

    /**
     * 判断是否具有权限
     *
     * @param player     玩家
     * @param permission 权限
     * @return
     */
    boolean hasPermission(Player player, String permission);

    /**
     * 判断是否具有权限
     *
     * @param player      玩家
     * @param permissions 权限列表
     * @return
     */
    boolean hasPermission(Player player, Set<String> permissions);

    /**
     * 是否OP
     */
    boolean isOp(Player player);
}

