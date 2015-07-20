package com.chaemil.hgms.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.chaemil.hgms.R;
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
    }

    private void setupUI() {

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