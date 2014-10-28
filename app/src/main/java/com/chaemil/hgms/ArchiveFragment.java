package com.chaemil.hgms;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.chaemil.hgms.Adapters.ArchiveAdapter;
import com.chaemil.hgms.Utils.Utils;

import org.w3c.dom.Text;

import static com.chaemil.hgms.Utils.Basic.startPhotoalbumViewer;
import static com.chaemil.hgms.Utils.Basic.startVideoPlayer;

/**
 * Created by chaemil on 17.10.14.
 */
public class ArchiveFragment extends Fragment {

    public ArchiveFragment() {
    }

    private GridView archiveGrid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_archive, container, false);

        Bundle bundle = this.getArguments();
        String link = "";
        if (bundle != null) {
            link = bundle.getString("link");
        }

        ArchiveAdapter mArchiveAdapter = new ArchiveAdapter(getActivity(),R.layout.archive_block);

        archiveGrid = (GridView) rootView.findViewById(R.id.archiveGrid);

        archiveGrid.setAdapter(mArchiveAdapter);
        archiveGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                TextView typeElement = (TextView) v.findViewById(R.id.type);
                String type = typeElement.getText().toString();
                if(type.equals("video")) {
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
                else if(type.equals("photoAlbum")) {
                    TextView albumIdElement = (TextView) v.findViewById(R.id.albumId);
                    String albumId = albumIdElement.getText().toString();
                    startPhotoalbumViewer(getView(),albumId);
                }
            }
        });


        com.chaemil.hgms.Utils.Utils.fetchArchive(getActivity().getApplicationContext(),getResources().getString(R.string.mainServerJson)+"?page=archive&lang="+ Utils.lang+link,mArchiveAdapter,"archive");


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