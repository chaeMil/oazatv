package com.chaemil.hgms.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaemil.hgms.adapters.ArchiveAdapter;
import com.chaemil.hgms.adapters.ArchiveMenuAdapter;
import com.chaemil.hgms.adapters.ArchiveMenuRecord;
import com.chaemil.hgms.adapters.ArchiveRecord;
import com.chaemil.hgms.adapters.PhotoalbumAdapter;
import com.chaemil.hgms.adapters.PhotoalbumRecord;
import com.chaemil.hgms.R;
import com.wefika.flowlayout.FlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by chaemil on 17.10.14.
 */
public class Utils extends Activity {
    public static String lang = Locale.getDefault().getLanguage();

    public static void fetchMenuData(final Context c, final ArchiveMenuAdapter adapter) {
        JsonObjectRequest request = new JsonObjectRequest(
                Basic.MAIN_SERVER_JSON+"?page=menu&lang="+Utils.lang,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            List<ArchiveMenuRecord> archiveMenuRecords = parseMenu(jsonObject);

                            adapter.swapImageRecords(archiveMenuRecords);

                        }
                        catch(JSONException e) {
                            Toast.makeText(c.getApplicationContext(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Toast.makeText(getApplicationContext(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(c.getApplicationContext(), c.getResources().getString(R.string.connection_problem), Toast.LENGTH_LONG).show();
                    }
                });

        VolleyApplication.getInstance().getRequestQueue().add(request);
    }

    public static void fetchArchive(final Context c, String url, final ArchiveAdapter adapter, final String jsonArray) {
        JsonObjectRequest request = new JsonObjectRequest(
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            List<ArchiveRecord> archiveRecords = parseArchive(jsonObject, jsonArray);

                            adapter.swapImageRecords(archiveRecords);

                        }
                        catch(JSONException e) {
                            Toast.makeText(c.getApplicationContext(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Toast.makeText(getApplicationContext(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(c.getApplicationContext(), c.getResources().getString(R.string.connection_problem), Toast.LENGTH_LONG).show();
                    }
                });

        VolleyApplication.getInstance().getRequestQueue().add(request);
    }

    public static void fetchPhotoalbum(final Context c, final PhotoalbumAdapter adapter,String albumId) {
        JsonObjectRequest request = new JsonObjectRequest(
                Basic.MAIN_SERVER_JSON+"?page=photoalbum&albumId="+albumId+"&lang="+Utils.lang,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            List<PhotoalbumRecord> photoalbumRecords = parsePhotoalbum(jsonObject);

                            adapter.swapImageRecords(photoalbumRecords);

                        }
                        catch(JSONException e) {
                            Toast.makeText(c.getApplicationContext(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Toast.makeText(getApplicationContext(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(c.getApplicationContext(), c.getResources().getString(R.string.connection_problem), Toast.LENGTH_LONG).show();
                    }
                });

        VolleyApplication.getInstance().getRequestQueue().add(request);
    }

    static private List<ArchiveMenuRecord> parseMenu(JSONObject json) throws JSONException {
        ArrayList<ArchiveMenuRecord> records = new ArrayList<ArchiveMenuRecord>();

        JSONArray jsonImages = json.getJSONArray(Basic.JSON_ARRAY_MENU);

        for(int i =0; i < jsonImages.length(); i++) {
            JSONObject jsonImage = jsonImages.getJSONObject(i);
            String label = jsonImage.getString(Basic.JSON_MENU_LABEL);
            String type = jsonImage.getString(Basic.JSON_MENU_TYPE);
            String content = jsonImage.getString(Basic.JSON_MENU_CONTENT);
            String titleToShow = jsonImage.getString(Basic.JSON_MENU_TITLE_TO_SHOW);

            ArchiveMenuRecord record = new ArchiveMenuRecord(type, content, label, titleToShow);
            records.add(record);
        }

        return records;
    }

    static private List<ArchiveRecord> parseArchive(JSONObject json, String jsonArray) throws JSONException {
        ArrayList<ArchiveRecord> records = new ArrayList<ArchiveRecord>();

        JSONArray jsonImages = json.getJSONArray(jsonArray);

        for(int i=0; i< jsonImages.length(); i++) {
            JSONObject jsonImage = jsonImages.getJSONObject(i);

            String type = jsonImage.getString(Basic.JSON_ARCHIVE_TYPE);
            String title = jsonImage.getString(Basic.JSON_ARCHIVE_TITLE);
            String date = jsonImage.getString(Basic.JSON_ARCHIVE_DATE);
            String playCount = jsonImage.getString(Basic.JSON_ARCHIVE_PLAY_COUNT);
            String thumb = jsonImage.getString(Basic.JSON_ARCHIVE_THUMB);
            String thumbBlur = jsonImage.getString(Basic.JSON_ARCHIVE_THUMB_BLUR);
            String videoURL = jsonImage.getString(Basic.JSON_ARCHIVE_VIDEO_URL);
            String albumId = jsonImage.getString(Basic.JSON_ARCHIVE_ALBUM_ID);

            ArchiveRecord record = new ArchiveRecord(type, title, date, playCount, thumb, thumbBlur, videoURL, albumId);
            records.add(record);

        }
        return records;
    }

    static private List<PhotoalbumRecord> parsePhotoalbum(JSONObject json) throws JSONException {
        ArrayList<PhotoalbumRecord> records = new ArrayList<PhotoalbumRecord>();

        JSONArray jsonImages = json.getJSONArray(Basic.JSON_ARRAY_PHOTOALUMB);

        for(int i =0; i < jsonImages.length(); i++) {
            JSONObject jsonImage = jsonImages.getJSONObject(i);
            String thumb = jsonImage.getString(Basic.JSON_PHOTOALBUM_THUMB);
            String photoLarge = jsonImage.getString(Basic.JSON_PHOTOALBUM_PHOTO_LARGE);
            String photoBig = jsonImage.getString(Basic.JSON_PHOTOALBUM_PHOTO_BIG);
            String label = jsonImage.getString(Basic.JSON_PHOTOALBUM_LABEL);
            String photoId = jsonImage.getString(Basic.JSON_PHOTOALBUM_PHOTO_ID);

            PhotoalbumRecord record = new PhotoalbumRecord(thumb, photoLarge, photoBig, label, photoId);
            records.add(record);
        }

        return records;
    }

    public static void displayVideoTags(final Context c, String videoId, final FlowLayout layout) {
        JsonObjectRequest request = new JsonObjectRequest(
                Basic.MAIN_SERVER_JSON+"?page=videoTags&video="+videoId+"&lang="+ Utils.lang,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONArray jsonImages = jsonObject.getJSONArray(Basic.JSON_ARRAY_VIDEO_TAGS);

                            for(int i =0; i < jsonImages.length(); i++) {
                                JSONObject jsonImage = jsonImages.getJSONObject(i);
                                String tag = jsonImage.getString(Basic.JSON_VIDEO_TAGS_TAG);
                                String tagText = jsonImage.getString(Basic.JSON_VIDEO_TAGS_TAG_TEXT);

                                LayoutInflater inflater = LayoutInflater.from(c);
                                View view  = inflater.inflate(R.layout.tag, layout, false);

                                TextView tagElement = (TextView) view.findViewById(R.id.tag);
                                tagElement.setText(tag);

                                TextView tagTextElement = (TextView) view.findViewById(R.id.tagText);
                                tagTextElement.setText(tagText);

                                layout.addView(view);
                            }
                        }
                        catch(JSONException e) {
                            Toast.makeText(c.getApplicationContext(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Toast.makeText(getApplicationContext(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(c.getApplicationContext(), c.getResources().getString(R.string.connection_problem), Toast.LENGTH_LONG).show();
                    }
                });

        VolleyApplication.getInstance().getRequestQueue().add(request);
    }

    public static int getScreenWidth(Context c) {
        WindowManager wm = (WindowManager) c
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        return point.x;
    }

    public static int getScreenHeight(Context c) {
        WindowManager wm = (WindowManager) c
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        return point.y;
    }

    // This snippet hides the system bars.
    public static void hideSystemUI(Activity a, View v) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        if (a.getActionBar() != null) {
            a.getActionBar().hide();
        }
        a.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        a.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        a.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    public static void showSystemUI(Activity a, View v) {
        if (a.getActionBar() != null) {
            a.getActionBar().show();
        }
        a.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /*v.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);*/
    }


    public static int getActionBarHeight(Activity a) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (a.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,a.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public static int getStatusBarHeight(Activity a) {
        int result = 0;
        int resourceId = a.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = a.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int getNavigationBarHeight(Context context, int orientation) {
        Resources resources = context.getResources();

        int id = resources.getIdentifier(
                orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape",
                "dimen", "android");
        if (id > 0) {
            return resources.getDimensionPixelSize(id);
        }
        return 0;
    }

    /*private static void setStatusBarColor(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(activity.getResources().getColor(R.color.example_color));
    }*/

    public static void setStatusBarColor(Activity a, View statusBar, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = a.getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //status bar height
            int actionBarHeight = getActionBarHeight(a);
            int statusBarHeight = getStatusBarHeight(a);
            Log.i("statusBarHeight", Integer.toString(statusBarHeight));
            Log.i("actionBarHeight", Integer.toString(actionBarHeight));
            //action bar height
            //statusBar.getLayoutParams().height = actionBarHeight + statusBarHeight;
            statusBar.setBackgroundColor(color);
        }
    }


    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean isDownloadManagerAvailable(Context context) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClassName("com.android.providers.downloads.ui", "com.android.providers.downloads.ui.DownloadList");
            List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

}
