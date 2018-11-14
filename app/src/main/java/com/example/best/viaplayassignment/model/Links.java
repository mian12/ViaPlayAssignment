package com.example.best.viaplayassignment.model;

import java.util.List;

public class Links {

    private List<ViaplaySection> sections;

    public Links() {
    }

    public Links(List<ViaplaySection> sections) {
        this.sections = sections;
    }

    public List<ViaplaySection> getSections() {
        return sections;
    }

    public void setSections(List<ViaplaySection> sections) {
        this.sections = sections;
    }
}
