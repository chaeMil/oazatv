package com.chaemil.hgms;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.chaemil.hgms.Utils.Utils;
import com.chaemil.hgms.Adapters.ArchiveMenuAdapter;

import static com.chaemil.hgms.Utils.Utils.fetchMenuData;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.content_frame, new HomeFragment())
                    .commit();
        }

        mTitle = mDrawerTitle = getTitle();
        if (getActionBar() != null) {
            getActionBar().setTitle(mDrawerTitle);
        }

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
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        menuList.setEmptyView(emptyText);
        menuList.setAdapter(mArchiveMenuAdapter);

        fetchMenuData(getApplicationContext(), mArchiveMenuAdapter);

        if (savedInstanceState == null) {
            //selectItem(0);
        }

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
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
        args.putString("link", link);
        args.putString("title", title);

        mTitle = title;

        // Insert the fragment by replacing any existing fragment


        if(type.equals("homeLink")) {
            fragment = new HomeFragment();
        }
        else if (type.equals("archiveLink")) {
            fragment = new ArchiveFragment();

        }
        //Toast.makeText(getApplicationContext(),type,Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),getResources().getString(R.string.mainServerJson)+"?page=archive&lang="+ Utils.lang+link,Toast.LENGTH_LONG).show();
        Log.i("link",getResources().getString(R.string.mainServerJson)+"?page=archive&lang="+ Utils.lang+link);

        FragmentManager fragmentManager = getFragmentManager();

        fragment.setArguments(args);

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();

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