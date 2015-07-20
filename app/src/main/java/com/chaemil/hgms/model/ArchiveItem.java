package com.chaemil.hgms.model;

/**
 * Created by chaemil on 17.9.14.
 */
public class ArchiveItem {
    public static final String TYPE = "type";
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String VIDEO_VIEWS = "playCount";
    public static final String THUMB = "thumb";
    public static final String THUMB_BLUR = "thumbBlur";
    public static final String ALBUM_ID = "albumId";

    public boolean bigLayout;
    public String thumb;
    public String title;
    public String videoDate;
    public String videoURL;
    public String videoViews;
    public String type;
    public String albumId;
    public String thumbBlur;

    public ArchiveItem() {
    }

    public ArchiveItem(boolean bigLayout, String thumb, String title, String videoDate, String videoURL, String videoViews, String type, String albumId, String thumbBlur) {
        this.bigLayout = bigLayout;
        this.thumb = thumb;
        this.title = title;
        this.videoDate = videoDate;
        this.videoURL = videoURL;
        this.videoViews = videoViews;
        this.type = type;
        this.albumId = albumId;
        this.thumbBlur = thumbBlur;
    }

    public boolean isBigLayout() {
        return bigLayout;
    }

    public void setBigLayout(boolean bigLayout) {
        this.bigLayout = bigLayout;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoDate() {
        return videoDate;
    }

    public void setVideoDate(String videoDate) {
        this.videoDate = videoDate;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getVideoViews() {
        return videoViews;
    }

    public void setVideoViews(String videoViews) {
        this.videoViews = videoViews;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getThumbBlur() {
        return thumbBlur;
    }

    public void setThumbBlur(String thumbBlur) {
        this.thumbBlur = thumbBlur;
    }
}