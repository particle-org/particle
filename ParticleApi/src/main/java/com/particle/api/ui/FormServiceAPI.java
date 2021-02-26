package com.particle.api.ui;

import com.particle.model.player.Player;
import com.particle.model.ui.form.layout.LayoutContext;

import java.io.InputStream;

public interface FormServiceAPI {

    /**
     * 将xml解析成对应的布局控件
     *
     * @param inputStream 输入
     * @return 结果
     */
    public LayoutContext inflate(InputStream inputStream);

    /**
     * 添加表单
     *
     * @param player  玩家
     * @param context layout
     * @return 返回id
     */
    public int openFormLayout(Player player, LayoutContext context);

    /**
     * 删除表单
     *
     * @param player 玩家
     * @param formId id
     */
    public void removeFormLayout(Player player, int formId);

    /**
     * 根据id获取formlayout
     *
     * @param player 玩家
     * @param formId id
     * @return layout
     */
    public LayoutContext getFormLayoutByFormId(Player player, int formId);
}
