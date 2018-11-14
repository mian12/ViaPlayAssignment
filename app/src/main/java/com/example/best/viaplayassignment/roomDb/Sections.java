package com.example.best.viaplayassignment.roomDb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "section")
public class Sections {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ColumnInfo(name = "sec_id")
    private String SectionId;

    @ColumnInfo(name = "sec_name")
    private String SectionName;


    @ColumnInfo(name = "sec_href")
    private String SectionHref;

    @ColumnInfo(name ="sec_offline")
    private String SectionHrefOffline;

    public String getSectionHrefOffline() {
        return SectionHrefOffline;
    }

    public void setSectionHrefOffline(String sectionHrefOffline) {
        SectionHrefOffline = sectionHrefOffline;
    }

    public String getSectionTitle() {
        return SectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        SectionTitle = sectionTitle;
    }

    @ColumnInfo(name = "sec_title")
    private String SectionTitle;


    public String getSectionId() {
        return SectionId;
    }

    public void setSectionId(String sectionId) {
        SectionId = sectionId;
    }

    public String getSectionName() {
        return SectionName;
    }

    public void setSectionName(String sectionName) {
        SectionName = sectionName;
    }

    public String getSectionHref() {
        return SectionHref;
    }

    public void setSectionHref(String sectionHref) {
        SectionHref = sectionHref;
    }
}
