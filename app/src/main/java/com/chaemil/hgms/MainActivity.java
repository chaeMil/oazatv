package com.chaemil.hgms;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaemil.hgms.adapters.ArchiveMenuAdapter;
import com.chaemil.hgms.utils.Basic;
import com.chaemil.hgms.utils.Utils;

import static com.chaemil.hgms.utils.Utils.fetchMenuData;
import static com.chaemil.hgms.utils.Utils.setActionStatusBarTint;


public class MainActivity extends Activity {

    private ArchiveMenuAdapter mArchiveMenuAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private TextView emptyText;
    private ListView menuList;
    private Fragment fragment;
    private String youtubeVideoId;
    private MenuItem searchMenuItem;
    private SearchView searchView;
    private Menu _menu;

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences settings = getSharedPreferences(Basic.MAIN_PREFS, 0);
        boolean appFirstRun = settings.getBoolean(Basic.FIRST_RUN, true);
        if (appFirstRun) {
            Intent i = new Intent(this, FirstRun.class);
            startActivity(i);
            finish();
        }
    }

    private boolean isFirstRun() {
        return getIntent().hasExtra(Basic.FIRST_RUN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setActionStatusBarTint(getWindow(),this,Basic.MAIN_ACTIVITY_STATUSBAR_COLOR,
                Basic.MAIN_ACTIVITY_STATUSBAR_COLOR);

        Bundle extras = getIntent().getExtras();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.content_frame, new HomeFragment())
                    .commit();
        }


        if (extras != null) {
            if (extras.containsKey("tag")) {
                if(extras.getString("tag") != null) {
                    submitSearch(extras.getString("tag"), true);
                }
            }
        }
        mTitle = getResources().getString(R.string.app_name);
        mDrawerTitle = mTitle;
        setTitle(getResources().getString(R.string.app_name));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        emptyText = (TextView) findViewById(android.R.id.empty);
        menuList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mArchiveMenuAdapter = new ArchiveMenuAdapter(this);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
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


                submitSearch(query, false);

                return false;
            }

        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }

    public void submitSearch(String query, boolean calledExternally) {
        fragment = new ArchiveFragment();
        Bundle args = new Bundle();

        String link;

        if (query.startsWith("#")) {
            link = Basic.MAIN_SERVER_JSON+"?page=archive&lang="+ Utils.lang+"&tagy="+query
                    .replace("#","");
        }
        else {
            link = Basic.MAIN_SERVER_JSON+"?page=archive&lang="+ Utils.lang+"&nazev="+query;
        }

        Log.i("submitSearch",link);

        args.putString(Basic.BUNDLE_LINK, link);

        FragmentManager fragmentManager = getFragmentManager();
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

        setTitle(getResources().getString(R.string.action_search)+": "+query);


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            TextView titleElement = (TextView) view.findViewById(R.id.titleToShow);
            String title = titleElement.getText().toString();
            TextView typeElement = (TextView) view.findViewById(R.id.type);
            String type = typeElement.getText().toString();
            TextView linkElement = (TextView) view.findViewById(R.id.content);
            String link = linkElement.getText().toString();
            selectItem(position, type, link, title);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position, String type, String link, String title) {
        // Create a new fragment and specify the planet to show based on position


        Bundle args = new Bundle();
        args.putString(Basic.BUNDLE_LINK, link);
        args.putString(Basic.BUNDLE_TITLE, title);



        // Insert the fragment by replacing any existing fragment

        boolean isFragment = false;

        if(!Utils.isOnline(getApplicationContext())) {
            fragment = new OfflineFragment();
            isFragment = true;
        }
        else {
            setActionStatusBarTint(getWindow(),this,Basic.MAIN_ACTIVITY_STATUSBAR_COLOR,
                    Basic.MAIN_ACTIVITY_STATUSBAR_COLOR);
            if (type.equals(Basic.JSON_MENU_TYPE_HOME_LINK)) {
                fragment = new HomeFragment();
                isFragment = true;
            } else if (type.equals(Basic.JSON_MENU_TYPE_ARCHIVE_LINK)) {
                fragment = new ArchiveFragment();
                isFragment = true;
            } else if (type.equals(Basic.JSON_MENU_TYPE_REPORT_BUG)) {
                fragment = new WebViewFragment();
                args.putString(Basic.BUNDLE_LINK, Basic.REPORT_BUG_URL);
                isFragment = true;
            } else if (type.equals(Basic.JSON_MENU_TYPE_ABOUT_APP)) {
                fragment = new WebViewFragment();
                args.putString(Basic.BUNDLE_LINK, Basic.ABOUT_APP_URL);
                isFragment = true;
            } else if (type.equals(Basic.JSON_MENU_TYPE_DOWNLOADED_AUDIO)) {
                Intent i = new Intent(this, ListDownloadedAudio.class);
                startActivity(i);
                Utils.goForwardAnimation(this);
            } else if (type.equals(Basic.JSON_MENU_TYPE_EXIT)) {
                finish();
            } else if (type.equals(Basic.JSON_MENU_TYPE_LIVE_PLAYER)) {

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                String url = Basic.MAIN_SERVER_JSON + "?page=live";

                StringRequest request = new StringRequest(
                        url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String s) {
                                Log.i("request", s);
                                if(!s.trim().replaceAll("\n", "").equals("null")) {
                                    youtubeVideoId = s.trim().replaceAll("\n", "");
                                }
                                if(youtubeVideoId != null) {
                                    Intent i = new Intent(getApplicationContext(), LivePlayer.class);
                                    i.putExtra(Basic.YOUTUBE_VIDEO_ID, youtubeVideoId);
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
                                Log.i("VolleyError.error", volleyError.toString());
                            }
                        }
                );

                queue.add(request);

            }
        }

        if (!type.equals(Basic.JSON_MENU_TYPE_DOWNLOADED_AUDIO)
            || !type.equals(Basic.JSON_MENU_TYPE_LIVE_PLAYER)) {
            mTitle = getResources().getString(R.string.app_name);
        }


        Log.i("link",Basic.MAIN_SERVER_JSON+"?page=archive&lang="+ Utils.lang+link);

        if(isFragment) {
            FragmentManager fragmentManager = getFragmentManager();


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

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        if (getActionBar() != null) {
            getActionBar().setTitle(mTitle);
        }
    }
}
