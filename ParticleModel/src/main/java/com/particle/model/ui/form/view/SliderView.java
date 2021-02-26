package com.particle.model.ui.form.view;

import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.ui.form.json.element.Element;
import com.particle.model.ui.form.json.element.Slider;
import org.apache.commons.lang3.StringUtils;

public class SliderView extends ElementView implements Cloneable {

    private String min = "0";

    private String max = "100";

    private String step;

    private String _default;

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String get_default() {
        return _default;
    }

    public void set_default(String _default) {
        this._default = _default;
    }

    @Override
    public Element toElement() {
        Slider slider = new Slider();
        slider.setText(getText());
        slider.setMin(Float.parseFloat(this.min));
        slider.setMax(Float.parseFloat(this.max));
        slider.setStep(Integer.parseInt(this.step));
        slider.set_default(Float.parseFloat(this._default));
        return slider;
    }

    @Override
    public boolean check() {
        if (StringUtils.isEmpty(getId())) {
            throw new ProphetException(ErrorCode.CONFIG_ERROR, String.format("slider[%s]配置异常", getId()));
        } else if (StringUtils.isEmpty(getText())) {
            throw new ProphetException(ErrorCode.CONFIG_ERROR, String.format("slider[%s]配置异常", getId()));
        }
        return true;
    }
}
