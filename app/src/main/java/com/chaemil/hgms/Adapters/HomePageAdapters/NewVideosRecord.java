package com.chaemil.hgms.Adapters.HomePageAdapters;

import com.chaemil.hgms.Adapters.ArchiveRecord;

/**
 * Created by chaemil on 21.10.14.
 */
public class NewVideosRecord extends ArchiveRecord {
    public NewVideosRecord(String type, String thumb, String title, String videoDate, String videoURL, String albumId, String videoViews, String thumbBlur) {
        super(type, thumb, title, videoDate, videoURL, albumId, videoViews, thumbBlur);
    }
}
