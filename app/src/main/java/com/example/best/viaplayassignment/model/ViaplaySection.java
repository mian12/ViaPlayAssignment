package com.example.best.viaplayassignment.model;

public class ViaplaySection {

    private String id;
    private String title;
    private String href;
    private String type;
    private String name;
    private boolean templated;

    public ViaplaySection() {
    }

    public ViaplaySection(String id, String title, String href, String type, String name, boolean templated) {
        this.id = id;
        this.title = title;
        this.href = href;
        this.type = type;
        this.name = name;
        this.templated = templated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTemplated() {
        return templated;
    }

    public void setTemplated(boolean templated) {
        this.templated = templated;
    }
}
