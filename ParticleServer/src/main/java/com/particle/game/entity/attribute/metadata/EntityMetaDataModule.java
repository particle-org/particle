package com.particle.game.entity.attribute.metadata;

import com.particle.core.ecs.module.ECSModuleBindSingleComponent;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.entity.metadata.type.*;
import com.particle.model.math.Vector3f;

import java.util.Map;

public class EntityMetaDataModule extends ECSModuleBindSingleComponent<EntityMetaDataComponent> {

    /**
     * 获取生物的MetaData
     *
     * @return
     */
    public Map<EntityMetadataType, EntityData> getEntityMetaData() {
        return this.component.getEntityMetaData();
    }

    public LongEntityData setDataFlag(MetadataDataFlag dataFlag, boolean enabled) {
        Map<EntityMetadataType, EntityData> entityMetaData = this.component.getEntityMetaData();

        LongEntityData data = (LongEntityData) entityMetaData.get(EntityMetadataType.FLAGS);

        if (data == null) {
            data = new LongEntityData((byte) 0);
            entityMetaData.put(EntityMetadataType.FLAGS, data);
        }

        long flagData = data.getData();
        if (enabled) {
            flagData = flagData | (1L << dataFlag.value());
        } else {
            flagData &= (~(1L << dataFlag.value()));
        }
        data.setData(flagData);

        return data;
    }

    public boolean getDataFlag(MetadataDataFlag dataFlag) {
        Map<EntityMetadataType, EntityData> entityMetaData = this.component.getEntityMetaData();

        LongEntityData data = (LongEntityData) entityMetaData.get(EntityMetadataType.FLAGS);

        if (data == null) {
            return false;
        }

        return (data.getData() & (1L << dataFlag.value())) != 0;
    }

    /**
     * 设置boolean类型的entityData
     *
     * @param type
     * @param value
     */
    public ByteEntityData setBooleanData(EntityMetadataType type, boolean value) {
        Map<EntityMetadataType, EntityData> entityMetaData = this.component.getEntityMetaData();

        ByteEntityData data = (ByteEntityData) entityMetaData.get(type);

        if (data == null) {
            data = new ByteEntityData((byte) 0);
            entityMetaData.put(type, data);
        }

        data.setData((byte) (value ? 1 : 0));

        return data;
    }

    /**
     * 获取boolean类型的entityData
     *
     * @param type
     * @return
     */
    public boolean getBooleanData(EntityMetadataType type) {
        Map<EntityMetadataType, EntityData> entityMetaData = this.component.getEntityMetaData();

        ByteEntityData data = (ByteEntityData) entityMetaData.get(type);

        if (data == null) {
            return false;
        }

        return data.getData() == 1;
    }

    /**
     * 设置String类型的数据
     *
     * @param type
     * @param value
     */
    public StringEntityData setStringData(EntityMetadataType type, String value) {
        Map<EntityMetadataType, EntityData> entityMetaData = this.component.getEntityMetaData();

        StringEntityData data = (StringEntityData) entityMetaData.get(type);

        if (data == null) {
            data = new StringEntityData(value);
            entityMetaData.put(type, data);
        }

        data.setData(value);

        return data;
    }

    /**
     * 获取String类型的数据
     *
     * @param type
     * @return
     */
    public String getStringData(EntityMetadataType type) {
        Map<EntityMetadataType, EntityData> entityMetaData = this.component.getEntityMetaData();

        StringEntityData data = (StringEntityData) entityMetaData.get(type);

        if (data == null) {
            return "";
        }

        return data.getData();
    }

    public LongEntityData setLongData(EntityMetadataType type, long value) {
        Map<EntityMetadataType, EntityData> entityMetaData = this.component.getEntityMetaData();

        LongEntityData data = (LongEntityData) entityMetaData.get(type);

        if (data == null) {
            data = new LongEntityData(value);
            entityMetaData.put(type, data);
        }

        data.setData(value);

        return data;
    }

    public long getLongData(EntityMetadataType type) {
        Map<EntityMetadataType, EntityData> entityMetaData = this.component.getEntityMetaData();

        LongEntityData data = (LongEntityData) entityMetaData.get(type);

        if (data == null) {
            return 0;
        }

        return data.getData();
    }

    /**
     * 设置Integer类型的数据
     *
     * @param type
     * @param value
     */
    public IntEntityData setIntegerData(EntityMetadataType type, int value) {
        Map<EntityMetadataType, EntityData> entityMetaData = this.component.getEntityMetaData();

        IntEntityData data = (IntEntityData) entityMetaData.get(type);

        if (data == null) {
            data = new IntEntityData(value);
            entityMetaData.put(type, data);
        }

        data.setData(value);

        return data;
    }

    /**
     * 获取Integer类型的数据
     *
     * @param type
     * @return
     */
    public int getIntegerData(EntityMetadataType type) {
        Map<EntityMetadataType, EntityData> entityMetaData = this.component.getEntityMetaData();

        IntEntityData data = (IntEntityData) entityMetaData.get(type);

        if (data == null) {
            return 0;
        }

        return data.getData();
    }

    /**
     * 设置Short类型的数据
     *
     * @param type
     * @param value
     */
    public ShortEntityData setShortData(EntityMetadataType type, short value) {
        Map<EntityMetadataType, EntityData> entityMetaData = this.component.getEntityMetaData();

        ShortEntityData data = (ShortEntityData) entityMetaData.get(type);

        if (data == null) {
            data = new ShortEntityData(value);
            entityMetaData.put(type, data);
        }

        data.setData(value);

        return data;
    }

    /**
     * 获取Short类型的数据
     *
     * @param type
     * @return
     */
    public short getShortData(EntityMetadataType type) {
        Map<EntityMetadataType, EntityData> entityMetaData = this.component.getEntityMetaData();

        ShortEntityData data = (ShortEntityData) entityMetaData.get(type);

        if (data == null) {
            return 0;
        }

        return data.getData();
    }

    /**
     * 设置Byte类型的数据
     *
     * @param type
     * @param value
     */
    public ByteEntityData setByteData(EntityMetadataType type, byte value) {
        Map<EntityMetadataType, EntityData> entityMetaData = this.component.getEntityMetaData();

        ByteEntityData data = (ByteEntityData) entityMetaData.get(type);

        if (data == null) {
            data = new ByteEntityData(value);
            entityMetaData.put(type, data);
        }

        data.setData(value);

        return data;
    }

    /**
     * 设置Byte类型的数据
     *
     * @param type
     * @return
     */
    public byte getByteData(EntityMetadataType type) {
        Map<EntityMetadataType, EntityData> entityMetaData = this.component.getEntityMetaData();

        ByteEntityData data = (ByteEntityData) entityMetaData.get(type);

        if (data == null) {
            return 0;
        }

        return data.getData();
    }

    /**
     * 设置Float类型的数据
     *
     * @param type
     * @param value
     */
    public FloatEntityData setFloatData(EntityMetadataType type, float value) {
        Map<EntityMetadataType, EntityData> entityMetaData = this.component.getEntityMetaData();

        FloatEntityData data = (FloatEntityData) entityMetaData.get(type);

        if (data == null) {
            data = new FloatEntityData(value);
            entityMetaData.put(type, data);
        }

        data.setData(value);

        return data;
    }

    /**
     * 获取Float类型的数据
     *
     * @param type
     * @return
     */
    public float getFloatData(EntityMetadataType type) {
        Map<EntityMetadataType, EntityData> entityMetaData = this.component.getEntityMetaData();

        FloatEntityData data = (FloatEntityData) entityMetaData.get(type);

        if (data == null) {
            return 0;
        }

        return data.getData();
    }

    public Vector3fEntityData setVector3fData(EntityMetadataType type, Vector3f value) {
        Map<EntityMetadataType, EntityData> entityMetaData = this.component.getEntityMetaData();

        Vector3fEntityData data = (Vector3fEntityData) entityMetaData.get(type);

        if (data == null) {
            data = new Vector3fEntityData(value);
            entityMetaData.put(type, data);
        }

        data.setData(value);

        return data;
    }

    public Vector3f getVector3fData(EntityMetadataType type) {
        Map<EntityMetadataType, EntityData> entityMetaData = this.component.getEntityMetaData();

        Vector3fEntityData data = (Vector3fEntityData) entityMetaData.get(type);

        if (data == null) {
            return null;
        }

        return data.getData();
    }

    @Override
    protected Class<EntityMetaDataComponent> getTypeClass() {
        return EntityMetaDataComponent.class;
    }
}
