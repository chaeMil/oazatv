package com.chaemil.hgms.adapters.home_page_adapters;

import com.chaemil.hgms.adapters.ArchiveRecord;

/**
 * Created by chaemil on 21.10.14.
 */
public class NewVideosRecord extends ArchiveRecord {
    public NewVideosRecord(String type, String thumb, String title, String videoDate, String videoURL, String albumId, String videoViews, String thumbBlur) {
        super(type, thumb, title, videoDate, videoURL, albumId, videoViews, thumbBlur);
    }
}
