package com.particle.model.ui.form.view;

import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.ui.form.json.element.Button;
import com.particle.model.ui.form.json.element.Element;
import com.particle.model.ui.form.json.element.ImageData;
import com.particle.model.ui.form.listener.OnClickListener;
import org.apache.commons.lang3.StringUtils;

public class ButtonView extends ElementView implements Cloneable {

    private String imageType;

    private String imagePath;

    private String onClick;

    private String isShow;

    private String permission;

    private OnClickListener onClickListener;

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getOnClick() {
        return onClick;
    }

    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }


    @Override
    public Element toElement() {
        Button button = new Button();
        ImageData image = new ImageData();
        image.setType(this.getImageType());
        image.setData(this.getImagePath());
        button.setImage(image);
        button.setText(this.getText());
        return button;

    }

    @Override
    public boolean check() {
        if (!StringUtils.isEmpty(this.imagePath)) {
            if (!this.imageType.equals(ImageData.IMAGE_DATA_TYPE_URL) && !this.imageType.equals(ImageData.IMAGE_DATA_TYPE_PATH)) {
                throw new ProphetException(ErrorCode.PARAM_ERROR, "图片类型只支持url和path");
            }
        } else if (StringUtils.isEmpty(getId())) {
            throw new ProphetException(ErrorCode.CONFIG_ERROR, String.format("dropDown[%s]配置异常", getId()));
        } else if (StringUtils.isEmpty(getText())) {
            throw new ProphetException(ErrorCode.CONFIG_ERROR, String.format("dropDown[%s]配置异常", getId()));
        }
        return true;
    }
}
