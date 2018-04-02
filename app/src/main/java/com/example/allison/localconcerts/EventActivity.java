package com.example.allison.localconcerts;

import android.content.Intent;
import android.os.AsyncTask;
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
    private ListView mainListView ;
    private ArrayList<ListItem> item = new ArrayList<>();
    CustomListView customListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent i = getIntent();
        artist = i.getStringExtra("artist");

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

            for (int i = 0; i < jsonArray.length(); i++){
                Log.d("Array", jsonArray.get(i).toString());
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                JSONObject venueObject = jsonObject.getJSONObject("venue");
                Log.d("Object", venueObject.getString("name"));
                ListItem li = new ListItem();
                li.setVenue(venueObject.getString("name"));
                li.setDate(jsonObject.getString("datetime"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class ListItem {
        private String venue;
        private String date;

        public void setVenue(String venue){
            this.venue = venue;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getVenue() {
            return venue;
        }

        public String getDate() {
            return date;
        }

    }
}
