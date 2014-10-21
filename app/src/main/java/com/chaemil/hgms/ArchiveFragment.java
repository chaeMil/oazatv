package com.chaemil.hgms;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.chaemil.hgms.Adapters.ArchiveAdapter;
import com.chaemil.hgms.Utils.Utils;
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