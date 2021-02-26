package com.particle.model.level.chunk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NavSquareCollection {

    private static final List<NavSquare> EMPTY_NAVSQUARE_LIST = Collections.unmodifiableList(new ArrayList<>(0));

    // NavSquare索引，index为y
    private NavSquareLayout[] navSquareLayouts = new NavSquareLayout[256];

    /**
     * 添加NavSquare
     *
     * @param navSquare
     */
    public void addNavSquare(NavSquare navSquare) {
        if (this.navSquareLayouts[navSquare.getY()] == null) {
            this.navSquareLayouts[navSquare.getY()] = new NavSquareLayout();
        }

        // 添加NavSquare
        this.navSquareLayouts[navSquare.getY()].addNavSquare(navSquare);
    }

    public NavSquare findNavSquare(int x, int y, int z) {
        NavSquareLayout navSquareLayout = this.navSquareLayouts[y];

        if (navSquareLayout == null) {
            return null;
        }

        return navSquareLayout.getNavSquare(x, z);
    }

    public List<NavSquare> findNavSquaresByX(int y, int x) {
        NavSquareLayout navSquareLayout = this.navSquareLayouts[y];

        if (navSquareLayout == null) {
            return EMPTY_NAVSQUARE_LIST;
        }

        return navSquareLayout.getNavSquaresInX(x);
    }

    public List<NavSquare> findNavSquaresByZ(int y, int z) {
        NavSquareLayout navSquareLayout = this.navSquareLayouts[y];

        if (navSquareLayout == null) {
            return EMPTY_NAVSQUARE_LIST;
        }

        return navSquareLayout.getNavSquaresInZ(z);
    }

    public List<NavSquare> resetLayout(int y) {
        NavSquareLayout navSquareLayout = this.navSquareLayouts[y];

        if (navSquareLayout == null) {
            return EMPTY_NAVSQUARE_LIST;
        } else {
            List<NavSquare> navSquares = navSquareLayout.getNavSquares();

            this.navSquareLayouts[y] = null;

            return navSquares;
        }
    }

    public void removeAll() {
        // 移除所有指向当前collection的链接
        for (NavSquareLayout navSquareLayout : this.navSquareLayouts) {
            if (navSquareLayout != null) {
                // 遍历所有的NavSquares
                for (NavSquare navSquare : navSquareLayout.getNavSquares()) {
                    for (NavSquare connectTo : navSquare.getConnectTo()) {
                        connectTo.removeConnectFrom(navSquare);
                        connectTo.removeConnectTo(navSquare);
                    }
                }
            }
        }

        // 重置connection
        for (int i = 0; i < this.navSquareLayouts.length; i++) {
            this.navSquareLayouts[i] = null;
        }
    }

    /**
     * NavSquare 单层分布快速索引
     */
    private class NavSquareLayout {
        // NavSquare索引，index为x
        private NavSquareList[] navSquaresX = new NavSquareList[16];
        // NavSquare索引，index为z
        private NavSquareList[] navSquaresZ = new NavSquareList[16];

        private NavSquare[][] navSquaresMap = new NavSquare[16][16];
        private List<NavSquare> navSquares = new ArrayList<>();

        public NavSquareLayout() {
            for (int i = 0; i < this.navSquaresX.length; i++) {
                this.navSquaresX[i] = new NavSquareList();
            }

            for (int i = 0; i < this.navSquaresZ.length; i++) {
                this.navSquaresZ[i] = new NavSquareList();
            }
        }

        /**
         * 索引NavSquare
         *
         * @param navSquare
         */
        public void addNavSquare(NavSquare navSquare) {
            for (int x = navSquare.getxMin(); x <= navSquare.getxMax(); x++) {
                this.navSquaresX[x & 0xf].add(navSquare);
            }

            for (int z = navSquare.getzMin(); z <= navSquare.getzMax(); z++) {
                this.navSquaresZ[z & 0xf].add(navSquare);
            }

            for (int x = navSquare.getxMin(); x <= navSquare.getxMax(); x++) {
                for (int z = navSquare.getzMin(); z <= navSquare.getzMax(); z++) {
                    this.navSquaresMap[x & 0xf][z & 0xf] = navSquare;
                }
            }

            this.navSquares.add(navSquare);
        }

        /**
         * 索引NavSquare
         *
         * @param x
         * @param z
         * @return
         */
        public NavSquare getNavSquare(int x, int z) {
            return this.navSquaresMap[x & 0xf][z & 0xf];
        }

        /**
         * 索引在此x轴上的NavSquare
         *
         * @param x
         * @return
         */
        public List<NavSquare> getNavSquaresInX(int x) {
            return this.navSquaresX[x & 0xf].getNavSquares();
        }

        /**
         * 索引在此z轴上的NavSquare
         *
         * @param z
         * @return
         */
        public List<NavSquare> getNavSquaresInZ(int z) {
            return this.navSquaresZ[z & 0xf].getNavSquares();
        }

        /**
         * 查询该层所有的NavSquare
         *
         * @return
         */
        public List<NavSquare> getNavSquares() {
            return navSquares;
        }
    }

    /**
     * NavSquare集合
     */
    private class NavSquareList {
        private List<NavSquare> navSquares = new ArrayList<>();

        public void add(NavSquare navSquare) {
            this.navSquares.add(navSquare);
        }

        public List<NavSquare> getNavSquares() {
            return navSquares;
        }
    }

}
