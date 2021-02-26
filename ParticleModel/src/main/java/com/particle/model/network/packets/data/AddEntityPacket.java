package com.particle.model.network.packets.data;

import com.particle.model.entity.attribute.EntityAttribute;
import com.particle.model.entity.link.EntityLink;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.type.EntityData;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddEntityPacket extends DataPacket {
    private long entityUniqueId;
    private long entityRuntimeId;

    /**
     * entity的类型
     * 1.8及其以上
     */
    private String actorType;

    private Vector3f position;
    private Direction direction;
    private float speedX;
    private float speedY;
    private float speedZ;
    private Map<EntityMetadataType, EntityData> metadata;
    private EntityAttribute[] attribute;
    private List<EntityLink> entityLinks = new ArrayList<>();

    @Override
    public int pid() {
        return ProtocolInfo.ADD_ENTITY_PACKET;
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

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
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

    public EntityAttribute[] getAttribute() {
        return attribute;
    }

    public void setAttribute(EntityAttribute[] attribute) {
        this.attribute = attribute;
    }

    public List<EntityLink> getEntityLinks() {
        return entityLinks;
    }

    public void setEntityLinks(List<EntityLink> entityLinks) {
        this.entityLinks = entityLinks;
    }

    public String getActorType() {
        return actorType;
    }

    public void setActorType(String actorType) {
        this.actorType = actorType;
    }
}
