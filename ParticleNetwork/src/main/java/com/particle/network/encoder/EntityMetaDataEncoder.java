package com.particle.network.encoder;

import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.type.*;
import com.particle.model.network.packets.DataPacket;
import com.particle.network.handler.AbstractPacketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityMetaDataEncoder extends ModelHandler<Map<EntityMetadataType, EntityData>> {

    /**
     * 单例对象
     */
    private static final EntityMetaDataEncoder INSTANCE = new EntityMetaDataEncoder();

    /**
     * 获取单例
     */
    public static EntityMetaDataEncoder getInstance() {
        return EntityMetaDataEncoder.INSTANCE;
    }

    private Vector3FEncoder vector3FEncoder = Vector3FEncoder.getInstance();

    @Override
    public Map<EntityMetadataType, EntityData> decode(DataPacket dataPacket, int version) {
        Map<EntityMetadataType, EntityData> entityMetaData = new ConcurrentHashMap<>();

        int size = dataPacket.readUnsignedVarInt();

        for (int i = 0; i < size; i++) {
            int key = dataPacket.readUnsignedVarInt();

            if (key == 40) {
                key = key + 1;
            }

            EntityMetadataVariableType type = EntityMetadataVariableType.valueOf(dataPacket.readUnsignedVarInt());

            switch (type) {
                case BYTE:
                    entityMetaData.put(EntityMetadataType.valueOf(key), new ByteEntityData(dataPacket.readByte()));
                    break;
                case SHORT:
                    entityMetaData.put(EntityMetadataType.valueOf(key), new ShortEntityData(dataPacket.readLShort()));
                    break;
                case INT:
                    entityMetaData.put(EntityMetadataType.valueOf(key), new IntEntityData(dataPacket.readSignedVarInt()));
                    break;
                case FLOAT:
                    entityMetaData.put(EntityMetadataType.valueOf(key), new FloatEntityData(dataPacket.readLFloat()));
                    break;
                case STRING:
                    entityMetaData.put(EntityMetadataType.valueOf(key), new StringEntityData(dataPacket.readString()));
                    break;
                case LONG:
                    LongEntityData longEntityData = new LongEntityData(dataPacket.readSignedVarLong().longValue());
                    if (key == EntityMetadataType.FLAGS.value()) {
                        entityMetaData.put(EntityMetadataType.valueOf(key), this.handleDecodeEntityFlagData(longEntityData, version));
                    } else {
                        entityMetaData.put(EntityMetadataType.valueOf(key), longEntityData);
                    }

                    break;
                case VECTORY3F:
                    entityMetaData.put(EntityMetadataType.valueOf(key), new Vector3fEntityData(this.vector3FEncoder.decode(dataPacket, version)));
                    break;
            }
        }

        return entityMetaData;
    }

    /**
     * 1.7 更改了ActorFlags的顺序
     *
     * @param longEntityData
     * @param version
     * @return
     */
    private LongEntityData handleDecodeEntityFlagData(LongEntityData longEntityData, int version) {
        if (version >= AbstractPacketHandler.VERSION_1_7) {
            return longEntityData;
        }
        long data = longEntityData.getData();
        if (data > 1 << 29) {
            // 变大
            long powData = data & 0x1FFFFFFF;
            data <<= 1;
            data &= 0xFFFFFFFFC0000000L;
            data |= powData;
            longEntityData.setData(data);
            return longEntityData;
        } else {
            return longEntityData;
        }
    }

    @Override
    public void encode(DataPacket dataPacket, Map<EntityMetadataType, EntityData> entityMetaData, int version) {
        if (entityMetaData == null) {
            dataPacket.writeUnsignedVarInt(0);

            return;
        }

        dataPacket.writeUnsignedVarInt(entityMetaData.size());

        entityMetaData.forEach((key, data) -> {
            // 这里1.11和1.12版本升级导致的key偏移被抵消
            if (key.value() == 41) {
                dataPacket.writeUnsignedVarInt(key.value() - 1);
            } else {
                dataPacket.writeUnsignedVarInt(key.value());
            }

            dataPacket.writeUnsignedVarInt(data.getType().value());

            switch (data.getType()) {
                case BYTE:
                    dataPacket.writeByte((Byte) data.getData());
                    break;
                case SHORT:
                    dataPacket.writeLShort((Short) data.getData());
                    break;
                case INT:
                    dataPacket.writeSignedVarInt((Integer) data.getData());
                    break;
                case FLOAT:
                    dataPacket.writeLFloat((Float) data.getData());
                    break;
                case STRING:
                    dataPacket.writeString((String) data.getData());
                    break;
                case LONG:
                    if (key == EntityMetadataType.FLAGS) {
                        dataPacket.writeSignedVarLong(this.handleEncodeEntityFlagData((Long) data.getData(), version));
                    } else {
                        dataPacket.writeSignedVarLong((Long) data.getData());
                    }
                    break;
                case VECTORY3F:
                    vector3FEncoder.encode(dataPacket, ((Vector3fEntityData) data).getData(), version);
                    break;
            }
        });
    }

    /**
     * 1.7 更改了ActorFlags的顺序
     *
     * @param data
     * @param version
     * @return
     */
    private long handleEncodeEntityFlagData(long data, int version) {
        if (version >= AbstractPacketHandler.VERSION_1_7) {
            return data;
        }
        if (data > 1 << 29) {
            // 变小
            long powData = data & 0x1FFFFFFF;
            long highData = data >> 1;
            highData &= 0xFFFFFFFFE0000000L;
            return highData | powData;
        } else {
            return data;
        }
    }
}
