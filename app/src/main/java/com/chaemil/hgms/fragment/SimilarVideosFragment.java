package com.chaemil.hgms.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.chaemil.hgms.R;
import com.chaemil.hgms.adapters.ArchiveAdapter;
import com.chaemil.hgms.utils.Basic;
import com.chaemil.hgms.utils.SmartLog;
import com.chaemil.hgms.utils.Utils;

import static com.chaemil.hgms.utils.IntentUtils.startVideoPlayer;

public class SimilarVideosFragment extends Fragment {

    private ArchiveAdapter similarVideosAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_similar_videos, container, false);

        Bundle extras = getArguments();
        String videoURL = extras.getString(Basic.VIDEO_LINK);
        if (extras != null) {
            SmartLog.log("videoURLFrag", extras.getString(Basic.VIDEO_LINK));
        }
        String videoID = videoURL.substring(videoURL.lastIndexOf("/") + 1, videoURL.lastIndexOf("."));

        similarVideosAdapter = new ArchiveAdapter(getActivity(),R.layout.archive_block);
        ListView similarVideos = (ListView) rootView.findViewById(R.id.similarVideos);
        similarVideos.setAdapter(similarVideosAdapter);
        String similarVideosJSON = Basic.MAIN_SERVER_JSON + "?page=similarVideos&video="
                + videoID + "&lang=" + Utils.lang;
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

        SmartLog.log("similarVideos", similarVideosJSON);


        return rootView;
    }
}
