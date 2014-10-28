package com.chaemil.hgms.Adapters;

/**
 * Created by chaemil on 27.10.14.
 */
public class TagsRecord {
    public String tag;
    public String tagText;

    public TagsRecord(String tag, String tagText) {
        this.tag = tag;
        this.tagText = tagText;
    }

    public String getTag() {
        return tag;
    }

    public String getTagText() {
        return tagText;
    }

}