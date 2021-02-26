package com.particle.game.block.tile.model;

import com.particle.model.entity.model.tile.TileEntity;

public class EnchantingTileEntity extends TileEntity {

    @Override
    public String getName() {
        return "EnchantTable";
    }

    @Override
    public boolean needNoticeClient() {
        return true;
    }

    @Override
    protected void onInit() {

    }
}
