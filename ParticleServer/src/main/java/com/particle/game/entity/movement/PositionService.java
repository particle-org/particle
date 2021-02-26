package com.particle.game.entity.movement;

import com.particle.api.entity.attribute.PositionServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.TransformModule;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.position.IMoveEntityPacketBuilder;
import com.particle.model.math.*;
import com.particle.model.network.packets.DataPacket;

import javax.inject.Singleton;

@Singleton
public class PositionService implements PositionServiceApi {

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    @Override
    public void bindTransformModule(Entity entity, Vector3f position, Direction direction) {
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.bindModule(entity);
        transformModule.setPosition(position);
        transformModule.setDirection(direction);
    }

    /**
     * 获取 4 方向，返回對應 class
     *
     * @return
     */
    @Override
    public <T extends Enum<T>> T get4FaceDirection(Vector3 targetPosition, Vector3 clickBlock, Class<T> faceClass) {
        // 放在東邊
        if (targetPosition.getX() - clickBlock.getX() > 0) {
            return Enum.valueOf(faceClass, "EAST");
        }
        // 放在西邊
        else if (targetPosition.getX() - clickBlock.getX() < 0) {
            return Enum.valueOf(faceClass, "WEST");
        }
        // 放在南邊
        else if (targetPosition.getZ() - clickBlock.getZ() > 0) {
            return Enum.valueOf(faceClass, "SOUTH");
        }
        // 放在北邊
        else if (targetPosition.getZ() - clickBlock.getZ() < 0) {
            return Enum.valueOf(faceClass, "NORTH");
        }

        return null;
    }

    /**
     * 获取根據玩家轉向的 16 方向 API, 返回對應 class
     *
     * @return
     */
    @Override
    public BlockDetailFace get16FaceDirectionByPlayer(Entity entity) {
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);

        float rotation = transformModule.getYaw() % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }

        if (((0 <= rotation && rotation < 11.25) || (348.75 <= rotation && rotation < 360))) {
            return BlockDetailFace.NORTH;
        } else if (11.25 <= rotation && rotation < 33.75) {
            return BlockDetailFace.EAST_NORTH_NORTH;
        } else if (33.75 <= rotation && rotation < 56.25) {
            return BlockDetailFace.EAST_NORTH;
        } else if (56.25 <= rotation && rotation < 78.75) {
            return BlockDetailFace.EAST_NORTH_EAST;
        } else if (78.75 <= rotation && rotation < 101.25) {
            return BlockDetailFace.EAST;
        } else if (101.25 <= rotation && rotation < 123.75) {
            return BlockDetailFace.EAST_SOUTH_EAST;
        } else if (123.75 <= rotation && rotation < 146.25) {
            return BlockDetailFace.EAST_SOUTH;
        } else if (146.25 <= rotation && rotation < 168.75) {
            return BlockDetailFace.EAST_SOUTH_SOUTH;
        } else if (168.75 <= rotation && rotation < 191.25) {
            return BlockDetailFace.SOUTH;
        } else if (191.25 <= rotation && rotation < 213.75) {
            return BlockDetailFace.WEST_SOUTH_SOUTH;
        } else if (213.75 <= rotation && rotation < 236.25) {
            return BlockDetailFace.WEST_SOUTH;
        } else if (236.25 <= rotation && rotation < 258.75) {
            return BlockDetailFace.WEST_SOUTH_WEST;
        } else if (258.75 <= rotation && rotation < 281.25) {
            return BlockDetailFace.WEST;
        } else if (281.25 <= rotation && rotation < 303.75) {
            return BlockDetailFace.WEST_NORTH_WEST;
        } else if (303.75 <= rotation && rotation < 326.25) {
            return BlockDetailFace.WEST_NORTH;
        } else if (326.25 <= rotation && rotation < 348.75) {
            return BlockDetailFace.WEST_NORTH_NORTH;
        } else {
            return null;
        }
    }

    /**
     * 获取門方向，返回BlockDoorFace
     *
     * @return
     */
    @Override
    public BlockDoorFace getDoorFaceDirection(Entity entity) {
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);

        float rotation = transformModule.getYaw() % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (((0 <= rotation && rotation < 45) || (315 <= rotation && rotation < 360))) {
            return BlockDoorFace.NORTH;
        } else if (45 <= rotation && rotation < 135) {
            return BlockDoorFace.EAST;
        } else if (135 <= rotation && rotation < 225) {
            return BlockDoorFace.SOUTH;
        } else if (225 <= rotation && rotation < 315) {
            return BlockDoorFace.WEST;
        } else {
            return null;
        }
    }

    /**
     * 获取拉桿方向，返回BlockLeverFace
     *
     * @return
     */
    @Override
    public BlockLeverFace getLeverFaceDirection(Entity entity, Vector3 targetPosition, Vector3 clickBlock) {
        // 放在東邊
        if (targetPosition.getX() - clickBlock.getX() > 0) {
            return BlockLeverFace.SIDE_EAST;
        }
        // 放在西邊
        else if (targetPosition.getX() - clickBlock.getX() < 0) {
            return BlockLeverFace.SIDE_WEST;
        }
        // 放在南邊
        else if (targetPosition.getZ() - clickBlock.getZ() > 0) {
            return BlockLeverFace.SIDE_SOUTH;
        }
        // 放在北邊
        else if (targetPosition.getZ() - clickBlock.getZ() < 0) {
            return BlockLeverFace.SIDE_NORTH;
        }
        // 放在上面
        else if (targetPosition.getY() - clickBlock.getY() > 0) {
            TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
            float rotation = transformModule.getYaw() % 360;
            if (rotation < 0) {
                rotation += 360.0;
            }

            if (((0 <= rotation && rotation < 45) || (315 <= rotation && rotation < 360)) || (135 <= rotation && rotation < 225)) {
                return BlockLeverFace.UP_SOUTH;
            } else if ((45 <= rotation && rotation < 135) || (225 <= rotation && rotation < 315)) {
                return BlockLeverFace.UP_EAST;
            } else {
                return null;
            }
        }
        // 放在下面
        else if (targetPosition.getY() - clickBlock.getY() < 0) {
            TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
            float rotation = transformModule.getYaw() % 360;
            if (rotation < 0) {
                rotation += 360.0;
            }

            if (((0 <= rotation && rotation < 45) || (315 <= rotation && rotation < 360)) || (135 <= rotation && rotation < 225)) {
                return BlockLeverFace.DOWN_SOUTH;
            } else if ((45 <= rotation && rotation < 135) || (225 <= rotation && rotation < 315)) {
                return BlockLeverFace.DOWN_EAST;
            } else {
                return null;
            }
        }

        return null;
    }

    /**
     * 获取按鈕方向，返回BlockButtonFace
     *
     * @return
     */
    @Override
    public BlockButtonFace getButtonFaceDirection(Vector3 targetPosition, Vector3 clickBlock) {
        // 放在東邊
        if (targetPosition.getX() - clickBlock.getX() > 0) {
            return BlockButtonFace.EAST;
        }
        // 放在西邊
        else if (targetPosition.getX() - clickBlock.getX() < 0) {
            return BlockButtonFace.WEST;
        }
        // 放在南邊
        else if (targetPosition.getZ() - clickBlock.getZ() > 0) {
            return BlockButtonFace.SOUTH;
        }
        // 放在北邊
        else if (targetPosition.getZ() - clickBlock.getZ() < 0) {
            return BlockButtonFace.NORTH;
        }
        // 放在上面
        else if (targetPosition.getY() - clickBlock.getY() > 0) {
            return BlockButtonFace.UP;
        }
        // 放在下面
        else if (targetPosition.getY() - clickBlock.getY() < 0) {
            return BlockButtonFace.DOWN;
        }

        return null;
    }

    /**
     * 获取火把方向，返回BlockTouchFace
     *
     * @return
     */
    @Override
    public BlockTouchFace getTouchFaceDirection(Vector3 targetPosition, Vector3 clickBlock) {
        // 放在東邊
        if (targetPosition.getX() - clickBlock.getX() > 0) {
            return BlockTouchFace.EAST;
        }
        // 放在西邊
        else if (targetPosition.getX() - clickBlock.getX() < 0) {
            return BlockTouchFace.WEST;
        }
        // 放在南邊
        else if (targetPosition.getZ() - clickBlock.getZ() > 0) {
            return BlockTouchFace.SOUTH;
        }
        // 放在北邊
        else if (targetPosition.getZ() - clickBlock.getZ() < 0) {
            return BlockTouchFace.NORTH;
        }
        // 放在上面
        else if (targetPosition.getY() - clickBlock.getY() > 0) {
            return BlockTouchFace.UP;
        }

        return null;
    }

    /**
     * 获取階梯方向，返回BlockStairsFace
     *
     * @return
     */
    @Override
    public BlockStairsFace getStairsFaceDirection(Entity entity) {
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);

        float horizontalAngle = transformModule.getPitch();

        float rotation = transformModule.getYaw() % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (((0 <= rotation && rotation < 45) || (315 <= rotation && rotation < 360)) && horizontalAngle > 0) {
            return BlockStairsFace.DOWN_SOUTH;
        } else if (45 <= rotation && rotation < 135 && horizontalAngle > 0) {
            return BlockStairsFace.DOWN_WEST;
        } else if (135 <= rotation && rotation < 225 && horizontalAngle > 0) {
            return BlockStairsFace.DOWN_NORTH;
        } else if (225 <= rotation && rotation < 315 && horizontalAngle > 0) {
            return BlockStairsFace.DOWN_EAST;
        } else if (((0 <= rotation && rotation < 45) || (315 <= rotation && rotation < 360)) && horizontalAngle < 0) {
            return BlockStairsFace.UP_SOUTH;
        } else if (45 <= rotation && rotation < 135 && horizontalAngle < 0) {
            return BlockStairsFace.UP_WEST;
        } else if (135 <= rotation && rotation < 225 && horizontalAngle < 0) {
            return BlockStairsFace.UP_NORTH;
        } else if (225 <= rotation && rotation < 315 && horizontalAngle < 0) {
            return BlockStairsFace.UP_EAST;
        } else {
            return null;
        }
    }

    /**
     * 获取織布機方向，返回BlockFace
     *
     * @return
     */
    @Override
    public BlockLoomFace getLoomFaceDirection(Entity entity) {
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);

        float rotation = transformModule.getYaw() % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if ((0 <= rotation && rotation < 45) || (315 <= rotation && rotation < 360)) {
            return BlockLoomFace.SOUTH;
        } else if (45 <= rotation && rotation < 135) {
            return BlockLoomFace.WEST;
        } else if (135 <= rotation && rotation < 225) {
            return BlockLoomFace.NORTH;
        } else if (225 <= rotation && rotation < 315) {
            return BlockLoomFace.EAST;
        } else {
            return null;
        }
    }

    /**
     * 获取方向，返回BlockFace
     *
     * @return
     */
    @Override
    public BlockFace getFaceDirection(Entity entity) {
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);

        float rotation = transformModule.getYaw() % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if ((0 <= rotation && rotation < 45) || (315 <= rotation && rotation < 360)) {
            return BlockFace.SOUTH;
        } else if (45 <= rotation && rotation < 135) {
            return BlockFace.WEST;
        } else if (135 <= rotation && rotation < 225) {
            return BlockFace.NORTH;
        } else if (225 <= rotation && rotation < 315) {
            return BlockFace.EAST;
        } else {
            return null;
        }
    }

    public boolean isOnGround(Entity entity) {
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);

        return transformModule.isOnGround();
    }

    /**
     * 获取生物的位置
     *
     * @param entity
     * @return
     */
    @Override
    public Vector3f getPosition(Entity entity) {
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);

        return transformModule.getPosition();
    }

    @Override
    public Vector3 getFloorPosition(Entity entity) {
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);

        return transformModule.getFloorPosition();
    }

    public void setPositionAndDirection(Entity entity, Vector3f position, Direction direction) {
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);

        transformModule.setPosition(position);
        transformModule.setDirection(direction);
    }

    @Override
    public void setPosition(Entity entity, Vector3f position) {
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);

        transformModule.setPosition(position);
    }

    public void setPosition(Entity entity, Vector3 position) {
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);

        transformModule.setPosition(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public void setDirection(Entity entity, Direction direction) {
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);

        transformModule.setDirection(direction);
    }

    public DataPacket getMoveEntityPacket(Entity entity) {
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);

        IMoveEntityPacketBuilder moveEntityPacketBuilder = transformModule.getMoveEntityPacketBuilder();
        if (moveEntityPacketBuilder != null) {
            return moveEntityPacketBuilder.build();
        }

        return null;
    }

    /**
     * 获取生物的朝向
     *
     * @param entity
     * @return
     */
    @Override
    public Direction getDirection(Entity entity) {
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);

        return transformModule.getDirection();
    }
}
