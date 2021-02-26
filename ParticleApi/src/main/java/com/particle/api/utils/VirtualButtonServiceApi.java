package com.particle.api.utils;

import com.particle.api.entity.IEntityInteractivedHandle;
import com.particle.model.player.Player;

public interface VirtualButtonServiceApi {
    /**
     * 创建
     *
     * @param player
     * @param buttonText
     * @param handle
     */
    void createButton(Player player, String buttonText, IEntityInteractivedHandle handle);

    /**
     * 关闭
     *
     * @param player
     */
    void closeButton(Player player);

    /**
     * 是否存在此button
     *
     * @param player
     * @return
     */
    boolean isExistedButton(Player player);
}
