package com.chaemil.hgms.Adapters;

/**
 * Created by chaemil on 27.10.14.
 */
public class PhotoalbumRecord {
    public String thumb;
    public String photoLarge;
    public String photoBig;
    public String label;

    public PhotoalbumRecord(String thumb, String photoLarge, String photoBig, String label) {
        this.thumb = thumb;
        this.photoLarge = photoLarge;
        this.photoBig = photoBig;
        this.label = label;
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

}