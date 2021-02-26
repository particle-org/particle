package com.particle.game.world.physical.service;

import com.particle.game.world.physical.collider.AABBCollider;
import com.particle.game.world.physical.collider.PointCollider;
import com.particle.game.world.physical.modules.BoxColliderModule;
import com.particle.game.world.physical.modules.PointColliderModule;
import com.particle.model.math.Vector3f;

import java.util.LinkedList;
import java.util.List;

public class EntityColliderDetectTool {

    /**
     * 采样检测，判断是否与目标发生碰撞
     *
     * @param colliderModuleFrom
     */
    public static boolean checkColliderWithTarget(BoxColliderModule colliderModuleFrom, BoxColliderModule colliderModuleTo) {
        switch (colliderModuleFrom.getDetectAlgorithm()) {
            case SAMPLING:
                return doDetect(colliderModuleFrom.getAABBCollider(), colliderModuleTo.getAABBCollider());
            case SWEPT_VOLUME:
                return doDetect(colliderModuleFrom, colliderModuleTo.getAABBCollider());
        }

        return false;
    }

    public static boolean checkColliderWithTarget(PointColliderModule colliderModuleFrom, BoxColliderModule colliderModuleTo) {
        switch (colliderModuleFrom.getDetectAlgorithm()) {
            case SAMPLING:
                return doDetect(colliderModuleFrom.getPointCollider(), colliderModuleTo.getAABBCollider());
            case SWEPT_VOLUME:
                return doDetect(colliderModuleFrom, colliderModuleTo.getAABBCollider());
        }

        return false;
    }

    /**
     * 执行AABB碰撞检测
     * <p>
     * 若检测到碰撞，返回修正后的位置
     * 若未检测到碰撞，返回null
     *
     * @param aabbColliderFrom
     * @param aabbColliderTo
     * @return
     */
    private static boolean doDetect(AABBCollider aabbColliderFrom, AABBCollider aabbColliderTo) {
        Vector3f distance = aabbColliderFrom.getLastPosition().subtract(aabbColliderTo.getLastPosition());
        Vector3f checkDistance = aabbColliderTo.getSize().add(aabbColliderFrom.getSize()).multiply(0.5f);

        //检测碰撞
        return Math.abs(distance.getX()) < checkDistance.getX()
                && Math.abs(distance.getY()) < checkDistance.getY()
                && Math.abs(distance.getZ()) < checkDistance.getZ();
    }

    private static boolean doDetect(PointCollider pointColliderFrom, AABBCollider aabbColliderTo) {
        Vector3f distance = pointColliderFrom.getLastPosition().subtract(aabbColliderTo.getLastPosition());
        Vector3f checkDistance = aabbColliderTo.getSize().multiply(0.5f);

        //检测碰撞
        return Math.abs(distance.getX()) < checkDistance.getX()
                && Math.abs(distance.getY()) < checkDistance.getY()
                && Math.abs(distance.getZ()) < checkDistance.getZ();
    }

    public static boolean doDetect(BoxColliderModule boxColliderModuleFrom, AABBCollider aabbColliderTo) {
        AABBCollider aabbCollider = boxColliderModuleFrom.getAABBCollider();

        // 判断移动情况
        Vector3f u = aabbCollider.getLastMovement();
        if (u == null) {
            return false;
        }

        // 计算射线
        Vector3f offsetPosition = aabbCollider.getLastPosition().add(aabbCollider.getCenter());
        Vector3f size = aabbCollider.getSize();

        List<Vector3f> lines = new LinkedList<>();
        lines.add(new Vector3f(offsetPosition.getX() - (size.getX() / 2), offsetPosition.getY() - (size.getY() / 2), offsetPosition.getZ() - (size.getZ() / 2)));
        lines.add(new Vector3f(offsetPosition.getX() - (size.getX() / 2), offsetPosition.getY() - (size.getY() / 2), offsetPosition.getZ() + (size.getZ() / 2)));
        lines.add(new Vector3f(offsetPosition.getX() - (size.getX() / 2), offsetPosition.getY() + (size.getY() / 2), offsetPosition.getZ() - (size.getZ() / 2)));
        lines.add(new Vector3f(offsetPosition.getX() + (size.getX() / 2), offsetPosition.getY() - (size.getY() / 2), offsetPosition.getZ() - (size.getZ() / 2)));
        lines.add(new Vector3f(offsetPosition.getX() - (size.getX() / 2), offsetPosition.getY() + (size.getY() / 2), offsetPosition.getZ() + (size.getZ() / 2)));
        lines.add(new Vector3f(offsetPosition.getX() + (size.getX() / 2), offsetPosition.getY() - (size.getY() / 2), offsetPosition.getZ() + (size.getZ() / 2)));
        lines.add(new Vector3f(offsetPosition.getX() + (size.getX() / 2), offsetPosition.getY() + (size.getY() / 2), offsetPosition.getZ() - (size.getZ() / 2)));
        lines.add(new Vector3f(offsetPosition.getX() + (size.getX() / 2), offsetPosition.getY() + (size.getY() / 2), offsetPosition.getZ() + (size.getZ() / 2)));

        for (Vector3f p0 : lines) {
            if (!rayCheck(u, p0, aabbColliderTo)) {
                return false;
            }
        }

        return true;
    }

    public static boolean doDetect(PointColliderModule pointColliderModule, AABBCollider aabbColliderTo) {
        PointCollider pointCollider = pointColliderModule.getPointCollider();

        // 判断移动情况
        Vector3f u = pointCollider.getLastMovement();
        if (u == null) {
            return false;
        }

        // 计算射线
        Vector3f p0 = pointCollider.getLastPosition().add(pointCollider.getCenter());

        return rayCheck(u, p0, aabbColliderTo);
    }

    public static boolean rayCheck(Vector3f u, Vector3f p0, AABBCollider aabbColliderTo) {
        Vector3f lastPosition = aabbColliderTo.getLastPosition();

        /*
         * 计算方程
         * t = - (d + np0) / nu
         * t  触碰系数
         * n  平面法线
         * p0 射线起点
         * u  射线方向
         * d = -np
         */
        float centerX = lastPosition.getX() + aabbColliderTo.getCenter().getX();
        if (u.getX() == 0) {
            if (p0.getX() < centerX - aabbColliderTo.getXPadding()
                    || p0.getX() > centerX + aabbColliderTo.getXPadding()) {
                return false;
            }
        } else {
            float t1 = (centerX - aabbColliderTo.getXPadding() - p0.getX()) / u.getX();
            float t2 = (centerX + aabbColliderTo.getXPadding() - p0.getX()) / u.getX();

            if ((t1 < 1 && t2 > 0) || (t1 > 0 && t2 < 1)) {
            } else {
                return false;
            }
        }

        float centerY = lastPosition.getY() + aabbColliderTo.getCenter().getY();
        if (u.getY() == 0) {
            if (p0.getY() < centerY - aabbColliderTo.getYPadding()
                    || p0.getY() > centerY + aabbColliderTo.getYPadding()) {
                return false;
            }
        } else {
            float t1 = (centerY - aabbColliderTo.getYPadding() - p0.getY()) / u.getY();
            float t2 = (centerY + aabbColliderTo.getYPadding() - p0.getY()) / u.getY();

            if ((t1 < 1 && t2 > 0) || (t1 > 0 && t2 < 1)) {
            } else {
                return false;
            }
        }

        float centerZ = lastPosition.getZ() + aabbColliderTo.getCenter().getZ();
        if (u.getZ() == 0) {
            if (p0.getZ() < centerZ - aabbColliderTo.getZPadding()
                    || p0.getZ() > centerZ + aabbColliderTo.getZPadding()) {
                return false;
            }
        } else {
            float t1 = (centerZ - aabbColliderTo.getZPadding() - p0.getZ()) / u.getZ();
            float t2 = (centerZ + aabbColliderTo.getZPadding() - p0.getZ()) / u.getZ();

            if ((t1 < 1 && t2 > 0) || (t1 > 0 && t2 < 1)) {
            } else {
                return false;
            }
        }

        return true;
    }

}
