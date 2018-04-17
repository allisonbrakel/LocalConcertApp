package com.example.allison.localconcerts;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Created by Allison on 2018-03-11.
 */

public class Tab1Location extends Fragment {
    static private ListView mainListView ;
    static APIHelper helper;
    static ArrayList<APIHelper.ListItem> item = null;
    static Activity currentActivity;
    static SharedPreferences SP;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1location, container, false);

        mainListView = rootView.findViewById(R.id.tab1ListView);

        helper = (APIHelper)(getActivity().getApplicationContext());
        currentActivity = getActivity();

        helper.setCurrentActivity(getActivity());
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        helper.setCurrentUrl("http://acousti.co/feeds/metro_area/Winnipeg");
        helper.sendRequest();
    }

    public static void getData(){
        item = helper.getItem();

        ArrayList<String> titleList = new ArrayList<String>();
        ArrayList<String> dateList = new ArrayList<String>();

        SP = PreferenceManager.getDefaultSharedPreferences(currentActivity.getBaseContext());
        int maxItems = Integer.valueOf(SP.getString("numItems", "10"));
        int fontSize = Integer.valueOf(SP.getString("fontSize", "18"));

        int count = 0;
        for(APIHelper.ListItem i: item){
            if (count < maxItems) {
                titleList.add(i.getTitle());
                dateList.add(i.getPubDate());
                count++;
            }
        }

        CustomListView customListView = new CustomListView(currentActivity, titleList, dateList, fontSize);

        mainListView.setAdapter( customListView );
    }

}
