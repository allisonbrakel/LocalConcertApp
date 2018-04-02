package com.example.allison.localconcerts;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Allison on 2018-03-11.
 */

public class Tab2Venue extends Fragment {
    ListView mainListView;
    String [] venueArray = {"Bell MTS Place", "Burton Cummings Theatre", "The Garrick Center", "The Park Theatre"};
    ArrayList<String> venues = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2venue, container, false);
        mainListView = rootView.findViewById(R.id.tab2ListView);
        setData();
        return rootView;
    }

    private void setData() {
        // Currently hardcoded - will eventually be pulled from the database
        // that will be set during the API call in tab 1.
        venues.add("Bell MTS Place");
        venues.add("Burton Cummings Theatre");
        venues.add("The Garrick Center");
        venues.add("The Park Theatre");
        ArrayList<String> addresses = new ArrayList<String>();
        addresses.add("300 Portage Ave, Winnipeg, MB R3C 5S4");
        addresses.add("364 Smith St, Winnipeg, MB R3B 2H2");
        addresses.add("330 Garry St, Winnipeg, MB R3B 2G7");
        addresses.add("698 Osborne St, Winnipeg, MB R3L 2B9");

        CustomListView customListView = new CustomListView(getActivity(), venues, addresses, 18);

        mainListView.setAdapter( customListView );

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(getActivity(), VenueActivity.class);
                i.putExtra("venue_name", venues.get(position));
                startActivity(i);
            }
        });

    }

}
