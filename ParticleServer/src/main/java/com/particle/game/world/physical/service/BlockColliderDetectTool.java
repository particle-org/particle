package com.particle.game.world.physical.service;

import com.particle.api.inject.RequestStaticInject;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.world.level.LevelService;
import com.particle.game.world.physical.collider.AABBCollider;
import com.particle.game.world.physical.collider.PointCollider;
import com.particle.model.entity.Entity;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@RequestStaticInject
public class BlockColliderDetectTool {

    @Inject
    private static LevelService levelService;

    public static boolean isStandOnBlock(Entity entity, Vector3f checkedPosition, AABBCollider aabbCollider) {
        Vector3f center = checkedPosition.add(aabbCollider.getCenter());
        int xmin = (int) Math.floor(center.getX() - aabbCollider.getSize().getX() / 2);
        int xmax = (int) Math.floor(center.getX() + aabbCollider.getSize().getX() / 2);
        int ymin = (int) (Math.floor(center.getY() - aabbCollider.getSize().getY() / 2 - 0.1));
        int zmin = (int) Math.floor(center.getZ() - aabbCollider.getSize().getZ() / 2);
        int zmax = (int) Math.floor(center.getZ() + aabbCollider.getSize().getZ() / 2);
        List<Vector3> positionList = new ArrayList<>();
        for (int x = xmin; x <= xmax; x++) {
            for (int z = zmin; z <= zmax; z++) {
                positionList.add(new Vector3(x, ymin, z));
            }
        }

        // 判断是否发生碰撞
        for (Vector3 position : positionList) {
            if (!levelService.getBlockTypeAt(entity.getLevel(), position).getBlockGeometry().canPassThrow()) {
                return true;
            }
        }

        return false;
    }

    public static boolean isStandOnBlock(Entity entity, Vector3f checkedPosition, PointCollider pointCollider) {

        Vector3 checkPosition = new Vector3(checkedPosition.add(pointCollider.getCenter()).subtract(0, 0.1f, 0));

        // 判断是否发生碰撞
        return !levelService.getBlockTypeAt(entity.getLevel(), checkPosition).getBlockGeometry().canPassThrow();
    }

    /**
     * 执行AABB碰撞检测
     * <p>
     * 若检测到碰撞，返回修正后的位置
     * 若未检测到碰撞，返回null
     *
     * @param level
     * @param transformModule
     * @param aabbCollider
     * @return
     */
    public static Vector3f doDetect(Level level, TransformModule transformModule, AABBCollider aabbCollider) {
        // 计算当前坐标
        Vector3f currentPosition = transformModule.getPosition().add(aabbCollider.getCenter());
        Vector3f lastPosition = aabbCollider.getLastPosition().add(aabbCollider.getCenter());

        Vector3f movement = currentPosition.subtract(lastPosition);
        Vector3f normalize = null;

        // 移动距离超过1格
        while (movement.lengthSquared() > 1) {
            // 懒初始化，节省计算开销
            if (normalize == null) {
                normalize = movement.normalize();
            }

            // 碰撞检测
            boolean collider = checkPosition(level, lastPosition.add(normalize).subtract(aabbCollider.getCenter()), aabbCollider);
            if (collider) {
                // 计算接触面，把三维的碰撞降成边界上的点与面的碰撞
                Vector3f checkSurface = new Vector3f(0, 0, 0);

                // 计算x轴接触面
                float xOffset = caculateAABBColliderOffset(lastPosition.getX() + normalize.getX(), lastPosition.getX(), aabbCollider.getXPadding());
                // 计算y轴接触面
                float yOffset = caculateAABBColliderOffset(lastPosition.getY() + normalize.getY(), lastPosition.getY(), aabbCollider.getYPadding());
                // 计算z轴接触面
                float zOffset = caculateAABBColliderOffset(lastPosition.getZ() + normalize.getZ(), lastPosition.getZ(), aabbCollider.getZPadding());

                float xRate = normalize.getX() == 0 ? Float.MAX_VALUE : xOffset / normalize.getX();
                float yRate = normalize.getY() == 0 ? Float.MAX_VALUE : yOffset / (normalize.getY());
                float zRate = normalize.getZ() == 0 ? Float.MAX_VALUE : zOffset / (normalize.getZ());

                float rate = xRate;
                if (rate > yRate) {
                    rate = yRate;
                }
                if (rate > zRate) {
                    rate = zRate;
                }

                checkSurface.setX(lastPosition.getX() + rate * (currentPosition.getX() - lastPosition.getX()));
                checkSurface.setY(lastPosition.getY() + rate * (currentPosition.getY() - lastPosition.getY()));
                checkSurface.setZ(lastPosition.getZ() + rate * (currentPosition.getZ() - lastPosition.getZ()));

                return checkSurface.subtract(aabbCollider.getCenter());
            }

            movement = movement.subtract(normalize);
            lastPosition = lastPosition.add(normalize);
        }

        // 移动距离小于1格
        boolean collider = checkPosition(level, transformModule.getPosition(), aabbCollider);
        // 发送碰撞
        if (collider) {
            // 计算接触面，把三维的碰撞降成边界上的点与面的碰撞
            Vector3f checkSurface = new Vector3f(0, 0, 0);

            // 计算x轴接触面
            float xOffset = caculateAABBColliderOffset(currentPosition.getX(), lastPosition.getX(), aabbCollider.getXPadding());
            // 计算y轴接触面
            float yOffset = caculateAABBColliderOffset(currentPosition.getY(), lastPosition.getY(), aabbCollider.getYPadding());
            // 计算z轴接触面
            float zOffset = caculateAABBColliderOffset(currentPosition.getZ(), lastPosition.getZ(), aabbCollider.getZPadding());

            float xRate = movement.getX() == 0 ? Float.MAX_VALUE : xOffset / movement.getX();
            float yRate = movement.getY() == 0 ? Float.MAX_VALUE : yOffset / movement.getY();
            float zRate = movement.getZ() == 0 ? Float.MAX_VALUE : zOffset / movement.getZ();

            float rate = xRate;
            if (rate > yRate) {
                rate = yRate;
            }
            if (rate > zRate) {
                rate = zRate;
            }

            checkSurface.setX(lastPosition.getX() + rate * (movement.getX()));
            checkSurface.setY(lastPosition.getY() + rate * (movement.getY()));
            checkSurface.setZ(lastPosition.getZ() + rate * (movement.getZ()));

            return checkSurface.subtract(aabbCollider.getCenter());
        }

        return null;
    }

    /**
     * 执行点碰撞检测
     * <p>
     * 若检测到碰撞，返回修正后的位置
     * 若未检测到碰撞，返回null
     *
     * @param level
     * @param transformModule
     * @param pointCollider
     * @return
     */
    public static Vector3f doDetect(Level level, TransformModule transformModule, PointCollider pointCollider) {
        Vector3 fixedPosition = checkPosition(level, transformModule.getPosition(), pointCollider);

        // 发送碰撞
        if (fixedPosition != null) {
            // 计算当前坐标
            Vector3 lastPosition = new Vector3(pointCollider.getLastPosition());

            // 计算接触面
            Vector3f checkSurface = new Vector3f(0, 0, 0);

            // 计算x轴接触面
            if (fixedPosition.getX() > lastPosition.getX()) checkSurface.setX(fixedPosition.getX() - 0.01f);
            else if (fixedPosition.getX() < lastPosition.getX()) checkSurface.setX(lastPosition.getX());
            else checkSurface.setX(transformModule.getX());

            // 计算y轴接触面
            if (fixedPosition.getY() > lastPosition.getY()) checkSurface.setY(fixedPosition.getY() - 0.01f);
            else if (fixedPosition.getY() < lastPosition.getY()) checkSurface.setY(lastPosition.getY());
            else checkSurface.setY(transformModule.getY());

            // 计算z轴接触面
            if (fixedPosition.getZ() > lastPosition.getZ()) checkSurface.setZ(fixedPosition.getZ() - 0.01f);
            else if (fixedPosition.getZ() < lastPosition.getZ()) checkSurface.setZ(lastPosition.getZ());
            else checkSurface.setZ(transformModule.getZ());

            return checkSurface;
        }

        return null;
    }

    private static float caculateAABBColliderOffset(float current, float last, float padding) {
        if (current > last) {
            // 往x轴正方向移动
            float cx = current + padding;
            float lx = last + padding;

            if (((int) cx) == ((int) lx)) {
                return 0f;
            } else {
                // 计算偏移
                return (((int) cx) - 0.01f) - lx;
            }
        } else if (current < last) {
            // 往x轴负方向移动
            float cx = current - padding;
            float lx = last - padding;

            if (((int) cx) == ((int) lx)) {
                return 0f;
            } else {
                // 计算偏移
                return ((int) lx) - lx;
            }
        } else {
            return 0f;
        }
    }

    /**
     * @param level
     * @param entityPosition
     * @param pointCollider
     * @return 碰撞的位置
     */
    public static Vector3 checkPosition(Level level, Vector3f entityPosition, PointCollider pointCollider) {
        Vector3f fromPosition = pointCollider.getLastPosition().add(pointCollider.getCenter());
        Vector3f toPosition = entityPosition.add(pointCollider.getCenter());

        return rayTrace(level, fromPosition, toPosition);
    }

    /**
     * 检测射线与方块的碰撞情况
     *
     * @param level
     * @param fromPosition
     * @param toPosition
     * @return
     */
    public static Vector3 rayTrace(Level level, Vector3f fromPosition, Vector3f toPosition) {
        // 相同方块内移动，不进行碰撞检测
        if (fromPosition.getFloorX() == toPosition.getFloorX()
                && fromPosition.getFloorY() == toPosition.getFloorY()
                && fromPosition.getFloorZ() == toPosition.getFloorZ()) {
            return null;
        }

        Vector3f moveDistance = toPosition.subtract(fromPosition);
        Vector3f direction = moveDistance.normalize();
        float length = (float) moveDistance.length();

        // 移动过程中的点检测
        for (; length > 1; length -= 1) {
            // 每次往前移动一格
            fromPosition = fromPosition.add(direction);

            // 跨方块移动，检测目标方块是否可达
            Vector3 checkPosition = new Vector3(fromPosition);
            if (!levelService.getBlockTypeAt(level, checkPosition).getBlockGeometry().canPassThrow()) {
                // 如果发生碰撞，则立刻返回
                return checkPosition;
            }
        }

        // 目的地检测
        Vector3 checkPosition = new Vector3(toPosition);
        if (!levelService.getBlockTypeAt(level, checkPosition).getBlockGeometry().canPassThrow()) {
            // 如果发生碰撞，则立刻返回
            return checkPosition;
        }

        return null;
    }

    public static boolean checkPosition(Level level, Vector3f entityPosition, AABBCollider aabbCollider) {
        // 缓存碰撞箱涉及的方块列表
        List<Vector3> positionList = new ArrayList<>();

        // 计算碰撞箱所包含的方块
        Vector3f center = entityPosition.add(aabbCollider.getCenter());
        int xmin = (int) Math.floor(center.getX() - aabbCollider.getSize().getX() / 2);
        int xmax = (int) Math.floor(center.getX() + aabbCollider.getSize().getX() / 2);
        int ymin = (int) Math.floor(center.getY() - aabbCollider.getSize().getY() / 2);
        int ymax = (int) Math.floor(center.getY() + aabbCollider.getSize().getY() / 2);
        int zmin = (int) Math.floor(center.getZ() - aabbCollider.getSize().getZ() / 2);
        int zmax = (int) Math.floor(center.getZ() + aabbCollider.getSize().getZ() / 2);
        for (int x = xmin; x <= xmax; x++) {
            for (int y = ymin; y <= ymax; y++) {
                for (int z = zmin; z <= zmax; z++) {
                    positionList.add(new Vector3(x, y, z));
                }
            }
        }

        // 移除该生物本来就碰撞的方块
        center = aabbCollider.getLastPosition().add(aabbCollider.getCenter());
        xmin = (int) Math.floor(center.getX() - aabbCollider.getSize().getX() / 2);
        xmax = (int) Math.floor(center.getX() + aabbCollider.getSize().getX() / 2);
        ymin = (int) Math.floor(center.getY() - aabbCollider.getSize().getY() / 2);
        ymax = (int) Math.floor(center.getY() + aabbCollider.getSize().getY() / 2);
        zmin = (int) Math.floor(center.getZ() - aabbCollider.getSize().getZ() / 2);
        zmax = (int) Math.floor(center.getZ() + aabbCollider.getSize().getZ() / 2);
        for (int x = xmin; x <= xmax; x++) {
            for (int y = ymin; y <= ymax; y++) {
                for (int z = zmin; z <= zmax; z++) {
                    positionList.remove(new Vector3(x, y, z));
                }
            }
        }

        // 判断是否发生碰撞
        for (Vector3 position : positionList) {
            if (!levelService.getBlockTypeAt(level, position).getBlockGeometry().canPassThrow()) {
                return true;
            }
        }

        return false;
    }
}
