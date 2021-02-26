package com.particle.model.level;

import com.particle.core.aoi.api.Lighthouse;
import com.particle.core.aoi.model.Grid;
import com.particle.core.ecs.GameObject;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.entity.model.others.MonsterEntity;
import com.particle.model.entity.model.others.NpcEntity;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.level.chunk.ChunkSection;
import com.particle.model.level.chunk.ChunkState;
import com.particle.model.level.chunk.NavSquareCollection;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Chunk extends GameObject implements Lighthouse {

    private static final Logger LOGGER = LoggerFactory.getLogger(Chunk.class);

    /**
     * 区块状态
     * UNLOAD: 未加载状态，该状态区块刚刚创建，但是数据未就绪
     * RUNNING: 运行状态，该状态数据已就绪，可以承担业务逻辑执行
     * BLOCKING: 锁定状态，该状态数据禁止操作，等待区块存储或重载
     * CLOSED: 关闭状态，该状态区块已经卸载，且不再使用
     */
    private ChunkState state = ChunkState.UNLOAD;

    /**
     * 区块坐标
     */
    private int xPos;
    private int zPos;

    /**
     * 区块方块数据
     */
    private ChunkSection[] chunkSections;
    private byte[] biome;
    private byte[] heightMap;

    /**
     * 区块AOI节点
     */
    private Grid grid;

    /**
     * 区块中Entity缓存
     */
    private ChunkEntityCollection<Player> players = new ChunkEntityCollection<>();
    private ChunkEntityCollection<NpcEntity> npcEntities = new ChunkEntityCollection<>();
    private ChunkEntityCollection<Entity> projectileEntities = new ChunkEntityCollection<>();
    private ChunkEntityCollection<MobEntity> mobEntities = new ChunkEntityCollection<>();
    private ChunkEntityCollection<ItemEntity> itemEntities = new ChunkEntityCollection<>();
    private ChunkEntityCollection<MonsterEntity> monsterEntities = new ChunkEntityCollection<>();

    private ChunkEntityCollection<TileEntity> tileEntities = new ChunkTileEntityCollection<>();


    private List<TileEntity> tickedTileEntities;
    private int tileEntityTickIndex = 0; // 当前Tick位置

    /**
     * 导航网格
     */
    private NavSquareCollection navSquares = new NavSquareCollection();

    /**
     * 数据提供器
     */
    private LevelProvider provider;

    /**
     * 区块加载时间
     */
    private long timestamp = System.currentTimeMillis();

    public Chunk(Grid grid, LevelProvider provider, int chunkX, int chunkZ) {
        this.grid = grid;
        this.provider = provider;
        this.xPos = chunkX;
        this.zPos = chunkZ;
    }

    //----------------------------- player 操作 -----------------------------
    public ChunkEntityCollection<Player> getPlayersCollection() {
        return this.players;
    }


    //----------------------------- npc 操作 -----------------------------
    public ChunkEntityCollection<NpcEntity> getNPCCollection() {
        return this.npcEntities;
    }

    //----------------------------- mob 操作 -----------------------------
    public ChunkEntityCollection<MobEntity> getMobEntitiesCollection() {
        return this.mobEntities;
    }

    //----------------------------- monster 操作 -----------------------------
    public ChunkEntityCollection<MonsterEntity> getMonsterEntitiesCollection() {
        return this.monsterEntities;
    }

    //----------------------------- projectile entity 操作 -----------------------------
    public ChunkEntityCollection<Entity> getProjectileEntitiesCollection() {
        return this.projectileEntities;
    }

    //----------------------------- item entity 操作 -----------------------------
    public ChunkEntityCollection<ItemEntity> getItemEntitiesCollection() {
        return this.itemEntities;
    }

    //----------------------------- blockentity 操作 ------------------------------
    public ChunkEntityCollection<TileEntity> getTileEntitiesCollection() {
        return this.tileEntities;
    }

    public List<TileEntity> getTickedTileEntities() {
        return tickedTileEntities;
    }

    public void setTickedTileEntities(List<TileEntity> tickedTileEntities) {
        this.tickedTileEntities = tickedTileEntities;
    }

    public int getTileEntityTickIndex() {
        return tileEntityTickIndex;
    }

    public void setTileEntityTickIndex(int tileEntityTickIndex) {
        this.tileEntityTickIndex = tileEntityTickIndex;
    }

    //----------------------------- 方块操作 ---------------------------------------
    public Block getBlockAt(int x, int y, int z) {
        int sectionIndex = y >> 4;

        if (this.chunkSections != null && sectionIndex < this.chunkSections.length && sectionIndex >= 0 && this.chunkSections[sectionIndex] != null) {
            int blockId = this.chunkSections[sectionIndex].getBlockId(x & 15, y & 15, z & 15);

            Block block = Block.getBlock(blockId);

            if (block != null) {
                block.setMeta(this.chunkSections[sectionIndex].getBlockData(x & 15, y & 15, z & 15));
            }

            return block;
        }

        return Block.getBlock(BlockPrototype.AIR);
    }

    public BlockPrototype getBlockTypeAt(int x, int y, int z) {
        int sectionIndex = y >> 4;

        if (this.chunkSections != null && sectionIndex < this.chunkSections.length && sectionIndex >= 0 && this.chunkSections[sectionIndex] != null) {
            int blockId = this.chunkSections[sectionIndex].getBlockId(x & 15, y & 15, z & 15);

            return Block.getBlockType(blockId);
        }

        return BlockPrototype.AIR;
    }

    public void setBlockAt(Block block, int x, int y, int z) {
        int sectionIndex = y >> 4;

        if (this.chunkSections != null && sectionIndex < this.chunkSections.length && this.chunkSections[sectionIndex] != null) {
            this.chunkSections[sectionIndex].setBlock(x & 0b1111, y & 0b1111, z & 0b1111, block.getType().getId(), block.getMeta());

            this.chunkSections[sectionIndex].setEncodeCache(null);
        }
    }

    public int getTopBlockHeightBelow(int x, int y, int z) {
        if (state == ChunkState.RUNNING) {
            for (int i = 0; i < y; i++) {
                int height = y - i;

                if (this.chunkSections[height >> 4] != null) {
                    int blockId = this.chunkSections[height >> 4].getBlockId(x & 15, height & 15, z & 15);

                    if (blockId != 0) {
                        return y - i;
                    }
                }
            }

            return 0;
        }

        return 0;
    }

    //-------------------------- 其它操作 ------------------------------------------
    public int getxPos() {
        return xPos;
    }

    public int getzPos() {
        return zPos;
    }

    public ChunkSection[] getChunkSections() {
        return chunkSections;
    }

    public void setChunkSections(ChunkSection[] chunkSections) {
        this.chunkSections = chunkSections;

        if (chunkSections != null) {
            for (ChunkSection chunkSection : chunkSections) {
                if (chunkSection != null) {
                    chunkSection.setEncodeCache(null);
                }
            }
        }

    }

    public byte[] getBiome() {
        return biome;
    }

    public void setBiome(byte[] biome) {
        this.biome = biome;
    }

    public byte[] getHeightMap() {
        return heightMap;
    }

    public void setHeightMap(byte[] heightMap) {
        this.heightMap = heightMap;
    }

    public Grid getGrid() {
        return grid;
    }

    // ---------------------- 数据源 -----------------------
    public LevelProvider getProvider() {
        return provider;
    }

    // ---------------------- 加载标记 -----------------------
    public boolean isRunning() {
        return this.state == ChunkState.RUNNING;
    }

    public ChunkState getState() {
        return state;
    }

    public synchronized boolean updateState(ChunkState state) {
        switch (state) {
            case RUNNING:
                if (this.state == ChunkState.UNLOAD || this.state == ChunkState.BLOCKING) {
                    this.state = state;
                    break;
                }
            case BLOCKING:
                if (this.state == ChunkState.RUNNING) {
                    this.state = state;
                    break;
                } else if (this.state == ChunkState.BLOCKING) {
                    // 当已经是block状态的时候，返回false
                    return false;
                }
            case CLOSED:
                if (this.state == ChunkState.BLOCKING) {
                    this.state = state;
                    break;
                }
            default:
                return false;
        }

        return true;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public NavSquareCollection getNavSquares() {
        return navSquares;
    }

    @Override
    public int hashCode() {
        return (this.xPos << 16) | (this.zPos & 0b1111111111111111);
    }
}
