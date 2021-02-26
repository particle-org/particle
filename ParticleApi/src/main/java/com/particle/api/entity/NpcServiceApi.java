package com.particle.api.entity;

import com.particle.model.entity.model.others.NpcEntity;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;

import java.util.UUID;

public interface NpcServiceApi {

    /**
     * 创建生物
     *
     * @param position 默认位置
     * @return 创建的生物
     */
    NpcEntity createEntity(Vector3f position);

    /**
     * 创建生物
     *
     * @param position  默认位置
     * @param direction 默认方向
     * @return 创建的生物
     */
    NpcEntity createEntity(Vector3f position, Direction direction);

    /**
     * 查询NPC的uuid
     *
     * @param npcEntity 操作的生物
     * @return UUID
     */
    UUID getNpcUuid(NpcEntity npcEntity);
}

