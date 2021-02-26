package com.particle.game.block.tile;

import com.particle.api.entity.TileEntityServiceApi;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.block.tile.model.*;
import com.particle.game.common.modules.NBTTagCompoundModule;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.network.TileEntityPacketBuilder;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.entity.spawn.processor.TileEntitySpawnProcessor;
import com.particle.game.utils.ecs.ECSComponentManager;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.game.world.level.ChunkService;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.network.packets.DataPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class TileEntityService implements TileEntityServiceApi {

    private static Logger LOGGER = LoggerFactory.getLogger(TileEntityService.class);

    private static final ECSModuleHandler<NBTTagCompoundModule> NBT_TAG_COMPOUND_MODULE_HANDLER = ECSModuleHandler.buildHandler(NBTTagCompoundModule.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);


    private Map<BlockPrototype, Class<? extends TileEntity>> tileEntityDictionary = new ConcurrentHashMap<>();
    private Map<BlockPrototype, TileEntityConfig> tileEntityConfigDictionary = new ConcurrentHashMap<>();

    @Inject
    private ChunkService chunkService;

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;

    @Inject
    private ECSComponentManager ecsComponentManager;

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private TileEntitySpawnProcessor tileEntitySpawnProcessor;
    @Inject
    private TileEntityPacketBuilder tileEntityPacketBuilder;

    @Inject
    private PositionService positionService;

    @Inject
    private void init() {
        this.tileEntityDictionary.put(BlockPrototype.CHEST, ChestTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.STANDING_SIGN, SignTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.WALL_SIGN, SignTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.SPRUCE_STANDING_SIGN, SignTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.SPRUCE_WALL_SIGN, SignTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.BIRCH_STANDING_SIGN, SignTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.BIRCH_WALL_SIGN, SignTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.JUNGLE_STANDING_SIGN, SignTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.JUNGLE_WALL_SIGN, SignTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.ACACIA_STANDING_SIGN, SignTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.ACACIA_WALL_SIGN, SignTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.DARKOAK_STANDING_SIGN, SignTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.DARKOAK_WALL_SIGN, SignTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.BREWING_STAND, BrewingTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.ENCHANTING_TABLE, EnchantingTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.FURNACE, FurnaceTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.LIT_FURNACE, FurnaceTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.ENDER_CHEST, EnderTileEntity.class);


        // 仙人掌
        this.tileEntityDictionary.put(BlockPrototype.CACTUS, CactusTileEntity.class);
        // 蘆薈
        this.tileEntityDictionary.put(BlockPrototype.REEDS, ReedsTileEntity.class);
        // 耕地
        this.tileEntityDictionary.put(BlockPrototype.FARMLAND, FarmlandTileEntity.class);

        // 蘑菇
        this.tileEntityDictionary.put(BlockPrototype.BROWN_MUSHROOM, MushroomTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.RED_MUSHROOM, MushroomTileEntity.class);

        // 种植
        this.tileEntityDictionary.put(BlockPrototype.WHEAT, NormalCropsTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.POTATOES, NormalCropsTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.CARROTS, NormalCropsTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.BEETROOT, BeetrootTileEntity.class);

        // 地獄疙瘩
        this.tileEntityDictionary.put(BlockPrototype.NETHER_WART, NetherWartTileEntity.class);

        // 西瓜
        this.tileEntityDictionary.put(BlockPrototype.MELON_STEM, MelonStemTileEntity.class);

        // 南瓜
        this.tileEntityDictionary.put(BlockPrototype.PUMPKIN_STEM, PumpkinStemTileEntity.class);

        // 樹苗
        this.tileEntityDictionary.put(BlockPrototype.SAPLING, SaplingTileEntity.class);

        // 水
        this.tileEntityDictionary.put(BlockPrototype.WATER, WaterTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.FLOWING_WATER, WaterTileEntity.class);

        // 頭顱
        this.tileEntityDictionary.put(BlockPrototype.SKULL, SkullTileEntity.class);

        // 旗幟
        this.tileEntityDictionary.put(BlockPrototype.STANDING_BANNER, BannerTileEntity.class);
        this.tileEntityDictionary.put(BlockPrototype.WALL_BANNER, BannerTileEntity.class);

        // 蛋糕
        this.tileEntityDictionary.put(BlockPrototype.CAKE, CakeTileEntity.class);

        // 可口豆
        this.tileEntityDictionary.put(BlockPrototype.COCOA, CocoaTileEntity.class);

        // 炼药锅
        this.tileEntityDictionary.put(BlockPrototype.CAULDRON, CauldronTileEntity.class);

        // 泥土
        this.tileEntityDictionary.put(BlockPrototype.DIRT, GrassingTileEntity.class);
    }

    @Override
    public boolean registerBlockTileEntity(BlockPrototype blockPrototype, Class<? extends TileEntity> clazz) {
        Class<? extends TileEntity> entityClass = this.tileEntityDictionary.get(blockPrototype);
        if (entityClass != null) {
            return false;
        }
        this.tileEntityDictionary.put(blockPrototype, clazz);
        return true;
    }

    @Override
    public boolean registerBlockTileEntity(BlockPrototype blockPrototype, String name, boolean clientSide, Class<? extends ECSModule>... bindModules) {
        return this.tileEntityConfigDictionary.put(blockPrototype, new TileEntityConfig(name, clientSide, bindModules)) == null;
    }

    public TileEntity createEntity(BlockPrototype blockPrototype, Vector3 position) {
        TileEntityConfig tileEntityConfig = this.tileEntityConfigDictionary.get(blockPrototype);
        if (tileEntityConfig != null) {
            return this.createEntity(tileEntityConfig, position);
        }

        Class<? extends TileEntity> entityClass = this.tileEntityDictionary.get(blockPrototype);
        if (entityClass != null) {
            return this.createEntity(entityClass, position);
        }

        return null;
    }

    public TileEntity createEntity(Class<? extends TileEntity> entityClass, Vector3 position) {
        //block无tileEntity时直接返回null
        if (entityClass == null) {
            return null;
        }

        TileEntity tileEntity = null;

        try {
            tileEntity = entityClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            LOGGER.error("Fail to build tile entity.", e);

            return null;
        }

        tileEntity.init();

        this.initTileEntity(tileEntity, position);

        tileEntity.onCreated(position);

        return tileEntity;
    }

    public TileEntity createEntity(TileEntityConfig config, Vector3 position) {
        TileEntity tileEntity = new CommonTileEntity(config.getName(), config.isClientSide());
        tileEntity.init();

        this.initTileEntity(tileEntity, position);
        for (Class<? extends ECSModule> bindModule : config.getBindModules()) {
            ECSModuleHandler.buildHandler(bindModule).bindModule(tileEntity);
        }

        tileEntity.onCreated(position);

        return tileEntity;
    }

    private void initTileEntity(TileEntity tileEntity, Vector3 position) {
        // 位置相关信息
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.bindModule(tileEntity);
        transformModule.setPosition(position.getX(), position.getY(), position.getZ());
        transformModule.setDirection(0, 0, 0);

        // Spawn相关业务
        this.entitySpawnService.enableSpawn(
                tileEntity,
                this.tileEntitySpawnProcessor.getEntitySpawnProcessor(tileEntity),
                this.tileEntityPacketBuilder.getAddPacketBuilder(tileEntity),
                null
        );

        //初始化NBTTagCompoundComponent
        NBTTagCompoundModule nbtTagCompoundModule = NBT_TAG_COMPOUND_MODULE_HANDLER.bindModule(tileEntity);
        NBTTagCompound nbtTagCompound = nbtTagCompoundModule.getNbtTagCompound();
        nbtTagCompound.setString("id", tileEntity.getName());
        nbtTagCompound.setInteger("x", position.getX());
        nbtTagCompound.setInteger("y", position.getY());
        nbtTagCompound.setInteger("z", position.getZ());


        //配置system
        ecsComponentManager.filterTickedSystem(tileEntity);
    }

    /**
     * 查询指定区块上的指定位置tileEntity
     *
     * @param level
     * @param position
     * @return
     */
    public TileEntity getEntityAt(Level level, Vector3 position) {
        //查找并加载区块
        Chunk chunk = this.chunkService.indexChunk(level, position);

        if (chunk.isRunning())
            return chunk.getTileEntitiesCollection().findEntity(TileEntity.position2Id(position));

        return null;
    }

    public void refreshNbtComponent(TileEntity entity) {
        DataPacket[] addPacket = this.entitySpawnService.getEntitySpawnPacket(entity);

        Vector3f position = this.positionService.getPosition(entity);

        for (DataPacket dataPacket : addPacket) {
            this.broadcastServiceProxy.broadcast(entity.getLevel(), position, dataPacket);
        }
    }

    public NBTTagCompound getViewNbtCompound(TileEntity entity) {
        return NBT_TAG_COMPOUND_MODULE_HANDLER.getModule(entity).getNbtTagCompound();
    }
}
