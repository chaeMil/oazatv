package com.chaemil.hgms;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chaemil.hgms.adapters.ArchiveAdapter;
import com.chaemil.hgms.adapters.home_page_adapters.NewVideosAdapter;
import com.chaemil.hgms.adapters.home_page_adapters.PhotoalbumsAdapter;
import com.chaemil.hgms.db.AudioDBHelper;
import com.chaemil.hgms.utils.Basic;
import com.chaemil.hgms.utils.Utils;
import com.chaemil.hgms.view.ExpandedListView;

import java.util.ResourceBundle;

import at.markushi.ui.CircleButton;

import static com.chaemil.hgms.utils.Basic.startPhotoalbumViewer;
import static com.chaemil.hgms.utils.Basic.startVideoPlayer;
import static com.chaemil.hgms.utils.Utils.fetchArchive;
import static com.chaemil.hgms.utils.Utils.setActionStatusBarTint;

/**
 * Created by chaemil on 17.10.14.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
    }


    private ArchiveAdapter mFirstVideoDataAdapter;
    private ListView homeFirstVideo;
    private ExpandedListView newVideos;
    private ExpandedListView photoAlbums;
    private NewVideosAdapter mNewVideosAdapter;
    private PhotoalbumsAdapter mPhotoalbumsAdapter;
    private TextView newVideosTitle;
    private TextView photoalbumsTitle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FrameLayout frameLayout = new FrameLayout(getActivity());
        populateViewForOrientation(inflater, frameLayout);





        return frameLayout;


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        populateViewForOrientation(inflater, (ViewGroup) getView());
    }

    private void populateViewForOrientation(LayoutInflater inflater, ViewGroup viewGroup) {
        viewGroup.removeAllViewsInLayout();
        View rootView = inflater.inflate(R.layout.fragment_home, viewGroup);

        //View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        FragmentManager fragmentManager = getFragmentManager();

        if(!Utils.isOnline(getActivity().getApplicationContext())) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new OfflineFragment())
                    .commit();
        }

        mFirstVideoDataAdapter = new ArchiveAdapter(getActivity(),R.layout.home_first_video);

        newVideosTitle = (TextView) rootView.findViewById(R.id.newVideosTitle);
        newVideosTitle.setText(Utils.getStringWithRegularCustomFont(getActivity(),getResources().getString(R.string.newVideos), "Titillium-BoldUpright.otf"));

        photoalbumsTitle = (TextView) rootView.findViewById(R.id.photoalbumsTitle);
        photoalbumsTitle.setText(Utils.getStringWithRegularCustomFont(getActivity(),getResources().getString(R.string.photoalbums), "Titillium-BoldUpright.otf"));

        homeFirstVideo = (ListView) rootView.findViewById(R.id.firstVideo);
        homeFirstVideo.setAdapter(mFirstVideoDataAdapter);
        fetchArchive(
                getActivity().getApplicationContext(),
                Basic.MAIN_SERVER_JSON + "?page=home&lang=" + Utils.lang,
                mFirstVideoDataAdapter,
                "mainVideo");
        homeFirstVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TextView videoUrlElement = (TextView) v.findViewById(R.id.videoURL);
                String videoURL = videoUrlElement.getText().toString();
                TextView videoNameElement = (TextView) v.findViewById(R.id.videoName);
                String videoName = videoNameElement.getText().toString();
                TextView videoDateElement = (TextView) v.findViewById(R.id.videoDate);
                String videoDate = videoDateElement.getText().toString();
                TextView videoViewsElement = (TextView) v.findViewById(R.id.videoViews);
                String videoViews = videoViewsElement.getText().toString();
                startVideoPlayer(getView(), videoURL, videoName, videoDate, videoViews);
            }
        });

        newVideos = (ExpandedListView) rootView.findViewById(R.id.newVideos);
        newVideos.setExpanded(true);
        mNewVideosAdapter = new NewVideosAdapter(getActivity().getApplicationContext(),R.layout.home_block);
        newVideos.setAdapter(mNewVideosAdapter);
        fetchArchive(
                getActivity(),
                Basic.MAIN_SERVER_JSON + "?page=home&lang=" + Utils.lang,
                mNewVideosAdapter,
                "newVideos"
        );
        newVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                TextView videoUrlElement = (TextView) v.findViewById(R.id.videoURL);
                String videoURL = videoUrlElement.getText().toString();
                TextView videoNameElement = (TextView) v.findViewById(R.id.videoName);
                String videoName = videoNameElement.getText().toString();
                TextView videoDateElement = (TextView) v.findViewById(R.id.videoDate);
                String videoDate = videoDateElement.getText().toString();
                TextView videoViewsElement = (TextView) v.findViewById(R.id.videoViews);
                String videoViews = videoViewsElement.getText().toString();
                startVideoPlayer(getView(), videoURL, videoName, videoDate, videoViews);
                Utils.goForwardAnimation(getActivity());
            }
        });

        photoAlbums = (ExpandedListView) rootView.findViewById(R.id.photoAlbums);
        photoAlbums.setExpanded(true);
        mPhotoalbumsAdapter = new PhotoalbumsAdapter(getActivity().getApplicationContext(),R.layout.home_block);
        photoAlbums.setAdapter(mPhotoalbumsAdapter);
        fetchArchive(
                getActivity().getApplicationContext(),
                Basic.MAIN_SERVER_JSON + "?page=home&lang=" + Utils.lang,
                mPhotoalbumsAdapter,
                "photoAlbums"
        );
        photoAlbums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                TextView albumIdElement = (TextView) v.findViewById(R.id.albumId);
                String albumId = albumIdElement.getText().toString();
                TextView albumNameElement = (TextView) v.findViewById(R.id.videoName);
                String albumName = albumNameElement.getText().toString();
                TextView albumDateElement = (TextView) v.findViewById(R.id.videoDate);
                String albumDate = albumDateElement.getText().toString();
                startPhotoalbumViewer(getView(), albumId, albumName, albumDate);
                Utils.goForwardAnimation(getActivity());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        Bundle bundle = this.getArguments();
        String title = "";
        if (bundle != null) {
            title = bundle.getString(Basic.BUNDLE_TITLE);
        }
        getActivity().getActionBar().setTitle(title);
    }


}