package com.particle.model.ui.form.json.window;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.particle.model.ui.form.json.element.Button;

import java.util.List;

public class SimpleWindow extends BaseWindow {

    private final String type = "form";

    private String title = "";

    private String content = "";

    private List<Button> buttons;

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    /**
     * 下发表单数据之用
     *
     * @return
     */
    @Override
    public String toString() {
        String json = JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
        json = json.replaceAll("\\\\n", "\\n");
        return json;
    }
}
