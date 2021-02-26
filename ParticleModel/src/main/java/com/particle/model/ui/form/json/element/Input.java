package com.particle.model.ui.form.json.element;

public class Input extends Element {

    private final String type = "input";

    private String text = "";

    private String placeholder = "";

    private String _default = "";

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String get_default() {
        return _default;
    }

    public void set_default(String _default) {
        this._default = _default;
    }
}
