package com.particle.model.ui.form.view;

import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.ui.form.json.element.Element;
import com.particle.model.ui.form.json.element.Label;
import org.apache.commons.lang3.StringUtils;

public class LabelView extends ElementView implements Cloneable {

    @Override
    public Element toElement() {
        Label label = new Label();
        label.setText(getText());
        return label;
    }

    @Override
    public boolean check() {
        if (StringUtils.isEmpty(getId())) {
            throw new ProphetException(ErrorCode.CONFIG_ERROR, String.format("label[%s]配置异常", getId()));
        } else if (StringUtils.isEmpty(getText())) {
            throw new ProphetException(ErrorCode.CONFIG_ERROR, String.format("label[%s]配置异常", getId()));
        }
        return true;
    }
}
