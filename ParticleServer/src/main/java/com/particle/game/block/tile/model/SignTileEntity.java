package com.particle.game.block.tile.model;

import com.particle.model.entity.model.tile.TileEntity;

public class SignTileEntity extends TileEntity {

    @Override
    public String getName() {
        return "Sign";
    }

    @Override
    public void onInit() {
    }

    @Override
    public boolean needNoticeClient() {
        return true;
    }

}
