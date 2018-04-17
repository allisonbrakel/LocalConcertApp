package com.example.allison.localconcerts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Allison on 2018-03-11.
 */

public class Tab3 extends Fragment {
    ListView listView;
    SharedPreferences SP;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3, container, false);

        listView = rootView.findViewById(R.id.tab3ListView);

        Button btnAdd = rootView.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Search_Activity.class);

                startActivity(i);
            }
        });

        displayListView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        displayListView();
    }

    private void displayListView() {
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        String [] selection = {"name"};
        final ArrayList<String> bands = dbHelper.loadBandData(selection);

        SP = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        int fontSize = Integer.valueOf(SP.getString("fontSize", "18"));

        CustomListView customListView = new CustomListView(getActivity(), bands, null, fontSize);

        listView.setAdapter( customListView );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(getActivity(), BandActivity.class);
                i.putExtra("band_name", bands.get(position));
                startActivity(i);
            }
        });
    }
}
