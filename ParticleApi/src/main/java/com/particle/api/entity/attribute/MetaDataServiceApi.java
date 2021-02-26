package com.particle.api.entity.attribute;

import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.entity.metadata.type.EntityData;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import java.util.Map;

public interface MetaDataServiceApi {

    /**
     * 配置组件
     *
     * @param entity
     */
    void enableMetadata(Entity entity);

    /**
     * 获取生物的meta信息
     *
     * @param entity
     * @return
     */
    Map<EntityMetadataType, EntityData> getEntityMetaData(Entity entity);

    /**
     * 设置生物DataFlag
     *
     * @param entity
     * @param dataFlag
     * @param enabled
     * @param broadcast
     */
    void setDataFlag(Entity entity, MetadataDataFlag dataFlag, boolean enabled, boolean broadcast);

    /**
     * 获取生物DataFlag
     *
     * @param entity
     * @param dataFlag
     * @return
     */
    boolean getDataFlag(Entity entity, MetadataDataFlag dataFlag);

    /**
     * 设置boolean类型的entityData
     *
     * @param entity
     * @param type
     * @param value
     */
    void setBooleanData(Entity entity, EntityMetadataType type, boolean value);

    /**
     * 设置boolean类型的entityData
     *
     * @param entity
     * @param type
     * @param value
     * @param broadcast
     */
    void setBooleanData(Entity entity, EntityMetadataType type, boolean value, boolean broadcast);

    /**
     * 获取boolean类型的entityData
     *
     * @param entity
     * @param type
     * @return
     */
    boolean getBooleanData(Entity entity, EntityMetadataType type);

    /**
     * 设置String类型的数据
     *
     * @param entity
     * @param type
     * @param value
     */
    void setStringData(Entity entity, EntityMetadataType type, String value);

    /**
     * 设置String类型的数据
     *
     * @param entity
     * @param type
     * @param value
     * @param broadcast
     */
    void setStringData(Entity entity, EntityMetadataType type, String value, boolean broadcast);

    /**
     * 获取String类型的数据
     *
     * @param entity
     * @param type
     * @return
     */
    String getStringData(Entity entity, EntityMetadataType type);

    /**
     * 设置Long类型的数据
     *
     * @param entity
     * @param type
     * @param value
     */
    void setLongData(Entity entity, EntityMetadataType type, long value);

    /**
     * 设置Long类型的数据
     *
     * @param entity
     * @param type
     * @param value
     * @param broadcast
     */
    void setLongData(Entity entity, EntityMetadataType type, long value, boolean broadcast);

    /**
     * 获取Long类型的数据
     *
     * @param entity
     * @param type
     * @return
     */
    long getLongData(Entity entity, EntityMetadataType type);

    /**
     * 设置Integer类型的数据
     *
     * @param entity
     * @param type
     * @param value
     */
    void setIntegerData(Entity entity, EntityMetadataType type, int value);

    /**
     * 设置Integer类型的数据
     *
     * @param entity
     * @param type
     * @param value
     * @param broadcast
     */
    void setIntegerData(Entity entity, EntityMetadataType type, int value, boolean broadcast);

    /**
     * 获取Integer类型的数据
     *
     * @param entity
     * @param type
     * @return
     */
    int getIntegerData(Entity entity, EntityMetadataType type);

    /**
     * 设置Short类型的数据
     *
     * @param entity
     * @param type
     * @param value
     */
    void setShortData(Entity entity, EntityMetadataType type, short value);

    /**
     * 设置Short类型的数据
     *
     * @param entity
     * @param type
     * @param value
     * @param broadcast
     */
    void setShortData(Entity entity, EntityMetadataType type, short value, boolean broadcast);

    /**
     * 获取Short类型的数据
     *
     * @param entity
     * @param type
     * @return
     */
    short getShortData(Entity entity, EntityMetadataType type);

    /**
     * 设置Byte类型的数据
     *
     * @param entity
     * @param type
     * @param value
     */
    void setByteData(Entity entity, EntityMetadataType type, byte value);

    /**
     * 设置Byte类型的数据
     *
     * @param entity
     * @param type
     * @param value
     * @param broadcast
     */
    void setByteData(Entity entity, EntityMetadataType type, byte value, boolean broadcast);

    /**
     * 设置Byte类型的数据
     *
     * @param entity
     * @param type
     * @return
     */
    byte getByteData(Entity entity, EntityMetadataType type);

    /**
     * 设置Float类型的数据
     *
     * @param entity
     * @param type
     * @param value
     */
    void setFloatData(Entity entity, EntityMetadataType type, float value);

    /**
     * 设置Float类型的数据
     *
     * @param entity
     * @param type
     * @param value
     * @param broadcast
     */
    void setFloatData(Entity entity, EntityMetadataType type, float value, boolean broadcast);

    /**
     * 获取Float类型的数据
     *
     * @param entity
     * @param type
     * @return
     */
    float getFloatData(Entity entity, EntityMetadataType type);

    /**
     * 设置Vector3f类型的数据
     *
     * @param entity
     * @param type
     * @param value
     */
    void setVector3fData(Entity entity, EntityMetadataType type, Vector3f value);

    /**
     * 设置Vector3f类型的数据
     *
     * @param entity
     * @param type
     * @param value
     * @param broadcast
     */
    void setVector3fData(Entity entity, EntityMetadataType type, Vector3f value, boolean broadcast);

    /**
     * 设置Vector3f类型的数据
     *
     * @param entity
     * @param type
     * @return
     */
    Vector3f getVector3fData(Entity entity, EntityMetadataType type);

    /**
     * 更新并广播生物的metadata
     * <p>
     * 注意：这里不能广播给玩家自己，否则会导致更新循环
     *
     * @param entity
     * @param type
     * @param entityData
     */
    void updateMetadata(Entity entity, EntityMetadataType type, EntityData entityData);

    /**
     * 刷新玩家的metadata
     *
     * @param entity
     */
    void refreshMetadata(Entity entity);

    /**
     * 给指定AOI列表刷新MateData
     *
     * @param entity
     * @param forEntity
     */
    void refreshMetaDataFor(Entity entity, Entity forEntity);

    /**
     * 刷新玩家的metadata
     *
     * @param player
     */
    void refreshPlayerMetadata(Player player);

    void refreshPlayerMetadata(Player player, EntityMetadataType metadataType);

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
    void refreshMetadataTo(Player toPlayer, Entity entity, EntityMetadataType type, EntityData data);
}

