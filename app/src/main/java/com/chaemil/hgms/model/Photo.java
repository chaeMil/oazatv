package com.chaemil.hgms.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chaemil on 27.10.14.
 */
public class Photo implements Parcelable {
    public static final String PHOTO = "com.chaemil.hgms.model.Photo";
    private String thumb;
    private String photoLarge;
    private String photoBig;
    private String label;
    private String photoId;

    public Photo() {}

    public Photo(Parcel source) {
        this.thumb = source.readString();
        this.photoLarge = source.readString();
        this.photoBig = source.readString();
        this.label = source.readString();
        this.photoId = source.readString();
    }

    public Photo(String thumb, String photoLarge, String photoBig, String label,
                 String photoId) {
        this.thumb = thumb;
        this.photoLarge = photoLarge;
        this.photoBig = photoBig;
        this.label = label;
        this.photoId = photoId;
    }

    public String getThumb() {
        return thumb;
    }

    public String getPhotoLarge() {
        return photoLarge;
    }

    public String getPhotoBig() {
        return photoBig;
    }

    public String getLabel() {
        return label;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setPhotoLarge(String photoLarge) {
        this.photoLarge = photoLarge;
    }

    public void setPhotoBig(String photoBig) {
        this.photoBig = photoBig;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(thumb);
        dest.writeString(photoLarge);
        dest.writeString(photoBig);
        dest.writeString(label);
        dest.writeString(photoId);
    }

    public static final Parcelable.Creator<Photo> CREATOR
            = new Parcelable.Creator<Photo>() {
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}