package com.chaemil.hgms;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.chaemil.hgms.Adapters.ArchiveAdapter;
import com.chaemil.hgms.Utils.Utils;

/**
 * Created by chaemil on 23.10.14.
 */
public class SimilarVideosFragment extends Fragment {

    private ArchiveAdapter similarVideosAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_similar_videos, container, false);

        Bundle extras = getActivity().getIntent().getExtras();
        String videoURL = extras.getString("videoLink");
        String videoID = videoURL.substring(videoURL.lastIndexOf("/")+1,videoURL.lastIndexOf("."));

        similarVideosAdapter = new ArchiveAdapter(getActivity(),R.layout.archive_block);
        ListView similarVideos = (ListView) rootView.findViewById(R.id.similarVideos);
        similarVideos.setAdapter(similarVideosAdapter);
        String similarVideosJSON = getResources().getString(R.string.mainServerJson) + "?page=similarVideos&video=" + videoID + "&lang=" + Utils.lang;
        com.chaemil.hgms.Utils.Utils.fetchArchive(
                getActivity().getApplicationContext(),
                similarVideosJSON,
                similarVideosAdapter,
                "similarVideos");

        Log.i("similarVideos", similarVideosJSON);


        return rootView;
    }
}
