package com.particle.network.encoder;

import com.particle.model.entity.attribute.EntityAttribute;
import com.particle.model.entity.attribute.EntityAttributeType;
import com.particle.model.network.packets.DataPacket;

public class UpdateEntityAttributesEncoder extends ModelHandler<EntityAttribute[]> {

    /**
     * 单例对象
     */
    private static final UpdateEntityAttributesEncoder INSTANCE = new UpdateEntityAttributesEncoder();

    /**
     * 获取单例
     */
    public static UpdateEntityAttributesEncoder getInstance() {
        return UpdateEntityAttributesEncoder.INSTANCE;
    }

    @Override
    public EntityAttribute[] decode(DataPacket dataPacket, int version) {
        int size = dataPacket.readUnsignedVarInt();
        EntityAttribute[] entityAttributes = new EntityAttribute[size];

        for (int i = 0; i < size; i++) {
            float minValue = dataPacket.readLFloat();
            float maxValue = dataPacket.readLFloat();
            float value = dataPacket.readLFloat();
            float defaultValue = dataPacket.readLFloat();
            String name = dataPacket.readString();

            entityAttributes[i] = new EntityAttribute(EntityAttributeType.fromName(name), minValue, maxValue, value, defaultValue);
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
                dataPacket.writeLFloat(attribute.getMinValue());
                dataPacket.writeLFloat(attribute.getMaxValue());
                dataPacket.writeLFloat(attribute.getCurrentValue());
                dataPacket.writeLFloat(attribute.getDefaultValue());
                dataPacket.writeString(attribute.getName());
            }
        }
    }
}