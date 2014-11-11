package com.chaemil.hgms;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.chaemil.hgms.Adapters.ArchiveAdapter;
import com.chaemil.hgms.Adapters.HomePageAdapters.NewVideosAdapter;
import com.chaemil.hgms.Adapters.HomePageAdapters.PhotoalbumsAdapter;
import com.chaemil.hgms.Utils.Utils;
import com.chaemil.hgms.View.ExpandedListView;

import static com.chaemil.hgms.Utils.Basic.startPhotoalbumViewer;
import static com.chaemil.hgms.Utils.Basic.startVideoPlayer;
import static com.chaemil.hgms.Utils.Utils.fetchArchive;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mFirstVideoDataAdapter = new ArchiveAdapter(getActivity(),R.layout.home_first_video);

        homeFirstVideo = (ListView) rootView.findViewById(R.id.firstVideo);
        homeFirstVideo.setAdapter(mFirstVideoDataAdapter);
        fetchArchive(
                getActivity().getApplicationContext(),
                getResources().getString(R.string.mainServerJson) + "?page=home&lang=" + Utils.lang,
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
                getResources().getString(R.string.mainServerJson) + "?page=home&lang=" + Utils.lang,
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
            }
        });

        photoAlbums = (ExpandedListView) rootView.findViewById(R.id.photoAlbums);
        photoAlbums.setExpanded(true);
        mPhotoalbumsAdapter = new PhotoalbumsAdapter(getActivity().getApplicationContext(),R.layout.home_block);
        photoAlbums.setAdapter(mPhotoalbumsAdapter);
        fetchArchive(
                getActivity().getApplicationContext(),
                getResources().getString(R.string.mainServerJson) + "?page=home&lang=" + Utils.lang,
                mPhotoalbumsAdapter,
                "photoAlbums"
        );
        photoAlbums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                TextView albumIdElement = (TextView) v.findViewById(R.id.albumId);
                String albumId = albumIdElement.getText().toString();
                startPhotoalbumViewer(getView(),albumId);
            }
        });

        //rootView.invalidate();

        return rootView;


    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        Bundle bundle = this.getArguments();
        String title = "";
        if (bundle != null) {
            title = bundle.getString("title");
        }
        getActivity().getActionBar().setTitle(title);
    }


}