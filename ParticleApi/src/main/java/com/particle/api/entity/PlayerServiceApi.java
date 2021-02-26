package com.particle.api.entity;

import com.particle.model.entity.attribute.EntityAttribute;
import com.particle.model.level.Level;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;
import com.particle.model.player.GameMode;
import com.particle.model.player.Player;

import java.util.List;
import java.util.UUID;

public interface PlayerServiceApi {

    /**
     * 查询玩家的属性
     *
     * @param player 操作玩家
     * @return 属性列表
     */
    EntityAttribute[] getAttributes(Player player);

    /**
     * 修改玩家GameMode
     *
     * @param player     操作玩家
     * @param mode       模式
     * @param clientSide 是否通知客户端
     */
    void changePlayerGameMode(Player player, GameMode mode, boolean clientSide);

    /**
     * 查询玩家GameMode
     *
     * @param player 操作玩家
     * @return
     */
    GameMode getGameMode(Player player);

    /**
     * tp玩家至指定地点
     *
     * @param player   操作玩家
     * @param position 位置
     */
    void teleport(Player player, Vector3f position);

    /**
     * tp玩家至指定地点，并设置朝向
     *
     * @param player    操作玩家
     * @param position  位置
     * @param direction 朝向
     */
    void teleport(Player player, Vector3f position, Direction direction);

    /**
     * 修改玩家所在的Level
     *
     * @param player   操作玩家
     * @param newLevel 新的Level
     */
    void switchLevel(Player player, Level newLevel);

    /**
     * 修改玩家所在的Level
     *
     * @param player
     * @param newLevel
     * @param spawnPosition
     */
    public void switchLevel(Player player, Level newLevel, Vector3f spawnPosition);

    /**
     * 修改玩家所在的Level
     *
     * @param player       操作玩家
     * @param newLevelName 新的Level名称
     */
    void switchLevel(Player player, String newLevelName);

    /**
     * 将玩家重定向至其它服务器
     *
     * @param player  操作玩家
     * @param address 新地址
     * @param port    新端口
     */
    void transfer(Player player, String address, short port);

    /**
     * 获取玩家的UUID
     *
     * @param player 操作玩家
     * @return 玩家UUID
     */
    UUID getPlayerUUID(Player player);

    /**
     * 更改玩家的UUID
     *
     * @param player 操作玩家
     */
    void setPlayerUUID(Player player, UUID targetUuid);

    /**
     * 获取玩家的 role_id
     *
     * @param player 操作玩家
     * @return 玩家UUID
     */
    long getPlayerRoleId(Player player);

    /**
     * 更改玩家的 role_id
     *
     * @param player 操作玩家
     */
    void setPlayerRoleId(Player player, long roleId);


    /**
     * 获取某个坐标最近的玩家（不跨区快检查）
     *
     * @param level    检查Level
     * @param position 检查坐标
     * @param limit    最远距离
     * @return 获取某个坐标最近的玩家
     */
    Player getClosestPlayer(Level level, Vector3f position, float limit);

    /**
     * 获取某个坐标最近的玩家
     *
     * @param level    检查Level
     * @param position 检查坐标
     * @param limit    最远距离
     * @return
     */
    List<Player> getNearPlayers(Level level, Vector3f position, float limit);
}

