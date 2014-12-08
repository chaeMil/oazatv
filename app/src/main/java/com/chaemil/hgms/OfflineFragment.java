package com.chaemil.hgms;

import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chaemil.hgms.db.AudioDBHelper;
import com.chaemil.hgms.utils.Utils;

import at.markushi.ui.CircleButton;

import static com.chaemil.hgms.utils.Utils.setActionStatusBarTint;

/**
 * Created by chaemil on 8.12.14.
 */
public class OfflineFragment extends Fragment {

    public OfflineFragment() {

    }

    private TextView homeNetworkProblemText;
    private CircleButton refreshButton;
    private CircleButton goToAudioPlayer;

    public void refresh() {
        getActivity().finish();
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_offline, container, false);

        homeNetworkProblemText = (TextView) rootView.findViewById(R.id.homeNetworkProblemText);
        refreshButton = (CircleButton) rootView.findViewById(R.id.refresh);
        goToAudioPlayer = (CircleButton) rootView.findViewById(R.id.goToAudioPlayer);

        setActionStatusBarTint(getActivity().getWindow(),getActivity(), "#000000", "#550000");

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });

        goToAudioPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),ListDownloadedAudio.class);
                startActivity(i);
            }
        });

        AudioDBHelper helper = new AudioDBHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        if (AudioDBHelper.count(db) > 0) {
            homeNetworkProblemText.setText(Utils.getStringWithRegularCustomFont(getActivity(),
                    getResources().getString(R.string.offline_but_have_audio), "Titillium-BoldUpright.otf"));
        }
        else {
            homeNetworkProblemText.setText(Utils.getStringWithRegularCustomFont(getActivity(),
                    getResources().getString(R.string.offline_but_have_no_audio), "Titillium-BoldUpright.otf"));
            goToAudioPlayer.setVisibility(View.GONE);
        }

        return rootView;
    }
}
