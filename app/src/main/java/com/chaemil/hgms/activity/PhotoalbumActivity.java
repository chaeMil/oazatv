package com.chaemil.hgms.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.chaemil.hgms.R;
import com.chaemil.hgms.adapters.PhotoalbumAdapter;
import com.chaemil.hgms.adapters.PhotosViewPagerAdapter;
import com.chaemil.hgms.factory.RequestFactory;
import com.chaemil.hgms.factory.RequestFactoryListener;
import com.chaemil.hgms.factory.ResponseFactory;
import com.chaemil.hgms.model.ArchiveItem;
import com.chaemil.hgms.model.Photo;
import com.chaemil.hgms.model.RequestType;
import com.chaemil.hgms.service.MyRequestService;
import com.chaemil.hgms.utils.Constants;
import com.chaemil.hgms.utils.SmartLog;
import com.chaemil.hgms.utils.Utils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONObject;

import java.util.ArrayList;

public class PhotoalbumActivity extends ActionBarActivity implements RequestFactoryListener {

    private GridView photoThumbsGrid;
    private PhotoalbumAdapter mPhotoalbumAdapter;
    private ArchiveItem archiveItem;
    private ArrayList<Photo> photosArray;
    private ViewPager photosViewPager;
    private PhotosViewPagerAdapter mPhotosViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoalbum);

        archiveItem = getIntent().getExtras().getParcelable(ArchiveItem.ARCHIVE_ITEM);

        Utils.submitStatistics(getApplicationContext());

        getUI();
        setupUI();
        getData();

    }

    private void resetImmersiveFullscreen() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (photosViewPager.getVisibility() == View.VISIBLE) {
                    setImmersiveFullscreen(true);
                }
                handler.postDelayed(this, 2000);
            }
        }, 2000);
    }

    private void getData() {
        Request photosRequest = RequestFactory.getPhotos(this, archiveItem.getAlbumId());
        MyRequestService.getRequestQueue().add(photosRequest);
    }

    private void getUI() {
        photoThumbsGrid = (GridView) findViewById(R.id.photoThumbsGrid);
        photosViewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    private void setupUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
        }

        getSupportActionBar().setTitle(archiveItem.getTitle());
        getSupportActionBar().setSubtitle(archiveItem.getDate());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photoThumbsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                photosViewPager.setCurrentItem(i);
                photosViewPager.setVisibility(View.VISIBLE);
                setImmersiveFullscreen(true);
            }
        });

        photosViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() != MotionEvent.ACTION_SCROLL) {
                    setImmersiveFullscreen(false);
                }
                return false;
            }
        });

        resetImmersiveFullscreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photoalbum, menu);
        return true;
    }

    private int getThumbWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        if (width < height) {
            return width / 3;
        } else {
            return height / 4;
        }
    }

    public void shareLink() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, archiveItem.getTitle());
        share.putExtra(Intent.EXTRA_TEXT, Constants.MAIN_SERVER_PHOTOALBUM_LINK_PREFIX + archiveItem.getAlbumId());
        startActivity(Intent.createChooser(share, getResources().getString(R.string.action_share)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share_link:
                shareLink();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (photosViewPager.getVisibility() == View.VISIBLE) {
            int currentPhoto = photosViewPager.getCurrentItem();
            photoThumbsGrid.smoothScrollToPosition(currentPhoto);

            if (photoThumbsGrid.getChildAt(currentPhoto) != null) {
                YoYo.with(Techniques.Pulse).duration(500).playOn(photoThumbsGrid.getChildAt(currentPhoto));
            }

            photosViewPager.setVisibility(View.GONE);
            setImmersiveFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (photosViewPager.getVisibility() == View.VISIBLE) {
            if (hasFocus) {
                setImmersiveFullscreen(true);
            }
        } else {
            setImmersiveFullscreen(false);
        }
    }

    private void setImmersiveFullscreen(boolean enable) {
        if (enable) {
            getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            if (photosViewPager.getVisibility() == View.VISIBLE) {
                getSupportActionBar().hide();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.black));
                }
            } else {
                getSupportActionBar().show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
                }
            }
        }
    }


    @Override
    public void onSuccessResponse(JSONObject response, RequestType requestType) {
        SmartLog.log("PhotoalbumActivity response", String.valueOf(response));

        switch (requestType) {
            case PHOTOALBUM:
                photosArray = ResponseFactory.parsePhotos(response);
                mPhotoalbumAdapter = new PhotoalbumAdapter(this, R.layout.photo_thumb, this, photosArray, getThumbWidth());
                photoThumbsGrid.setAdapter(mPhotoalbumAdapter);
                mPhotosViewPagerAdapter = new PhotosViewPagerAdapter(getSupportFragmentManager(), photosArray);
                photosViewPager.setAdapter(mPhotosViewPagerAdapter);
        }
    }

    @Override
    public void onErrorResponse(VolleyError exception) {
        Toast.makeText(this, getString(R.string.something_went_wrong),
                Toast.LENGTH_SHORT).show();
        SmartLog.log("errorResponse", String.valueOf(exception));
    }
}
