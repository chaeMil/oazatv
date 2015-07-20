package com.chaemil.hgms.factory;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.chaemil.hgms.model.ArchiveItem;
import com.chaemil.hgms.model.HomePage;
import com.chaemil.hgms.model.RequestType;

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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return archiveItem;
    }

    private static Response.Listener<JSONObject> createMyReqSuccessListener(
            final RequestFactoryListener listener, final RequestType requestType) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.onSuccessResponse(response, requestType);
            }
        };
    }

    private static Response.ErrorListener createMyReqErrorListener(final RequestFactoryListener listener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onErrorResponse(error);
            }
        };
    }
}