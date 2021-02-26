package com.particle.game.world.level.convert;

import com.particle.api.level.convert.ChunkData2NBTTagCompoundApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.block.tile.TileEntityService;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.service.ItemEntityService;
import com.particle.game.entity.service.MobEntityService;
import com.particle.game.item.ItemBindModule;
import com.particle.game.utils.ecs.ECSComponentManager;
import com.particle.model.block.types.BlockPrototypeDictionary;
import com.particle.model.entity.component.ECSComponent;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.item.ItemStack;
import com.particle.model.level.chunk.ChunkData;
import com.particle.model.level.chunk.ChunkSection;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.nbt.NBTTagDouble;
import com.particle.model.nbt.NBTTagList;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class ChunkData2NBTTagCompound implements ChunkData2NBTTagCompoundApi {

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    private static final ECSModuleHandler<ItemBindModule> ITEM_BIND_MODULE_HANDLER = ECSModuleHandler.buildHandler(ItemBindModule.class);

    @Inject
    private ItemEntityService itemEntityService;

    @Inject
    private TileEntityService tileEntityService;

    @Inject
    private MobEntityService mobEntityService;

    @Inject
    private BlockPrototypeDictionary blockPrototypeDictionary;

    @Inject
    private ECSComponentManager ecsComponentManager;

    @Override
    public ChunkData fromNBT(NBTTagCompound chunkTag) {
        ChunkData chunkData = new ChunkData();

        NBTTagCompound level = chunkTag.getCompoundTag("Level");

        chunkData.setxPos(level.getInteger("xPos"));
        chunkData.setzPos(level.getInteger("zPos"));

        //填充biome
        byte[] biomeColors = new byte[256];
        chunkData.setBiomColors(biomeColors);

        int[] biomeColorsArray = level.getIntArray("BiomeColors");
        if (biomeColorsArray != null) {
            for (int i = 0; i < biomeColorsArray.length; i++) {
                biomeColors[i] = (byte) (biomeColorsArray[i] & 0xff);
            }
        }

        //填充HeightMap
        byte[] heightMap = new byte[256];
        chunkData.setHeightMap(heightMap);

        int[] heightMapArray = level.getIntArray("HeightMap");
        if (heightMapArray != null) {
            for (int i = 0; i < heightMapArray.length; i++) {
                heightMap[i] = (byte) (heightMapArray[i] & 0xff);
            }
        }

        //填充Sections
        ChunkSection[] chunkSections = new ChunkSection[16];
        chunkData.setSections(chunkSections);

        NBTTagList sectionLists = level.getTagList("Sections", 10);
        if (sectionLists != null) {
            for (int i = 0; i < sectionLists.tagCount(); i++) {
                ChunkSection chunkSection = ChunkSection2NBTTagCompound.fromNBT(sectionLists.getCompoundTagAt(i));

                chunkSections[chunkSection.getY()] = chunkSection;
            }
        }
        for (byte i = 0; i < 16; i++) {
            if (chunkSections[i] == null) {
                chunkSections[i] = new ChunkSection(i);
            }
        }


        //填充ExtraData
        chunkData.setExtraData(level.getByteArray("ExtraData"));

        //填充ItemEntities
        List<ItemEntity> itemEntitiesList = new LinkedList<>();
        chunkData.setItemEntities(itemEntitiesList);
        NBTTagList entitiesTag = level.getTagList("ItemEntities", 10);
        if (entitiesTag != null) {
            for (int i = 0; i < entitiesTag.tagCount(); i++) {
                ItemEntity itemEntity = null;

                NBTTagCompound entityTag = (NBTTagCompound) entitiesTag.get(i);

                // 构造Item
                if (entityTag.hasKey("Item", 10)) {
                    NBTTagCompound itemTag = entityTag.getCompoundTag("Item");
                    short id = itemTag.getShort("id");
                    byte count = itemTag.getByte("Count");
                    short meta = itemTag.getShort("Damage");

                    ItemStack itemStack = ItemStack.getItem((int) id);
                    itemStack.setCount(count);
                    itemStack.setMeta(meta);

                    itemEntity = this.itemEntityService.createEntity(itemStack);
                } else {
                    continue;
                }

                // 兼容PC的位置信息
                if (entityTag.hasKey("Pos", 9)) {
                    NBTTagList nbtTagList = entityTag.getTagList("Pos", 6);
                    float x = (float) nbtTagList.getDoubleAt(0);
                    float y = (float) nbtTagList.getDoubleAt(1);
                    float z = (float) nbtTagList.getDoubleAt(2);
                    TRANSFORM_MODULE_HANDLER.getModule(itemEntity).setPosition(x, y, z);
                }

                // 提取component信息
                NBTTagList componentsTags = entityTag.getTagList("Components", 10);
                for (int j = 0; j < componentsTags.tagCount(); j++) {
                    NBTTagCompound compoundTagAt = componentsTags.getCompoundTagAt(j);

                    int v = compoundTagAt.getInteger("v");
                    String data = compoundTagAt.getString("data");

                    ecsComponentManager.importECSComponent(itemEntity, data);
                }

                // 缓存itemEntity
                itemEntitiesList.add(itemEntity);
            }
        }

        //填充Entities
        List<MobEntity> entitiesList = new LinkedList<>();
        chunkData.setMobEntities(entitiesList);

        //填充TileEntities
        List<TileEntity> tileEntitiesList = new LinkedList<>();
        chunkData.setTileEntities(tileEntitiesList);

        NBTTagList tileEntitiesTag = level.getTagList("TileEntities", 10);
        if (tileEntitiesTag != null) {
            for (int i = 0; i < tileEntitiesTag.tagCount(); i++) {
                NBTTagCompound entityTag = (NBTTagCompound) tileEntitiesTag.get(i);

                //只有有位置信息的tile entity才算是合法的tile entity
                if (entityTag.hasKey("Pos", 9)) {
                    NBTTagList nbtTagList = entityTag.getTagList("Pos", 6);

                    int x = (int) nbtTagList.getDoubleAt(0);
                    int y = (int) nbtTagList.getDoubleAt(1);
                    int z = (int) nbtTagList.getDoubleAt(2);

                    //通过blockid来构造tile entity，以保证非法的tile entity被正确清理掉
                    int blockId = chunkData.getSections()[y >> 4].getBlockId(x & 15, y & 15, z & 15);

                    TileEntity tileEntity = this.tileEntityService.createEntity(blockPrototypeDictionary.map(blockId), new Vector3(x, y, z));

                    if (tileEntity != null) {
                        //提取component信息
                        NBTTagList componentsTags = entityTag.getTagList("Components", 10);
                        for (int j = 0; j < componentsTags.tagCount(); j++) {
                            NBTTagCompound compoundTagAt = componentsTags.getCompoundTagAt(j);

                            int v = compoundTagAt.getInteger("v");
                            String data = compoundTagAt.getString("data");

                            ecsComponentManager.importECSComponent(tileEntity, data);
                        }

                        ecsComponentManager.filterTickedSystem(tileEntity);
                        tileEntitiesList.add(tileEntity);
                    }
                }
            }
        }

        return chunkData;
    }

    @Override
    public NBTTagCompound toNbt(ChunkData chunkData) {
        NBTTagCompound chunkNbtData = new NBTTagCompound();

        NBTTagCompound level = new NBTTagCompound();
        chunkNbtData.setTag("Level", level);

        level.setInteger("xPos", chunkData.getxPos());
        level.setInteger("zPos", chunkData.getzPos());

        //填充biome
        int[] biomeColors = new int[256];
        for (int i = 0; i < chunkData.getBiomColors().length; i++) {
            biomeColors[i] = chunkData.getBiomColors()[i];
        }
        level.setIntArray("BiomeColors", biomeColors);


        //填充HeightMap
        int[] heightMap = new int[256];
        for (int i = 0; i < chunkData.getHeightMap().length; i++) {
            heightMap[i] = chunkData.getHeightMap()[i];
        }
        level.setIntArray("HeightMap", heightMap);

        //填充ItemEntities
        NBTTagList entitiesTag = new NBTTagList();
        level.setTag("ItemEntities", entitiesTag);

        List<ItemEntity> itemEntitiesList = chunkData.getItemEntities();
        for (ItemEntity itemEntity : itemEntitiesList) {
            ItemBindModule itemBindModule = ITEM_BIND_MODULE_HANDLER.getModule(itemEntity);

            NBTTagCompound entityTag = new NBTTagCompound();

            NBTTagCompound itemTag = new NBTTagCompound();
            ItemStack itemStack = itemBindModule.getItem();
            itemTag.setShort("id", (short) itemStack.getItemType().getId());
            itemTag.setByte("Count", (byte) itemStack.getCount());
            itemTag.setShort("Damage", (short) itemStack.getMeta());
            entityTag.setTag("Item", itemTag);

            NBTTagList componentsTags = new NBTTagList();
            for (ECSComponent component : itemEntity.getComponents()) {
                if (component == null)
                    continue;

                String data = ecsComponentManager.exportComponent(component);

                if (data != null) {
                    NBTTagCompound componentTag = new NBTTagCompound();
                    componentTag.setInteger("v", ECSComponentManager.VERSION);
                    componentTag.setString("id", component.getName());
                    componentTag.setString("data", data);
                    componentsTags.appendTag(componentTag);
                }
            }

            entitiesTag.appendTag(entityTag);
        }

        //填充Entities
        NBTTagList entitiesTagList = new NBTTagList();
        level.setTag("Entities", entitiesTagList);

        //填充TileEntities
        NBTTagList tileEntitiesTagList = new NBTTagList();
        level.setTag("TileEntities", tileEntitiesTagList);
        List<TileEntity> tileEntityList = chunkData.getTileEntities();
        for (TileEntity tileEntity : tileEntityList) {
            //创建entity的tag
            NBTTagCompound entityTag = new NBTTagCompound();

            //提取并保存位置信息
            NBTTagList positionTag = new NBTTagList();
            Vector3f position = TRANSFORM_MODULE_HANDLER.getModule(tileEntity).getPosition();
            positionTag.appendTag(new NBTTagDouble(position.getX()));
            positionTag.appendTag(new NBTTagDouble(position.getY()));
            positionTag.appendTag(new NBTTagDouble(position.getZ()));
            entityTag.setTag("Pos", positionTag);

            //提取保存其它信息
            NBTTagList componentsTags = new NBTTagList();
            for (ECSComponent component : tileEntity.getComponents()) {
                //跳过位置信息存储
                if (component == null)
                    continue;

                String data = ecsComponentManager.exportComponent(component);

                if (data != null) {
                    NBTTagCompound componentTag = new NBTTagCompound();
                    componentTag.setInteger("v", ECSComponentManager.VERSION);
                    componentTag.setString("id", component.getName());
                    componentTag.setString("data", data);
                    componentsTags.appendTag(componentTag);
                }
            }
            entityTag.setTag("Components", componentsTags);

            tileEntitiesTagList.appendTag(entityTag);
        }

        //填充Sections
        NBTTagList sectionLists = new NBTTagList();
        for (ChunkSection section : chunkData.getSections()) {
            if (section != null) {
                sectionLists.appendTag(ChunkSection2NBTTagCompound.toNBT(section));
            }
        }
        level.setTag("Sections", sectionLists);

        //填充ExtraData
        level.setByteArray("ExtraData", chunkData.getExtraData());

        return chunkNbtData;
    }
}
