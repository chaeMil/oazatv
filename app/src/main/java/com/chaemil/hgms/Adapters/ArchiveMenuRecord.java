package com.chaemil.hgms.Adapters;

/**
 * Created by chaemil on 16.10.14.
 */
public class ArchiveMenuRecord {
    private String label;
    private String type;
    private String content;
    private String titleToShow;

    public ArchiveMenuRecord(String type, String content, String label, String titleToShow) {
        this.label = label;
        this.type = type;
        this.content = content;
        this.titleToShow = titleToShow;
    }

    public String getLabel() {
        return label;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getTitleToShow() { return titleToShow; }
}