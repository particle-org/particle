package com.particle.game.block.planting;

import com.particle.game.block.tile.TileEntityService;
import com.particle.game.block.tile.model.GrassingTileEntity;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.utils.ecs.ECSComponentService;
import com.particle.game.world.level.ChunkService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GrassService {

    @Inject
    private LevelService levelService;

    @Inject
    private ChunkService chunkService;

    @Inject
    private TileEntityService tileEntityService;

    @Inject
    private ECSComponentService ecsComponentService;

    @Inject
    private EntitySpawnService entitySpawnService;

    /**
     * 检查周围是否有草方块
     *
     * @param level
     * @param position
     */
    public void checkUpdate(Level level, Vector3 position) {
        if (this.levelService.getBlockTypeAt(level, position) != BlockPrototype.DIRT) {
            return;
        }

        // 上方有遮挡，不生长
        if (this.levelService.getBlockTypeAt(level, position.add(0, 1, 0)) != BlockPrototype.AIR) {
            return;
        }

        if (this.checkGrass(level, position.add(1, 0, 0))
                || this.checkGrass(level, position.add(-1, 0, 0))
                || this.checkGrass(level, position.add(0, 0, 1))
                || this.checkGrass(level, position.add(0, 0, -1))) {
            TileEntity entity = this.tileEntityService.createEntity(GrassingTileEntity.class, position);

            this.entitySpawnService.spawn(level, entity);
        } else {
            this.entitySpawnService.despawnTileEntity(level, position);
        }
    }

    /**
     * 泥土生长为草
     *
     * @param level
     * @param position
     */
    public void grow(Level level, Vector3 position) {
        // 清除TileEntity，不再tick
        this.entitySpawnService.despawnTileEntity(level, position);

        if (this.levelService.getBlockTypeAt(level, position) != BlockPrototype.DIRT) {
            return;
        }

        this.levelService.setBlockAt(level, Block.getBlock(BlockPrototype.GRASS), position);

        this.checkUpdate(level, position.add(1, 0, 0));
        this.checkUpdate(level, position.add(-1, 0, 0));
        this.checkUpdate(level, position.add(0, 0, 1));
        this.checkUpdate(level, position.add(0, 0, -1));
    }

    private boolean checkGrass(Level level, Vector3 position) {
        Chunk chunk = this.chunkService.indexChunk(level, position);
        BlockPrototype blockTypeAt = this.levelService.getBlockTypeAt(chunk, position.getX(), position.getY() + 1, position.getZ());
        if (blockTypeAt == BlockPrototype.AIR) {
            blockTypeAt = this.levelService.getBlockTypeAt(chunk, position.getX(), position.getY(), position.getZ());

            if (blockTypeAt == BlockPrototype.AIR) {
                blockTypeAt = this.levelService.getBlockTypeAt(chunk, position.getX(), position.getY() - 1, position.getZ());
            }
        }

        return blockTypeAt == BlockPrototype.GRASS;
    }

}
