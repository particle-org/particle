package com.particle.api.entity;

import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.item.ItemStack;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;

public interface ItemEntityServiceAPI {

    /**
     * 创建生物
     *
     * @param itemStack 绑定的物品
     * @return 创建的生物
     */
    ItemEntity createEntity(ItemStack itemStack);

    /**
     * 创建生物
     *
     * @param itemStack 绑定的物品
     * @param position  默认位置
     * @param motion    默认动量
     * @return 创建的生物
     */
    ItemEntity createEntity(ItemStack itemStack, Vector3 position, Vector3f motion);

    /**
     * 创建生物
     *
     * @param itemStack 绑定的物品
     * @param position  默认位置
     * @param motion    默认动量
     * @return 创建的生物
     */
    ItemEntity createEntity(ItemStack itemStack, Vector3f position, Vector3f motion);

    /**
     * 创建生物
     *
     * @param itemStack 绑定的物品
     * @param position  默认位置
     * @return 创建的生物
     */
    ItemEntity createEntity(ItemStack itemStack, Vector3 position);

    /**
     * 创建生物
     *
     * @param itemStack 绑定的物品
     * @param position  默认位置
     * @return 创建的生物
     */
    ItemEntity createEntity(ItemStack itemStack, Vector3f position);
}
