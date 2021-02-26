package com.particle.model.ui.form.json.element;

public class Slider extends Element {

    private final String type = "slider";

    private String text = "";

    private float min = 0f;

    private float max = 100f;

    private int step;

    private float _default;

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public float get_default() {
        return _default;
    }

    public void set_default(float _default) {
        this._default = _default;
    }
}
