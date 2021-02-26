package com.particle.model.ui.form.view;

import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.ui.form.json.element.DropDown;
import com.particle.model.ui.form.json.element.Element;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DropDownView extends ElementView implements Cloneable {

    private List<OptionView> options = new ArrayList<>();

    private String defaultValue = "0";

    public List<OptionView> getOptions() {
        return options;
    }

    public void addOption(OptionView option) {
        this.options.add(option);
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public Element toElement() {
        DropDown dropDown = new DropDown();
        dropDown.set_default(Integer.parseInt(this.defaultValue));
        dropDown.setText(getText());
        List<String> temps = new ArrayList<>();
        for (OptionView option : this.options) {
            temps.add(option.getDisplay());
        }
        dropDown.setOptions(temps);
        return dropDown;
    }

    @Override
    public boolean check() {
        if (this.options.isEmpty()) {
            throw new ProphetException(ErrorCode.CONFIG_ERROR, String.format("dropDown[%s]配置异常", getId()));
        } else if (StringUtils.isEmpty(getText())) {
            throw new ProphetException(ErrorCode.CONFIG_ERROR, String.format("dropDown[%s]配置异常", getId()));
        } else if (StringUtils.isEmpty(getId())) {
            throw new ProphetException(ErrorCode.CONFIG_ERROR, String.format("dropDown[%s]配置异常", getId()));
        }
        return true;
    }
}
