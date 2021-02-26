package com.particle.game.block.tile.model;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.component.farming.CropsModule;
import com.particle.model.entity.component.farming.PlantGrowUpProgressModule;
import com.particle.model.entity.component.farming.StemModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.math.Vector3;

public class MelonStemTileEntity extends TileEntity {

    private static final ECSModuleHandler<PlantGrowUpProgressModule> PROGRESS_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlantGrowUpProgressModule.class);
    private static final ECSModuleHandler<CropsModule> CROPS_MODULE_HANDLER = ECSModuleHandler.buildHandler(CropsModule.class);
    private static final ECSModuleHandler<StemModule> STEM_MODULE_HANDLER = ECSModuleHandler.buildHandler(StemModule.class);


    @Override
    public void onCreated(Vector3 position) {
        super.onCreated(position);

        // 种植物标志
        CropsModule cropsModule = CROPS_MODULE_HANDLER.bindModule(this);
        cropsModule.setMaxGrowMeta(7);
        cropsModule.setRemoveTileEntityAfterMature(false);

        // 生长控制
        PROGRESS_MODULE_HANDLER.bindModule(this);

        // 西瓜藤
        StemModule stemModule = STEM_MODULE_HANDLER.bindModule(this);
        stemModule.setStemType(BlockPrototype.MELON_STEM);
        stemModule.setFruitType(BlockPrototype.MELON_BLOCK);
    }


    @Override
    public String getName() {
        return "MelonStem";
    }

    @Override
    public boolean needNoticeClient() {
        return false;
    }

    @Override
    protected void onInit() {

    }
}
