package com.particle.api.entity.attribute;

import com.particle.model.entity.Entity;
import com.particle.model.math.*;

public interface PositionServiceApi {

    /**
     * 绑定移动组件
     *
     * @param entity
     * @param position
     * @param direction
     */
    void bindTransformModule(Entity entity, Vector3f position, Direction direction);

    /**
     * 获取門方向，返回BlockDoorFace
     *
     * @return
     */
    BlockDoorFace getDoorFaceDirection(Entity entity);

    /**
     * 获取 4 方向 API, 返回對應 class
     *
     * @return
     */
    <T extends Enum<T>> T get4FaceDirection(Vector3 targetPosition, Vector3 clickBlock, Class<T> faceClass);

    /**
     * 获取根據玩家轉向的 16 方向 API ，返回對應 class
     *
     * @return
     */
    BlockDetailFace get16FaceDirectionByPlayer(Entity entity);

    /**
     * 获取拉桿方向，返回BlockLeverFace
     *
     * @return
     */
    BlockLeverFace getLeverFaceDirection(Entity entity, Vector3 targetPosition, Vector3 clickBlock);

    /**
     * 获取按鈕方向，返回BlockButtonFace
     *
     * @return
     */
    BlockButtonFace getButtonFaceDirection(Vector3 targetPosition, Vector3 clickBlock);

    /**
     * 获取火把方向，返回BlockTouchFace
     *
     * @return
     */
    BlockTouchFace getTouchFaceDirection(Vector3 targetPosition, Vector3 clickBlock);

    /**
     * 获取階梯方向，返回BlockStairsFace
     *
     * @return
     */
    BlockStairsFace getStairsFaceDirection(Entity entity);

    /**
     * 获取織布機朝向
     *
     * @param entity 操作的生物
     * @return 返回朝向
     */
    BlockLoomFace getLoomFaceDirection(Entity entity);

    /**
     * 获取生物朝向
     *
     * @param entity 操作的生物
     * @return 返回朝向
     */
    BlockFace getFaceDirection(Entity entity);

    /**
     * 获取生物位置
     *
     * @param entity 操作的生物
     * @return 返回位置
     */
    Vector3f getPosition(Entity entity);

    /**
     * 获取生物取整位置
     *
     * @param entity 操作的生物
     * @return 返回取整位置
     */
    Vector3 getFloorPosition(Entity entity);

    /**
     * 设置生物位置
     *
     * @param entity   操作的生物
     * @param position 设置的位置
     */
    void setPosition(Entity entity, Vector3f position);

    /**
     * 设置生物朝向
     *
     * @param entity    操作的生物
     * @param direction 设置的朝向
     */
    void setDirection(Entity entity, Direction direction);

    /**
     * 查询生物朝向
     *
     * @param entity 操作的生物
     * @return 返回朝向
     */
    Direction getDirection(Entity entity);
}
