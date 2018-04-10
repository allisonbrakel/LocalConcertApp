package com.example.allison.localconcerts;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Search_Activity extends Activity {
    Button btnSearch;
    Button btnLink;
    String searchCriteria;
    EditText etSearch;
    private DatabaseHelper dbHelper;
    private String currentURL = "https://rest.bandsintown.com/artists/" + searchCriteria + "?app_id=allison%27";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btnSearch = findViewById(R.id.btnSearch);
        etSearch = findViewById(R.id.etSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

    }

    private void performSearch() {
        searchCriteria = etSearch.getText().toString();
        Log.d("Search", searchCriteria);
        OkHttpHandler okHttpHandler= new OkHttpHandler();
        currentURL = "https://rest.bandsintown.com/artists/" + searchCriteria + "?app_id=allison%27";
        okHttpHandler.execute(currentURL);
    }

    public class OkHttpHandler extends AsyncTask {
        // Client used to send request
        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(Object[] params) {
            // Building the request before sending
            Request.Builder builder = new Request.Builder();
            // Sets the api url
            builder.url(params[0].toString());
            Request request = builder.build();

            try {
                // Set the response by executing the request
                Response response = client.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(currentURL.contains("events")){
                parseJSONResponse(o.toString());
            } else {
                parseResponse(o.toString());
            }
        }
    }

    private void parseResponse(String response) {
        try{
            JSONObject json = new JSONObject(response);
            TextView tvName = findViewById(R.id.artistName);
            TextView tvEvents = findViewById(R.id.eventCount);
            ImageView imageView = findViewById(R.id.imageView);
            btnLink = findViewById(R.id.btnLink);
            Button btnFavourites = findViewById(R.id.btnFavourites);

            Picasso.get().load(json.getString("image_url")).resize(500, 600).into(imageView);
            tvName.setText("Artist: " + json.getString("name"));
            tvEvents.setText("Upcoming events: " + json.getString("upcoming_event_count"));

            tvName.setVisibility(View.VISIBLE);
            tvEvents.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            btnLink.setVisibility(View.VISIBLE);
            btnFavourites.setVisibility(View.VISIBLE);

            btnLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Search_Activity.this, EventActivity.class);
                    i.putExtra("artist", searchCriteria);
                    startActivity(i);
                }
            });
            
            btnFavourites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addBandToFavourites();
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void addBandToFavourites() {
        dbHelper = new DatabaseHelper(this);

        boolean success = dbHelper.saveBandExec(searchCriteria);

        if (!success){
            Toast.makeText(this, "Already in your favourites!",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Successfully added to favourites!",
                    Toast.LENGTH_LONG).show();
            OkHttpHandler okHttpHandler= new OkHttpHandler();
            currentURL = "https://rest.bandsintown.com/artists/" + searchCriteria + "/events?app_id=allison%27";
            okHttpHandler.execute(currentURL);
        }

        // load data to the logcat as a test
        String [] selection = {"rowid"};
        ArrayList<String> bands = dbHelper.loadBandData(selection);

        for (String band: bands){
            Log.d("bands", band);
        }

    }

    private void parseJSONResponse(String response) {
        try{
            JSONArray jsonArray = new JSONArray(response);

            int id = dbHelper.getBandId(searchCriteria);

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                JSONObject venueObject = jsonObject.getJSONObject("venue");

                String venue = venueObject.getString("name");
                String date = jsonObject.getString("datetime");
                String city = venueObject.getString("city");
                String country = venueObject.getString("country");
                Log.d("Event", venue);

                dbHelper.saveEventExec(venue, date, city, country, id);
            }

            // load data to the logcat as a test
            ArrayList<String> events = dbHelper.loadEventData(id);

            for (String event: events){
                Log.d("event", event);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
