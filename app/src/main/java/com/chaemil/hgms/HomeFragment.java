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
import com.chaemil.hgms.Adapters.ArchiveDataAdapter;
import com.chaemil.hgms.Adapters.ArchiveDataRecord;
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

    private ArchiveDataAdapter mNewVideosAdapter;
    private ArchiveDataAdapter mPhotoalbumsAdapter;
    private ArchiveDataAdapter mFirstVideoDataAdapter;
    private ListView homeFirstVideo;
    private ListView homePhotoAlbums;

    private List<ArchiveDataRecord> parseArchive(JSONObject json, String array) throws JSONException {
        ArrayList<ArchiveDataRecord> records = new ArrayList<ArchiveDataRecord>();

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


            ArchiveDataRecord record = new ArchiveDataRecord(type, thumb, title, videoDate, videoURL, albumId, videoViews, thumbBlur);
            records.add(record);
        }

        return records;
    }
    private void fetchNewVideos(String link) {
        JsonObjectRequest request = new JsonObjectRequest(
                link,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            List<ArchiveDataRecord> archiveDataRecords = parseArchive(jsonObject,"newVideos");

                            mNewVideosAdapter.swapImageRecords(archiveDataRecords);
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
    }
    private void fetchFirstVideoData(String link) {
        JsonObjectRequest request = new JsonObjectRequest(
                link,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            List<ArchiveDataRecord> archiveDataRecords = parseArchive(jsonObject,"mainVideo");

                            mFirstVideoDataAdapter.swapImageRecords(archiveDataRecords);
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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Bundle bundle = this.getArguments();
        String link = "";
        if (bundle != null) {
            link = bundle.getString("link");
        }

        mNewVideosAdapter = new ArchiveDataAdapter(getActivity(),R.layout.home_block);
        mPhotoalbumsAdapter = new ArchiveDataAdapter(getActivity(),R.layout.home_block);
        mFirstVideoDataAdapter = new ArchiveDataAdapter(getActivity(),R.layout.home_first_video);

        homeFirstVideo = (ListView) rootView.findViewById(R.id.firstVideo);
        homeFirstVideo.setAdapter(mFirstVideoDataAdapter);

        ExpandableListView newVideos = (ExpandableListView) rootView.findViewById(R.id.newVideos);
        newVideos.setExpanded(true);
        newVideos.setAdapter(mNewVideosAdapter);

        ExpandableListView photoAlbums = (ExpandableListView) rootView.findViewById(R.id.photoAlbums);
        photoAlbums.setExpanded(true);
        photoAlbums.setAdapter(mPhotoalbumsAdapter);

        fetchFirstVideoData(getResources().getString(R.string.mainServerJson) + "?page=home&lang=" + Utils.lang + link);
        fetchNewVideos(getResources().getString(R.string.mainServerJson)+"?page=home&lang="+ Utils.lang+link);

        rootView.invalidate();

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