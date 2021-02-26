package com.particle.model.ui.form.json.element;

import java.util.List;

public class DropDown extends Element {

    private final String type = "dropdown";

    private String text;

    private List<String> options;

    private int _default = 0;

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int get_default() {
        return _default;
    }

    public void set_default(int _default) {
        this._default = _default;
    }
}
