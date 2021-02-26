package com.particle.game.entity.attribute.metadata;

import com.particle.api.entity.attribute.MetaDataServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.entity.metadata.type.*;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.data.SetEntityDataPacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Singleton
public class MetaDataService implements MetaDataServiceApi {

    private static final ECSModuleHandler<EntityMetaDataModule> ENTITY_META_DATA_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMetaDataModule.class);

    @Inject
    private NetworkManager networkManager;

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;

    /**
     * 配置组件
     *
     * @param entity
     */
    public void enableMetadata(Entity entity) {
        ENTITY_META_DATA_MODULE_HANDLER.bindModule(entity);
    }

    /**
     * 获取生物的MetaData
     *
     * @return
     */
    @Override
    public Map<EntityMetadataType, EntityData> getEntityMetaData(Entity entity) {
        return ENTITY_META_DATA_MODULE_HANDLER.getModule(entity).getEntityMetaData();
    }

    public void setDataFlag(Entity entity, MetadataDataFlag dataFlag, boolean enabled, boolean broadcast) {
        LongEntityData data = ENTITY_META_DATA_MODULE_HANDLER.getModule(entity).setDataFlag(dataFlag, enabled);

        if (broadcast) {
            this.updateMetadata(entity, EntityMetadataType.FLAGS, data);
        }
    }

    public boolean getDataFlag(Entity entity, MetadataDataFlag dataFlag) {
        return ENTITY_META_DATA_MODULE_HANDLER.getModule(entity).getDataFlag(dataFlag);
    }

    /**
     * 设置boolean类型的entityData
     *
     * @param entity
     * @param type
     * @param value
     */
    public void setBooleanData(Entity entity, EntityMetadataType type, boolean value) {
        this.setBooleanData(entity, type, value, false);
    }

    /**
     * 设置boolean类型的entityData
     *
     * @param entity
     * @param type
     * @param value
     * @param broadcast
     */
    public void setBooleanData(Entity entity, EntityMetadataType type, boolean value, boolean broadcast) {
        ByteEntityData data = ENTITY_META_DATA_MODULE_HANDLER.getModule(entity).setBooleanData(type, value);

        if (broadcast) {
            this.refreshMetadata(entity, type, data);
        }
    }

    /**
     * 获取boolean类型的entityData
     *
     * @param entity
     * @param type
     * @return
     */
    public boolean getBooleanData(Entity entity, EntityMetadataType type) {
        return ENTITY_META_DATA_MODULE_HANDLER.getModule(entity).getBooleanData(type);
    }

    /**
     * 设置String类型的数据
     *
     * @param entity
     * @param type
     * @param value
     */
    public void setStringData(Entity entity, EntityMetadataType type, String value) {
        this.setStringData(entity, type, value, false);
    }

    /**
     * 设置String类型的数据
     *
     * @param entity
     * @param type
     * @param value
     * @param broadcast
     */
    public void setStringData(Entity entity, EntityMetadataType type, String value, boolean broadcast) {
        StringEntityData data = ENTITY_META_DATA_MODULE_HANDLER.getModule(entity).setStringData(type, value);

        if (broadcast) {
            this.refreshMetadata(entity, type, data);
        }
    }

    /**
     * 获取String类型的数据
     *
     * @param entity
     * @param type
     * @return
     */
    public String getStringData(Entity entity, EntityMetadataType type) {
        return ENTITY_META_DATA_MODULE_HANDLER.getModule(entity).getStringData(type);
    }

    @Override
    public void setLongData(Entity entity, EntityMetadataType type, long value) {
        this.setLongData(entity, type, value, false);
    }

    @Override
    public void setLongData(Entity entity, EntityMetadataType type, long value, boolean broadcast) {
        LongEntityData data = ENTITY_META_DATA_MODULE_HANDLER.getModule(entity).setLongData(type, value);

        if (broadcast) {
            this.refreshMetadata(entity, type, data);
        }
    }

    @Override
    public long getLongData(Entity entity, EntityMetadataType type) {
        return ENTITY_META_DATA_MODULE_HANDLER.getModule(entity).getLongData(type);
    }

    /**
     * 设置Integer类型的数据
     *
     * @param entity
     * @param type
     * @param value
     */
    public void setIntegerData(Entity entity, EntityMetadataType type, int value) {
        this.setIntegerData(entity, type, value, false);
    }

    /**
     * 设置Integer类型的数据
     *
     * @param entity
     * @param type
     * @param value
     * @param broadcast
     */
    public void setIntegerData(Entity entity, EntityMetadataType type, int value, boolean broadcast) {
        IntEntityData data = ENTITY_META_DATA_MODULE_HANDLER.getModule(entity).setIntegerData(type, value);

        if (broadcast) {
            this.refreshMetadata(entity, type, data);
        }
    }

    /**
     * 获取Integer类型的数据
     *
     * @param entity
     * @param type
     * @return
     */
    public int getIntegerData(Entity entity, EntityMetadataType type) {
        return ENTITY_META_DATA_MODULE_HANDLER.getModule(entity).getIntegerData(type);
    }

    /**
     * 设置Short类型的数据
     *
     * @param entity
     * @param type
     * @param value
     */
    public void setShortData(Entity entity, EntityMetadataType type, short value) {
        this.setShortData(entity, type, value, false);
    }

    /**
     * 设置Short类型的数据
     *
     * @param entity
     * @param type
     * @param value
     * @param broadcast
     */
    public void setShortData(Entity entity, EntityMetadataType type, short value, boolean broadcast) {
        ShortEntityData data = ENTITY_META_DATA_MODULE_HANDLER.getModule(entity).setShortData(type, value);

        if (broadcast) {
            this.refreshMetadata(entity, type, data);
        }
    }

    /**
     * 获取Short类型的数据
     *
     * @param entity
     * @param type
     * @return
     */
    public short getShortData(Entity entity, EntityMetadataType type) {
        return ENTITY_META_DATA_MODULE_HANDLER.getModule(entity).getShortData(type);
    }

    @Override
    public void setByteData(Entity entity, EntityMetadataType type, byte value) {
        this.setByteData(entity, type, value, false);
    }

    /**
     * 设置Byte类型的数据
     *
     * @param entity
     * @param type
     * @param value
     * @param broadcast
     */
    public void setByteData(Entity entity, EntityMetadataType type, byte value, boolean broadcast) {
        ByteEntityData data = ENTITY_META_DATA_MODULE_HANDLER.getModule(entity).setByteData(type, value);

        if (broadcast) {
            this.refreshMetadata(entity, type, data);
        }
    }

    /**
     * 设置Byte类型的数据
     *
     * @param entity
     * @param type
     * @return
     */
    public byte getByteData(Entity entity, EntityMetadataType type) {
        return ENTITY_META_DATA_MODULE_HANDLER.getModule(entity).getByteData(type);
    }

    /**
     * 设置Float类型的数据
     *
     * @param entity
     * @param type
     * @param value
     */
    public void setFloatData(Entity entity, EntityMetadataType type, float value) {
        this.setFloatData(entity, type, value, false);
    }

    /**
     * 设置Float类型的数据
     *
     * @param entity
     * @param type
     * @param value
     * @param broadcast
     */
    public void setFloatData(Entity entity, EntityMetadataType type, float value, boolean broadcast) {
        FloatEntityData data = ENTITY_META_DATA_MODULE_HANDLER.getModule(entity).setFloatData(type, value);

        if (broadcast) {
            this.refreshMetadata(entity, type, data);
        }
    }

    /**
     * 获取Float类型的数据
     *
     * @param entity
     * @param type
     * @return
     */
    public float getFloatData(Entity entity, EntityMetadataType type) {
        return ENTITY_META_DATA_MODULE_HANDLER.getModule(entity).getFloatData(type);
    }

    @Override
    public void setVector3fData(Entity entity, EntityMetadataType type, Vector3f value) {
        this.setVector3fData(entity, type, value, false);
    }

    @Override
    public void setVector3fData(Entity entity, EntityMetadataType type, Vector3f value, boolean broadcast) {
        Vector3fEntityData data = ENTITY_META_DATA_MODULE_HANDLER.getModule(entity).setVector3fData(type, value);

        if (broadcast) {
            this.refreshMetadata(entity, type, data);
        }
    }

    @Override
    public Vector3f getVector3fData(Entity entity, EntityMetadataType type) {
        return ENTITY_META_DATA_MODULE_HANDLER.getModule(entity).getVector3fData(type);
    }

    /**
     * 更新并广播生物的metadata
     * <p>
     * 注意：这里不能广播给玩家自己，否则会导致更新循环
     *
     * @param entity
     * @param type
     * @param entityData
     */
    public void updateMetadata(Entity entity, EntityMetadataType type, EntityData entityData) {
        SetEntityDataPacket setEntityDataPacket = new SetEntityDataPacket();
        setEntityDataPacket.setEid(entity.getRuntimeId());

        Map<EntityMetadataType, EntityData> entityMetaData = new HashMap<>();
        entityMetaData.put(type, entityData);
        setEntityDataPacket.setMetaData(entityMetaData);

        this.broadcastServiceProxy.broadcast(entity, setEntityDataPacket);
    }

    /**
     * 刷新玩家的metadata
     *
     * @param entity
     */
    @Override
    public void refreshMetadata(Entity entity) {
        SetEntityDataPacket setEntityDataPacket = new SetEntityDataPacket();
        setEntityDataPacket.setEid(entity.getRuntimeId());
        setEntityDataPacket.setMetaData(this.getEntityMetaData(entity));

        this.broadcastServiceProxy.broadcast(entity, setEntityDataPacket);

        if (entity instanceof Player) {
            this.networkManager.sendMessage(((Player) entity).getClientAddress(), setEntityDataPacket);
        }
    }

    /**
     * 给指定AOI列表刷新MateData
     *
     * @param entity
     * @param forEntity
     */
    @Override
    public void refreshMetaDataFor(Entity entity, Entity forEntity) {
        SetEntityDataPacket setEntityDataPacket = new SetEntityDataPacket();
        setEntityDataPacket.setEid(entity.getRuntimeId());
        setEntityDataPacket.setMetaData(this.getEntityMetaData(entity));

        this.broadcastServiceProxy.broadcast(forEntity, setEntityDataPacket);
    }

    public void refreshMetadata(Entity entity, EntityMetadataType type, EntityData data) {
        SetEntityDataPacket setEntityDataPacket = new SetEntityDataPacket();
        setEntityDataPacket.setEid(entity.getRuntimeId());

        Map<EntityMetadataType, EntityData> entityDataMap = new TreeMap<>();
        entityDataMap.put(type, data);
        setEntityDataPacket.setMetaData(entityDataMap);

        this.broadcastServiceProxy.broadcast(entity, setEntityDataPacket);

        if (entity instanceof Player) {
            this.networkManager.sendMessage(((Player) entity).getClientAddress(), setEntityDataPacket);
        }
    }

    /**
     * 这个接口是针对小熊宠物需求中特殊名称显示需求做的
     * 因为小熊坚持要这个效果，又没有足够的时间排期做定制AOI，所以只能临时开接口使用
     * 此接口以后需要移除，在AOI层面改进支持对应功能
     *
     * @param toPlayer
     * @param entity
     * @param type
     * @param data
     */
    @Deprecated
    public void refreshMetadataTo(Player toPlayer, Entity entity, EntityMetadataType type, EntityData data) {
        SetEntityDataPacket setEntityDataPacket = new SetEntityDataPacket();
        setEntityDataPacket.setEid(entity.getRuntimeId());

        Map<EntityMetadataType, EntityData> entityDataMap = new TreeMap<>();
        entityDataMap.put(type, data);
        setEntityDataPacket.setMetaData(entityDataMap);

        this.networkManager.sendMessage(toPlayer.getClientAddress(), setEntityDataPacket);
    }

    /**
     * 刷新玩家的metadata
     *
     * @param player
     */
    @Override
    public void refreshPlayerMetadata(Player player) {
        SetEntityDataPacket setEntityDataPacket = new SetEntityDataPacket();
        setEntityDataPacket.setEid(player.getRuntimeId());
        setEntityDataPacket.setMetaData(this.getEntityMetaData(player));

        this.networkManager.sendMessage(player.getClientAddress(), setEntityDataPacket);
    }

    /**
     * 刷新指定的metadata
     *
     * @param player
     * @param metadataType
     */
    @Override
    public void refreshPlayerMetadata(Player player, EntityMetadataType metadataType) {
        Map<EntityMetadataType, EntityData> entityMetaData = ENTITY_META_DATA_MODULE_HANDLER.getModule(player).getEntityMetaData();
        EntityData entityData = entityMetaData.get(metadataType);

        if (entityData != null) {
            SetEntityDataPacket setEntityDataPacket = new SetEntityDataPacket();
            setEntityDataPacket.setEid(player.getRuntimeId());

            Map<EntityMetadataType, EntityData> sendData = new HashMap<>();
            sendData.put(metadataType, entityData);

            setEntityDataPacket.setMetaData(sendData);

            this.networkManager.sendMessage(player.getClientAddress(), setEntityDataPacket);
        }

    }
}
