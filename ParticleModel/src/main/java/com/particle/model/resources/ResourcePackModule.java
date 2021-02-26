package com.particle.model.resources;

import java.util.List;

public class ResourcePackModule {

    private String description;

    private String name;

    private String uuid;

    private List<Integer> version;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<Integer> getVersion() {
        return version;
    }

    public void setVersion(List<Integer> version) {
        this.version = version;
    }
}
