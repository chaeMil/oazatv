package com.chaemil.hgms.model;

/**
 * Created by chaemil on 27.10.14.
 */
public class Photo {
    public String thumb;
    public String photoLarge;
    public String photoBig;
    public String label;
    public String photoId;

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

}