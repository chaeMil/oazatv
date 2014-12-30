package com.chaemil.hgms.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.chaemil.hgms.MainActivity;
import com.chaemil.hgms.R;
import com.chaemil.hgms.adapters.ArchiveAdapter;
import com.chaemil.hgms.adapters.ArchiveMenuAdapter;
import com.chaemil.hgms.adapters.ArchiveMenuRecord;
import com.chaemil.hgms.adapters.ArchiveRecord;
import com.chaemil.hgms.adapters.PhotoalbumAdapter;
import com.chaemil.hgms.adapters.PhotoalbumRecord;
import com.koushikdutta.ion.Ion;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wefika.flowlayout.FlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


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
                            Toast.makeText(c.getApplicationContext(),
                                    "Unable to parse data: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(c.getApplicationContext(),
                                c.getResources().getString(R.string.connection_problem),
                                Toast.LENGTH_LONG).show();
                    }
                });

        VolleyApplication.getInstance().getRequestQueue().add(request);
    }

    public static void fetchArchive(final Context c, String url,
                                    final ArchiveAdapter adapter, final String jsonArray) {
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
                            Toast.makeText(c.getApplicationContext(),
                                    "Unable to parse data: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(c.getApplicationContext(),
                                c.getResources().getString(R.string.connection_problem),
                                Toast.LENGTH_LONG).show();
                    }
                });

        VolleyApplication.getInstance().getRequestQueue().add(request);
    }

    public static void fetchPhotoalbum(final Context c, final PhotoalbumAdapter adapter,
                                       String albumId) {
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
                            Toast.makeText(c.getApplicationContext(), "Unable to parse data: "
                                    + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(c.getApplicationContext(),
                                c.getResources().getString(R.string.connection_problem),
                                Toast.LENGTH_LONG).show();
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

    static private List<ArchiveRecord> parseArchive(JSONObject json, String jsonArray)
            throws JSONException {
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

            ArchiveRecord record = new ArchiveRecord(type, title, date, playCount,
                    thumb, thumbBlur, videoURL, albumId);
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

            PhotoalbumRecord record = new PhotoalbumRecord(thumb, photoLarge, photoBig,
                    label, photoId);
            records.add(record);
        }

        return records;
    }

    public static void displayVideoTags(final Context c, final Activity a,
                                        String videoId, final FlowLayout layout) {
        JsonObjectRequest request = new JsonObjectRequest(
                Basic.MAIN_SERVER_JSON+"?page=videoTags&video="+videoId+"&lang="+ Utils.lang,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONArray jsonImages = jsonObject
                                    .getJSONArray(Basic.JSON_ARRAY_VIDEO_TAGS);

                            for(int i =0; i < jsonImages.length(); i++) {
                                JSONObject jsonImage = jsonImages.getJSONObject(i);
                                String tag = jsonImage.getString(Basic.JSON_VIDEO_TAGS_TAG);
                                final String tagText = jsonImage
                                        .getString(Basic.JSON_VIDEO_TAGS_TAG_TEXT);

                                LayoutInflater inflater = LayoutInflater.from(c);
                                View view  = inflater.inflate(R.layout.tag, layout, false);

                                TextView tagElement = (TextView) view.findViewById(R.id.tag);
                                tagElement.setText(tag);

                                TextView tagTextElement = (TextView) view.findViewById(R.id.tagText);
                                tagTextElement.setText(Utils
                                        .getStringWithRegularCustomFont(c,
                                                tagText,Basic.FONT_BOLD_UPRIGHT));

                                tagTextElement.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(c.getApplicationContext(),
                                                MainActivity.class);
                                        i.putExtra(Basic.BUNDLE_TAG,tagText);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        c.getApplicationContext().startActivity(i);
                                        //a.finish();
                                        Utils.goBackwardAnimation(a);
                                    }
                                });

                                layout.addView(view);
                            }
                        }
                        catch(JSONException e) {
                            Toast.makeText(c.getApplicationContext(), "Unable to parse data: "
                                    + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(c.getApplicationContext(),
                                c.getResources().getString(R.string.connection_problem),
                                Toast.LENGTH_LONG).show();
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
    public static void hideSystemUI(Activity a) {
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
    public static void showSystemUI(Activity a) {
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
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    a.getResources().getDisplayMetrics());
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
                orientation == Configuration.ORIENTATION_PORTRAIT
                        ? "navigation_bar_height" : "navigation_bar_height_landscape",
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
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //status bar height
            int actionBarHeight = getActionBarHeight(a);
            int statusBarHeight = getStatusBarHeight(a);
            Utils.log("statusBarHeight", Integer.toString(statusBarHeight));
            Utils.log("actionBarHeight", Integer.toString(actionBarHeight));
            //action bar height
            //statusBar.getLayoutParams().height = actionBarHeight + statusBarHeight;
            statusBar.setBackgroundColor(color);
        }
    }


    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }


    public static Spannable getStringWithRegularCustomFont(Context context,
                                                           String text, String font) {
        if (text != null) {
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new TypefaceSpan(context, font), 0, spannableString.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }
        return null;
    }

    public static void setActionStatusBarTint(Window w, Activity a, String statusBarColor,
                                              String actionBarColor) {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (a.getActionBar() != null) {
            if (actionBarColor != null) {
                a.getActionBar().setBackgroundDrawable(
                        new ColorDrawable(Color.parseColor(actionBarColor)));
            }
        }
        if (currentapiVersion >= Build.VERSION_CODES.KITKAT) {
            if (currentapiVersion > Build.VERSION_CODES.KITKAT) {
                w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            if(statusBarColor != null) {
                // create our manager instance after the content view is set
                SystemBarTintManager tintManager = new SystemBarTintManager(a);
                // enable status bar tint
                tintManager.setStatusBarTintEnabled(true);
                // enable navigation bar tint
                tintManager.setNavigationBarTintEnabled(true);
                // set a custom tint color for all system bars
                tintManager.setTintColor(Color.parseColor(statusBarColor));
            }
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;
    }

    public static void goForwardAnimation(Activity a) {
        a.overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
    }

    public static void goBackwardAnimation(Activity a) {
        a.overridePendingTransition(R.anim.fade_in, R.anim.slide_out_right);
    }

    public static void flipCardAnimation(Activity a) {
        a.overridePendingTransition(R.anim.card_flip_left_in, R.anim.card_flip_left_out);
    }


    public static void sendGet(String url, Context c) {
        if (!Basic.DEBUG) {
            Ion.with(c).load(url).asString();
        }
    }

    public static void submitStatistics(Context c) {
        if (!Basic.DEBUG) {
            String toSubmit = Basic.MAIN_SERVER + "stats.php?osVersion=" + Build.VERSION.RELEASE
                    + "&imei=" + Build.SERIAL
                    + "&device=" + Build.MODEL.replace(" ", "%20")
                    + "&appVersion=" + c.getResources().getString(R.string.app_version);
            Utils.log("submitStatistics", toSubmit);
            sendGet(toSubmit, c);
        }
    }

    public static int dpToPx(int dp, Context c) {
        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static void log(String tag, String sql) {
        if(Basic.DEBUG) {
            Log.i(tag, sql);
        }
    }
}
