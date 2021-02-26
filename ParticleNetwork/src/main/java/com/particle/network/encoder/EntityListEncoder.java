package com.particle.network.encoder;

import com.particle.model.entity.EntityType;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.PacketBuffer;
import com.particle.network.handler.common.ItemInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntityListEncoder {

    private PacketBuffer runtimeIdTableBuf;

    public EntityListEncoder(List<ItemInfo> standItemInfos) {
        // 读取官方id列表
        Set<String> bedrockNames = new HashSet<>();
        for (ItemInfo itemInfo : standItemInfos) {
            bedrockNames.add(itemInfo.getName());
        }

        // 读取本地id列表
        EntityType[] entityTypes = EntityType.values();
        Set<String> localNames = new HashSet<>();
        for (EntityType itemPrototype : entityTypes) {
            localNames.add(itemPrototype.actorType());
        }

        // 计算增补
        HashSet<String> missing = new HashSet<>(bedrockNames);
        missing.removeAll(localNames);

        // 计算缺失
        HashSet<String> exclude = new HashSet<>(localNames);
        exclude.removeAll(bedrockNames);
    }

    public void encode(DataPacket dataPacket) {
        dataPacket.writeBytes(this.runtimeIdTableBuf.getBuffer());
    }

}
