package com.particle.model.ui.form.layout;

import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.player.Player;
import com.particle.model.ui.form.json.window.ModalWindow;
import com.particle.model.ui.form.view.ButtonView;
import com.particle.model.ui.form.view.ElementView;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModalLayout extends LayoutContext implements Cloneable {

    private static final Logger logger = LoggerFactory.getLogger(ModalLayout.class);

    private String text;

    private ButtonView conform;

    private ButtonView cancel;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ButtonView getConform() {
        return conform;
    }

    public void setConform(ButtonView conform) {
        this.conform = conform;
    }

    public ButtonView getCancel() {
        return cancel;
    }

    public void setCancel(ButtonView cancel) {
        this.cancel = cancel;
    }

    @Override
    public boolean handleResult(Player player, String result) {
        if (StringUtils.isEmpty(result) || result.equals("null")) {
            if (this.getOnClickListener() != null) {
                this.getOnClickListener().onClose(player, this);
            }
            return true;
        }
        if (getOnClickListener() != null) {
            if (result.equalsIgnoreCase("true")) {
                getOnClickListener().onModalSubmit(player, this, true);
            } else {
                getOnClickListener().onModalSubmit(player, this, false);
            }
        }
        return true;
    }

    @Override
    public String toJson() {
        ModalWindow modalWindow = new ModalWindow();
        modalWindow.setTitle(this.title);
        modalWindow.setContent(this.text);
        modalWindow.setButton1(conform.getText());
        modalWindow.setButton2(cancel.getText());
        return modalWindow.toString();
    }

    @Override
    public boolean check() {
        if (StringUtils.isEmpty(this.text)) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, "text属性为空");
        }
        if (conform == null) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, "请添加确认按钮");
        }
        if (cancel == null) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, "请添加取消按钮");
        }
        if (StringUtils.isEmpty(conform.getText())) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, "请添加确认按钮的text属性");
        }
        if (StringUtils.isEmpty(cancel.getText())) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, "请添加取消按钮text属性");
        }
        return super.check();
    }

    @Override
    public ElementView getElementViewById(String id) {
        throw new ProphetException(ErrorCode.CORE_EROOR, "未实现该方法");
    }

    @Override
    public ModalLayout clone() {
        try {
            ModalLayout modalLayout = (ModalLayout) super.clone();
            modalLayout.setConform((ButtonView) this.conform.clone());
            modalLayout.setCancel((ButtonView) this.cancel.clone());
            return modalLayout;
        } catch (CloneNotSupportedException e) {
            logger.error("clone error:", e);
            return null;
        }

    }
}
