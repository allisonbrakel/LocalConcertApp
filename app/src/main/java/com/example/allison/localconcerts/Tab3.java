package com.example.allison.localconcerts;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Allison on 2018-03-11.
 */

public class Tab3 extends Fragment {
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3, container, false);

        listView = rootView.findViewById(R.id.tab3ListView);

        displayListView();
        return rootView;
    }

    private void displayListView() {
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        String [] selection = {"name"};
        ArrayList<String> bands = dbHelper.loadBandData(selection);
        CustomListView customListView = new CustomListView(getActivity(), bands, null, 18);

        listView.setAdapter( customListView );
    }
}
