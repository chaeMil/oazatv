package com.chaemil.hgms.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.chaemil.hgms.R;
import com.chaemil.hgms.activity.MainActivity;
import com.chaemil.hgms.activity.PhotoalbumActivity;
import com.chaemil.hgms.activity.VideoPlayer;
import com.chaemil.hgms.adapters.ArchiveAdapter;
import com.chaemil.hgms.factory.RequestFactory;
import com.chaemil.hgms.factory.RequestFactoryListener;
import com.chaemil.hgms.factory.ResponseFactory;
import com.chaemil.hgms.model.ArchiveItem;
import com.chaemil.hgms.model.RequestType;
import com.chaemil.hgms.service.MyRequestService;
import com.chaemil.hgms.utils.Constants;
import com.chaemil.hgms.utils.SmartLog;
import com.chaemil.hgms.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;


public class ArchiveFragment extends Fragment implements RequestFactoryListener {

    private String title;
    private String link;
    private ArrayList<ArchiveItem> archiveData;
    private ArchiveAdapter archiveAdapter;
    private ProgressBar progressBar;
    private GridView archiveGrid;
    private FragmentActivity context;
    private int myLastVisiblePos;
    private android.support.v7.app.ActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_archive, container, false);

        context = getActivity();

        Bundle bundle = this.getArguments();
        link = "";
        if (bundle != null) {
            link = bundle.getString(Constants.BUNDLE_LINK);
            title = bundle.getString(Constants.BUNDLE_TITLE);
        }

        Utils.submitStatistics(getActivity().getApplicationContext());

        getUI(rootView);
        setupUI();
        getData();

        return rootView;
    }

    private void getData() {
        Request archiveRequest = RequestFactory.getArchive(this, link);
        MyRequestService.getRequestQueue().add(archiveRequest);
    }

    private void setupUI() {
        actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        MainActivity.setActionBarTitle((ActionBarActivity) getActivity(), title);
        archiveGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (archiveData.get(position).getType().equals(ArchiveItem.TYPE_VIDEO)) {
                    Intent videoPlayer = new Intent(getActivity(), VideoPlayer.class);
                    videoPlayer.putExtra(ArchiveItem.ARCHIVE_ITEM, archiveData.get(position));

                    startActivity(videoPlayer);
                }

                if (archiveData.get(position).getType().equals(ArchiveItem.TYPE_ALBUM)) {
                    Intent photoAlbum = new Intent(getActivity(), PhotoalbumActivity.class);
                    photoAlbum.putExtra(ArchiveItem.ARCHIVE_ITEM, archiveData.get(position));

                    startActivity(photoAlbum);
                }
            }
        });

        archiveGrid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int currentFirstVisPos = view.getFirstVisiblePosition();
                if(currentFirstVisPos > myLastVisiblePos) {
                    actionBar.hide();
                }
                if(currentFirstVisPos < myLastVisiblePos) {
                    actionBar.show();
                }
                myLastVisiblePos = currentFirstVisPos;
            }
        });
    }

    private void getUI(View rootView) {
        archiveGrid = (GridView) rootView.findViewById(R.id.archiveGrid);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
    }

    @Override
    public void onSuccessResponse(JSONObject response, RequestType requestType) {
        SmartLog.log("ArchiveFragment response", String.valueOf(response));

        switch (requestType) {
            case ARCHIVE:
                archiveData = ResponseFactory.parseArchive(response);
                archiveAdapter = new ArchiveAdapter(context, R.layout.archive_block, archiveData);
                archiveGrid.setAdapter(archiveAdapter);
                progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onErrorResponse(VolleyError exception) {
        Toast.makeText(getActivity(), getString(R.string.something_went_wrong),
                Toast.LENGTH_SHORT).show();
        SmartLog.log("errorResponse", String.valueOf(exception));
    }
}