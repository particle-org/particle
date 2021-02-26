package com.particle.game.world.level.loader.combine;

import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.NBTTagCompoundModule;
import com.particle.game.common.modules.TransformModule;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.level.Chunk;
import com.particle.model.level.LevelProvider;
import com.particle.model.level.chunk.ChunkData;
import com.particle.model.level.chunk.ChunkSection;
import com.particle.model.level.chunk.NavSquare;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.nbt.NBTTagCompound;

import java.util.LinkedList;
import java.util.List;

public class CombineLevelProvider implements LevelProvider {

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);
    private static final ECSModuleHandler<NBTTagCompoundModule> NBT_TAG_COMPOUND_MODULE_HANDLER = ECSModuleHandler.buildHandler(NBTTagCompoundModule.class);

    private LevelProvider levelProviderHeight;
    private LevelProvider levelProviderLow;

    /**
     * 分界线，分界线处属于Height部分
     */
    private int separation;


    public CombineLevelProvider(LevelProvider levelProviderLow, LevelProvider levelProviderHeight, int separation) {
        this.levelProviderHeight = levelProviderHeight;
        this.levelProviderLow = levelProviderLow;
        this.separation = separation;
    }

    @Override
    public void saveTileEntity(TileEntity tileEntity) {
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(tileEntity);
        if ((transformModule.getFloorY() >> 4) < separation) {
            this.levelProviderLow.saveTileEntity(tileEntity);
        } else {
            this.levelProviderHeight.saveTileEntity(tileEntity);
        }
    }

    @Override
    public void removeTileEntity(TileEntity tileEntity) {
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(tileEntity);
        if ((transformModule.getFloorY() >> 4) < separation) {
            this.levelProviderLow.removeTileEntity(tileEntity);
        } else {
            this.levelProviderHeight.removeTileEntity(tileEntity);
        }
    }

    @Override
    public void loadModule(Chunk chunk, String moduleName) {
        this.levelProviderLow.loadModule(chunk, moduleName);
    }

    @Override
    public void saveModule(Chunk chunk, ECSModule module) {
        this.levelProviderLow.saveModule(chunk, module);
    }

    @Override
    public ChunkData loadChunk(int x, int z) {
        ChunkData chunkDataHeight = this.levelProviderHeight.loadChunk(x, z);
        ChunkData chunkDataLow = this.levelProviderLow.loadChunk(x, z);

        /*
         *
         * 这里需要针对空返回和异常返回做区分
         *
         * 1) 任何一个数据返回异常
         *     上层自己处理，决定重载还是弃用
         *
         * 2) 任何任何一个数据返回空
         *     视为有效数据，另一部分数据填空处理
         *
         * 3) 两个数据均为空
         *     视为空数据，返回null
         *
         * 针对家园服加载成功，但是主世界加载失败的情况，为了避免家园数据丢失，这边在底层再加一层校验。
         * 如果主世界没有数据，
         *
         */
        if (chunkDataLow == null && chunkDataHeight == null) {
            return null;
        }

        if (chunkDataLow == null) {
            chunkDataLow = new ChunkData();
            chunkDataLow.setxPos(x);
            chunkDataLow.setzPos(z);
            chunkDataLow.setBiomColors(new byte[256]);
            chunkDataLow.setExtraData(new byte[0]);
            chunkDataLow.setSections(new ChunkSection[16]);
        }

        if (chunkDataHeight == null) {
            chunkDataHeight = new ChunkData();
            chunkDataHeight.setxPos(x);
            chunkDataHeight.setzPos(z);
            chunkDataHeight.setHeightMap(new byte[256]);
            chunkDataHeight.setSections(new ChunkSection[16]);
        }

        return this.combineChunkData(chunkDataLow, chunkDataHeight);
    }

    @Override
    public boolean isChunkExist(int x, int z) {
        return this.levelProviderLow.isChunkExist(x, z);
    }

    @Override
    public void saveChunk(ChunkData chunkData, boolean release) {
        this.saveCombineChunkData(chunkData, release);
    }

    /**
     * 保存合并的chunkData
     *
     * @param chunkData
     * @param release
     */
    private void saveCombineChunkData(ChunkData chunkData, boolean release) {
        // 构造低部分的ChunkData
        ChunkData chunkDataLow = new ChunkData();
        chunkDataLow.setxPos(chunkData.getxPos());
        chunkDataLow.setzPos(chunkData.getzPos());
        chunkDataLow.setBiomColors(chunkData.getBiomColors());
        chunkDataLow.setHeightMap(chunkData.getHeightMap());
        chunkDataLow.setExtraData(chunkData.getExtraData());
        chunkDataLow.setV(chunkData.getV());
        ChunkSection[] chunkSectionLow = new ChunkSection[16];
        for (int i = 0; i < separation; i++) {
            chunkSectionLow[i] = chunkData.getSections()[i];
        }
        chunkDataLow.setSections(chunkSectionLow);
        List<MobEntity> mobEntitiesLow = new LinkedList<>();
        for (MobEntity mobEntity : chunkData.getMobEntities()) {
            if ((TRANSFORM_MODULE_HANDLER.getModule(mobEntity).getFloorY() >> 4) < separation) {
                mobEntitiesLow.add(mobEntity);
            }
        }
        chunkDataLow.setMobEntities(mobEntitiesLow);
        List<TileEntity> tileEntitiesLow = new LinkedList<>();
        for (TileEntity tileEntity : chunkData.getTileEntities()) {
            if ((TRANSFORM_MODULE_HANDLER.getModule(tileEntity).getFloorY() >> 4) < separation) {
                tileEntitiesLow.add(tileEntity);
            }
        }
        chunkDataLow.setTileEntities(tileEntitiesLow);
        List<NavSquare> navSquaresLow = new LinkedList<>();
        for (NavSquare navSquare : chunkData.getNavSquares()) {
            if ((navSquare.getY() >> 4) < separation) {
                navSquaresLow.add(navSquare);
            }
        }
        chunkDataLow.setNavSquares(navSquaresLow);

        // 构造高部分的ChunkData
        ChunkData chunkDataHeight = new ChunkData();
        chunkDataHeight.setxPos(chunkData.getxPos());
        chunkDataHeight.setzPos(chunkData.getzPos());
        chunkDataHeight.setBiomColors(chunkData.getBiomColors());
        chunkDataHeight.setHeightMap(chunkData.getHeightMap());
        chunkDataHeight.setExtraData(chunkData.getExtraData());
        chunkDataHeight.setV(chunkData.getV());
        ChunkSection[] chunkSectionHeight = new ChunkSection[16];
        for (int i = separation; i < 16; i++) {
            chunkSectionHeight[i] = chunkData.getSections()[i];
        }
        chunkDataHeight.setSections(chunkSectionHeight);
        List<MobEntity> mobEntitiesHeight = new LinkedList<>();
        for (MobEntity mobEntity : chunkData.getMobEntities()) {
            if ((TRANSFORM_MODULE_HANDLER.getModule(mobEntity).getFloorY() >> 4) >= separation) {
                mobEntitiesHeight.add(mobEntity);
            }
        }
        chunkDataHeight.setMobEntities(mobEntitiesHeight);
        List<TileEntity> tileEntitiesHeight = new LinkedList<>();
        for (TileEntity tileEntity : chunkData.getTileEntities()) {
            if ((TRANSFORM_MODULE_HANDLER.getModule(tileEntity).getFloorY() >> 4) >= separation) {
                tileEntitiesHeight.add(tileEntity);
            }
        }
        chunkDataHeight.setTileEntities(tileEntitiesHeight);
        List<NavSquare> navSquaresHeight = new LinkedList<>();
        for (NavSquare navSquare : chunkData.getNavSquares()) {
            if ((navSquare.getY() >> 4) >= separation) {
                navSquaresHeight.add(navSquare);
            }
        }
        chunkDataLow.setNavSquares(navSquaresHeight);

        this.levelProviderLow.saveChunk(chunkDataLow, release);
        this.levelProviderHeight.saveChunk(chunkDataHeight, release);
    }

    private ChunkData combineChunkData(ChunkData chunkDataLow, ChunkData chunkDataHeight) {
        ChunkData chunkData = new ChunkData();

        ChunkSection[] sectionsLow = chunkDataLow.getSections();
        ChunkSection[] sectionsHeight = chunkDataHeight.getSections();
        ChunkSection[] chunkSections = new ChunkSection[16];
        for (int i = 0; i < separation; i++) {
            chunkSections[i] = sectionsLow[i];
        }
        for (int i = separation; i < 16; i++) {
            chunkSections[i] = sectionsHeight[i];
        }
        chunkData.setSections(chunkSections);

        List<MobEntity> mobEntities = new LinkedList<>();
        mobEntities.addAll(chunkDataHeight.getMobEntities());
        mobEntities.addAll(chunkDataLow.getMobEntities());
        chunkData.setMobEntities(mobEntities);

        List<TileEntity> tileEntities = new LinkedList<>();
        for (TileEntity tileEntity : chunkDataHeight.getTileEntities()) {
            // 偏移坐标
            TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(tileEntity);

            // 抛弃没有用的数据
            if ((transformModule.getFloorY() >> 4) < separation) {
                continue;
            }

            Vector3 position = new Vector3(transformModule.getPosition());
            position.setX(chunkDataLow.getxPos() * 16 + (position.getX() % 16));
            position.setZ(chunkDataLow.getzPos() * 16 + (position.getZ() % 16));
            transformModule.setPosition(new Vector3f(position));

            NBTTagCompoundModule nbtTagCompoundModule = NBT_TAG_COMPOUND_MODULE_HANDLER.getModule(tileEntity);
            NBTTagCompound nbtTagCompound = nbtTagCompoundModule.getNbtTagCompound();
            nbtTagCompound.setInteger("x", position.getX());
            nbtTagCompound.setInteger("z", position.getZ());

            tileEntities.add(tileEntity);
        }
        tileEntities.addAll(chunkDataLow.getTileEntities());
        chunkData.setTileEntities(tileEntities);

        List<NavSquare> navSquares = new LinkedList<>();
        navSquares.addAll(chunkDataHeight.getNavSquares());
        navSquares.addAll(chunkDataLow.getNavSquares());
        chunkData.setNavSquares(navSquares);

        chunkData.setxPos(chunkDataLow.getxPos());
        chunkData.setzPos(chunkDataLow.getzPos());
        chunkData.setBiomColors(chunkDataLow.getBiomColors());
        // TODO: 2019/4/9 重新计算
        chunkData.setHeightMap(chunkDataHeight.getHeightMap());
        chunkData.setExtraData(chunkDataLow.getExtraData());
        chunkData.setV(chunkDataLow.getV());

        return chunkData;
    }
}
