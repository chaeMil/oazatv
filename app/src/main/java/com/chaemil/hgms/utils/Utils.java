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
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
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
import com.chaemil.hgms.activity.MainActivity;
import com.chaemil.hgms.R;
import com.chaemil.hgms.adapters.ArchiveAdapter;
import com.chaemil.hgms.adapters.ArchiveMenuAdapter;
import com.chaemil.hgms.model.ArchiveMenu;
import com.chaemil.hgms.model.ArchiveItem;
import com.chaemil.hgms.adapters.PhotoalbumAdapter;
import com.chaemil.hgms.model.Photo;
import com.koushikdutta.ion.Ion;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wefika.flowlayout.FlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Utils {

    public static String lang = Locale.getDefault().getLanguage();

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
    public static void hideSystemUI(ActionBarActivity a) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        if (a.getSupportActionBar() != null) {
            a.getSupportActionBar().hide();
        }
        a.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        a.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        a.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    public static void showSystemUI(ActionBarActivity a) {
        if (a.getSupportActionBar() != null) {
            a.getSupportActionBar().show();
        }
        a.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /*v.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);*/
    }


    public static int getActionBarHeight(ActionBarActivity a) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (a.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    a.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public static int getStatusBarHeight(ActionBarActivity a) {
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

    public static void setStatusBarColor(ActionBarActivity a, View statusBar, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = a.getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //status bar height
            int actionBarHeight = getActionBarHeight(a);
            int statusBarHeight = getStatusBarHeight(a);
            SmartLog.log("statusBarHeight", Integer.toString(statusBarHeight));
            SmartLog.log("actionBarHeight", Integer.toString(actionBarHeight));
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

    public static void setActionStatusBarTint(Window w, ActionBarActivity a, String statusBarColor,
                                              String actionBarColor) {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (a.getSupportActionBar() != null) {
            if (actionBarColor != null) {
                a.getSupportActionBar().setBackgroundDrawable(
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
        if (!Constants.DEBUG) {
            Ion.with(c).load(url).asString();
        }
    }

    public static void submitStatistics(Context c) {
        if (!Constants.DEBUG) {
            String toSubmit = Constants.MAIN_SERVER + "stats.php?osVersion=" + Build.VERSION.RELEASE
                    + "&imei=" + Build.SERIAL
                    + "&device=" + Build.MODEL.replace(" ", "%20")
                    + "&appVersion=" + c.getResources().getString(R.string.app_version);
            SmartLog.log("submitStatistics", toSubmit);
            sendGet(toSubmit, c);
        }
    }

    public static int dpToPx(int dp, Context c) {
        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

}
