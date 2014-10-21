package com.chaemil.hgms;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chaemil.hgms.Adapters.ArchiveAdapter;
import com.chaemil.hgms.Adapters.HomePageAdapters.NewVideosAdapter;
import com.chaemil.hgms.Adapters.HomePageAdapters.NewVideosRecord;
import com.chaemil.hgms.Adapters.ArchiveRecord;
import com.chaemil.hgms.Adapters.HomePageAdapters.PhotoalbumsAdapter;
import com.chaemil.hgms.Utils.Utils;
import com.chaemil.hgms.Utils.VolleyApplication;
import com.chaemil.hgms.View.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaemil on 17.10.14.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
    }


    private ArchiveAdapter mFirstVideoDataAdapter;
    private ListView homeFirstVideo;
    private ExpandableListView newVideos;
    private ExpandableListView photoAlbums;
    private NewVideosAdapter mNewVideosAdapter;
    private PhotoalbumsAdapter mPhotoalbumsAdapter;


    /*private List<ArchiveRecord> parseFirstVideo(JSONObject json, String array) throws JSONException {
        ArrayList<ArchiveRecord> records = new ArrayList<ArchiveRecord>();

        JSONArray jsonImages = json.getJSONArray(array);

        for(int i =0; i < jsonImages.length(); i++) {
            JSONObject jsonImage = jsonImages.getJSONObject(i);
            String type = jsonImage.getString("type");
            String title = jsonImage.getString("title");
            String thumb = jsonImage.getString("thumb");
            String videoDate = jsonImage.getString("date");
            String videoURL = jsonImage.getString("videoURL");
            String videoViews = jsonImage.getString("playCount");
            String albumId = jsonImage.getString("albumId");
            String thumbBlur = jsonImage.getString("thumbBlur");


            ArchiveRecord record = new ArchiveRecord(type, thumb, title, videoDate, videoURL, albumId, videoViews, thumbBlur);
            records.add(record);
        }

        return records;
    }*/
    /*private void fetchFirstVideoData(String link) {
        JsonObjectRequest request = new JsonObjectRequest(
                link,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            List<ArchiveRecord> firstVideoRecords = parseFirstVideo(jsonObject, "mainVideo");

                            mFirstVideoDataAdapter.swapImageRecords(firstVideoRecords);
                        }
                        catch(JSONException e) {
                            Toast.makeText(getActivity(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_problem), Toast.LENGTH_LONG).show();
                    }
                });

        VolleyApplication.getInstance().getRequestQueue().add(request);
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mFirstVideoDataAdapter = new ArchiveAdapter(getActivity(),R.layout.home_first_video);

        homeFirstVideo = (ListView) rootView.findViewById(R.id.firstVideo);
        homeFirstVideo.setAdapter(mFirstVideoDataAdapter);
        com.chaemil.hgms.Utils.Utils.fetchArchive(
                getActivity().getApplicationContext(),
                getResources().getString(R.string.mainServerJson) + "?page=home&lang=" + Utils.lang,
                mFirstVideoDataAdapter,
                "mainVideo");

        newVideos = (ExpandableListView) rootView.findViewById(R.id.newVideos);
        newVideos.setExpanded(true);
        mNewVideosAdapter = new NewVideosAdapter(getActivity().getApplicationContext(),R.layout.home_block);
        newVideos.setAdapter(mNewVideosAdapter);
        com.chaemil.hgms.Utils.Utils.fetchArchive(
                getActivity(),
                getResources().getString(R.string.mainServerJson) + "?page=home&lang=" + Utils.lang,
                mNewVideosAdapter,
                "newVideos"
        );

        photoAlbums = (ExpandableListView) rootView.findViewById(R.id.photoAlbums);
        photoAlbums.setExpanded(true);
        mPhotoalbumsAdapter = new PhotoalbumsAdapter(getActivity().getApplicationContext(),R.layout.home_block);
        photoAlbums.setAdapter(mPhotoalbumsAdapter);
        com.chaemil.hgms.Utils.Utils.fetchArchive(
                getActivity().getApplicationContext(),
                getResources().getString(R.string.mainServerJson) + "?page=home&lang=" + Utils.lang,
                mPhotoalbumsAdapter,
                "photoAlbums"
        );

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