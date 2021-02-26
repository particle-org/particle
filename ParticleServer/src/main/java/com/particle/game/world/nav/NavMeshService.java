package com.particle.game.world.nav;

import com.particle.game.world.level.ChunkService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.level.chunk.ChunkSection;
import com.particle.model.level.chunk.ChunkState;
import com.particle.model.level.chunk.NavSquare;
import com.particle.model.math.Vector3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class NavMeshService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NavMeshService.class);

    @Inject
    private ChunkService chunkService;

    /**
     * 标记连通情况
     *
     * @param level
     * @param chunk
     */
    public void markLayout(Level level, Chunk chunk) {
        ChunkSection[] chunkSections = chunk.getChunkSections();

        // 按高度分层
        for (int y = 0; y < 254; y++) {
            boolean[][] passable = this.standCheck(chunkSections, y);

            List<NavSquare> navSquares = this.caculateArea(passable, y);

            int[][] cell = this.visualNavSquare(navSquares);

            this.markChunkSection(y, cell, chunkSections);

            for (NavSquare navSquare : navSquares) {
                // 偏移至绝对坐标
                navSquare.setxMin(navSquare.getxMin() + (chunk.getxPos() * 16));
                navSquare.setxMax(navSquare.getxMax() + (chunk.getxPos() * 16));
                navSquare.setzMin(navSquare.getzMin() + (chunk.getzPos() * 16));
                navSquare.setzMax(navSquare.getzMax() + (chunk.getzPos() * 16));

                // 缓存NavSquare
                chunk.getNavSquares().addNavSquare(navSquare);

                // 计算连通情况
                this.caculateNavSquareConnection(level, navSquare);
            }
        }
    }

    /**
     * 刷新导航窗格
     *
     * @param level
     * @param chunk
     */
    public void calculateLayouts(Level level, Chunk chunk) {
        ChunkSection[] chunkSections = chunk.getChunkSections();

        // 按高度分层
        for (int y = 0; y < 254; y++) {
            boolean[][] passable = this.standCheck(chunkSections, y);

            List<NavSquare> navSquares = this.caculateArea(passable, y);

            for (NavSquare navSquare : navSquares) {
                // 偏移至绝对坐标
                navSquare.setxMin(navSquare.getxMin() + (chunk.getxPos() * 16));
                navSquare.setxMax(navSquare.getxMax() + (chunk.getxPos() * 16));
                navSquare.setzMin(navSquare.getzMin() + (chunk.getzPos() * 16));
                navSquare.setzMax(navSquare.getzMax() + (chunk.getzPos() * 16));

                // 缓存NavSquare
                chunk.getNavSquares().addNavSquare(navSquare);

                // 计算连通情况
                this.caculateNavSquareConnection(level, navSquare);
            }
        }
    }

    /**
     * 刷新指定层的navSquare
     *
     * @param level
     * @param chunk
     * @param y
     */
    public void refreshLayout(Level level, Chunk chunk, int y) {
        if (y < 0 || y > 255) {
            return;
        }

        // 暂时不开启刷新
        boolean refresh = false;
        if (!refresh) {
            return;
        }

        // 移除老的NavSquare
        List<NavSquare> navSquares = chunk.getNavSquares().resetLayout(y);
        for (NavSquare navSquare : navSquares) {
            for (NavSquare square : navSquare.getConnectFrom()) {
                square.removeConnectTo(navSquare);
            }
            for (NavSquare square : navSquare.getConnectTo()) {
                square.removeConnectFrom(navSquare);
            }
        }

        // 重新计算navSquare
        ChunkSection[] chunkSections = chunk.getChunkSections();
        boolean[][] passable = this.standCheck(chunkSections, y);

        // 重新计算NavSquare
        List<NavSquare> newNavSquares = this.caculateArea(passable, y);
        for (NavSquare navSquare : newNavSquares) {
            navSquare.setxMin(navSquare.getxMin() + (chunk.getxPos() * 16));
            navSquare.setxMax(navSquare.getxMax() + (chunk.getxPos() * 16));
            navSquare.setzMin(navSquare.getzMin() + (chunk.getzPos() * 16));
            navSquare.setzMax(navSquare.getzMax() + (chunk.getzPos() * 16));

            // 缓存NavSquare
            chunk.getNavSquares().addNavSquare(navSquare);

            // 计算连通情况
            this.caculateNavSquareConnection(level, navSquare);
        }
    }

    /**
     * 移除指定chunk所有的navSquare
     *
     * @param chunk
     */
    public void removeNavsquares(Chunk chunk) {
        chunk.getNavSquares().removeAll();
    }

    /**
     * 查找指定位置的NavSquare
     *
     * @param level
     * @param position
     * @return
     */
    public NavSquare findNavSquare(Level level, Vector3 position) {
        Chunk chunk = this.chunkService.indexChunk(level, position);

        position.setX(position.getX());
        position.setZ(position.getZ());

        return chunk.getNavSquares().findNavSquare(position.getX(), position.getY(), position.getZ());
    }

    /**
     * 计算指定Nav的连通情况
     *
     * @param level
     * @param navSquare
     */
    public void caculateNavSquareConnection(Level level, NavSquare navSquare) {
        Chunk currentChunk = this.chunkService.getChunk(level, navSquare.getxMin() >> 4, navSquare.getzMin() >> 4, false);

        int xMinOffset = navSquare.getxMin() & 0b1111;
        int zMinOffset = navSquare.getzMin() & 0b1111;
        int xMaxOffset = navSquare.getxMax() & 0b1111;
        int zMaxOffset = navSquare.getzMax() & 0b1111;

        if (xMinOffset == 0) {
            Chunk searchChunk = this.chunkService.getChunk(level, (navSquare.getxMin() - 1) >> 4, navSquare.getzMin() >> 4, false);

            if (searchChunk != null && searchChunk.getState() == ChunkState.RUNNING) {
                List<NavSquare> navSquares = new ArrayList<>(searchChunk.getNavSquares().findNavSquaresByX(navSquare.getY(), 15));
                if (navSquare.getY() > 0)
                    navSquares.addAll(searchChunk.getNavSquares().findNavSquaresByX(navSquare.getY() - 1, 15));
                for (NavSquare bindSquare : navSquares) {
                    if (!(navSquare.getzMax() < bindSquare.getzMin() || navSquare.getzMin() > bindSquare.getzMax())) {
                        this.bindNavSquaresConnection(navSquare, bindSquare);
                    }
                }

                if (navSquare.getY() < 255)
                    navSquares = searchChunk.getNavSquares().findNavSquaresByX(navSquare.getY() + 1, 15);
                for (NavSquare bindSquare : navSquares) {
                    if (!navSquare.getConnectTo().contains(bindSquare)) {
                        this.bindNavSquaresConnection(navSquare, bindSquare);
                    }
                }
            }
        } else {
            List<NavSquare> navSquares = new ArrayList<>(currentChunk.getNavSquares().findNavSquaresByX(navSquare.getY(), xMinOffset - 1));
            if (navSquare.getY() > 0)
                navSquares.addAll(currentChunk.getNavSquares().findNavSquaresByX(navSquare.getY() - 1, xMinOffset - 1));
            for (NavSquare bindSquare : navSquares) {
                if (!(navSquare.getzMax() < bindSquare.getzMin() || navSquare.getzMin() > bindSquare.getzMax())) {
                    this.bindNavSquaresConnection(navSquare, bindSquare);
                }
            }
        }

        if (xMaxOffset == 15) {
            Chunk searchChunk = this.chunkService.getChunk(level, (navSquare.getxMax() + 1) >> 4, navSquare.getzMax() >> 4, false);

            if (searchChunk != null && searchChunk.getState() == ChunkState.RUNNING) {
                List<NavSquare> navSquares = new ArrayList<>(searchChunk.getNavSquares().findNavSquaresByX(navSquare.getY(), 0));
                if (navSquare.getY() > 0)
                    navSquares.addAll(searchChunk.getNavSquares().findNavSquaresByX(navSquare.getY() - 1, 0));
                for (NavSquare bindSquare : navSquares) {
                    if (!(navSquare.getzMax() < bindSquare.getzMin() || navSquare.getzMin() > bindSquare.getzMax())) {
                        this.bindNavSquaresConnection(navSquare, bindSquare);
                    }
                }

                if (navSquare.getY() < 255)
                    navSquares = searchChunk.getNavSquares().findNavSquaresByX(navSquare.getY() + 1, 0);
                for (NavSquare bindSquare : navSquares) {
                    if (!navSquare.getConnectTo().contains(bindSquare)) {
                        this.bindNavSquaresConnection(navSquare, bindSquare);
                    }
                }
            }
        } else {
            List<NavSquare> navSquares = new ArrayList<>(currentChunk.getNavSquares().findNavSquaresByX(navSquare.getY(), xMaxOffset + 1));
            if (navSquare.getY() > 0)
                navSquares.addAll(currentChunk.getNavSquares().findNavSquaresByX(navSquare.getY() - 1, xMaxOffset + 1));
            for (NavSquare bindSquare : navSquares) {
                if (!(navSquare.getzMax() < bindSquare.getzMin() || navSquare.getzMin() > bindSquare.getzMax())) {
                    this.bindNavSquaresConnection(navSquare, bindSquare);
                }
            }
        }

        if (zMinOffset == 0) {
            Chunk searchChunk = this.chunkService.getChunk(level, navSquare.getxMin() >> 4, (navSquare.getzMin() - 1) >> 4, false);

            if (searchChunk != null && searchChunk.getState() == ChunkState.RUNNING) {
                List<NavSquare> navSquares = new ArrayList<>(searchChunk.getNavSquares().findNavSquaresByZ(navSquare.getY(), 15));
                if (navSquare.getY() > 0)
                    navSquares.addAll(searchChunk.getNavSquares().findNavSquaresByZ(navSquare.getY() - 1, 15));
                for (NavSquare bindSquare : navSquares) {
                    if (!(navSquare.getxMax() < bindSquare.getxMin() || navSquare.getxMin() > bindSquare.getxMax())) {
                        this.bindNavSquaresConnection(navSquare, bindSquare);
                    }
                }

                if (navSquare.getY() < 255)
                    navSquares = searchChunk.getNavSquares().findNavSquaresByZ(navSquare.getY() + 1, 15);
                for (NavSquare bindSquare : navSquares) {
                    if (!navSquare.getConnectTo().contains(bindSquare)) {
                        this.bindNavSquaresConnection(navSquare, bindSquare);
                    }
                }
            }
        } else {
            List<NavSquare> navSquares = new ArrayList<>(currentChunk.getNavSquares().findNavSquaresByZ(navSquare.getY(), zMinOffset - 1));
            if (navSquare.getY() > 0)
                navSquares.addAll(currentChunk.getNavSquares().findNavSquaresByZ(navSquare.getY() - 1, zMinOffset - 1));
            for (NavSquare bindSquare : navSquares) {
                if (!(navSquare.getxMax() < bindSquare.getxMin() || navSquare.getxMin() > bindSquare.getxMax())) {
                    this.bindNavSquaresConnection(navSquare, bindSquare);
                }
            }
        }

        if (zMaxOffset == 15) {
            Chunk searchChunk = this.chunkService.getChunk(level, navSquare.getxMin() >> 4, (navSquare.getzMax() + 1) >> 4, false);

            if (searchChunk != null && searchChunk.getState() == ChunkState.RUNNING) {
                List<NavSquare> navSquares = new ArrayList<>(searchChunk.getNavSquares().findNavSquaresByZ(navSquare.getY(), 0));
                if (navSquare.getY() > 0)
                    navSquares.addAll(searchChunk.getNavSquares().findNavSquaresByZ(navSquare.getY() - 1, 0));
                for (NavSquare bindSquare : navSquares) {
                    if (!(navSquare.getxMax() < bindSquare.getxMin() || navSquare.getxMin() > bindSquare.getxMax())) {
                        this.bindNavSquaresConnection(navSquare, bindSquare);
                    }
                }

                if (navSquare.getY() < 255)
                    navSquares = searchChunk.getNavSquares().findNavSquaresByZ(navSquare.getY() + 1, 0);
                for (NavSquare bindSquare : navSquares) {
                    if (!navSquare.getConnectTo().contains(bindSquare)) {
                        this.bindNavSquaresConnection(navSquare, bindSquare);
                    }
                }
            }
        } else {
            List<NavSquare> navSquares = new ArrayList<>(currentChunk.getNavSquares().findNavSquaresByZ(navSquare.getY(), zMaxOffset + 1));
            if (navSquare.getY() > 0)
                navSquares.addAll(currentChunk.getNavSquares().findNavSquaresByZ(navSquare.getY() - 1, zMaxOffset + 1));
            for (NavSquare bindSquare : navSquares) {
                if (!(navSquare.getxMax() < bindSquare.getxMin() || navSquare.getxMin() > bindSquare.getxMax())) {
                    this.bindNavSquaresConnection(navSquare, bindSquare);
                }
            }
        }
    }

    /**
     * 绑定两个NavSquare的连通关系
     *
     * @param navSquare
     * @param uncheckedNavSquare
     */
    private void bindNavSquaresConnection(NavSquare navSquare, NavSquare uncheckedNavSquare) {
        navSquare.addConnectTo(uncheckedNavSquare);
        uncheckedNavSquare.addConnectTo(navSquare);
        navSquare.addConnectFrom(uncheckedNavSquare);
        uncheckedNavSquare.addConnectFrom(navSquare);
    }

    /**
     * 跳过初始部分
     *
     * @param current
     * @param navSquares
     * @return
     */
    private int indexNavSquares(NavSquare current, List<NavSquare> navSquares) {
        int checkIndex = 0;
        for (int i = 0; i < navSquares.size(); i++) {
            if (current.getY() - navSquares.get(i).getY() > -2) {
                checkIndex = i;
            }
        }

        return checkIndex;
    }

    /**
     * 计算连通情况
     *
     * @param navSquare
     * @param uncheckedSquares
     */
    private void findClosestNavSquare(NavSquare navSquare, List<NavSquare> uncheckedSquares, int x, int z, int minHeight) {
        // 计算连通情况
        for (int j = 0; j < uncheckedSquares.size(); j++) {
            NavSquare checkNav = uncheckedSquares.get(j);

            // 遇到障碍物
            if (minHeight > checkNav.getY()) {
                break;
            }

            // 遇到Nav
            if (checkNav.getxMin() <= x
                    && checkNav.getxMax() >= x
                    && checkNav.getzMin() <= z
                    && checkNav.getzMax() >= z) {
                if (navSquare.getY() - checkNav.getY() > -2) {
                    navSquare.addConnectTo(checkNav);
                    checkNav.addConnectFrom(navSquare);
                }

                if (checkNav.getY() - navSquare.getY() > -2) {
                    checkNav.addConnectTo(navSquare);
                    navSquare.addConnectFrom(checkNav);
                }
            }
        }
    }

    /**
     * 可通行区域检测
     *
     * @param chunkSections
     * @param y
     * @return
     */
    private boolean[][] standCheck(ChunkSection[] chunkSections, int y) {
        boolean[][] passable = new boolean[16][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                passable[i][j] = false;
            }
        }

        // 标记可站立像素
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                short stand = (y >> 4) < chunkSections.length && chunkSections[y >> 4] != null ? chunkSections[y >> 4].getBlockId(x, y & 0xf, z) : 0;
                short height1 = ((y + 1) >> 4) < chunkSections.length && chunkSections[(y + 1) >> 4] != null ? chunkSections[(y + 1) >> 4].getBlockId(x, (y + 1) & 0xf, z) : 0;
                short height2 = ((y + 2) >> 4) < chunkSections.length && chunkSections[(y + 2) >> 4] != null ? chunkSections[(y + 2) >> 4].getBlockId(x, (y + 2) & 0xf, z) : 0;

                if (!Block.getBlockType(stand).getBlockGeometry().canPassThrow()
                        && Block.getBlockType(height1).getBlockGeometry().canPassThrow()
                        && Block.getBlockType(height2).getBlockGeometry().canPassThrow()) {

                    passable[x][z] = true;
                }
            }
        }

        return passable;
    }

    /**
     * 子区域划分
     *
     * @param passable
     * @return
     */
    private List<NavSquare> caculateArea(boolean[][] passable, int y) {
        List<NavSquare> navSquares = new ArrayList<>();

        // 子区域划分
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (passable[x][z]) {

                    // 创建新的导航窗格
                    NavSquare navSquare = new NavSquare();
                    navSquares.add(navSquare);

                    navSquare.setxMin(x);
                    navSquare.setzMin(z);
                    navSquare.setxMax(x);
                    navSquare.setzMax(z);
                    navSquare.setY(y);

                    // 计算横向长度
                    int length = 16;
                    for (int w = 0; w < 16; w++) {
                        if (x + w < 16 && passable[x + w][z]) {
                            passable[x + w][z] = false;
                        } else {
                            length = w;
                            break;
                        }
                    }
                    navSquare.setxMax(x + length - 1);

                    // 计算纵向长度
                    for (int l = 1; l < 16; l++) {
                        // 每次检查一行
                        if (z + l < 16) {
                            int currentLength = length;
                            for (int w = 0; w < length; w++) {
                                if (x + w > 15 || !passable[x + w][z + l]) {
                                    currentLength = w;
                                    break;
                                }
                            }

                            if (currentLength == length) {
                                // 若检查通过，则标记
                                for (int w = 0; w < currentLength; w++) {
                                    passable[x + w][z + l] = false;
                                }
                                navSquare.setzMax(z + l);
                            } else {
                                // 若检查不通过，则跳出
                                break;
                            }
                        }
                    }
                }
            }
        }

        return navSquares;
    }

    /**
     * 计算区块内部的navsquare连接情况
     *
     * @param navSquares
     * @param chunkSections
     */
    private void caculateNavSquaresConnection(List<NavSquare> navSquares, ChunkSection[] chunkSections) {
        // 建立映射关系
        for (int i = 0; i < navSquares.size(); i++) {
            NavSquare navSquare = navSquares.get(i);

            for (int x = navSquare.getxMin(); x <= navSquare.getxMax(); x++) {
                int z = navSquare.getzMin() - 1;

                int h = navSquare.getY();
                for (; h >= 0; h--) {
                    if (!Block.getBlock(chunkSections[h >> 4].getBlockId(x, h, z)).getType().getBlockGeometry().canPassThrow()) {
                        break;
                    }
                }
                for (int j = i; j < navSquares.size(); j++) {
                    NavSquare checkNav = navSquares.get(j);

                    if (h > checkNav.getY()) {
                        break;
                    }

                    if (checkNav.getxMin() <= x
                            && checkNav.getxMax() >= x
                            && checkNav.getzMin() <= z
                            && checkNav.getzMax() >= z) {
                        navSquare.addConnectTo(checkNav);
                        checkNav.addConnectFrom(navSquare);

                        if (navSquare.getY() - checkNav.getY() < 2) {
                            checkNav.addConnectTo(navSquare);
                            navSquare.addConnectFrom(checkNav);
                        }
                    }


                }

                z = navSquare.getzMax() + 1;
                h = navSquare.getY();
                for (; h >= 0; h--) {
                    if (!Block.getBlock(chunkSections[h >> 4].getBlockId(x, h, z)).getType().getBlockGeometry().canPassThrow()) {
                        break;
                    }
                }
                for (int j = i; j < navSquares.size(); j++) {
                    NavSquare checkNav = navSquares.get(j);

                    if (h > checkNav.getY()) {
                        break;
                    }

                    if (checkNav.getxMin() <= x
                            && checkNav.getxMax() >= x
                            && checkNav.getzMin() <= z
                            && checkNav.getzMax() >= z) {
                        navSquare.addConnectTo(checkNav);
                        checkNav.addConnectFrom(navSquare);

                        if (navSquare.getY() - checkNav.getY() < 2) {
                            checkNav.addConnectTo(navSquare);
                            navSquare.addConnectFrom(checkNav);
                        }
                    }
                }
            }

            for (int z = navSquare.getzMin(); z <= navSquare.getzMax(); z++) {
                int x = navSquare.getxMin() - 1;

                int h = navSquare.getY();
                for (; h >= 0; h--) {
                    if (!Block.getBlock(chunkSections[h >> 4].getBlockId(x, h & 0xf, z)).getType().getBlockGeometry().canPassThrow()) {
                        break;
                    }
                }
                for (int j = i; j < navSquares.size(); j++) {
                    NavSquare checkNav = navSquares.get(j);

                    if (h > checkNav.getY()) {
                        break;
                    }

                    if (checkNav.getxMin() <= x
                            && checkNav.getxMax() >= x
                            && checkNav.getzMin() <= z
                            && checkNav.getzMax() >= z) {
                        navSquare.addConnectTo(checkNav);
                        checkNav.addConnectFrom(navSquare);

                        if (navSquare.getY() - checkNav.getY() < 2) {
                            checkNav.addConnectTo(navSquare);
                            navSquare.addConnectFrom(checkNav);
                        }
                    }
                }

                x = navSquare.getxMax() + 1;
                h = navSquare.getY();
                for (; h >= 0; h--) {
                    if (!Block.getBlock(chunkSections[h >> 4].getBlockId(x, h & 0xf, z)).getType().getBlockGeometry().canPassThrow()) {
                        break;
                    }
                }
                for (int j = i; j < navSquares.size(); j++) {
                    NavSquare checkNav = navSquares.get(j);

                    if (h > checkNav.getY()) {
                        break;
                    }

                    if (checkNav.getxMin() <= x
                            && checkNav.getxMax() >= x
                            && checkNav.getzMin() <= z
                            && checkNav.getzMax() >= z) {
                        navSquare.addConnectTo(checkNav);
                        checkNav.addConnectFrom(navSquare);

                        if (navSquare.getY() - checkNav.getY() < 2) {
                            checkNav.addConnectTo(navSquare);
                            navSquare.addConnectFrom(checkNav);
                        }
                    }
                }
            }
        }
    }

    /**
     * 计算连通区域 [弃用]
     *
     * @param passable
     * @return
     */
    private int[][] caculateConnectedArea(boolean[][] passable) {
        // 初始化连通区域标记
        int[][] cell = new int[18][18];
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                cell[i][j] = -1;
            }
        }

        // 初始化标记值
        int mark = 0;

        // 相连区域标记
        List<Set<Integer>> connectedArea = new ArrayList<>();

        // 两次扫描标记连通区域
        // Step 1 : 第一次扫描
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (passable[x][z]) {

                    int i = x + 1;
                    int j = z + 1;

                    // 扫描附近像素的情况
                    if (cell[i - 1][j] != -1) {
                        cell[i][j] = cell[i - 1][j];
                    } else if (cell[i][j + 1] != -1) {
                        cell[i][j] = cell[i][j + 1];
                    } else if (cell[i + 1][j] != -1) {
                        cell[i][j] = cell[i + 1][j];
                    } else if (cell[i][j - 1] != -1) {
                        cell[i][j] = cell[i][j - 1];
                    } else {
                        cell[i][j] = mark++;

                        connectedArea.add(new HashSet<>());
                    }

                    // 相连区域标记
                    if (cell[i - 1][j] != -1 && cell[i - 1][j] != cell[i][j]) {
                        connectedArea.get(cell[i][j]).add(cell[i - 1][j]);
                        connectedArea.get(cell[i - 1][j]).add(cell[i][j]);
                    }
                    if (cell[i + 1][j] != -1 && cell[i + 1][j] != cell[i][j]) {
                        connectedArea.get(cell[i][j]).add(cell[i + 1][j]);
                        connectedArea.get(cell[i + 1][j]).add(cell[i][j]);
                    }
                    if (cell[i][j + 1] != -1 && cell[i][j + 1] != cell[i][j]) {
                        connectedArea.get(cell[i][j]).add(cell[i][j + 1]);
                        connectedArea.get(cell[i][j + 1]).add(cell[i][j]);
                    }
                    if (cell[i][j - 1] != -1 && cell[i][j - 1] != cell[i][j]) {
                        connectedArea.get(cell[i][j]).add(cell[i][j - 1]);
                        connectedArea.get(cell[i][j - 1]).add(cell[i][j]);
                    }
                }
            }
        }

        // Step 2 : 合并连通区域
        // 计算像素迁移策略
        Map<Integer, Integer> changeMap = new HashMap<>();
        for (int i = 0; i < connectedArea.size(); i++) {
            Set<Integer> connected = connectedArea.get(i);

            if (connected.size() == 0) {
                continue;
            }

            // 查找最小数字
            int min = Integer.MAX_VALUE;
            for (Integer number : connected) {
                if (min > number) {
                    min = number;
                }
            }

            // 迭代向下查询
            if (changeMap.containsKey(min)) {
                min = changeMap.get(min);
            }

            // 缓存数字迁移情况
            if (i > min) {
                changeMap.put(i, min);
            }
        }

        // 连接连通区域
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int i = x + 1;
                int j = z + 1;

                if (changeMap.containsKey(cell[i][j])) {
                    cell[i][j] = changeMap.get(cell[i][j]);
                }
            }
        }

        return cell;
    }

    /**
     * 可视化导航窗格
     *
     * @param navSquares
     * @return
     */
    private int[][] visualNavSquare(List<NavSquare> navSquares) {
        int[][] cell = new int[16][16];
        int color = 1;
        for (NavSquare navSquare : navSquares) {
            for (int i = navSquare.getxMin(); i <= navSquare.getxMax(); i++) {
                for (int j = navSquare.getzMin(); j <= navSquare.getzMax(); j++) {
                    cell[i & 15][j & 15] = color;
                }
            }
            color++;
        }

        return cell;
    }

    /**
     * 标记可视化结果
     *
     * @param y
     * @param cell
     * @param chunkSections
     */
    private void markChunkSection(int y, int[][] cell, ChunkSection[] chunkSections) {
        // 标记像素颜色
        int offset = (int) (Math.random() * 16);
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (cell[i][j] != 0) {
                    chunkSections[y >> 4].setBlock(i, y, j, BlockPrototype.WOOL.getId(), (cell[i][j] + offset) % 16);
                }
            }
        }
    }
}
