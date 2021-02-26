package com.particle.model.ui.form.layout;

import com.alibaba.fastjson.JSONObject;
import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.player.Player;
import com.particle.model.ui.form.json.element.Element;
import com.particle.model.ui.form.json.element.ImageData;
import com.particle.model.ui.form.json.window.CustomWindow;
import com.particle.model.ui.form.view.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CustomLayout extends LayoutContext implements Cloneable {

    private static final Logger logger = LoggerFactory.getLogger(CustomLayout.class);

    private String imageType;

    private String imageUrl;

    /**
     * 关闭的时候调用
     */
    private String onClose;

    /**
     * 提交的时候调用
     */
    private String onSubmit;

    private List<ElementView> content = new ArrayList<>();

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOnClose() {
        return onClose;
    }

    public void setOnClose(String onClose) {
        this.onClose = onClose;
    }

    public String getOnSubmit() {
        return onSubmit;
    }

    public void setOnSubmit(String onSubmit) {
        this.onSubmit = onSubmit;
    }

    public List<ElementView> getContent() {
        return content;
    }

    public void addContent(ElementView content) {
        this.content.add(content);
    }

    @Override
    public boolean handleResult(Player player, String result) {
        if (StringUtils.isEmpty(result) || result.equals("null")) {
            if (this.getOnClickListener() != null) {
                this.getOnClickListener().onClose(player, this);
            }
            return true;
        }
        List<String> responses = JSONObject.parseArray(result, String.class);
        int index = 0;
        if (responses == null) {
            return true;
        }
        Map<String, String> params = new HashMap<>();
        for (String entry : responses) {
            ElementView element = this.content.get(index);
            if (element == null) {
                break;
            }
            if (element instanceof LabelView) {
                index++;
                continue;
            } else if (element instanceof InputView) {
                // 主要为了解决命令传递的空字符问题...好傻
                if (StringUtils.isEmpty(entry)) {
                    entry = "无";
                }
            } else if (StringUtils.isEmpty(entry)) {
                // 在inputView的时候，即使空的也会执行
                index++;
                continue;
            } else if (element instanceof DropDownView) {
                List<OptionView> optionViews = ((DropDownView) element).getOptions();
                int optionIndex = Integer.parseInt(entry);
                if (optionViews == null || optionViews.size() <= optionIndex) {
                    index++;
                    continue;
                }

                OptionView optionView = optionViews.get(optionIndex);
                entry = optionView.getValue();
            }

            params.put(element.getId(), entry);
            index++;
        }
        if (getOnClickListener() != null) {
            getOnClickListener().onCustomSubmit(player, this, params);
        }
        return true;
    }

    @Override
    public String toJson() {
        CustomWindow customWindow = new CustomWindow();
        customWindow.setTitle(title);
        ImageData icon = new ImageData();
        icon.setType(imageType);
        icon.setData(imageUrl);
        List<Element> elements = new ArrayList<>();
        for (ElementView elementView : this.content) {
            elements.add(elementView.toElement());
        }
        customWindow.setIcon(icon);
        customWindow.setContent(elements);
        return customWindow.toString();
    }

    @Override
    public boolean check() {
        super.check();
        if (StringUtils.isEmpty(getId())) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, String.format("表单[%s]ID不合法", getId()));
        }

        if (!StringUtils.isEmpty(this.imageUrl)) {
            if (!this.imageType.equals(ImageData.IMAGE_DATA_TYPE_URL) && !this.imageType.equals(ImageData.IMAGE_DATA_TYPE_PATH)) {
                throw new ProphetException(ErrorCode.PARAM_ERROR, "图片类型只支持url和path");
            }
        }

        Set<String> existedIds = new HashSet<>();
        for (ElementView view : this.content) {
            view.check();
            if (existedIds.contains(view.getId())) {
                throw new ProphetException(ErrorCode.PARAM_ERROR, String.format("表单[%s]子项存在重复的ID[%s]", getId(), view.getId()));
            }
            existedIds.add(view.getId());
        }

        return true;
    }

    @Override
    public ElementView getElementViewById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        for (ElementView elementView : this.content) {
            if (id.equals(elementView.getId())) {
                return elementView;
            }
        }
        return null;
    }

    @Override
    public CustomLayout clone() {
        try {
            CustomLayout customLayout = (CustomLayout) super.clone();
            List<ElementView> newElementView = new ArrayList<>();
            for (ElementView view : this.getContent()) {
                newElementView.add(view.clone());
            }
            customLayout.content = newElementView;
            return customLayout;
        } catch (CloneNotSupportedException e) {
            logger.error("clone error ：", e);
            return null;
        }
    }
}
