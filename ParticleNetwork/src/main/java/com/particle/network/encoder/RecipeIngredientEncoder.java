package com.particle.network.encoder;

import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.DataPacket;

public class RecipeIngredientEncoder extends ModelHandler<ItemStack> {

    /**
     * 单例对象
     */
    private static final RecipeIngredientEncoder INSTANCE = new RecipeIngredientEncoder();

    /**
     * 获取单例
     */
    public static RecipeIngredientEncoder getInstance() {
        return RecipeIngredientEncoder.INSTANCE;
    }

    @Override
    public ItemStack decode(DataPacket dataPacket, int version) {
        return ItemStack.getItem(dataPacket.readSignedVarInt(), dataPacket.readSignedVarInt(), dataPacket.readSignedVarInt());
    }

    @Override
    public void encode(DataPacket dataPacket, ItemStack itemStack, int version) {
        if (itemStack.getItemType().getId() == 0) {
            dataPacket.writeSignedVarInt(itemStack.getItemType().getId());
        } else {
            dataPacket.writeSignedVarInt(itemStack.getItemType().getId());
            dataPacket.writeSignedVarInt(itemStack.getMeta() & 0x7fff);
            dataPacket.writeSignedVarInt(itemStack.getCount());
        }
    }
}
