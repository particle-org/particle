package com.particle.api.entity;

import com.particle.core.ecs.module.ECSModule;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.nbt.NBTTagCompound;

public interface TileEntityServiceApi {
    TileEntity createEntity(BlockPrototype blockPrototype, Vector3 position);

    TileEntity getEntityAt(Level level, Vector3 position);

    void refreshNbtComponent(TileEntity entity);

    NBTTagCompound getViewNbtCompound(TileEntity entity);

    /**
     * 注册tileEntity，创建自定义tileEntity的时候，必须先注册
     * 一种方块类型，只允许注册一种tileEntity
     *
     * @param blockPrototype
     * @param clazz
     * @return
     */
    @Deprecated
    boolean registerBlockTileEntity(BlockPrototype blockPrototype, Class<? extends TileEntity> clazz);

    boolean registerBlockTileEntity(BlockPrototype blockPrototype, String name, boolean clientSide, Class<? extends ECSModule>... bindModules);
}
