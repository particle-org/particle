package com.particle.game.world.level.convert;

import com.particle.api.level.convert.ChunkData2DocumentApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.serialization.SerializationTool;
import com.particle.game.block.tile.TileEntityService;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.utils.ecs.ECSComponentManager;
import com.particle.model.block.types.BlockPrototypeDictionary;
import com.particle.model.entity.component.ECSComponent;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.level.chunk.ChunkData;
import com.particle.model.level.chunk.ChunkSection;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.util.compress.CompressManage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bson.Document;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class ChunkData2Document implements ChunkData2DocumentApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChunkData2Document.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    @Inject
    private TileEntityService tileEntityService;

    @Inject
    private ECSComponentManager ecsComponentManager;

    @Override
    public Document toDocument(ChunkData chunkData) {
        Document document = new Document();
        document.append("xPos", chunkData.getxPos());
        document.append("zPos", chunkData.getzPos());
        document.append("v", chunkData.getV());

        if (chunkData.getBiomColors() != null)
            document.append("biomColors", chunkData.getBiomColors());
        if (chunkData.getHeightMap() != null)
            document.append("heightMap", chunkData.getHeightMap());
        if (chunkData.getExtraData() != null)
            document.append("extraData", new Binary(chunkData.getExtraData()));

        // 填充ChunkSection
        if (chunkData.getSections() != null) {

            ByteBuf buffer = Unpooled.buffer(196624);

            for (ChunkSection section : chunkData.getSections()) {
                if (section != null) {
                    ByteBuf byteBuf = ChunkSection2SaveBuffer.toSaveFormat(section);
                    while (byteBuf.readerIndex() < byteBuf.writerIndex()) {
                        buffer.writeByte(byteBuf.readByte());
                    }
                }
            }

            byte[] section = null;
            try {
                byte[] bytes = new byte[buffer.writerIndex()];
                buffer.readBytes(bytes);
                section = CompressManage.getInstance().compress(bytes);
            } catch (IOException e) {
                e.printStackTrace();

                return null;
            }

            Document sectionData = new Document();
            sectionData.append("data", new Binary(section));
            sectionData.append("compressType", CompressManage.getInstance().getDefaultCompressType().value());

            document.put("sections", sectionData);

        }

        //填充Entities
        List<Document> entitiesData = new ArrayList<>(0);
        document.append("Entities", entitiesData);

        //填充TileEntities
        List<Document> tileEntitiesTagList = new ArrayList<>();
        document.append("TileEntities", tileEntitiesTagList);
        List<TileEntity> tileEntityList = chunkData.getTileEntities();
        for (TileEntity tileEntity : tileEntityList) {
            //创建entity的tag
            Document entityData = new Document();

            //提取并保存位置信息
            Vector3f position = TRANSFORM_MODULE_HANDLER.getModule(tileEntity).getPosition();
            entityData.put("x", (int) (position.getX()));
            entityData.put("y", (int) (position.getY()));
            entityData.put("z", (int) (position.getZ()));

            //提取保存其它信息
            List<Document> componentsData = new ArrayList<>();
            for (ECSComponent component : tileEntity.getComponents()) {
                //跳过位置信息存储
                if (component == null)
                    continue;

                String data = ecsComponentManager.exportComponent(component);

                if (data != null) {
                    Document componentTag = new Document();
                    componentTag.put("v", ECSComponentManager.VERSION);
                    componentTag.put("id", component.getName());
                    componentTag.put("data", data);
                    componentsData.add(componentTag);
                }
            }
            entityData.put("Components", componentsData);
            entityData.put("ecs", SerializationTool.exportModuleData(tileEntity));

            tileEntitiesTagList.add(entityData);
        }


        return document;
    }

    @Override
    public ChunkData fromDocument(Document documentChunks) {
        if (documentChunks != null && documentChunks.containsKey("v")) {
            ChunkData chunkData = new ChunkData();
            chunkData.setxPos(documentChunks.get("xPos", 0));
            chunkData.setzPos(documentChunks.get("zPos", 0));
            chunkData.setV(documentChunks.get("v", 0).byteValue());

            if (documentChunks.containsKey("biomColors")) {
                chunkData.setBiomColors(documentChunks.get("biomColors", Binary.class).getData());
            }
            if (documentChunks.containsKey("heightMap")) {
                chunkData.setHeightMap(documentChunks.get("heightMap", Binary.class).getData());
            }
            if (documentChunks.containsKey("extraData")) {
                chunkData.setExtraData(documentChunks.get("extraData", Binary.class).getData());
            }

            if (documentChunks.containsKey("sections")) {
                Document sectionDocument = documentChunks.get("sections", Document.class);

                Integer type = sectionDocument.getInteger("compressType");

                if (type == null) {
                    type = CompressManage.getInstance().getDefaultCompressType().value();
                }

                byte[] sectionData = sectionDocument.get("data", Binary.class).getData();

                ByteBuf sectionBuff = null;
                try {
                    sectionBuff = Unpooled.wrappedBuffer(CompressManage.getInstance().uncompress(sectionData, type));
                } catch (IOException e) {
                    e.printStackTrace();

                    return null;
                }

                ChunkSection[] chunkSections = new ChunkSection[16];
                while (sectionBuff.readerIndex() < sectionBuff.writerIndex()) {
                    ChunkSection chunkSection = ChunkSection2SaveBuffer.fromSaveFormat(sectionBuff);

                    chunkSections[chunkSection.getY()] = chunkSection;
                }

                chunkData.setSections(chunkSections);
            }

            List<MobEntity> entitiesList = new LinkedList<>();
            chunkData.setMobEntities(entitiesList);


            //填充TileEntities
            List<TileEntity> tileEntitiesList = new LinkedList<>();
            chunkData.setTileEntities(tileEntitiesList);

            List<Document> tileEntities = documentChunks.get("TileEntities", ArrayList.class);
            if (tileEntities != null) {
                for (Document entityData : tileEntities) {
                    //只有有位置信息的tile entity才算是合法的tile entity
                    Integer x = entityData.getInteger("x");
                    Integer y = entityData.getInteger("y");
                    Integer z = entityData.getInteger("z");

                    if (x != null && y != null && z != null) {
                        // 如果该TileEntity对应的ChunkSection不存在，则跳过
                        ChunkSection section = chunkData.getSections()[y >> 4];
                        if (section == null) {
                            continue;
                        }

                        //通过blockid来构造tile entity，以保证非法的tile entity被正确清理掉
                        int blockId = section.getBlockId(x, y, z);
                        TileEntity tileEntity = this.tileEntityService.createEntity(BlockPrototypeDictionary.map(blockId), new Vector3(x, y, z));

                        if (tileEntity != null) {
                            //提取component信息
                            List<Document> components = entityData.get("Components", ArrayList.class);
                            // 解析组件
                            if (components != null && !components.isEmpty()) {
                                for (Document component : components) {
                                    int v = component.getInteger("v");
                                    String componentData = component.getString("data");

                                    ecsComponentManager.importECSComponent(tileEntity, componentData);
                                }
                            }

                            // 提取component信息
                            Document componentsData = entityData.get("ecs", Document.class);
                            // 解析组件
                            if (componentsData != null && !componentsData.isEmpty()) {
                                for (String key : componentsData.keySet()) {
                                    SerializationTool.importModuleData(tileEntity, key, componentsData.getString(key));
                                }
                            }

                            tileEntitiesList.add(tileEntity);

                            ecsComponentManager.filterTickedSystem(tileEntity);
                        } else {
                            // 因为家园加载机制的原因，暂时不打印主世界的日志，否则会导致日志刷的非常多
                            if (y > 127) {
                                LOGGER.error("Tile entity type miss match at ({},{},{}), block id {}", x, y, z, blockId);
                                // 提取component信息
                                Document componentsData = entityData.get("ecs", Document.class);
                                // 解析组件
                                if (componentsData != null && !componentsData.isEmpty()) {
                                    for (String key : componentsData.keySet()) {
                                        LOGGER.error("Tile entity data {} : {}", key, componentsData.getString(key));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return chunkData;
        }

        return null;
    }
}
