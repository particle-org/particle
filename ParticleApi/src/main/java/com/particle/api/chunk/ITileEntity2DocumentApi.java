package com.particle.api.chunk;

import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.model.tile.TileEntity;
import org.bson.Document;

public interface ITileEntity2DocumentApi {
    Document toDocument(TileEntity tileEntity);

    TileEntity fromDocument(BlockPrototype blockPrototype, Document entityData);
}
