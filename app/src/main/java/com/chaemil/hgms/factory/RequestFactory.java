package com.chaemil.hgms.factory;

import android.app.DownloadManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chaemil.hgms.model.RequestType;
import com.chaemil.hgms.utils.Constants;
import com.chaemil.hgms.utils.SmartLog;
import com.chaemil.hgms.utils.Utils;

import org.json.JSONObject;

public class RequestFactory {
    public static final int DEFAULT_TIMEOUT_MS = 10000;

    public static Request getMenu(RequestFactoryListener listener) {
        String url = Constants.MAIN_SERVER_JSON_MENU;

        SmartLog.log("getMenu()", "url: " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                createMyReqSuccessListener(listener, RequestType.MENU),
                createMyReqErrorListener(listener));

        return request;
    }

    public static Request getHomePage(RequestFactoryListener listener) {
        String url = Constants.MAIN_SERVER_JSON_HOMEPAGE;

        SmartLog.log("getHomePage()", "url: " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                createMyReqSuccessListener(listener, RequestType.HOME),
                createMyReqErrorListener(listener));

        return request;
    }

    public static Request getVideoTags(RequestFactoryListener listener, String videoId) {
        String url = Constants.MAIN_SERVER_JSON_VIDEO_TAGS + videoId;

        SmartLog.log("getVideoTags()", "url: " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                createMyReqSuccessListener(listener, RequestType.VIDEO_TAGS),
                createMyReqErrorListener(listener));

        return request;
    }

    public static Request getPhotos(RequestFactoryListener listener, String photoalbumId) {
        String url = Constants.MAIN_SERVER_JSON_PHOTOALBUM_PHOTOS + photoalbumId +
                Constants.JSON_LANG + Utils.lang;

        SmartLog.log("getPhotos()", "url: " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                createMyReqSuccessListener(listener, RequestType.PHOTOALBUM),
                createMyReqErrorListener(listener));

        return request;
    }

    public static Request getArchive(RequestFactoryListener listener, String query) {
        String url = Constants.MAIN_SERVER_JSON_ARCHIVE + query;

        SmartLog.log("getArchive()", "url: " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                createMyReqSuccessListener(listener, RequestType.ARCHIVE),
                createMyReqErrorListener(listener));

        return request;
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
