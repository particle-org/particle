package com.particle.model.ui.form.json.element;

public class Button extends Element {

    private String text;

    private ImageData image;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ImageData getImage() {
        return image;
    }

    public void setImage(ImageData image) {
        this.image = image;
    }
}
