package com.particle.model.ui.form.view;

import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.ui.form.json.element.Element;
import com.particle.model.ui.form.json.element.StepSlider;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class StepSliderView extends ElementView implements Cloneable {

    private List<String> steps;

    private String defaultValue = "0";

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public Element toElement() {
        StepSlider stepSlider = new StepSlider();
        stepSlider.setText(getText());
        stepSlider.set_default(Integer.parseInt(defaultValue));
        stepSlider.setSteps(steps);
        return stepSlider;
    }

    @Override
    public boolean check() {
        if (StringUtils.isEmpty(getId())) {
            throw new ProphetException(ErrorCode.CONFIG_ERROR, String.format("stepSlider[%s]配置异常", getId()));
        } else if (StringUtils.isEmpty(getText())) {
            throw new ProphetException(ErrorCode.CONFIG_ERROR, String.format("stepSlider[%s]配置异常", getId()));
        } else if (this.steps.isEmpty()) {
            throw new ProphetException(ErrorCode.CONFIG_ERROR, String.format("stepSlider[%s]配置异常", getId()));
        }
        return true;
    }
}
