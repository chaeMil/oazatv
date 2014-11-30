package com.chaemil.hgms.adapters.home_page_adapters;

import com.chaemil.hgms.adapters.ArchiveRecord;

/**
 * Created by chaemil on 21.10.14.
 */
public class PhotoalbumsRecord extends ArchiveRecord {
    public PhotoalbumsRecord(String type, String title, String videoDate, String videoViews, String thumb, String thumbBlur, String videoURL, String albumId) {
        super(type, title, videoDate, videoViews, thumb, thumbBlur, videoURL, albumId);
    }
}
