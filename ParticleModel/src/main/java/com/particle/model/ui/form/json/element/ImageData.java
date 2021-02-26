package com.particle.model.ui.form.json.element;

public class ImageData {

    public static final String IMAGE_DATA_TYPE_PATH = "path";

    public static final String IMAGE_DATA_TYPE_URL = "url";

    private String type;
    private String data;

    public ImageData() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
