package com.chaemil.hgms.utils;

public class Constants {

    /* zapnout DEBUG, aplikace neodesílá data o zhlédnutí na server,
    loguje do konzole a živý přenos je pouze testovací video */
    public static final Boolean DEBUG = true;

    //konstanty
    public static final String MAIN_SERVER = "http://oaza.tv/";
    public static final String MAIN_SERVER_JSON = MAIN_SERVER + "json.php";
    public static final String MAIN_SERVER_VIDEO_LINK_PREFIX = MAIN_SERVER + "?page=vp&v=";
    public static final String MAIN_SERVER_PHOTOALBUM_LINK_PREFIX
            = MAIN_SERVER + "?page=photo-album&album=";

    public static final String VIDEO_LINK = "videoLink";
    public static final String VIDEO_NAME = "videoName";
    public static final String VIDEO_DATE = "videoDate";
    public static final String VIDEO_VIEWS = "videoViews";
    public static final String ALBUM_ID = "albumId";
    public static final String ALBUM_NAME = "albumName";
    public static final String ALBUM_DATE = "albumDate";
    public static final String AUDIO_FILE = "audioFile";
    public static final String PHOTO_ID = "photoId";
    public static final String AUDIO_FILE_THUMB = "audioFileThumb";
    public static final String AUDIO_FILE_NAME = "audioFileName";
    public static final String AUDIO_FILE_DATE = "audioFileDate";

    public static final String JSON_ARRAY_MENU = "menu";
    public static final String JSON_MENU_LABEL = "label";
    public static final String JSON_MENU_TYPE = "type";
    public static final String JSON_MENU_CONTENT = "content";
    public static final String JSON_MENU_TITLE_TO_SHOW = "titleToShow";
    public static final String JSON_MENU_TYPE_HOME_LINK = "homeLink";
    public static final String JSON_MENU_TYPE_ARCHIVE_LINK = "archiveLink";
    public static final String JSON_MENU_TYPE_DOWNLOADED_AUDIO = "downloadedAudio";
    public static final String JSON_MENU_TYPE_EXIT = "exitApp";
    public static final String JSON_MENU_TYPE_LIVE_PLAYER = "liveBroadcast";
    public static final String JSON_MENU_TYPE_REPORT_BUG = "reportBug";
    public static final String JSON_MENU_TYPE_ABOUT_APP = "aboutApp";

    public static final String JSON_ARCHIVE_TITLE = "title";
    public static final String JSON_ARCHIVE_DATE = "date";
    public static final String JSON_ARCHIVE_TYPE = "type";
    public static final String JSON_ARCHIVE_PLAY_COUNT = "playCount";
    public static final String JSON_ARCHIVE_THUMB = "thumb";
    public static final String JSON_ARCHIVE_THUMB_BLUR = "thumbBlur";
    public static final String JSON_ARCHIVE_VIDEO_URL = "videoURL";
    public static final String JSON_ARCHIVE_ALBUM_ID = "albumId";
    public static final String JSON_ARCHIVE_TYPE_VIDEO = "video";
    public static final String JSON_ARCHIVE_TYPE_PHOTOALBUM = "photoAlbum";

    public static final String JSON_ARRAY_PHOTOALUMB = "photoalbum";
    public static final String JSON_PHOTOALBUM_THUMB = "thumb";
    public static final String JSON_PHOTOALBUM_PHOTO_LARGE = "photoLarge";
    public static final String JSON_PHOTOALBUM_PHOTO_BIG = "photoBig";
    public static final String JSON_PHOTOALBUM_LABEL = "label";
    public static final String JSON_PHOTOALBUM_PHOTO_ID = "n";

    public static final String JSON_ARRAY_VIDEO_TAGS = "videoTags";
    public static final String JSON_VIDEO_TAGS_TAG = "tag";
    public static final String JSON_VIDEO_TAGS_TAG_TEXT = "tagText";

    public static final String BUNDLE_TITLE = "title";
    public static final String BUNDLE_LINK = "link";
    public static final String BUNDLE_VIDEO_LINK = "videoLink";
    public static final String BUNDLE_TAG = "tag";


    public static final String EXTENSION_AUDIO = ".audio";
    public static final String EXTENSION_MP3 = ".mp3";
    public static final String EXTENSION_MP4 = ".mp4";
    public static final String EXTENSION_JPG = ".jpg";
    public static final String EXTENSION_WEBM = ".webm";
    public static final String EXTENSION_THUMB = ".thumb";

    public static final String YOUTUBE_VIDEO_ID = "videoId";

    public static final String AUDIOPLAYER_STATUSBAR_COLOR = "#FF6D3679";
    public static final String AUDIOPLAYER_ACTIONBAR_COLOR = "#FF894C96";
    public static final String MAIN_ACTIVITY_STATUSBAR_COLOR = "#FF4285F4";

    public static final String FONT_REGULAR_UPRIGHT = "Titillium-RegularUpright.otf";
    public static final String FONT_BOLD_UPRIGHT = "Titillium-BoldUpright.otf";
    public static final String PHOTO_URL = "photoUrl";

    public static final String MAIN_PREFS = "mainPrefs";
    public static final String FIRST_RUN = "firstRun";

    public static final String REPORT_BUG_URL = MAIN_SERVER + "?page=bug-report&source=app";
    public static final String ABOUT_APP_URL = MAIN_SERVER + "?page=about-app&source=app";


}
