package com.chaemil.hgms.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chaemil.hgms.Adapters.ArchiveAdapter;
import com.chaemil.hgms.Adapters.ArchiveMenuAdapter;
import com.chaemil.hgms.Adapters.ArchiveMenuRecord;
import com.chaemil.hgms.Adapters.ArchiveRecord;
//import com.chaemil.hgms.Adapters.PhotoalbumAdapter_;
import com.chaemil.hgms.Adapters.PhotoalbumAdapter;
import com.chaemil.hgms.Adapters.PhotoalbumRecord;
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
                c.getResources().getString(R.string.mainServerJson)+"?page=menu&lang="+Utils.lang,
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
                c.getResources().getString(R.string.mainServerJson)+"?page=photoalbum&albumId="+albumId+"&lang="+Utils.lang,
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

        JSONArray jsonImages = json.getJSONArray("menu");

        for(int i =0; i < jsonImages.length(); i++) {
            JSONObject jsonImage = jsonImages.getJSONObject(i);
            String label = jsonImage.getString("label");
            String type = jsonImage.getString("type");
            String content = jsonImage.getString("content");
            String titleToShow = jsonImage.getString("titleToShow");

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

            String type = jsonImage.getString("type");
            String title = jsonImage.getString("title");
            String date = jsonImage.getString("date");
            String playCount = jsonImage.getString("playCount");
            String thumb = jsonImage.getString("thumb");
            String thumbBlur = jsonImage.getString("thumbBlur");
            String videoURL = jsonImage.getString("videoURL");
            String albumId = jsonImage.getString("albumId");

            ArchiveRecord record = new ArchiveRecord(type, title, date, playCount, thumb, thumbBlur, videoURL, albumId);
            records.add(record);

        }
        return records;
    }

    static private List<PhotoalbumRecord> parsePhotoalbum(JSONObject json) throws JSONException {
        ArrayList<PhotoalbumRecord> records = new ArrayList<PhotoalbumRecord>();

        JSONArray jsonImages = json.getJSONArray("photoalbum");

        for(int i =0; i < jsonImages.length(); i++) {
            JSONObject jsonImage = jsonImages.getJSONObject(i);
            String thumb = jsonImage.getString("thumb");
            String photoLarge = jsonImage.getString("photoLarge");
            String photoBig = jsonImage.getString("photoBig");
            String label = jsonImage.getString("label");
            String photoId = jsonImage.getString("n");

            PhotoalbumRecord record = new PhotoalbumRecord(thumb, photoLarge, photoBig, label, photoId);
            records.add(record);
        }

        return records;
    }

    public static void displayVideoTags(final Context c, String videoId, final FlowLayout layout) {
        JsonObjectRequest request = new JsonObjectRequest(
                c.getResources().getString(R.string.mainServerJson)+"?page=videoTags&video="+videoId+"&lang="+ Utils.lang,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONArray jsonImages = jsonObject.getJSONArray("videoTags");

                            for(int i =0; i < jsonImages.length(); i++) {
                                JSONObject jsonImage = jsonImages.getJSONObject(i);
                                String tag = jsonImage.getString("tag");
                                String tagText = jsonImage.getString("tagText");

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

    public static void setStatusBarColor(Activity a, View statusBar, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = a.getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //status bar height
            int actionBarHeight = getActionBarHeight(a);
            int statusBarHeight = getStatusBarHeight(a);
            //action bar height
            statusBar.getLayoutParams().height = actionBarHeight + statusBarHeight;
            statusBar.setBackgroundColor(color);
        }
    }
}
