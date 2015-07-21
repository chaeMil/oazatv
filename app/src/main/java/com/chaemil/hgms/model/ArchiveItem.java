package com.chaemil.hgms.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chaemil on 17.9.14.
 */
public class ArchiveItem implements Parcelable {
    public static final String ARCHIVE_ITEM = "com.chaemil.hgms.model.ArchiveItem";

    public static final String TYPE_VIDEO = "video";
    public static final String TYPE_ALBUM = "photoAlbum";

    public static final String TYPE = "type";
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String VIDEO_VIEWS = "playCount";
    public static final String THUMB = "thumb";
    public static final String THUMB_BLUR = "thumbBlur";
    public static final String ALBUM_ID = "albumId";
    public static final String VIDEO_URL = "videoURL";

    private boolean bigLayout;
    private String thumb;
    private String title;
    private String videoDate;
    private String videoURL;
    private String videoViews;
    private String type;
    private String albumId;
    private String thumbBlur;

    public ArchiveItem() {
    }

    public ArchiveItem(Parcel source) {
        this.thumb = source.readString();
        this.title = source.readString();
        this.videoDate = source.readString();
        this.videoURL = source.readString();
        this.videoViews = source.readString();
        this.type = source.readString();
        this.albumId = source.readString();
        this.thumbBlur = source.readString();
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

    public String getVideoDBID() {
        return videoURL.substring(videoURL.lastIndexOf("/")+1,videoURL.lastIndexOf("."));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(thumb);
        dest.writeString(title);
        dest.writeString(videoDate);
        dest.writeString(videoURL);
        dest.writeString(videoViews);
        dest.writeString(type);
        dest.writeString(albumId);
        dest.writeString(thumbBlur);
    }

    public static final Parcelable.Creator<ArchiveItem> CREATOR
            = new Parcelable.Creator<ArchiveItem>() {
        public ArchiveItem createFromParcel(Parcel in) {
            return new ArchiveItem(in);
        }

        public ArchiveItem[] newArray(int size) {
            return new ArchiveItem[size];
        }
    };
}