package com.particle.game.block.tile.model;

import com.particle.model.entity.model.tile.TileEntity;

public class CommonTileEntity extends TileEntity {

    private String name;
    private boolean needNoticeClient;

    public CommonTileEntity(String name, boolean needNoticeClient) {
        this.name = name;
        this.needNoticeClient = needNoticeClient;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean needNoticeClient() {
        return this.needNoticeClient;
    }

    @Override
    protected void onInit() {

    }
}
