package com.example.allison.localconcerts;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class VenueActivity extends AppCompatActivity {
    static APIHelper helper;
    static ArrayList<APIHelper.ListItem> item = null;
    static Activity currentActivity;
    static private ListView mainListView ;
    private String venueName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);

        Intent i = getIntent();
        venueName = i.getStringExtra("venue_name");
        mainListView = findViewById(R.id.venueListView);
        TextView txtTitle = findViewById(R.id.txtViewTitle);
        txtTitle.setText(venueName);

        helper = (APIHelper)(getApplicationContext());
        helper.setCurrentUrl("http://acousti.co/feeds/venue/" + venueName);
        currentActivity = this;
        helper.setCurrentActivity(this);
        helper.sendRequest();
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
