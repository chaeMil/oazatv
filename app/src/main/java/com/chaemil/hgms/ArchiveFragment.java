package com.chaemil.hgms;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.chaemil.hgms.adapters.ArchiveAdapter;
import com.chaemil.hgms.utils.Basic;
import com.chaemil.hgms.utils.Utils;

import org.w3c.dom.Text;

import static com.chaemil.hgms.utils.Basic.startPhotoalbumViewer;
import static com.chaemil.hgms.utils.Basic.startVideoPlayer;
import static com.chaemil.hgms.utils.Utils.fetchArchive;

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
            link = bundle.getString(Basic.BUNDLE_LINK);
        }

        ArchiveAdapter mArchiveAdapter = new ArchiveAdapter(getActivity(),R.layout.archive_block);

        archiveGrid = (GridView) rootView.findViewById(R.id.archiveGrid);

        archiveGrid.setAdapter(mArchiveAdapter);
        archiveGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                TextView typeElement = (TextView) v.findViewById(R.id.type);
                String type = typeElement.getText().toString();
                if(type.equals(Basic.JSON_ARCHIVE_TYPE_VIDEO)) {
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
                else if(type.equals(Basic.JSON_ARCHIVE_TYPE_PHOTOALBUM)) {
                    TextView albumIdElement = (TextView) v.findViewById(R.id.albumId);
                    String albumId = albumIdElement.getText().toString();
                    TextView albumNameElement = (TextView) v.findViewById(R.id.videoName);
                    String albumName = albumNameElement.getText().toString();
                    TextView albumDateElement = (TextView) v.findViewById(R.id.videoDate);
                    String albumDate = albumDateElement.getText().toString();
                    startPhotoalbumViewer(getView(), albumId, albumName, albumDate);
                    Utils.goForwardAnimation(getActivity());
                }
            }
        });


        fetchArchive(getActivity().getApplicationContext(),Basic.MAIN_SERVER_JSON+"?page=archive&lang="+ Utils.lang+link,mArchiveAdapter,"archive");


        return rootView;


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