package com.particle.model.ui.form.view;

public class OptionView implements Cloneable {

    private String value;

    private String display;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
