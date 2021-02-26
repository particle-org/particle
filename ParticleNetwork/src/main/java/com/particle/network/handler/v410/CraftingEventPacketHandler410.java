package com.particle.network.handler.v410;

import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.data.CraftingEventPacket;
import com.particle.network.encoder.ItemStackEncoder410;
import com.particle.network.encoder.UUIDEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class CraftingEventPacketHandler410 extends AbstractPacketHandler<CraftingEventPacket> {

    private ItemStackEncoder410 itemStackEncoder = ItemStackEncoder410.getInstance();

    private UUIDEncoder uuidEncoder = UUIDEncoder.getInstance();

    @Override
    protected void doDecode(CraftingEventPacket dataPacket, int version) {
        dataPacket.setContainerId(dataPacket.readByte());
        dataPacket.setType(dataPacket.readSignedVarInt());
        dataPacket.setRecipeId(uuidEncoder.decode(dataPacket, version));
        int inputSize = dataPacket.readUnsignedVarInt();
        ItemStack[] inputs = new ItemStack[inputSize];
        for (int i = 0; i < inputSize; i++) {
            inputs[i] = itemStackEncoder.decode(dataPacket, version);
        }

        int outputSize = dataPacket.readUnsignedVarInt();
        ItemStack[] outputs = new ItemStack[outputSize];
        for (int i = 0; i < outputSize; i++) {
            outputs[i] = itemStackEncoder.decode(dataPacket, version);
        }
        dataPacket.setInputs(inputs);
        dataPacket.setOutputs(outputs);
    }

    @Override
    protected void doEncode(CraftingEventPacket dataPacket, int version) {

    }
}
