package com.particle.util.xml;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class ViewNode implements Serializable {

    private String viewName;

    private HashMap<String, String> attributes;

    private List<ViewNode> children;

    private String value;

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public HashMap<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, String> attributes) {
        this.attributes = attributes;
    }

    public List<ViewNode> getChildren() {
        return children;
    }

    public void setChildren(List<ViewNode> children) {
        this.children = children;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
