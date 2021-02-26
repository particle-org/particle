package com.particle.model.ui.form.view;

import com.particle.model.ui.form.json.element.Element;

public abstract class ElementView implements Cloneable {

    /**
     * 类型
     */
    private String type;

    /**
     * 必须表示唯一
     */
    private String id;

    /**
     * 文本
     */
    private String text;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * 转成Element
     *
     * @return
     */
    public abstract Element toElement();

    /**
     * check方法
     *
     * @return
     */
    public abstract boolean check();

    @Override
    public ElementView clone() throws CloneNotSupportedException {
        ElementView elementView = (ElementView) super.clone();
        return elementView;
    }
}
