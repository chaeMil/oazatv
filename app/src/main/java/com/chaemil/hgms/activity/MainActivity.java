package com.chaemil.hgms.activity;




import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaemil.hgms.R;
import com.chaemil.hgms.adapters.ArchiveMenuAdapter;
import com.chaemil.hgms.fragment.ArchiveFragment;
import com.chaemil.hgms.fragment.HomeFragment;
import com.chaemil.hgms.fragment.OfflineFragment;
import com.chaemil.hgms.fragment.WebViewFragment;
import com.chaemil.hgms.utils.Constants;
import com.chaemil.hgms.utils.SmartLog;
import com.chaemil.hgms.utils.Utils;

import at.markushi.ui.CircleButton;

import static com.chaemil.hgms.utils.Utils.fetchMenuData;
import static com.chaemil.hgms.utils.Utils.setActionStatusBarTint;


public class MainActivity extends ActionBarActivity {

    private ArchiveMenuAdapter mArchiveMenuAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private TextView emptyText;
    private ListView menuList;
    private android.support.v4.app.Fragment fragment;
    private String youtubeVideoId;
    private MenuItem searchMenuItem;
    private SearchView searchView;
    private Menu _menu;

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences settings = getSharedPreferences(Constants.MAIN_PREFS, 0);
        boolean appFirstRun = settings.getBoolean(Constants.FIRST_RUN, true);
        if (appFirstRun) {
            Intent i = new Intent(this, FirstRun.class);
            startActivity(i);
            finish();
        }
    }

    private boolean isFirstRun() {
        return getIntent().hasExtra(Constants.FIRST_RUN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Constants.DEBUG) {
            Toast.makeText(getApplicationContext(),
                    "you are using debug version of the app [utils.Constants.DEBUG = true]",
                    Toast.LENGTH_LONG).show();
        }


        setActionStatusBarTint(getWindow(), this, Constants.MAIN_ACTIVITY_STATUSBAR_COLOR,
                Constants.MAIN_ACTIVITY_STATUSBAR_COLOR);

        Bundle extras = getIntent().getExtras();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();

        if (savedInstanceState == null) {
            fragmentTransaction.add(R.id.content_frame, new HomeFragment())
                    .commit();
        }

        setActionBarTitle(this, getString(R.string.app_name));

        if (extras != null) {
            if (extras.containsKey(Constants.BUNDLE_TAG)) {
                if(extras.getString(Constants.BUNDLE_TAG) != null) {
                    submitSearch(MainActivity.this, extras.getString(Constants.BUNDLE_TAG), true);
                }
            }
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        emptyText = (TextView) findViewById(android.R.id.empty);
        menuList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mArchiveMenuAdapter = new ArchiveMenuAdapter(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

                CircleButton liveBroadcastButton = (CircleButton)
                        findViewById(R.id.liveBroadcastButton);
                if (liveBroadcastButton != null) {
                    liveBroadcastButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            launchLivePlayer();
                        }
                    });
                }

                CircleButton downloadedAudioButton = (CircleButton)
                        findViewById(R.id.downloadedAudioButton);
                if (downloadedAudioButton != null) {
                    downloadedAudioButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(MainActivity.this, ListDownloadedAudio.class);
                            startActivity(i);
                            Utils.goForwardAnimation(MainActivity.this);
                        }
                    });
                }
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        menuList.setEmptyView(emptyText);
        menuList.setAdapter(mArchiveMenuAdapter);

        fetchMenuData(getApplicationContext(), mArchiveMenuAdapter);

        if(isFirstRun()) {
            new Handler().postDelayed(openDrawerRunnable(), 200);
        }

    }

    private Runnable openDrawerRunnable() {
        return new Runnable() {

            @Override
            public void run() {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        };
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    private Menu getMenu()
    {
        return _menu;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);

        _menu = menu;

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();

        if (null != searchView) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {


                submitSearch(MainActivity.this, query, false);

                return false;
            }

        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }

    public void submitSearch(ActionBarActivity actionBarActivity,
                             String query, boolean calledExternally) {
        fragment = new ArchiveFragment();
        Bundle args = new Bundle();

        String link;

        if (query.startsWith("#")) {
            link = Constants.MAIN_SERVER_JSON+"?page=archive&lang="+ Utils.lang+"&tagy="+query
                    .replace("#","");
        }
        else {
            link = Constants.MAIN_SERVER_JSON+"?page=archive&lang="+ Utils.lang+"&nazev="+query;
        }

        SmartLog.log("submitSearch", link);

        args.putString(Constants.BUNDLE_LINK, link);


        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        fragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();

        if (!calledExternally) {
            searchMenuItem = getMenu().findItem(R.id.search);
            searchMenuItem.collapseActionView();
            mDrawerLayout.closeDrawers();
        }

        setActionBarTitle(actionBarActivity,
                getResources().getString(R.string.action_search)+": "+query);


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id) {
            case R.id.downloadedAudio:
                Intent i = new Intent(this, ListDownloadedAudio.class);
                startActivity(i);
                Utils.goForwardAnimation(this);
            return true;
            case android.R.id.home:
                if(!mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            if (position != 0) {
                TextView titleElement = (TextView) view.findViewById(R.id.titleToShow);
                String title = titleElement.getText().toString();
                TextView typeElement = (TextView) view.findViewById(R.id.type);
                String type = typeElement.getText().toString();
                TextView linkElement = (TextView) view.findViewById(R.id.content);
                String link = linkElement.getText().toString();
                selectItem(position, type, link, title);
            } else {

            }
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position, String type, String link, String title) {
        // Create a new fragment and specify the planet to show based on position


        Bundle args = new Bundle();
        args.putString(Constants.BUNDLE_LINK, link);
        args.putString(Constants.BUNDLE_TITLE, title);



        // Insert the fragment by replacing any existing fragment

        boolean isFragment = false;

        if(!Utils.isOnline(getApplicationContext())) {
            fragment = new OfflineFragment();
            isFragment = true;
        }
        else {
            setActionStatusBarTint(getWindow(),this, Constants.MAIN_ACTIVITY_STATUSBAR_COLOR,
                    Constants.MAIN_ACTIVITY_STATUSBAR_COLOR);
            if (type.equals(Constants.JSON_MENU_TYPE_HOME_LINK)) {
                fragment = new HomeFragment();
                isFragment = true;
            } else if (type.equals(Constants.JSON_MENU_TYPE_ARCHIVE_LINK)) {
                fragment = new ArchiveFragment();
                isFragment = true;
            } else if (type.equals(Constants.JSON_MENU_TYPE_REPORT_BUG)) {
                fragment = new WebViewFragment();
                args.putString(Constants.BUNDLE_LINK, Constants.REPORT_BUG_URL);
                isFragment = true;
            } else if (type.equals(Constants.JSON_MENU_TYPE_ABOUT_APP)) {
                fragment = new WebViewFragment();
                args.putString(Constants.BUNDLE_LINK, Constants.ABOUT_APP_URL);
                isFragment = true;
            } else if (type.equals(Constants.JSON_MENU_TYPE_DOWNLOADED_AUDIO)) {
                Intent i = new Intent(this, ListDownloadedAudio.class);
                startActivity(i);
                Utils.goForwardAnimation(this);
            } else if (type.equals(Constants.JSON_MENU_TYPE_EXIT)) {
                finish();
            } /*else if (type.equals(Constants.JSON_MENU_TYPE_LIVE_PLAYER)) {



            }*/
        }


        SmartLog.log("link", Constants.MAIN_SERVER_JSON + "?page=archive&lang=" + Utils.lang + link);

        if(isFragment) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

            fragment.setArguments(args);

            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        //setTitle(mArchiveMenuAdapter.getPosition(position));
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public static void setActionBarTitle(ActionBarActivity a, CharSequence title) {
        a.getSupportActionBar().setTitle(title);
    }

    public void launchLivePlayer() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = Constants.MAIN_SERVER_JSON + "?page=live";

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        SmartLog.log("request", s);
                        if(Constants.DEBUG) {
                            youtubeVideoId = "NF1OdLMTDEc";
                            Toast.makeText(getApplicationContext(),
                                    "you are using debug version of the app, this is just a test video",
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            if (!s.trim().replaceAll("\n", "").equals("null")) {
                                youtubeVideoId = s.trim().replaceAll("\n", "");
                            }
                        }
                        if(youtubeVideoId != null) {
                            Intent i = new Intent(getApplicationContext(), LivePlayer.class);
                            i.putExtra(Constants.YOUTUBE_VIDEO_ID, youtubeVideoId);
                            startActivity(i);
                            //Utils.goForwardAnimation(getParent());
                        }
                        else {
                            Toast.makeText(getApplicationContext(),
                                    getResources().getString(R.string.no_broadcats_message),
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        SmartLog.log("VolleyError.error", volleyError.toString());
                    }
                }
        );

        queue.add(request);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }
}
