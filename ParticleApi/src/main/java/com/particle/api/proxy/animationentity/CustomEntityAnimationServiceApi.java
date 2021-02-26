package com.particle.api.proxy.animationentity;

import com.particle.model.entity.Entity;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

public interface CustomEntityAnimationServiceApi {

    /**
     * 创建自定义动画生物，但是不负责spawn
     *
     * @param id        动画id
     * @param position  出生位置
     * @param direction 出生朝向
     * @return 创建的entity
     */
    Entity createAnimationEntity(String id, Vector3f position, Direction direction);


    /**
     * 创建自定义动画生物，并且骑在玩家身上，但是不负责spawn
     *
     * @param id        动画id
     * @param rided     被骑的玩家
     * @param direction 朝向
     * @return 创建的entity
     */
    Entity createAnimationEntity(String id, Player rided, Direction direction);

    /**
     * 检测某个动画是否存在
     *
     * @param id 动画id
     * @return 存在则返回true，否则返回false
     */
    boolean animationExists(String id);
}
