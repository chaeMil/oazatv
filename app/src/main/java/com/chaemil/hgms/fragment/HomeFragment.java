package com.chaemil.hgms.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.chaemil.hgms.R;
import com.chaemil.hgms.activity.VideoPlayer;
import com.chaemil.hgms.adapters.HomepageAdapter;
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

import static com.chaemil.hgms.activity.MainActivity.setActionBarTitle;

/**
 * Created by chaemil on 17.10.14.
 */
public class HomeFragment extends Fragment implements RequestFactoryListener {

    private View rootView;
    private ListView homeList;
    private ArrayList<ArchiveItem> homePageData;
    private HomepageAdapter homePageAdapter;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FrameLayout frameLayout = new FrameLayout(getActivity());
        rootView = inflater.inflate(R.layout.fragment_home, frameLayout);

        getUI(rootView);
        setupUI();
        getData();

        Utils.submitStatistics(getActivity().getApplicationContext());

        setActionBarTitle((ActionBarActivity) getActivity(), getString(R.string.app_name));

        return frameLayout;
    }

    private void getData() {
        Request homeRequest = RequestFactory.getHomePage(this);
        MyRequestService.getRequestQueue().add(homeRequest);
    }

    private void getUI(View rootView) {
        homeList = (ListView) rootView.findViewById(R.id.homeList);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
    }

    private void setupUI() {
        homeList.setDivider(new ColorDrawable(getResources().getColor(R.color.white)));
        homeList.setDividerHeight(8);
        homeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (homePageData.get(position).getType().equals(ArchiveItem.TYPE_VIDEO)) {
                    Intent videoPlayer = new Intent(getActivity(), VideoPlayer.class);
                    videoPlayer.putExtra(ArchiveItem.ARCHIVE_ITEM, homePageData.get(position));

                    SmartLog.log("position", String.valueOf(position));
                    SmartLog.log("videoUrl", homePageData.get(position).getVideoURL());

                    startActivity(videoPlayer);
                    Utils.goForwardAnimation(getActivity());
                }
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
            title = bundle.getString(Constants.BUNDLE_TITLE);
        }
        getActivity().setTitle(title);
        //getActivity().getActionBar().setTitle(title);
    }


    @Override
    public void onSuccessResponse(JSONObject response, RequestType requestType) {
        SmartLog.log("HomeFragment response", String.valueOf(response));

        switch (requestType) {
            case HOME:
                SmartLog.log("requestType", "HOME");
                homePageData = ResponseFactory.parseHomePage(response);
                homePageAdapter = new HomepageAdapter(getActivity(), homePageData, 0);
                homeList.setAdapter(homePageAdapter);
                progressBar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onErrorResponse(VolleyError exception) {
        Toast.makeText(getActivity(), getString(R.string.something_went_wrong),
                Toast.LENGTH_SHORT).show();
        SmartLog.log("errorResponse", String.valueOf(exception));
    }
}