package com.example.allison.localconcerts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Allison on 2018-03-11.
 */

public class Tab2Venue extends Fragment {
    ListView mainListView;
    String [] venueArray = {"Bell MTS Place", "Burton Cummings Theatre", "The Garrick Center", "The Park Theatre"};
    ArrayList<String> venues = new ArrayList<String>();
    ArrayList<String> addresses = new ArrayList<String>();
    SharedPreferences SP;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2venue, container, false);
        mainListView = rootView.findViewById(R.id.tab2ListView);
        setData();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        resetData();
    }

    private void resetData() {
       venues.clear();
       addresses.clear();
       setData();
    }

    private void setData() {
        venues.add("Bell MTS Place");
        venues.add("Burton Cummings Theatre");
        venues.add("The Garrick Center");
        venues.add("The Park Theatre");
        addresses.add("300 Portage Ave, Winnipeg, MB R3C 5S4");
        addresses.add("364 Smith St, Winnipeg, MB R3B 2H2");
        addresses.add("330 Garry St, Winnipeg, MB R3B 2G7");
        addresses.add("698 Osborne St, Winnipeg, MB R3L 2B9");

        SP = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        int fontSize = Integer.valueOf(SP.getString("fontSize", "18"));

        try {

            CustomListView customListView = new CustomListView(getActivity(), venues, addresses, fontSize);

            mainListView.setAdapter(customListView);

            mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Intent i = new Intent(getActivity(), VenueActivity.class);
                    i.putExtra("venue_name", venues.get(position));
                    startActivity(i);
                }
            });
        } catch (Exception e){
            Toast.makeText(getActivity(), "There was a problem displaying the list view.", Toast.LENGTH_SHORT).show();
        }

    }

}
