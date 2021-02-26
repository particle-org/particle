package com.particle.api.proxy.customgui;

import com.particle.model.player.Player;
import com.particle.model.ui.form.layout.LayoutContext;

import java.util.Map;

public interface CustomGuiApi {

    /**
     * 根据对应的namespace获取到对应的layoutContext
     * 注意：其对应的更改不会更改GUI模板
     *
     * @param player
     * @param namespace
     * @return
     */
    LayoutContext getLayoutContext(Player player, String namespace);

    /**
     * 显示
     *
     * @param player
     * @param layoutContext
     * @return
     */
    boolean show(Player player, LayoutContext layoutContext);

    /**
     * 显示
     *
     * @param player
     * @param layoutContext
     * @param params
     * @return
     */
    boolean show(Player player, LayoutContext layoutContext, Map<String, String> params);
}
