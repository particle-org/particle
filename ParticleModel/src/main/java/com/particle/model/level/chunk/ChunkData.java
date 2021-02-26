package com.particle.model.level.chunk;

import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.entity.model.others.NpcEntity;
import com.particle.model.entity.model.tile.TileEntity;

import java.util.LinkedList;
import java.util.List;

public class ChunkData {

    /**
     * 区块坐标
     */
    private int xPos;
    private int zPos;

    /**
     * 方块数据
     */
    private byte[] biomColors;
    private byte[] heightMap;
    private ChunkSection[] sections;

    /**
     * 生物数据
     */
    private List<ItemEntity> itemEntities = new LinkedList<>();
    private List<MobEntity> mobEntities = new LinkedList<>();
    private List<NpcEntity> npcEntities = new LinkedList<>();
    private List<TileEntity> tileEntities = new LinkedList<>();

    /**
     * 导航网格
     */
    private List<NavSquare> navSquares = new LinkedList<>();

    /**
     * 额外数据
     */
    private byte[] extraData;

    /**
     * 数据格式版本号
     */
    private byte v = 4;

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getzPos() {
        return zPos;
    }

    public void setzPos(int zPos) {
        this.zPos = zPos;
    }

    public byte[] getBiomColors() {
        return biomColors;
    }

    public void setBiomColors(byte[] biomColors) {
        this.biomColors = biomColors;
    }

    public byte[] getHeightMap() {
        return heightMap;
    }

    public void setHeightMap(byte[] heightMap) {
        this.heightMap = heightMap;
    }

    public ChunkSection[] getSections() {
        return sections;
    }

    public ChunkSection getSection(int i) {
        return sections[i];
    }

    public void setSections(ChunkSection[] sections) {
        this.sections = sections;
    }

    public List<ItemEntity> getItemEntities() {
        return itemEntities;
    }

    public void setItemEntities(List<ItemEntity> itemEntities) {
        this.itemEntities = itemEntities;
    }

    public List<MobEntity> getMobEntities() {
        return mobEntities;
    }

    public void setMobEntities(List<MobEntity> mobEntities) {
        this.mobEntities = mobEntities;
    }

    public List<TileEntity> getTileEntities() {
        return tileEntities;
    }

    public void setTileEntities(List<TileEntity> tileEntities) {
        this.tileEntities = tileEntities;
    }

    public List<NpcEntity> getNpcEntities() {
        return npcEntities;
    }

    public void setNpcEntities(List<NpcEntity> npcEntities) {
        this.npcEntities = npcEntities;
    }

    public byte[] getExtraData() {
        return extraData;
    }

    public void setExtraData(byte[] extraData) {
        this.extraData = extraData;
    }

    public List<NavSquare> getNavSquares() {
        return navSquares;
    }

    public void setNavSquares(List<NavSquare> navSquares) {
        this.navSquares = navSquares;
    }

    public byte getV() {
        return v;
    }

    public void setV(byte v) {
        this.v = v;
    }

    @Override
    public ChunkData clone() {
        ChunkData chunkData = new ChunkData();
        chunkData.setxPos(this.getxPos());
        chunkData.setzPos(this.getzPos());
        chunkData.setBiomColors(this.getBiomColors().clone());
        chunkData.setExtraData(this.getExtraData().clone());
        chunkData.setHeightMap(this.getHeightMap().clone());
        chunkData.setV(this.getV());

        ChunkSection[] sections = new ChunkSection[16];
        for (int i = 0; i < this.sections.length; i++) {
            sections[i] = this.sections[i].clone();
        }
        chunkData.setSections(sections);

        chunkData.getItemEntities().addAll(this.getItemEntities());
        chunkData.getMobEntities().addAll(this.getMobEntities());
        chunkData.getNpcEntities().addAll(this.getNpcEntities());
        chunkData.getTileEntities().addAll(this.getTileEntities());

        return chunkData;
    }
}
