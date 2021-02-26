package com.particle.model.ui.form.layout;

import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.player.Player;
import com.particle.model.ui.form.listener.OnClickListener;
import com.particle.model.ui.form.view.ElementView;
import org.apache.commons.lang3.StringUtils;

public abstract class LayoutContext {

    protected String id;

    protected String title;

    private OnClickListener onClickListener;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * 处理响应
     *
     * @param result
     * @return
     */
    public abstract boolean handleResult(Player player, String result);

    /**
     * 获取下发的json数据
     *
     * @return
     */
    public abstract String toJson();

    /**
     * 检测参数
     *
     * @return
     */
    public boolean check() {
        if (StringUtils.isEmpty(id)) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, "请添加id属性");
        }
        if (StringUtils.isEmpty(title)) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, "请添加title属性");
        }
        return true;
    }

    public abstract ElementView getElementViewById(String id);
}
