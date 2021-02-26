package com.particle.network.encoder;

import com.particle.model.item.types.ItemPrototype;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.PacketBuffer;
import com.particle.network.handler.common.ItemInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemListEncoderV410 {

    private PacketBuffer runtimeIdTableBuf;

    public ItemListEncoderV410(List<ItemInfo> standItemInfos) {
        // 读取官方id列表
        Set<String> bedrockNames = new HashSet<>();
        for (ItemInfo itemInfo : standItemInfos) {
            bedrockNames.add(itemInfo.getName());
        }

        // 读取本地id列表
        ItemPrototype[] itemPrototypes = ItemPrototype.values();
        Set<String> localNames = new HashSet<>();
        for (ItemPrototype itemPrototype : itemPrototypes) {
            localNames.add(itemPrototype.getName());
        }

        // 计算增补
        HashSet<String> missing = new HashSet<>(bedrockNames);
        missing.removeAll(localNames);

        // 计算缺失
        HashSet<String> exclude = new HashSet<>(localNames);
        exclude.removeAll(bedrockNames);

        int customItemCount = 0;

        for (ItemPrototype itemPrototype : itemPrototypes) {
            if (itemPrototype.getId() <= 10000) {
                continue;
            }
            customItemCount++;
        }

        this.runtimeIdTableBuf = new PacketBuffer(customItemCount);
        this.runtimeIdTableBuf.writeUnsignedVarInt(customItemCount);

        for (ItemPrototype itemPrototype : itemPrototypes) {
            if (itemPrototype.getId() <= 10000) {
                continue;
            }
            this.runtimeIdTableBuf.writeString(itemPrototype.getName());
            this.runtimeIdTableBuf.writeLShort((short) itemPrototype.getId());
        }
    }

    public void encode(DataPacket dataPacket) {
        dataPacket.writeBytes(this.runtimeIdTableBuf.getBuffer());
    }

}
