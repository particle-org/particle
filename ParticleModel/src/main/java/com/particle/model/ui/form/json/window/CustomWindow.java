package com.particle.model.ui.form.json.window;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.particle.model.ui.form.json.element.Element;
import com.particle.model.ui.form.json.element.ImageData;

import java.util.List;

public class CustomWindow extends BaseWindow {

    private final String type = "custom_form";

    private String title = "";

    private ImageData icon;

    private List<Element> content;

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ImageData getIcon() {
        return icon;
    }

    public void setIcon(ImageData icon) {
        this.icon = icon;
    }

    public List<Element> getContent() {
        return content;
    }

    public void setContent(List<Element> content) {
        this.content = content;
    }

    /**
     * 下发表单数据之用
     *
     * @return
     */
    @Override
    public String toString() {
        String json = JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
        json = json.replaceAll("_default", "default");
        json = json.replaceAll("\\\\n", "\\n");
        return json;
    }
}
