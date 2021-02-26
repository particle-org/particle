package com.particle.model.ui.form.layout;

import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.player.Player;
import com.particle.model.ui.form.json.element.Button;
import com.particle.model.ui.form.json.element.Element;
import com.particle.model.ui.form.json.window.SimpleWindow;
import com.particle.model.ui.form.view.ButtonView;
import com.particle.model.ui.form.view.ElementView;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimpleLayout extends LayoutContext implements Cloneable {

    private static final Logger logger = LoggerFactory.getLogger(SimpleLayout.class);

    private String text;

    private List<ButtonView> buttonViews = new ArrayList<>();

    /**
     * 关闭的时候调用
     */
    private String onClose;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOnClose() {
        return onClose;
    }

    public void setOnClose(String onClose) {
        this.onClose = onClose;
    }

    public List<ButtonView> getButtonViews() {
        return buttonViews;
    }

    public void setButtonViews(List<ButtonView> buttonViews) {
        this.buttonViews = buttonViews;
    }

    public void addButtonView(ButtonView view) {
        buttonViews.add(view);
    }

    @Override
    public boolean handleResult(Player player, String result) {
        if (StringUtils.isEmpty(result) || result.equals("null")) {
            if (this.getOnClickListener() != null) {
                this.getOnClickListener().onClose(player, this);
            }
            return true;
        }
        int index;
        try {
            index = Integer.parseInt(result);
        } catch (Exception e) {
            logger.error("handle result failed!", e);
            return false;
        }
        if (index >= buttonViews.size()) {
            return false;
        }
        ButtonView buttonView = this.buttonViews.get(index);
        if (buttonView.getOnClickListener() != null) {
            buttonView.getOnClickListener().onClick(player, buttonView);
        }
        return true;
    }

    @Override
    public String toJson() {
        SimpleWindow simpleWindow = new SimpleWindow();
        simpleWindow.setTitle(this.title);
        simpleWindow.setContent(this.text);
        List<Button> buttons = new ArrayList<>();
        for (ButtonView view : buttonViews) {
            if (view == null) {
                continue;
            }
            Element element = view.toElement();
            if (element == null) {
                continue;
            }
            buttons.add((Button) element);
        }
        simpleWindow.setButtons(buttons);
        return simpleWindow.toString();
    }

    @Override
    public boolean check() {
        if (this.text == null) {
            this.text = "";
        } else if (StringUtils.isEmpty(getId())) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, String.format("表单[%s]ID不合法", getId()));
        }
        Set<String> existedIds = new HashSet<>();
        for (ButtonView view : buttonViews) {
            view.check();
            if (existedIds.contains(view.getId())) {
                throw new ProphetException(ErrorCode.PARAM_ERROR, String.format("表单[%s]子项存在重复的ID[%s]", getId(), view.getId()));
            }
            existedIds.add(view.getId());
        }
        return super.check();
    }

    @Override
    public ElementView getElementViewById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        for (ElementView elementView : this.buttonViews) {
            if (id.equals(elementView.getId())) {
                return elementView;
            }
        }
        return null;
    }

    /**
     * 根据id获取按钮
     *
     * @param btnId
     * @return
     */
    public ButtonView getButtonById(String btnId) {
        if (StringUtils.isEmpty(btnId)) {
            return null;
        }
        for (ButtonView btn : buttonViews) {
            if (btnId.equalsIgnoreCase(btn.getId())) {
                return btn;
            }
        }
        return null;
    }

    @Override
    public SimpleLayout clone() {
        try {
            SimpleLayout simpleLayout = (SimpleLayout) super.clone();
            List<ButtonView> newBtnViews = new ArrayList<>();
            for (ButtonView view : this.getButtonViews()) {
                newBtnViews.add((ButtonView) view.clone());
            }
            simpleLayout.setButtonViews(newBtnViews);
            return simpleLayout;
        } catch (CloneNotSupportedException e) {
            logger.error("clone error : ", e);
            return null;
        }
    }
}
