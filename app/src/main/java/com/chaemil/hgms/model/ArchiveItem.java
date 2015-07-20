package com.chaemil.hgms.model;

/**
 * Created by chaemil on 17.9.14.
 */
public class ArchiveItem {
    public String thumb;
    public String title;
    public String videoDate;
    public String videoURL;
    public String videoViews;
    public String type;
    public String albumId;
    public String thumbBlur;

    public ArchiveItem(String type, String title, String videoDate, String videoViews, String thumb, String thumbBlur, String videoURL, String albumId) {
        this.thumb = thumb;
        this.title = title;
        this.videoDate = videoDate;
        this.videoURL = videoURL;
        this.videoViews = videoViews;
        this.type = type;
        this.albumId = albumId;
        this.thumbBlur = thumbBlur;
    }

    public String getTitle() {
        return title;
    }

    public String getThumb() {
        return thumb;
    }

    public String getVideoDate() {
        return videoDate;
    }

    public String getVideoUrl() {return videoURL; }

    public String getVideoViews() {return videoViews; }

    public String getType() {return type; }

    public String getAlbumId() {return albumId; }

    public String getThumbBlur() {return thumbBlur;}



}