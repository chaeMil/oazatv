package com.chaemil.hgms.model;

/**
 * Created by chaemil on 16.10.14.
 */
public class ArchiveMenu {
    private String label;
    private String type;
    private String content;
    private String titleToShow;

    public ArchiveMenu(String type, String content, String label, String titleToShow) {
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