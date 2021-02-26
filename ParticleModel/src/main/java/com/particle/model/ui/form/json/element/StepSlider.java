package com.particle.model.ui.form.json.element;

import java.util.List;

public class StepSlider extends Element {

    private final String type = "step_slider";

    private String text = "";

    private List<String> steps;

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

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public int get_default() {
        return _default;
    }

    public void set_default(int _default) {
        this._default = _default;
    }
}
