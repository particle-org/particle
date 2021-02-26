package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.ui.map.MapDecorator;

import java.awt.*;

public class ClientboundMapItemDataPacket extends DataPacket {

    // 数据类型
    public static final int TEXTURE_UPDATE_MASK = 1 << 1;
    public static final int DECORATION_UPDATE_MASK = 1 << 2;
    public static final int CREATION_MASK = 1 << 3;

    private long mapId;
    private int typeFlags;
    private byte dimension;
    private boolean isLocked;

    private byte scale;

    // CREATION
    private long[] mapIds;

    // DECORATION_UPDATE
    private int[] entityId = new int[0];
    private MapDecorator[] decorators = new MapDecorator[0];


    // TEXTURE_UPDATE
    private int width;
    private int height;
    private int offsetX;
    private int offsetZ;

    private Color[][] image = null;


    @Override
    public int pid() {
        return ProtocolInfo.CLIENTBOUND_MAP_ITEM_DATA_PACKET;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public int getTypeFlags() {
        return typeFlags;
    }

    public void setTypeFlags(int typeFlags) {
        this.typeFlags = typeFlags;
    }

    public byte getDimension() {
        return dimension;
    }

    public void setDimension(byte dimension) {
        this.dimension = dimension;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public long[] getMapIds() {
        return mapIds;
    }

    public void setMapIds(long[] mapIds) {
        this.mapIds = mapIds;
    }

    public byte getScale() {
        return scale;
    }

    public void setScale(byte scale) {
        this.scale = scale;
    }

    public int[] getEntityId() {
        return entityId;
    }

    public void setEntityId(int[] entityId) {
        this.entityId = entityId;
    }

    public MapDecorator[] getDecorators() {
        return decorators;
    }

    public void setDecorators(MapDecorator[] decorators) {
        this.decorators = decorators;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetZ() {
        return offsetZ;
    }

    public void setOffsetZ(int offsetZ) {
        this.offsetZ = offsetZ;
    }

    public Color[][] getImage() {
        return image;
    }

    public void setImage(Color[][] image) {
        this.image = image;
    }
}
