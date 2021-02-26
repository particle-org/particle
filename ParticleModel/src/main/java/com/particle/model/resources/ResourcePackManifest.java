package com.particle.model.resources;

import java.util.List;

public class ResourcePackManifest {

    private int format_version;

    private ResourcePackModule header;

    private List<ResourcePackModule> modules;

    public int getFormat_version() {
        return format_version;
    }

    public void setFormat_version(int format_version) {
        this.format_version = format_version;
    }

    public ResourcePackModule getHeader() {
        return header;
    }

    public void setHeader(ResourcePackModule header) {
        this.header = header;
    }

    public List<ResourcePackModule> getModules() {
        return modules;
    }

    public void setModules(List<ResourcePackModule> modules) {
        this.modules = modules;
    }
}
