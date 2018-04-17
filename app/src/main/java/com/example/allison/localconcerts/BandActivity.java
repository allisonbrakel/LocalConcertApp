package com.example.allison.localconcerts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BandActivity extends AppCompatActivity {
    String bandName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band);
        Intent i = getIntent();
        bandName = i.getStringExtra("band_name");
        Log.d("Band Name", bandName);
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText(bandName + ": Upcoming Events");

        displayBandEvents();
    }

    private void displayBandEvents() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int bandId = dbHelper.getBandId(bandName);
        ArrayList<String> events = dbHelper.loadEventData(bandId);
        ArrayList<String> venue = new ArrayList<String>();
        ArrayList<String> date = new ArrayList<String>();
        ArrayList<String> city = new ArrayList<String>();
        ArrayList<String> country = new ArrayList<String>();

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int maxItems = Integer.valueOf(SP.getString("numItems", "10"));
        int fontSize = Integer.valueOf(SP.getString("fontSize", "10"));
        int count = 0;

        for (int i = 0; i < events.size(); i +=4 ){
            if (count < maxItems) {
                venue.add(events.get(i));
                date.add(events.get(i + 1));
                city.add(events.get(i + 2));
                country.add(events.get(i + 3));
                count++;
            }
        }

        // set list view
        try {
            BandListView bandListView = new BandListView(this, venue, date, city, country, fontSize);
            ListView listView = findViewById(R.id.bandListView);
            listView.setAdapter(bandListView);
        } catch (Exception e){
            Toast.makeText(this, "There was a problem displaying the list view.", Toast.LENGTH_SHORT).show();
        }
    }
}