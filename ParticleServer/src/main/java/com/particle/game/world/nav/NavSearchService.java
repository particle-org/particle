package com.particle.game.world.nav;

import com.particle.model.level.chunk.NavSquare;
import com.particle.model.math.Vector3;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class NavSearchService {

    /**
     * 查找连通情况
     *
     * @param from
     * @param to
     * @return
     */
    public List<NavSquare> search(NavSquare from, NavSquare to, Vector3 startVector, Vector3 targetVector) {
        if (from == to) {
            return new LinkedList<>();
        }

        List<NavSquare> navSquareList = new ArrayList<>();

        AStar aStar = new AStar();
        AStar.Node parent = aStar.findPath(from, startVector, targetVector);

        ArrayList<AStar.Node> movePositionList = new ArrayList<>();
        // 取出路線節點
        while (parent != null) {
            AStar.Node node = new AStar.Node(parent.x, parent.y, parent.z);
            movePositionList.add(node);
            parent = parent.parent;
        }

        NavSquare navSquare = from;
        navSquareList.add(from);
        for (int i = movePositionList.size() - 1; i >= 0; i--) {
            AStar.Node point = movePositionList.get(i);
            for (NavSquare nav : navSquare.getConnectTo()) {
                if (point.x <= nav.getxMax() && point.x >= nav.getxMin()
                        && point.z <= nav.getzMax() && point.z >= nav.getzMin()
                        && point.y <= nav.getY() + 1 && point.y >= nav.getY() - 1) {
                    navSquareList.add(nav);
                    navSquare = nav;
                    break;
                }
            }
        }

        return navSquareList;
    }

    public List<Vector3> getRoadPoints(List<NavSquare> navSquares, Vector3 startVector, Vector3 targetVector) {
        List<Vector3> points = new ArrayList<>();
        if (navSquares.size() == 0) {
            return new ArrayList<>();
        }
        if (navSquares.size() == 1) {
            points.add(targetVector);
            return points;
        }

        // 取得可通路線點
        getRoadPoints(navSquares, points, 0, startVector, targetVector);
        points.add(targetVector);

        return points;
    }

    public List<Vector3> getRoadPoints(List<NavSquare> navSquares, List<Vector3> roadPointList, int depth, Vector3 startVector, Vector3 targetVector) {
        Vector3 nextVector = targetVector;
        NavSquare nowNavSquare = null;
        NavSquare nextNavSquare = null;

        if (depth < navSquares.size() - 1) {
            nowNavSquare = navSquares.get(depth);
            nextNavSquare = navSquares.get(depth + 1);
            int targetVectorX = targetVector.getX();
            int targetVectorZ = targetVector.getZ();
            if (targetVector.getX() > nextNavSquare.getxMax()) {
                targetVectorX = nextNavSquare.getxMax();
            } else if (targetVector.getX() < nextNavSquare.getxMin()) {
                targetVectorX = nextNavSquare.getxMin();
            }

            if (targetVector.getZ() > nextNavSquare.getzMax()) {
                targetVectorZ = nextNavSquare.getzMax();
            } else if (targetVector.getZ() < nextNavSquare.getzMin()) {
                targetVectorZ = nextNavSquare.getzMin();
            }

            nextVector = new Vector3(targetVectorX, nextNavSquare.getY(), targetVectorZ);
        }

        if (nextNavSquare == null) {
            return roadPointList;
        }

        int y = Math.max(nowNavSquare.getY(), nextNavSquare.getY());
        // 路線在左側
        if (nowNavSquare.getxMin() == nextNavSquare.getxMax() + 1) {
            double z = startVector.getEquationZ(nextVector, nowNavSquare.getxMin());
            // 找拐角
            if (z > nextNavSquare.getzMax() || z > nowNavSquare.getzMax()) {
                startVector = new Vector3(nowNavSquare.getxMin(), y, Math.min(nextNavSquare.getzMax(), nowNavSquare.getzMax()));
                roadPointList.add(startVector);
            } else if (z < nextNavSquare.getzMin() || z < nowNavSquare.getzMin()) {
                startVector = new Vector3(nowNavSquare.getxMin(), y, Math.max(nextNavSquare.getzMin(), nowNavSquare.getzMin()));
                roadPointList.add(startVector);
            }
        }
        // 路線在右側
        else if (nowNavSquare.getxMax() + 1 == nextNavSquare.getxMin()) {
            double z = startVector.getEquationZ(nextVector, nowNavSquare.getxMax());
            // 找拐角
            if (z > nextNavSquare.getzMax() || z > nowNavSquare.getzMax()) {
                startVector = new Vector3(nowNavSquare.getxMax(), y, Math.min(nextNavSquare.getzMax(), nowNavSquare.getzMax()));
                roadPointList.add(startVector);
            } else if (z < nextNavSquare.getzMin() || z < nowNavSquare.getzMin()) {
                startVector = new Vector3(nowNavSquare.getxMax(), y, Math.max(nextNavSquare.getzMin(), nowNavSquare.getzMin()));
                roadPointList.add(startVector);
            }
        }
        // 路線在上方
        else if (nowNavSquare.getzMin() == nextNavSquare.getzMax() + 1) {
            double x = startVector.getEquationX(nextVector, nowNavSquare.getzMin());
            // 找拐角
            if (x > nextNavSquare.getxMax() || x > nowNavSquare.getxMax()) {
                startVector = new Vector3(Math.min(nextNavSquare.getxMax(), nowNavSquare.getxMax()), y, nowNavSquare.getzMin());
                roadPointList.add(startVector);
            } else if (x < nextNavSquare.getxMin() || x < nowNavSquare.getxMin()) {
                startVector = new Vector3(Math.max(nextNavSquare.getxMin(), nowNavSquare.getxMin()), y, nowNavSquare.getzMin());
                roadPointList.add(startVector);
            }
        }
        // 路線在下方
        else if (nowNavSquare.getzMax() + 1 == nextNavSquare.getzMin()) {
            double x = startVector.getEquationX(nextVector, nowNavSquare.getzMax());
            // 找拐角
            if (x > nextNavSquare.getxMax() || x > nowNavSquare.getxMax()) {
                startVector = new Vector3(Math.min(nextNavSquare.getxMax(), nowNavSquare.getxMax()), y, nowNavSquare.getzMax());
                roadPointList.add(startVector);
            } else if (x < nextNavSquare.getxMin() || x < nowNavSquare.getxMin()) {
                startVector = new Vector3(Math.max(nextNavSquare.getxMin(), nowNavSquare.getxMin()), y, nowNavSquare.getzMax());
                roadPointList.add(startVector);
            }
        }

        depth++;
        return getRoadPoints(navSquares, roadPointList, depth, startVector, targetVector);
    }

    /**
     * 查找任意一条连通情况
     *
     * @param from
     * @param to
     * @param depth
     * @param maxDepth
     * @return
     */
    private List<NavSquare> search(NavSquare from, NavSquare to, int depth, int maxDepth) {
        if (from == to) {
            return new LinkedList<>();
        }

        if (depth > maxDepth) {
            return null;
        }

        for (NavSquare connectTo : from.getConnectTo()) {
            List<NavSquare> navSquares = search(connectTo, to, depth + 1, maxDepth);

            if (navSquares != null) {
                navSquares.add(0, connectTo);

                return navSquares;
            }
        }

        return null;
    }
}
