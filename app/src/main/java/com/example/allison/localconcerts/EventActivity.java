package com.example.allison.localconcerts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EventActivity extends AppCompatActivity {
    String artist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent i = getIntent();
        artist = i.getStringExtra("artist");

        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText(artist + ": Upcoming Events");

        OkHttpHandler okHttpHandler= new OkHttpHandler();
        okHttpHandler.execute("https://rest.bandsintown.com/artists/" + artist + "/events?app_id=allison%27");

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
            parseResponse(o.toString());
        }
    }

    private void parseResponse(String response) {
        try{
            JSONArray jsonArray = new JSONArray(response);
            ArrayList<String> venueList = new ArrayList<String>();
            ArrayList<String> dateList = new ArrayList<String>();
            ArrayList<String> cityList = new ArrayList<String>();
            ArrayList<String> countryList = new ArrayList<String>();

            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            int maxItems = Integer.valueOf(SP.getString("numItems", "10"));
            int fontSize = Integer.valueOf(SP.getString("fontSize", "18"));

            for (int i = 0; (i < jsonArray.length() && i < maxItems); i++){
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                JSONObject venueObject = jsonObject.getJSONObject("venue");
                venueList.add(venueObject.getString("name"));
                dateList.add(jsonObject.getString("datetime"));
                cityList.add(venueObject.getString("city"));
                countryList.add(venueObject.getString("country"));
            }
            BandListView bandListView = new BandListView(this, venueList, dateList, cityList, countryList, fontSize);
            ListView listView = findViewById(R.id.bandListView);
            listView.setAdapter(bandListView);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
