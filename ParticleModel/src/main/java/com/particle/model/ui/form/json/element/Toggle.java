package com.particle.model.ui.form.json.element;

public class Toggle extends Element {

    private final String type = "toggle";

    private String text = "";

    private boolean _default = false;

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean is_default() {
        return _default;
    }

    public void set_default(boolean _default) {
        this._default = _default;
    }
}
