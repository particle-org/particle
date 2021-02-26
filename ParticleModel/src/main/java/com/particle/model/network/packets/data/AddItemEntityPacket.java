package com.particle.model.network.packets.data;

import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.type.EntityData;
import com.particle.model.item.ItemStack;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

import java.util.Map;

public class AddItemEntityPacket extends DataPacket {
    private long entityUniqueId;
    private long entityRuntimeId;
    private ItemStack itemStack;
    private Vector3f position;
    private float speedX;
    private float speedY;
    private float speedZ;
    private Map<EntityMetadataType, EntityData> metadata;
    private boolean fromFishing;

    @Override
    public int pid() {
        return ProtocolInfo.ADD_ITEM_ENTITY_PACKET;
    }

    public long getEntityUniqueId() {
        return entityUniqueId;
    }

    public void setEntityUniqueId(long entityUniqueId) {
        this.entityUniqueId = entityUniqueId;
    }

    public long getEntityRuntimeId() {
        return entityRuntimeId;
    }

    public void setEntityRuntimeId(long entityRuntimeId) {
        this.entityRuntimeId = entityRuntimeId;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public float getSpeedZ() {
        return speedZ;
    }

    public void setSpeedZ(float speedZ) {
        this.speedZ = speedZ;
    }

    public Map<EntityMetadataType, EntityData> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<EntityMetadataType, EntityData> metadata) {
        this.metadata = metadata;
    }

    public boolean isFromFishing() {
        return fromFishing;
    }

    public void setFromFishing(boolean fromFishing) {
        this.fromFishing = fromFishing;
    }
}
