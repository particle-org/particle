package com.particle.model.ui.form.view;

import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.ui.form.json.element.Element;
import com.particle.model.ui.form.json.element.Toggle;
import org.apache.commons.lang3.StringUtils;

public class ToggleView extends ElementView implements Cloneable {

    private String defaultValue = "false";

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public Element toElement() {
        Toggle toggle = new Toggle();
        toggle.setText(getText());
        toggle.set_default(Boolean.parseBoolean(this.defaultValue));
        return toggle;
    }

    @Override
    public boolean check() {
        if (StringUtils.isEmpty(getId())) {
            throw new ProphetException(ErrorCode.CONFIG_ERROR, String.format("toggle[%s]配置异常", getId()));
        } else if (StringUtils.isEmpty(getText())) {
            throw new ProphetException(ErrorCode.CONFIG_ERROR, String.format("toggle[%s]配置异常", getId()));
        }
        return true;
    }

}
