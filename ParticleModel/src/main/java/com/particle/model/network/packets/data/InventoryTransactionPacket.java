package com.particle.model.network.packets.data;

import com.particle.model.inventory.common.InventoryTransactionType;
import com.particle.model.inventory.data.ComplexInventoryTransaction;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

import java.util.Arrays;

public class InventoryTransactionPacket extends DataPacket {

    private InventoryTransactionType transactionType;

    private InventoryActionData[] inventoryActionData;

    private ComplexInventoryTransaction complexInventoryTransaction;

    // 1.16
    private boolean hasNetworkIds = false;
    private int legacyRequestId = 0;

    @Override
    public int pid() {
        return ProtocolInfo.INVENTORY_TRANSACTION_PACKET;
    }

    public InventoryTransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(InventoryTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public InventoryActionData[] getInventoryActionData() {
        return inventoryActionData;
    }

    public void setInventoryActionData(InventoryActionData[] inventoryActionData) {
        this.inventoryActionData = inventoryActionData;
    }

    public ComplexInventoryTransaction getComplexInventoryTransaction() {
        return complexInventoryTransaction;
    }

    public void setComplexInventoryTransaction(ComplexInventoryTransaction complexInventoryTransaction) {
        this.complexInventoryTransaction = complexInventoryTransaction;
    }

    public boolean isHasNetworkIds() {
        return hasNetworkIds;
    }

    public void setHasNetworkIds(boolean hasNetworkIds) {
        this.hasNetworkIds = hasNetworkIds;
    }

    public int getLegacyRequestId() {
        return legacyRequestId;
    }

    public void setLegacyRequestId(int legacyRequestId) {
        this.legacyRequestId = legacyRequestId;
    }

    @Override
    public String toString() {
        return "InventoryTransactionPacket{" +
                "transactionType=" + transactionType +
                ", inventoryActionData=" + Arrays.toString(inventoryActionData) +
                ", itemTransactionData=" + complexInventoryTransaction +
                '}';
    }
}
