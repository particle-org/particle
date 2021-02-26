package com.particle.model.ui.form.view;

import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.ui.form.json.element.Element;
import com.particle.model.ui.form.json.element.Input;
import org.apache.commons.lang3.StringUtils;

public class InputView extends ElementView implements Cloneable {

    private String placeholder;

    private String defaultValue;

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public Element toElement() {
        Input input = new Input();
        input.setText(getText());
        input.setPlaceholder(placeholder);
        input.set_default(defaultValue);
        return input;
    }

    @Override
    public boolean check() {
        if (StringUtils.isEmpty(getId())) {
            throw new ProphetException(ErrorCode.CONFIG_ERROR, String.format("input[%s]配置异常", getId()));
        } else if (StringUtils.isEmpty(getText())) {
            throw new ProphetException(ErrorCode.CONFIG_ERROR, String.format("input[%s]配置异常", getId()));
        }
        return true;
    }
}
