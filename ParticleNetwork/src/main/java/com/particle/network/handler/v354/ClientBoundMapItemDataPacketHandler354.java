package com.particle.network.handler.v354;

import com.particle.model.network.packets.data.ClientboundMapItemDataPacket;
import com.particle.model.ui.map.MapDecorator;
import com.particle.network.handler.AbstractPacketHandler;

import java.awt.*;

public class ClientBoundMapItemDataPacketHandler354 extends AbstractPacketHandler<ClientboundMapItemDataPacket> {
    @Override
    protected void doDecode(ClientboundMapItemDataPacket dataPacket, int version) {
        dataPacket.setMapId(dataPacket.readSignedVarLong().longValue());
        dataPacket.setTypeFlags(dataPacket.readUnsignedVarInt());
        dataPacket.setDimension(dataPacket.readByte());
        dataPacket.setLocked(dataPacket.readBoolean());

        if ((dataPacket.getTypeFlags() & ClientboundMapItemDataPacket.CREATION_MASK) != 0) {
            int length = dataPacket.readUnsignedVarInt();
            long[] mapIds = new long[length];
            for (int i = 0; i < length; i++) {
                mapIds[i] = dataPacket.readSignedVarLong().longValue();
            }
            dataPacket.setMapIds(mapIds);
        }

        dataPacket.setScale(dataPacket.readByte());

        if ((dataPacket.getTypeFlags() & ClientboundMapItemDataPacket.DECORATION_UPDATE_MASK) != 0) {
            int entityLength = dataPacket.readUnsignedVarInt();
            int[] entities = new int[entityLength];
            for (int i = 0; i < entityLength; i++) {
                dataPacket.readInt();
                entities[i] = dataPacket.readSignedVarLong().intValue();
            }
            dataPacket.setEntityId(entities);

            int decoratorLength = dataPacket.readUnsignedVarInt();
            MapDecorator[] decorators = new MapDecorator[decoratorLength];
            for (int i = 0; i < decoratorLength; i++) {
                MapDecorator decorator = new MapDecorator();
                decorator.setIcon(MapDecorator.Type.fromData(dataPacket.readByte()));
                decorator.setRotation(dataPacket.readByte());
                decorator.setOffsetX(dataPacket.readByte());
                decorator.setOffsetZ(dataPacket.readByte());
                decorator.setLabel(dataPacket.readString());
                decorator.setColor(this.fromRGB(dataPacket.readUnsignedVarInt()));
            }
            dataPacket.setDecorators(decorators);
        }

        if ((dataPacket.getTypeFlags() & ClientboundMapItemDataPacket.TEXTURE_UPDATE_MASK) != 0) {
            dataPacket.setWidth(dataPacket.readSignedVarInt());
            dataPacket.setHeight(dataPacket.readSignedVarInt());
            dataPacket.setOffsetX(dataPacket.readSignedVarInt());
            dataPacket.setOffsetZ(dataPacket.readSignedVarInt());

            int width = dataPacket.getWidth();
            int height = dataPacket.getHeight();
            Color[][] image = new Color[width][height];
            for (int y = 0; y < width; y++) {
                for (int x = 0; x < height; x++) {
                    image[x][y] = this.fromRGB(dataPacket.readUnsignedVarInt());
                }
            }
        }
    }

    @Override
    protected void doEncode(ClientboundMapItemDataPacket dataPacket, int version) {
        dataPacket.writeSignedVarLong(dataPacket.getMapId());
        dataPacket.writeUnsignedVarInt(dataPacket.getTypeFlags());
        dataPacket.writeByte(dataPacket.getDimension());
        dataPacket.writeBoolean(dataPacket.isLocked());

        if ((dataPacket.getTypeFlags() & ClientboundMapItemDataPacket.CREATION_MASK) != 0) {
            long[] mapIds = dataPacket.getMapIds();
            dataPacket.writeUnsignedVarInt(mapIds.length);
            for (long eid : mapIds) {
                dataPacket.writeSignedVarLong(eid);
            }
        }

        if (dataPacket.getTypeFlags() != 0) {
            dataPacket.writeByte(dataPacket.getScale());
        }

        if ((dataPacket.getTypeFlags() & ClientboundMapItemDataPacket.DECORATION_UPDATE_MASK) != 0) {
            int[] entityId = dataPacket.getEntityId();
            dataPacket.writeUnsignedVarInt(entityId.length);
            for (Integer id : entityId) {
                dataPacket.writeInt(0);
                dataPacket.writeSignedVarLong(id);
            }

            MapDecorator[] decorators = dataPacket.getDecorators();
            dataPacket.writeUnsignedVarInt(decorators.length);
            for (MapDecorator decorator : decorators) {
                if (decorator != null) {
                    dataPacket.writeByte(decorator.getIcon().getData());
                    dataPacket.writeByte(decorator.getRotation());
                    dataPacket.writeByte(decorator.getOffsetX());
                    dataPacket.writeByte(decorator.getOffsetZ());
                    dataPacket.writeString(decorator.getLabel());
                    dataPacket.writeUnsignedVarInt(this.convertRGB(decorator.getColor()));
                }
            }
        }

        if ((dataPacket.getTypeFlags() & ClientboundMapItemDataPacket.TEXTURE_UPDATE_MASK) != 0) {
            int width = dataPacket.getWidth();
            int height = dataPacket.getHeight();
            Color[][] image = dataPacket.getImage();

            dataPacket.writeSignedVarInt(width);
            dataPacket.writeSignedVarInt(height);
            dataPacket.writeSignedVarInt(dataPacket.getOffsetX());
            dataPacket.writeSignedVarInt(dataPacket.getOffsetZ());

            if (dataPacket.getImage() != null) {
                dataPacket.writeUnsignedVarInt(dataPacket.getWidth() * dataPacket.getHeight());
                for (int y = 0; y < width; y++) {
                    for (int x = 0; x < height; x++) {
                        dataPacket.writeUnsignedVarInt(this.convertRGB(image[x][y]));
                    }
                }
            } else {
                dataPacket.writeUnsignedVarInt(0);
            }
        }
    }

    private int convertRGB(Color color) {
        if (color == null) return 0;

        int result = color.getRed() & 0xff;
        result |= (color.getGreen() & 0xff) << 8;
        result |= (color.getBlue() & 0xff) << 16;
        result |= (color.getAlpha() & 0xff) << 24;
        return result;
    }

    private Color fromRGB(int data) {
        int red = data * 0xff;
        int green = (data >>> 8) & 0xff;
        int blue = (data >>> 16) & 0xff;
        int alpha = (data >>> 24) & 0xff;

        return new Color(red, green, blue, alpha);
    }
}
