package com.particle.game.world.level.generate;

import com.particle.model.level.IChunkGenerate;
import com.particle.model.level.chunk.ChunkData;
import com.particle.model.level.chunk.ChunkSection;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;

@Singleton
public class TunnelGenerate implements IChunkGenerate {

    private static final int WALL_BLOCK_ID = 160;
    private static final int STAND_BLOCK_ID = 35;
    private static final int GUIDE_BLOCK_ID = 89;
    private static final int START_BLOCK_ID = 35;

    private static final int HEIGHT_ON_READY = 200;

    private static final int SOUTH = 0;
    private static final int WEST = 1;
    private static final int NORTH = 2;
    private static final int EAST = 3;

    private static final int STEP = 3;

    private int startX;
    private int startZ;
    private int endX;
    private int endZ;

    private int spawnX;
    private int spawnZ;

    private int stopX;
    private int stopZ;

    private ChunkData[][] chunkDataCache;

    private ChunkData emptyChunk;

    public TunnelGenerate() {
        this.startX = 0;
        this.startZ = 0;
        this.endX = 16;
        this.endZ = 16;
        this.spawnX = 0;
        this.spawnZ = 0;

        this.chunkDataCache = new ChunkData[endX - startX + 1][endZ - startZ + 1];
        for (int i = 0; i < chunkDataCache.length; i++) {
            for (int j = 0; j < chunkDataCache[i].length; j++) {
                chunkDataCache[i][j] = new ChunkData();
                chunkDataCache[i][j].setxPos(i);
                chunkDataCache[i][j].setzPos(j);
                chunkDataCache[i][j].setBiomColors(new byte[256]);
                chunkDataCache[i][j].setHeightMap(new byte[256]);
                chunkDataCache[i][j].setMobEntities(new LinkedList<>());
                chunkDataCache[i][j].setItemEntities(new LinkedList<>());
                chunkDataCache[i][j].setTileEntities(new LinkedList<>());
                chunkDataCache[i][j].setExtraData(new byte[0]);

                ChunkSection[] chunkSections = new ChunkSection[16];
                for (byte k = 0; k < 16; k++) {
                    ChunkSection chunkSection = new ChunkSection(k);
                    chunkSections[k] = chunkSection;
                }
                chunkDataCache[i][j].setSections(chunkSections);
            }
        }

        this.emptyChunk = new ChunkData();
        this.emptyChunk.setxPos(0);
        this.emptyChunk.setzPos(0);
        this.emptyChunk.setBiomColors(new byte[256]);
        this.emptyChunk.setHeightMap(new byte[256]);
        this.emptyChunk.setMobEntities(new LinkedList<>());
        this.emptyChunk.setItemEntities(new LinkedList<>());
        this.emptyChunk.setTileEntities(new LinkedList<>());
        this.emptyChunk.setExtraData(new byte[0]);
        ChunkSection[] chunkSections = new ChunkSection[16];
        for (byte k = 0; k < 16; k++) {
            ChunkSection chunkSection = new ChunkSection(k);
            chunkSections[k] = chunkSection;
        }
        this.emptyChunk.setSections(chunkSections);
    }

    @Override
    public ChunkData getEmptyChunk(int xPos, int zPos) {
        if (xPos < startX || xPos > endX || zPos < startZ || zPos > endZ) {
            this.emptyChunk.setxPos(xPos);
            this.emptyChunk.setzPos(zPos);

            return this.emptyChunk;
        }

        return this.chunkDataCache[xPos][zPos];
    }

    @Inject
    public void init() {
        int currentHeight = 240;

        int currentX = spawnX;
        int currentZ = spawnZ;

        //记录每个section空间的占用情况
        int[][][] sectionUsed = new int[endX - startX + 1][16][endZ - startZ + 1];

        sectionUsed[currentX][currentHeight >> 4][currentZ] = 240;

        boolean state = deepCheck(currentX, currentHeight, currentZ, sectionUsed);

        if (!state) {
            throw new RuntimeException("Fail to generate tunnel");
        }

        this.deepGenerate(currentX, currentHeight, currentZ, sectionUsed);
        this.generateStart(startX, startZ);
        this.generateEnd(stopX, stopZ);
    }

    private boolean deepCheck(int currentX, int currentHeight, int currentZ, int[][][] sectionUsed) {
        //递归条件
        if (currentHeight == 0) {
            this.stopX = currentX;
            this.stopZ = currentZ;

            return true;
        }

        //下降高度
        int height = currentHeight - STEP;
        int chunkHeight = height >> 4;
        //额外的y轴检查
        int externalCheck = ((height % 16) == 0 ? 0 : 1);

        if (height > HEIGHT_ON_READY) {
            int xOffset = currentX;
            int zOffset = currentZ + 1;

            //检查是否重叠
            if (sectionUsed[xOffset][chunkHeight][zOffset] == 0 && sectionUsed[xOffset][chunkHeight + externalCheck][zOffset] == 0) {
                sectionUsed[xOffset][chunkHeight][zOffset] = height;
                sectionUsed[xOffset][chunkHeight + externalCheck][zOffset] = height;

                boolean state = deepCheck(xOffset, height, zOffset, sectionUsed);

                if (state) {
                    return true;
                }

                sectionUsed[xOffset][chunkHeight][zOffset] = 0;
                sectionUsed[xOffset][chunkHeight + externalCheck][zOffset] = 0;
            }
        } else {
            //随机方向
            int[] directions = new int[]{1, 2, 3, 4};
            this.shuffle(directions);
            for (int direction : directions) {
                int xOffset = (direction & 1) == 0 ? (direction < 3 ? 1 : -1) : 0;
                int zOffset = (direction & 1) == 1 ? (direction < 3 ? 1 : -1) : 0;

                //换算四周
                xOffset += currentX;
                zOffset += currentZ;

                //超过边界
                if (xOffset < startX || xOffset > endX || zOffset < startZ || zOffset > endZ) {
                    continue;
                }

                //检查是否重叠
                if (sectionUsed[xOffset][chunkHeight][zOffset] == 0 && sectionUsed[xOffset][chunkHeight + externalCheck][zOffset] == 0) {
                    sectionUsed[xOffset][chunkHeight][zOffset] = height;
                    sectionUsed[xOffset][chunkHeight + externalCheck][zOffset] = height;

                    boolean state = deepCheck(xOffset, height, zOffset, sectionUsed);

                    if (state) {
                        return true;
                    }

                    sectionUsed[xOffset][chunkHeight][zOffset] = 0;
                    sectionUsed[xOffset][chunkHeight + externalCheck][zOffset] = 0;
                }
            }
        }

        return false;
    }

    private void deepGenerate(int currentX, int currentHeight, int currentZ, int[][][] sectionUsed) {
        this.generateCube(currentX, currentZ, currentHeight, sectionUsed);

        this.generateConnectHole(currentX, currentZ, currentHeight, sectionUsed);
        this.generateGuideLine(currentX, currentZ, currentHeight, sectionUsed);

        if (currentHeight == 0)
            return;

        int checkSectionY = (currentHeight - STEP) / 16;
        if (currentX > 0 && sectionUsed[currentX - 1][checkSectionY][currentZ] == currentHeight - STEP) {
            deepGenerate(currentX - 1, currentHeight - STEP, currentZ, sectionUsed);
        } else if (currentX < 16 && sectionUsed[currentX + 1][checkSectionY][currentZ] == currentHeight - STEP) {
            deepGenerate(currentX + 1, currentHeight - STEP, currentZ, sectionUsed);
        } else if (currentZ < 16 && sectionUsed[currentX][checkSectionY][currentZ + 1] == currentHeight - STEP) {
            deepGenerate(currentX, currentHeight - STEP, currentZ + 1, sectionUsed);
        } else if (currentZ > 0 && sectionUsed[currentX][checkSectionY][currentZ - 1] == currentHeight - STEP) {
            deepGenerate(currentX, currentHeight - STEP, currentZ - 1, sectionUsed);
        } else {
            throw new RuntimeException("Generate Fail");
        }
    }

    private void generateCube(int currentX, int currentZ, int currentHeight, int[][][] sectionUsed) {
        int color = 0;

        if (currentHeight > HEIGHT_ON_READY && currentHeight < 240) {
            //构造边框
            //挖洞
            int direction = checkDirection(currentHeight + STEP, currentX, currentZ, sectionUsed);

            switch (direction) {
                case EAST:
                    for (int j = 0; j < 16; j++) {
                        this.setBlockAt(currentX, currentZ, 0, currentHeight, j, STAND_BLOCK_ID);
                    }
                    for (int j = 0; j < 16; j++) {
                        this.setBlockAt(currentX, currentZ, 0, currentHeight + 15, j, STAND_BLOCK_ID);
                    }
                    for (int j = 1; j < 15; j++) {
                        this.setBlockAt(currentX, currentZ, 0, currentHeight + j, 0, STAND_BLOCK_ID);
                    }
                    for (int j = 1; j < 15; j++) {
                        this.setBlockAt(currentX, currentZ, 0, currentHeight + j, 15, STAND_BLOCK_ID);
                    }
                    break;
                case WEST:
                    for (int j = 0; j < 16; j++) {
                        this.setBlockAt(currentX, currentZ, 15, currentHeight, j, STAND_BLOCK_ID);
                    }
                    for (int j = 0; j < 16; j++) {
                        this.setBlockAt(currentX, currentZ, 15, currentHeight + 15, j, STAND_BLOCK_ID);
                    }
                    for (int j = 1; j < 15; j++) {
                        this.setBlockAt(currentX, currentZ, 15, currentHeight + j, 0, STAND_BLOCK_ID);
                    }
                    for (int j = 1; j < 15; j++) {
                        this.setBlockAt(currentX, currentZ, 15, currentHeight + j, 15, STAND_BLOCK_ID);
                    }
                    break;
                case SOUTH:
                    for (int j = 0; j < 16; j++) {
                        this.setBlockAt(currentX, currentZ, j, currentHeight, 15, STAND_BLOCK_ID);
                    }
                    for (int j = 0; j < 16; j++) {
                        this.setBlockAt(currentX, currentZ, j, currentHeight + 15, 15, STAND_BLOCK_ID);
                    }
                    for (int j = 1; j < 15; j++) {
                        this.setBlockAt(currentX, currentZ, 0, currentHeight + j, 15, STAND_BLOCK_ID);
                    }
                    for (int j = 1; j < 15; j++) {
                        this.setBlockAt(currentX, currentZ, 15, currentHeight + j, 15, STAND_BLOCK_ID);
                    }
                    break;
                case NORTH:
                    for (int j = 0; j < 16; j++) {
                        this.setBlockAt(currentX, currentZ, j, currentHeight, 0, STAND_BLOCK_ID);
                    }
                    for (int j = 0; j < 16; j++) {
                        this.setBlockAt(currentX, currentZ, j, currentHeight + 15, 0, STAND_BLOCK_ID);
                    }
                    for (int j = 1; j < 15; j++) {
                        this.setBlockAt(currentX, currentZ, 0, currentHeight + j, 0, STAND_BLOCK_ID);
                    }
                    for (int j = 1; j < 15; j++) {
                        this.setBlockAt(currentX, currentZ, 15, currentHeight + j, 0, STAND_BLOCK_ID);
                    }
            }
        } else {
            //构造正方体
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    this.setBlockAt(currentX, currentZ, i, currentHeight + j, 0, WALL_BLOCK_ID, color);
                    this.setBlockAt(currentX, currentZ, i, currentHeight + j, 15, WALL_BLOCK_ID, color);

                    this.setBlockAt(currentX, currentZ, 0, currentHeight + i, j, WALL_BLOCK_ID, color);
                    this.setBlockAt(currentX, currentZ, 15, currentHeight + i, j, WALL_BLOCK_ID, color);
                }
            }

            //构造屋顶
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    this.setBlockAt(currentX, currentZ, i, currentHeight, j, STAND_BLOCK_ID, color);
                    this.setBlockAt(currentX, currentZ, i, currentHeight + 15, j, STAND_BLOCK_ID, color);
                }
            }
        }
    }

    private void generateStart(int chunkX, int chunkZ) {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                this.setBlockAt(chunkX, chunkZ, i, 247, j, START_BLOCK_ID, ((i & 1) == 0) ? (((j & 1) == 0) ? 0 : 15) : (((j & 1) == 0) ? 15 : 0));
            }
        }
    }

    private void generateEnd(int chunkX, int chunkZ) {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                this.setBlockAt(chunkX, chunkZ, i, 3, j, START_BLOCK_ID, ((i & 1) == 0) ? (((j & 1) == 0) ? 0 : 15) : (((j & 1) == 0) ? 15 : 0));
            }
        }
    }

    private void generateGuideLine(int currentX, int currentZ, int currentHeight, int[][][] sectionUsed) {
        if (currentHeight >= HEIGHT_ON_READY) {
            return;
        }

        //构造指引
        if (currentHeight < 240) {
            int direction = checkDirection(currentHeight + STEP, currentX, currentZ, sectionUsed);
            switch (direction) {
                case EAST:
                    for (int i = 0; i < 8; i++) {
                        this.setBlockAt(currentX, currentZ, i, currentHeight, 7, GUIDE_BLOCK_ID);
                    }
                    break;
                case WEST:
                    for (int i = 8; i < 16; i++) {
                        this.setBlockAt(currentX, currentZ, i, currentHeight, 7, GUIDE_BLOCK_ID);
                    }
                    break;
                case SOUTH:
                    for (int i = 8; i < 16; i++) {
                        this.setBlockAt(currentX, currentZ, 7, currentHeight, i, GUIDE_BLOCK_ID);
                    }
                    break;
                case NORTH:
                    for (int i = 0; i < 8; i++) {
                        this.setBlockAt(currentX, currentZ, 7, currentHeight, i, GUIDE_BLOCK_ID);
                    }
            }
        }
        if (currentHeight > 0) {
            int direction = checkDirection(currentHeight - STEP, currentX, currentZ, sectionUsed);
            switch (direction) {
                case EAST:
                    for (int i = 0; i < 8; i++) {
                        this.setBlockAt(currentX, currentZ, i, currentHeight, 7, GUIDE_BLOCK_ID);
                    }
                    break;
                case WEST:
                    for (int i = 8; i < 16; i++) {
                        this.setBlockAt(currentX, currentZ, i, currentHeight, 7, GUIDE_BLOCK_ID);
                    }
                    break;
                case SOUTH:
                    for (int i = 8; i < 16; i++) {
                        this.setBlockAt(currentX, currentZ, 7, currentHeight, i, GUIDE_BLOCK_ID);
                    }
                    break;
                case NORTH:
                    for (int i = 0; i < 8; i++) {
                        this.setBlockAt(currentX, currentZ, 7, currentHeight, i, GUIDE_BLOCK_ID);
                    }
            }
        }
    }

    private int checkDirection(int heightValue, int currentX, int currentZ, int[][][] sectionUsed) {
        int sectionY = heightValue / 16;
        if (currentX > 0 && sectionUsed[currentX - 1][sectionY][currentZ] == heightValue) {
            return EAST;
        } else if (currentX < 16 && sectionUsed[currentX + 1][sectionY][currentZ] == heightValue) {
            return WEST;
        } else if (currentZ < 16 && sectionUsed[currentX][sectionY][currentZ + 1] == heightValue) {
            return SOUTH;
        } else if (currentZ > 0 && sectionUsed[currentX][sectionY][currentZ - 1] == heightValue) {
            return NORTH;
        } else {
            throw new RuntimeException("Generate Fail");
        }
    }

    private void generateConnectHole(int currentX, int currentZ, int currentHeight, int[][][] sectionUsed) {

        int heightStart = STEP + 1;

        //挖洞
        if (currentHeight < 240) {
            int direction = checkDirection(currentHeight + STEP, currentX, currentZ, sectionUsed);

            switch (direction) {
                case EAST:
                    for (int height = heightStart; height < 15; height++) {
                        for (int j = 1; j < 15; j++) {
                            this.setBlockAt(currentX, currentZ, 0, currentHeight + height, j, 0);
                        }
                    }
                    break;
                case WEST:
                    for (int height = heightStart; height < 15; height++) {
                        for (int j = 1; j < 15; j++) {
                            this.setBlockAt(currentX, currentZ, 15, currentHeight + height, j, 0);
                        }
                    }
                    break;
                case SOUTH:
                    for (int height = heightStart; height < 15; height++) {
                        for (int j = 1; j < 15; j++) {
                            this.setBlockAt(currentX, currentZ, j, currentHeight + height, 15, 0);
                        }
                    }
                    break;
                case NORTH:
                    for (int height = heightStart; height < 15; height++) {
                        for (int j = 1; j < 15; j++) {
                            this.setBlockAt(currentX, currentZ, j, currentHeight + height, 0, 0);
                        }
                    }
            }
        }

        int heightEnd = 16 - STEP - 1;

        if (currentHeight > 0) {
            int direction = checkDirection(currentHeight - STEP, currentX, currentZ, sectionUsed);
            switch (direction) {
                case EAST:
                    for (int height = 1; height < heightEnd; height++) {
                        for (int j = 1; j < 15; j++) {
                            this.setBlockAt(currentX, currentZ, 0, currentHeight + height, j, 0);
                        }
                    }
                    break;
                case WEST:
                    for (int height = 1; height < heightEnd; height++) {
                        for (int j = 1; j < 15; j++) {
                            this.setBlockAt(currentX, currentZ, 15, currentHeight + height, j, 0);
                        }
                    }
                    break;
                case SOUTH:
                    for (int height = 1; height < heightEnd; height++) {
                        for (int j = 1; j < 15; j++) {
                            this.setBlockAt(currentX, currentZ, j, currentHeight + height, 15, 0);
                        }
                    }
                    break;
                case NORTH:
                    for (int height = 1; height < heightEnd; height++) {
                        for (int j = 1; j < 15; j++) {
                            this.setBlockAt(currentX, currentZ, j, currentHeight + height, 0, 0);
                        }
                    }
            }
        }
    }

    private void setBlockAt(int currentX, int currentZ, int x, int y, int z, int blockId) {
        ChunkSection section = this.chunkDataCache[currentX][currentZ].getSections()[y / 16];

        section.setBlock(x, y % 16, z, blockId);
    }

    private void setBlockAt(int currentX, int currentZ, int x, int y, int z, int blockId, int data) {
        ChunkSection section = this.chunkDataCache[currentX][currentZ].getSections()[y / 16];

        section.setBlock(x, y % 16, z, blockId, data);
    }

    private void shuffle(int[] array) {
        int length = array.length;
        for (int i = length; i > 0; i--) {
            int randInd = (int) (Math.random() * array.length);

            int temp = array[randInd];
            array[randInd] = array[i - 1];
            array[i - 1] = temp;
        }
    }
}
