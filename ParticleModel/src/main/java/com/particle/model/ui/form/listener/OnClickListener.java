package com.particle.model.ui.form.listener;

import com.particle.model.player.Player;
import com.particle.model.ui.form.layout.LayoutContext;
import com.particle.model.ui.form.view.ButtonView;

import java.util.Map;

public interface OnClickListener<T extends LayoutContext> {

    /**
     * 用于按钮的响应
     *
     * @param view
     */
    default public void onClick(Player player, ButtonView view) {

    }

    /**
     * 用于关闭表单
     *
     * @param player
     * @param context
     */
    default public void onClose(Player player, T context) {

    }

    /**
     * 用于modal提交的响应
     *
     * @param context
     * @param result
     */
    default public void onModalSubmit(Player player, T context, boolean result) {

    }

    /**
     * 用于custom提交的响应
     *
     * @param context
     */
    default public void onCustomSubmit(Player player, T context, Map<String, String> result) {

    }
}
