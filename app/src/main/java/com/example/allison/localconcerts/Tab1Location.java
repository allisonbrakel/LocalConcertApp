package com.example.allison.localconcerts;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    public static void getData(){
        item = helper.getItem();
        item.remove(0);

        ArrayList<String> titleList = new ArrayList<String>();
        ArrayList<String> dateList = new ArrayList<String>();

        for(APIHelper.ListItem i: item){
            titleList.add(i.getTitle());
            dateList.add(i.getPubDate());
        }

        CustomListView customListView = new CustomListView(currentActivity, titleList, dateList, 18);

        mainListView.setAdapter( customListView );
    }

}
