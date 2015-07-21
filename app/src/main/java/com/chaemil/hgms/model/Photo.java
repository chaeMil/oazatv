package com.chaemil.hgms.model;

/**
 * Created by chaemil on 27.10.14.
 */
public class Photo {
    private String thumb;
    private String photoLarge;
    private String photoBig;
    private String label;
    private String photoId;

    public Photo() {}

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
}