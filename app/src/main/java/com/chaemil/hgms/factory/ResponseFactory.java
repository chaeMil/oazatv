package com.chaemil.hgms.factory;

import com.chaemil.hgms.model.ArchiveItem;
import com.chaemil.hgms.model.ArchiveMenu;
import com.chaemil.hgms.model.HomePage;
import com.chaemil.hgms.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResponseFactory {

    public static ArrayList<ArchiveItem> parseHomePage(JSONObject response) {
        ArrayList<ArchiveItem> homepage = new ArrayList<>();
        try {
            ArchiveItem mainVideo = parseArchiveItem(response.getJSONArray(HomePage.MAIN_VIDEO)
                    .getJSONObject(0));
            mainVideo.setBigLayout(true);

            homepage.add(mainVideo);

            JSONArray newVideosJson = response.getJSONArray(HomePage.NEW_VIDEOS);
            for(int i = 0; i < newVideosJson.length(); i++) {
                homepage.add(parseArchiveItem(newVideosJson.getJSONObject(i)));
            }

            JSONArray newAlbumsJson = response.getJSONArray(HomePage.PHOTO_ALBUMS);
            for(int i = 0; i < newAlbumsJson.length(); i++) {
                homepage.add(parseArchiveItem(newAlbumsJson.getJSONObject(i)));
            }

            JSONArray popularVideosJson = response.getJSONArray(HomePage.POPULAR_VIDEOS);
            for(int i = 0; i < popularVideosJson.length(); i++) {
                homepage.add(parseArchiveItem(popularVideosJson.getJSONObject(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return homepage;
    }

    public static ArchiveItem parseArchiveItem(JSONObject jsonObject) {
        ArchiveItem archiveItem = new ArchiveItem();

        try {
            archiveItem.setType(jsonObject.getString(ArchiveItem.TYPE));
            archiveItem.setTitle(jsonObject.getString(ArchiveItem.TITLE));
            archiveItem.setVideoDate(jsonObject.getString(ArchiveItem.DATE));
            archiveItem.setVideoViews(jsonObject.getString(ArchiveItem.VIDEO_VIEWS));
            archiveItem.setThumb(jsonObject.getString(ArchiveItem.THUMB));
            archiveItem.setThumbBlur(jsonObject.getString(ArchiveItem.THUMB_BLUR));
            archiveItem.setAlbumId(jsonObject.getString(ArchiveItem.ALBUM_ID));
            archiveItem.setVideoURL(jsonObject.getString(ArchiveItem.VIDEO_URL));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return archiveItem;
    }

    public static ArrayList<ArchiveMenu> parseMenu(JSONObject json) {
        ArrayList<ArchiveMenu> menu = new ArrayList<>();

        try {
            JSONArray jsonMenu = json.getJSONArray(Constants.JSON_ARRAY_MENU);

            for (int i = 0; i < jsonMenu.length(); i++) {
                JSONObject jsonItem = jsonMenu.getJSONObject(i);
                String label = jsonItem.getString(Constants.JSON_MENU_LABEL);
                String type = jsonItem.getString(Constants.JSON_MENU_TYPE);
                String content = jsonItem.getString(Constants.JSON_MENU_CONTENT);
                String titleToShow = jsonItem.getString(Constants.JSON_MENU_TITLE_TO_SHOW);

                ArchiveMenu item = new ArchiveMenu(type, content, label, titleToShow);
                menu.add(item);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return menu;
    }

    public static ArrayList<String> parseVideoTags(JSONObject jsonObject) {
        ArrayList<String> tags = new ArrayList<>();

        try {
            JSONArray jsonTagsArray = jsonObject.getJSONArray(Constants.JSON_ARRAY_VIDEO_TAGS);

            for (int i = 0; i < jsonTagsArray.length(); i++) {
                JSONObject jsonTag = jsonTagsArray.getJSONObject(i);
                String tag = jsonTag.getString(Constants.JSON_VIDEO_TAGS_TAG);
                tags.add(tag);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tags;
    }
}