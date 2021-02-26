package com.particle.game.world.nav;

import com.particle.model.level.chunk.NavSquare;
import com.particle.model.math.Vector3;

import java.util.HashMap;
import java.util.Map;

public class AStar {
    public static final int STEP = 1;

    private Map<Node, NavSquare> openMap = new HashMap<>();
    private Map<Node, NavSquare> closeMap = new HashMap<>();

    public Node findMinFNodeInOpenList() {
        Node tempNode = new Node(0, 0, 0);
        tempNode.F = 99999;
        for (Node node : openMap.keySet()) {
            if (node.F < tempNode.F) {
                tempNode = node;
            }
        }

        return tempNode;
    }

    public Node findPath(NavSquare startNavSquare, Vector3 startVector, Vector3 targetVector) {
        Node startNode = new Node(startVector.getX(), startVector.getY(), startVector.getZ());
        Node endNode = new Node(targetVector.getX(), targetVector.getY(), targetVector.getZ());

        // 把起点加入 open list
        openMap.put(startNode, startNavSquare);

        // 超過 200 次運算則視為無法抵達
        int depth = 0;
        while (openMap.size() > 0 && depth < 200) {
            depth++;
            // 遍历 open map ，查找 F值最小的节点，把它作为当前要处理的节点
            Node currentNode = findMinFNodeInOpenList();
            // 从open list中移除
            NavSquare navSquare = openMap.remove(currentNode);

            // 把这个节点移到 close map
            closeMap.put(currentNode, navSquare);

            for (NavSquare nav : navSquare.getConnectTo()) {
                int targetVectorX = targetVector.getX();
                int targetVectorY = nav.getY();
                int targetVectorZ = targetVector.getZ();
                if (targetVector.getX() > nav.getxMax()) {
                    targetVectorX = nav.getxMax();
                } else if (targetVector.getX() < nav.getxMin()) {
                    targetVectorX = nav.getxMin();
                }

                if (targetVector.getZ() > nav.getzMax()) {
                    targetVectorZ = nav.getzMax();
                } else if (targetVector.getZ() < nav.getzMin()) {
                    targetVectorZ = nav.getzMin();
                }
                Node node = new Node(targetVectorX, targetVectorY, targetVectorZ);
                if (exists(openMap, node)) {
                    foundPoint(currentNode, node);
                } else {
                    notFoundPoint(currentNode, nav, endNode, node);
                }
            }

            // 若抵達終點
            Node node = find(openMap, endNode);
            if (node != null) {
                return node;
            }
        }

        return find(openMap, endNode);
    }

    private void foundPoint(Node tempStart, Node node) {
        int G = calcG(tempStart, node);
        if (G < node.G) {
            node.parent = tempStart;
            node.G = G;
            node.calcF();
        }
    }

    private void notFoundPoint(Node tempStart, NavSquare nav, Node end, Node node) {
        node.parent = tempStart;
        node.G = calcG(tempStart, node);
        node.H = calcH(end, node);
        node.calcF();
        openMap.put(node, nav);
    }

    private int calcG(Node start, Node node) {
        int G = STEP * (Math.abs(node.x - start.x) + Math.abs(node.y - start.y) + Math.abs(node.z - start.z));
        int parentG = node.parent != null ? node.parent.G : 0;
        return G + parentG;
    }

    private int calcH(Node end, Node node) {
        int step = Math.abs(node.x - end.x) + Math.abs(node.y - end.y) + Math.abs(node.z - end.z);
        return step * STEP;
    }

    public static Node find(Map<Node, NavSquare> nodes, Node point) {
        for (Node n : nodes.keySet())
            if ((n.x == point.x) && (n.y == point.y) && (n.z == point.z)) {
                return n;
            }
        return null;
    }

    public static boolean exists(Map<Node, NavSquare> nodes, Node node) {
        for (Node n : nodes.keySet()) {
            if ((n.x == node.x) && (n.y == node.y) && (n.z == node.z)) {
                return true;
            }
        }
        return false;
    }

    public static class Node {
        public Node(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public int x;
        public int y;
        public int z;

        public int F;
        public int G;
        public int H;

        public void calcF() {
            this.F = this.G + this.H;
        }

        public Node parent;
    }
}