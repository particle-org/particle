package com.particle.network.handler.v410;

import com.particle.model.inventory.common.*;
import com.particle.model.inventory.data.*;
import com.particle.model.math.BlockFace;
import com.particle.model.network.packets.data.InventoryTransactionPacket;
import com.particle.network.encoder.ItemStackEncoder410;
import com.particle.network.encoder.NetworkBlockPositionEncoder;
import com.particle.network.encoder.PositionFEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class InventoryTransactionPacketHandler410 extends AbstractPacketHandler<InventoryTransactionPacket> {

    private ItemStackEncoder410 itemStackEncoder410 = ItemStackEncoder410.getInstance();

    private PositionFEncoder positionFEncoder = PositionFEncoder.getInstance();

    private NetworkBlockPositionEncoder networkBlockPositionEncoder = NetworkBlockPositionEncoder.getInstance();

    @Override
    protected void doDecode(InventoryTransactionPacket dataPacket, int version) {
        // 1.16
        dataPacket.setLegacyRequestId(dataPacket.readSignedVarInt());
        if (dataPacket.getLegacyRequestId() < -1 && (dataPacket.getLegacyRequestId() & 1) == 0) {
            int length = dataPacket.readUnsignedVarInt();
            for (int i = 0; i < length; i++) {
                dataPacket.readByte();
                byte[] chainDataBytes = new byte[dataPacket.readUnsignedVarInt()];
                dataPacket.readBytes(chainDataBytes);
            }
        }

        int transactionType = dataPacket.readUnsignedVarInt();
        // 设置transactionType
        dataPacket.setTransactionType(InventoryTransactionType.valueOf(transactionType));

        // 1.16
        dataPacket.setHasNetworkIds(dataPacket.readBoolean());

        int actionLen = dataPacket.readUnsignedVarInt();
        InventoryActionData[] inventoryActionData = new InventoryActionData[actionLen];
        for (int i = 0; i < actionLen; i++) {
            inventoryActionData[i] = this.readInventActionData(dataPacket, version);
        }
        // 设置actions
        dataPacket.setInventoryActionData(inventoryActionData);
        // 设置
        ComplexInventoryTransaction complexInventoryTransaction = this.readItemTransactionData(transactionType, dataPacket, version);
        dataPacket.setComplexInventoryTransaction(complexInventoryTransaction);
    }

    @Override
    protected void doEncode(InventoryTransactionPacket dataPacket, int version) {
        dataPacket.writeSignedVarInt(dataPacket.getLegacyRequestId());
        dataPacket.writeUnsignedVarInt(dataPacket.getTransactionType().getType());
        dataPacket.writeBoolean(dataPacket.isHasNetworkIds());
    }

    /**
     * 读取 inventoryActionData
     *
     * @param dataPacket
     * @param version
     * @return
     */
    private InventoryActionData readInventActionData(InventoryTransactionPacket dataPacket, int version) {
        InventoryActionData inventoryActionData = new InventoryActionData();

        InventorySourceData inventorySourceData = new InventorySourceData();
        int sourceType = dataPacket.readUnsignedVarInt();
        // 设置source type
        inventorySourceData.setSourceType(InventorySourceType.valueOf(sourceType));
        if (sourceType == InventorySourceType.CONTAINER.getSourceType()) {
            int containerId = dataPacket.readSignedVarInt();
            // 设置containId
            inventorySourceData.setContainerId(containerId);
        } else if (sourceType == InventorySourceType.WORLD.getSourceType()) {
            int sourceFlag = dataPacket.readUnsignedVarInt();
            // 设置source flag
            inventorySourceData.setBitFlags(InventorySourceFlags.valueOf(sourceFlag));
        } else if (sourceType == InventorySourceType.INVALID.getSourceType() ||
                sourceType == InventorySourceType.CRAFTING_GRID.getSourceType()) {
            int containerId = dataPacket.readSignedVarInt();
            // 设置containId
            inventorySourceData.setContainerId(containerId);
        }
        // 设置inventorySourceData
        inventoryActionData.setSource(inventorySourceData);
        // 设置slot
        inventoryActionData.setSlot(dataPacket.readUnsignedVarInt());
        inventoryActionData.setFromItem(this.itemStackEncoder410.decode(dataPacket, version));
        inventoryActionData.setToItem(this.itemStackEncoder410.decode(dataPacket, version));

        // 1.16
        if (dataPacket.isHasNetworkIds()) {
            inventoryActionData.setStackNetworkId(dataPacket.readSignedVarInt());
        }
        return inventoryActionData;
    }

    /**
     * 读取 ItemTransactionData数据
     *
     * @param transactionType
     * @param dataPacket
     * @param version
     * @return
     */
    private ComplexInventoryTransaction readItemTransactionData(int transactionType, InventoryTransactionPacket dataPacket, int version) {
        if (transactionType == InventoryTransactionType.ITEM_USE.getType()) {
            ItemUseInventoryData itemTransactionData = new ItemUseInventoryData();
            // 设置actionType
            itemTransactionData.setActionType(ItemUseInventoryActionType.valueOf(dataPacket.readUnsignedVarInt()));
            // 设置position
            itemTransactionData.setPosition(this.networkBlockPositionEncoder.decode(dataPacket, version));
            // 设置face
            int face = dataPacket.readSignedVarInt();
            itemTransactionData.setFace(BlockFace.fromIndex(face));
            // 设置slot
            itemTransactionData.setSlot(dataPacket.readSignedVarInt());
            // 设置item
            itemTransactionData.setItem(this.itemStackEncoder410.decode(dataPacket, version));
            // 设置from position
            itemTransactionData.setFromPosition(positionFEncoder.decode(dataPacket, version));
            // 设置click position
            itemTransactionData.setClickPosition(positionFEncoder.decode(dataPacket, version));
            return itemTransactionData;
        } else if (transactionType == InventoryTransactionType.ITEM_USE_ON_ENTITY.getType()) {
            ItemUseOnEntityInventoryData itemTransactionData = new ItemUseOnEntityInventoryData();
            // 设置 entity runtime id
            itemTransactionData.setEntityRuntimeId(dataPacket.readUnsignedVarLong());
            // 设置actionType
            itemTransactionData.setActionType(ItemUseOnEntityInventoryType.valueOf(dataPacket.readUnsignedVarInt()));
            // 设置slot
            itemTransactionData.setSlot(dataPacket.readSignedVarInt());
            // 设置item
            itemTransactionData.setItem(this.itemStackEncoder410.decode(dataPacket, version));
            // 设置form position
            itemTransactionData.setFromPosition(positionFEncoder.decode(dataPacket, version));
            // 设置 hit position
            itemTransactionData.setHitPosition(positionFEncoder.decode(dataPacket, version));
            return itemTransactionData;
        } else if (transactionType == InventoryTransactionType.ITEM_RELEASE.getType()) {
            ItemReleaseInventoryData itemTransactionData = new ItemReleaseInventoryData();
            // 设置actionType
            itemTransactionData.setActionType(ItemReleaseInventoryType.valueOf(dataPacket.readUnsignedVarInt()));
            // 设置slot
            itemTransactionData.setSlot(dataPacket.readSignedVarInt());
            // 设置item
            itemTransactionData.setItem(this.itemStackEncoder410.decode(dataPacket, version));
            // 设置from position
            itemTransactionData.setFormPosition(positionFEncoder.decode(dataPacket, version));
            return itemTransactionData;
        }
        return null;
    }
}
