package com.chaemil.hgms;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.chaemil.hgms.adapters.ArchiveAdapter;
import com.chaemil.hgms.utils.Utils;

import static com.chaemil.hgms.utils.Basic.startVideoPlayer;

/**
 * Created by chaemil on 23.10.14.
 */
public class SimilarVideosFragment extends Fragment {

    private ArchiveAdapter similarVideosAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_similar_videos, container, false);

        Bundle extras = getArguments();
        String videoURL = extras.getString("videoLink");
        if (extras != null) {
            Log.i("videoURLFrag",extras.getString("videoLink"));
        }
        String videoID = videoURL.substring(videoURL.lastIndexOf("/") + 1, videoURL.lastIndexOf("."));

        similarVideosAdapter = new ArchiveAdapter(getActivity(),R.layout.archive_block);
        ListView similarVideos = (ListView) rootView.findViewById(R.id.similarVideos);
        similarVideos.setAdapter(similarVideosAdapter);
        String similarVideosJSON = getResources().getString(R.string.mainServerJson) + "?page=similarVideos&video=" + videoID + "&lang=" + Utils.lang;
        com.chaemil.hgms.utils.Utils.fetchArchive(
                getActivity().getApplicationContext(),
                similarVideosJSON,
                similarVideosAdapter,
                "similarVideos");
        similarVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                startVideoPlayer(rootView, videoURL, videoName, videoDate, videoViews);
            }
        });

        Log.i("similarVideos", similarVideosJSON);


        return rootView;
    }
}
