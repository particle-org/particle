package com.particle.game.entity.service;

import com.particle.api.entity.ItemEntityServiceAPI;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.attribute.metadata.EntityMetaDataModule;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.entity.movement.module.EntityMovementModule;
import com.particle.game.entity.service.network.ItemEntityPacketBuilder;
import com.particle.game.entity.spawn.AutoRemovedModule;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.entity.spawn.processor.ItemEntitySpawnProcessor;
import com.particle.game.item.ItemBindModule;
import com.particle.game.item.PickableModule;
import com.particle.game.utils.ecs.ECSComponentManager;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.game.world.level.ChunkService;
import com.particle.game.world.physical.EntityColliderService;
import com.particle.game.world.physical.PhysicalService;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.item.ItemStack;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.data.TakeItemEntityPacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class ItemEntityService implements ItemEntityServiceAPI {

    private static Logger LOGGER = LoggerFactory.getLogger(ItemEntityService.class);

    private static final ECSModuleHandler<EntityMetaDataModule> ENTITY_META_DATA_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMetaDataModule.class);

    private static final ECSModuleHandler<EntityMovementModule> ENTITY_MOVEMENT_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMovementModule.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    private static final ECSModuleHandler<PickableModule> PICKABLE_MODULE_HANDLER = ECSModuleHandler.buildHandler(PickableModule.class);

    private static final ECSModuleHandler<AutoRemovedModule> AUTO_REMOVED_MODULE_HANDLER = ECSModuleHandler.buildHandler(AutoRemovedModule.class);

    private static final ECSModuleHandler<ItemBindModule> ITEM_BIND_MODULE_HANDLER = ECSModuleHandler.buildHandler(ItemBindModule.class);


    @Inject
    private ChunkService chunkService;

    @Inject
    private ItemEntitySpawnProcessor itemEntitySpawnProcessor;
    @Inject
    private ItemEntityPacketBuilder itemEntityPacketBuilder;

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private PhysicalService physicalService;
    @Inject
    private EntityColliderService entityColliderService;

    @Inject
    private ECSComponentManager ecsComponentManager;

    @Inject
    private MovementServiceProxy movementServiceProxy;

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;

    @Override
    public ItemEntity createEntity(ItemStack itemStack) {
        return this.createEntity(itemStack, new Vector3f(0, 0, 0));
    }

    @Override
    public ItemEntity createEntity(ItemStack itemStack, Vector3 position, Vector3f motion) {
        ItemEntity itemEntity = this.createEntity(itemStack, position);

        this.movementServiceProxy.setMotion(itemEntity, motion);

        return itemEntity;
    }

    @Override
    public ItemEntity createEntity(ItemStack itemStack, Vector3f position, Vector3f motion) {
        ItemEntity itemEntity = this.createEntity(itemStack, position);

        this.movementServiceProxy.setMotion(itemEntity, motion);

        return itemEntity;
    }

    @Override
    public ItemEntity createEntity(ItemStack itemStack, Vector3 position) {
        return this.createEntity(itemStack, new Vector3f(0.5f, 0.5f, 0.5f).add(position));
    }

    @Override
    public ItemEntity createEntity(ItemStack itemStack, Vector3f position) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.init();

        // 位置相关信息
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.bindModule(itemEntity);
        transformModule.setPosition(position);
        transformModule.setDirection(0, 0, 0);
        transformModule.setMoveEntityPacketBuilder(this.itemEntityPacketBuilder.getMovePacketBuilder(itemEntity));

        // 设置Entity基础信息
        EntityMetaDataModule entityMetaDataModule = ENTITY_META_DATA_MODULE_HANDLER.bindModule(itemEntity);
        entityMetaDataModule.setLongData(EntityMetadataType.FLAGS, 0);
        entityMetaDataModule.setFloatData(EntityMetadataType.SCALE, 1f);

        ENTITY_MOVEMENT_MODULE_HANDLER.bindModule(itemEntity);

        // 标记接入物理引擎
        this.entityColliderService.bindPointBindBox(itemEntity, position, new Vector3f(0, 0, 0));
        this.entityColliderService.bindDefaultColliderDetector(itemEntity);
        this.physicalService.initPhysicalEffects(itemEntity, false, true);

        // 设置可以被捡拾
        PICKABLE_MODULE_HANDLER.bindModule(itemEntity);
        AUTO_REMOVED_MODULE_HANDLER.bindModule(itemEntity);

        // Spawn相关业务
        this.entitySpawnService.enableSpawn(
                itemEntity,
                this.itemEntitySpawnProcessor.getEntitySpawnProcessor(itemEntity),
                this.itemEntityPacketBuilder.getAddPacketBuilder(itemEntity),
                this.itemEntityPacketBuilder.getRemovePacketBuilder(itemEntity)
        );

        // 配置绑定物品
        ItemBindModule itemBindModule = ITEM_BIND_MODULE_HANDLER.bindModule(itemEntity);
        itemBindModule.setItem(itemStack);

        // 配置system
        ecsComponentManager.filterTickedSystem(itemEntity);

        return itemEntity;
    }

    /**
     * 获取某个坐标附件的chunk
     *
     * @param vector3f
     * @return
     */
    public List<ItemEntity> getItemEntityNear(Level level, Vector3f vector3f) {
        Chunk chunk = this.chunkService.indexChunk(level, vector3f);

        List<ItemEntity> entities = null;

        for (ItemEntity entity : chunk.getItemEntitiesCollection().getEntitiesViewer()) {
            TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
            if (transformModule.getPosition().subtract(vector3f).lengthSquared() < 1) {

                //懒加载
                if (entities == null) {
                    entities = new LinkedList<>();
                }

                entities.add(entity);
            }
        }

        return entities;
    }

    /**
     * 掉落物捡拾动画
     *
     * @param player
     * @param entity
     */
    public void pickAnimate(Player player, ItemEntity entity) {
        TakeItemEntityPacket takeItemEntityPacket = new TakeItemEntityPacket();
        takeItemEntityPacket.setPlayerRuntimeId(player.getRuntimeId());
        takeItemEntityPacket.setEntityRuntimeId(entity.getRuntimeId());

        this.broadcastServiceProxy.broadcast(player, takeItemEntityPacket, true);
    }
}
