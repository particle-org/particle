package com.particle.model.ui.form.json.element;

public class Label extends Element {

    private final String type = "label";

    private String text = "";

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
