package com.particle.model.ui.form.json.window;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class ModalWindow extends BaseWindow {

    private final String type = "modal";

    private String title = "";

    private String content = "";

    private String button1 = "";

    private String button2 = "";

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

    public String getButton1() {
        return button1;
    }

    public void setButton1(String button1) {
        this.button1 = button1;
    }

    public String getButton2() {
        return button2;
    }

    public void setButton2(String button2) {
        this.button2 = button2;
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
