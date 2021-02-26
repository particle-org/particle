package com.particle.model.network.packets.data;

import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class SpawnParticleEffectPacket extends DataPacket {

    private int dimensionId;

    /*
    如果指定了entity，粒子效果就会在entity上生成，
    而position则是相对于entity位置的偏移量。
    如果没有指定entity，粒子效果就会在position这个世界坐标上生成。
     */
    private long entityUniqueId;

    private Vector3f position;

    private String effectName;

    @Override
    public int pid() {
        return ProtocolInfo.SPAWN_PARTICLE_EFFECT_PACKET;
    }

    public int getDimensionId() {
        return dimensionId;
    }

    public void setDimensionId(int dimensionId) {
        this.dimensionId = dimensionId;
    }

    public long getEntityUniqueId() {
        return entityUniqueId;
    }

    public void setEntityUniqueId(long entityUniqueId) {
        this.entityUniqueId = entityUniqueId;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public String getEffectName() {
        return effectName;
    }

    public void setEffectName(String effectName) {
        this.effectName = effectName;
    }
}
