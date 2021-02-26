package com.particle.game.world.level.convert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.particle.api.level.convert.ChunkData2JsonObjectApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.block.tile.TileEntityService;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.service.ItemEntityService;
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

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class ChunkData2JsonObject implements ChunkData2JsonObjectApi {

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    private static final ECSModuleHandler<ItemBindModule> ITEM_BIND_MODULE_HANDLER = ECSModuleHandler.buildHandler(ItemBindModule.class);


    @Inject
    private ItemEntityService itemEntityService;

    @Inject
    private TileEntityService tileEntityService;

    @Inject
    private BlockPrototypeDictionary blockPrototypeDictionary;

    @Inject
    private ECSComponentManager ecsComponentManager;

    @Override
    public ChunkData fromNBT(JSONObject data) {
        ChunkData chunkData = new ChunkData();

        JSONObject level = data.getJSONObject("Level");

        chunkData.setxPos(level.getInteger("xPos"));
        chunkData.setzPos(level.getInteger("zPos"));

        //填充biome
        byte[] biomeColors = new byte[256];
        chunkData.setBiomColors(biomeColors);

        JSONArray biomeColorsArray = level.getJSONArray("BiomeColors");
        if (biomeColorsArray != null && biomeColorsArray.size() == 256) {
            for (int i = 0; i < 256; i++) {
                biomeColors[i] = biomeColorsArray.getByteValue(i);
            }
        }

        //填充HeightMap
        byte[] heightMap = new byte[256];
        chunkData.setHeightMap(heightMap);

        JSONArray heightMapArray = level.getJSONArray("HeightMap");
        if (heightMapArray != null && heightMapArray.size() == 256) {
            for (int i = 0; i < 256; i++) {
                heightMap[i] = heightMapArray.getByteValue(i);
            }
        }

        //填充Sections
        ChunkSection[] chunkSections = new ChunkSection[16];
        chunkData.setSections(chunkSections);

        JSONArray sectionLists = level.getJSONArray("Sections");
        if (sectionLists != null) {
            for (int i = 0; i < sectionLists.size(); i++) {
                ChunkSection chunkSection = ChunkSection2JSONObject.fromJsonObject(sectionLists.getJSONObject(i));

                chunkSections[chunkSection.getY()] = chunkSection;
            }
        }
        for (byte i = 0; i < 16; i++) {
            if (chunkSections[i] == null) {
                chunkSections[i] = new ChunkSection(i);
            }
        }


        //填充ExtraData
        JSONArray extraData = level.getJSONArray("ExtraData");
        byte[] extraDataArray = new byte[extraData.size()];
        for (int i = 0; i < extraDataArray.length; i++) {
            extraDataArray[i] = extraData.getByteValue(i);
        }

        //填充ItemEntities
        List<ItemEntity> itemEntitiesList = new LinkedList<>();
        chunkData.setItemEntities(itemEntitiesList);
        JSONArray entities = level.getJSONArray("ItemEntities");
        if (entities != null) {
            for (int i = 0; i < entities.size(); i++) {
                ItemEntity itemEntity = null;

                JSONObject entitiesJSONObject = entities.getJSONObject(i);

                // 构造Item
                if (entitiesJSONObject.containsKey("Item")) {
                    JSONObject itemData = entitiesJSONObject.getJSONObject("Item");
                    short id = itemData.getShort("id");
                    byte count = itemData.getByte("Count");
                    short meta = itemData.getShort("Damage");

                    ItemStack itemStack = ItemStack.getItem((int) id);
                    itemStack.setCount(count);
                    itemStack.setMeta(meta);

                    itemEntity = this.itemEntityService.createEntity(itemStack);
                } else {
                    continue;
                }

                // 提取component信息
                JSONArray components = entitiesJSONObject.getJSONArray("Components");
                for (int j = 0; j < components.size(); j++) {
                    JSONObject component = components.getJSONObject(j);

                    int v = component.getInteger("v");
                    String componentData = component.getString("data");

                    ecsComponentManager.importECSComponent(itemEntity, componentData);
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

        JSONArray tileEntities = level.getJSONArray("TileEntities");
        if (tileEntities != null) {
            for (int i = 0; i < tileEntities.size(); i++) {
                JSONObject entityData = tileEntities.getJSONObject(i);

                //只有有位置信息的tile entity才算是合法的tile entity
                if (entityData.containsKey("Pos")) {
                    JSONArray positionData = entityData.getJSONArray("Pos");
                    int x = positionData.getInteger(0);
                    int y = positionData.getInteger(1);
                    int z = positionData.getInteger(2);

                    //通过blockid来构造tile entity，以保证非法的tile entity被正确清理掉
                    int blockId = chunkData.getSections()[y >> 4].getBlockId(x, y, z);
                    TileEntity tileEntity = this.tileEntityService.createEntity(blockPrototypeDictionary.map(blockId), new Vector3(x, y, z));

                    if (tileEntity != null) {
                        //提取component信息
                        JSONArray components = entityData.getJSONArray("Components");
                        for (int j = 0; j < components.size(); j++) {
                            JSONObject component = components.getJSONObject(j);

                            int v = component.getInteger("v");
                            String componentData = component.getString("data");

                            ecsComponentManager.importECSComponent(tileEntity, componentData);
                        }

                        tileEntitiesList.add(tileEntity);
                    }
                }
            }
        }

        return chunkData;
    }

    @Override
    public JSONObject toNbt(ChunkData chunkData) {
        JSONObject chunkJsonData = new JSONObject();

        JSONObject level = new JSONObject();
        chunkJsonData.put("Level", level);

        level.put("xPos", chunkData.getxPos());
        level.put("zPos", chunkData.getzPos());

        //填充biome
        int[] biomeColors = new int[256];
        for (int i = 0; i < chunkData.getBiomColors().length; i++) {
            biomeColors[i] = chunkData.getBiomColors()[i];
        }
        level.put("BiomeColors", biomeColors);


        //填充HeightMap
        int[] heightMap = new int[256];
        for (int i = 0; i < chunkData.getHeightMap().length; i++) {
            heightMap[i] = chunkData.getHeightMap()[i];
        }
        level.put("HeightMap", heightMap);

        //填充ItemEntities
        JSONArray itemEntitiesData = new JSONArray();
        level.put("ItemEntities", itemEntitiesData);

        List<ItemEntity> itemEntitiesList = chunkData.getItemEntities();
        for (ItemEntity itemEntity : itemEntitiesList) {
            ItemBindModule itemBindModule = ITEM_BIND_MODULE_HANDLER.getModule(itemEntity);

            JSONObject itemEntityData = new JSONObject();

            JSONObject itemTag = new JSONObject();
            ItemStack itemStack = itemBindModule.getItem();
            itemTag.put("id", (short) itemStack.getItemType().getId());
            itemTag.put("Count", (byte) itemStack.getCount());
            itemTag.put("Damage", (short) itemStack.getMeta());
            itemEntityData.put("Item", itemTag);

            JSONArray componentsTags = new JSONArray();
            for (ECSComponent component : itemEntity.getComponents()) {
                if (component == null)
                    continue;

                String data = ecsComponentManager.exportComponent(component);

                if (data != null) {
                    JSONObject componentTag = new JSONObject();
                    componentTag.put("v", ECSComponentManager.VERSION);
                    componentTag.put("id", component.getName());
                    componentTag.put("data", data);
                    componentsTags.add(componentTag);
                }
            }
            itemEntityData.put("Components", componentsTags);

            itemEntitiesData.add(itemEntityData);
        }

        //填充Entities
        JSONArray entitiesData = new JSONArray();
        level.put("Entities", entitiesData);

        //填充TileEntities
        JSONArray tileEntitiesTagList = new JSONArray();
        level.put("TileEntities", tileEntitiesTagList);
        List<TileEntity> tileEntityList = chunkData.getTileEntities();
        for (TileEntity tileEntity : tileEntityList) {
            //创建entity的tag
            JSONObject entityData = new JSONObject();

            //提取并保存位置信息
            JSONArray positionTag = new JSONArray();
            Vector3f position = TRANSFORM_MODULE_HANDLER.getModule(tileEntity).getPosition();
            positionTag.add((int) (position.getX()));
            positionTag.add((int) (position.getY()));
            positionTag.add((int) (position.getZ()));
            entityData.put("Pos", positionTag);

            //提取保存其它信息
            JSONArray componentsTags = new JSONArray();
            for (ECSComponent component : tileEntity.getComponents()) {
                //跳过位置信息存储
                if (component == null)
                    continue;

                String data = ecsComponentManager.exportComponent(component);

                if (data != null) {
                    JSONObject componentTag = new JSONObject();
                    componentTag.put("v", ECSComponentManager.VERSION);
                    componentTag.put("id", component.getName());
                    componentTag.put("data", data);
                    componentsTags.add(componentTag);
                }
            }
            entityData.put("Components", componentsTags);

            tileEntitiesTagList.add(entityData);
        }

        //填充Sections
        JSONArray sectionLists = new JSONArray();
        for (ChunkSection section : chunkData.getSections()) {
            if (section != null) {
                sectionLists.add(ChunkSection2JSONObject.toJsonObject(section));
            }
        }
        level.put("Sections", sectionLists);

        //填充ExtraData
        level.put("ExtraData", chunkData.getExtraData());

        return chunkJsonData;
    }
}
