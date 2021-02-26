package com.particle.network.encoder;

import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.type.*;
import com.particle.model.network.packets.DataPacket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityMetaDataEncoderV361 extends ModelHandler<Map<EntityMetadataType, EntityData>> {

    /**
     * 单例对象
     */
    private static final EntityMetaDataEncoderV361 INSTANCE = new EntityMetaDataEncoderV361();

    /**
     * 获取单例
     */
    public static EntityMetaDataEncoderV361 getInstance() {
        return EntityMetaDataEncoderV361.INSTANCE;
    }

    private Vector3FEncoder vector3FEncoder = Vector3FEncoder.getInstance();

    @Override
    public Map<EntityMetadataType, EntityData> decode(DataPacket dataPacket, int version) {
        Map<EntityMetadataType, EntityData> entityMetaData = new ConcurrentHashMap<>();

        int size = dataPacket.readUnsignedVarInt();

        for (int i = 0; i < size; i++) {
            int key = dataPacket.readUnsignedVarInt();
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
                    entityMetaData.put(EntityMetadataType.valueOf(key), new LongEntityData(dataPacket.readSignedVarLong().longValue()));
                    break;
                case VECTORY3F:
                    entityMetaData.put(EntityMetadataType.valueOf(key), new Vector3fEntityData(this.vector3FEncoder.decode(dataPacket, version)));
                    break;
            }
        }

        return entityMetaData;
    }

    @Override
    public void encode(DataPacket dataPacket, Map<EntityMetadataType, EntityData> entityMetaData, int version) {
        if (entityMetaData == null) {
            dataPacket.writeUnsignedVarInt(0);

            return;
        }

        dataPacket.writeUnsignedVarInt(entityMetaData.size());

        entityMetaData.forEach((key, data) -> {
            dataPacket.writeUnsignedVarInt(key.value());
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
                    dataPacket.writeSignedVarLong((Long) data.getData());
                    break;
                case VECTORY3F:
                    vector3FEncoder.encode(dataPacket, ((Vector3fEntityData) data).getData(), version);
                    break;
            }
        });
    }
}
