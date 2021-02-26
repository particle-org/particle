package com.particle.network.encoder;

import com.particle.model.entity.attribute.EntityAttribute;
import com.particle.model.entity.attribute.EntityAttributeType;
import com.particle.model.network.packets.DataPacket;

public class EntityAttributesEncoder extends ModelHandler<EntityAttribute[]> {
    /**
     * 单例对象
     */
    private static final EntityAttributesEncoder INSTANCE = new EntityAttributesEncoder();

    /**
     * 获取单例
     */
    public static EntityAttributesEncoder getInstance() {
        return EntityAttributesEncoder.INSTANCE;
    }

    @Override
    public EntityAttribute[] decode(DataPacket dataPacket, int version) {
        int size = dataPacket.readUnsignedVarInt();
        EntityAttribute[] entityAttributes = new EntityAttribute[size];

        for (int i = 0; i < size; i++) {
            String name = dataPacket.readString();
            float minValue = dataPacket.readLFloat();
            float value = dataPacket.readLFloat();
            float maxValue = dataPacket.readLFloat();

            entityAttributes[i] = new EntityAttribute(EntityAttributeType.fromName(name), minValue, maxValue, value, maxValue);
        }

        return entityAttributes;
    }

    @Override
    public void encode(DataPacket dataPacket, EntityAttribute[] entityAttributes, int version) {
        if (entityAttributes == null) {
            dataPacket.writeUnsignedVarInt(0);
        } else {
            dataPacket.writeUnsignedVarInt(entityAttributes.length);
            for (EntityAttribute attribute : entityAttributes) {
                dataPacket.writeString(attribute.getName());
                dataPacket.writeLFloat(attribute.getMinValue());
                dataPacket.writeLFloat(attribute.getCurrentValue());
                dataPacket.writeLFloat(attribute.getMaxValue());
            }
        }
    }
}