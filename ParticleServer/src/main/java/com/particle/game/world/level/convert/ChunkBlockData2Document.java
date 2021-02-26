package com.particle.game.world.level.convert;

import com.particle.api.level.convert.ChunkData2DocumentApi;
import com.particle.model.level.chunk.ChunkData;
import com.particle.model.level.chunk.ChunkSection;
import com.particle.util.compress.CompressManage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bson.Document;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class ChunkBlockData2Document implements ChunkData2DocumentApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChunkBlockData2Document.class);

    @Override
    public Document toDocument(ChunkData chunkData) {
        Document document = new Document();
        document.append("xPos", chunkData.getxPos());
        document.append("zPos", chunkData.getzPos());

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

            return chunkData;
        }

        return null;
    }
}
