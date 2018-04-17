package com.example.allison.localconcerts;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Allison on 2018-04-03.
 */

public class BandListView  extends ArrayAdapter<String> {
    private ArrayList<String> venue;
    private ArrayList<String> date;
    private ArrayList<String> city;
    private ArrayList<String> country;
    private Activity context;
    private int fontSize;

    public BandListView(Activity context, ArrayList<String> venue, ArrayList<String> date,
                        ArrayList<String> city, ArrayList<String> country, int fontSize) {
        super(context, R.layout.bandrow, venue);

        this.context = context;
        this.venue = venue;
        this.city = city;
        this.country = country;
        this.date = date;
        this.fontSize = fontSize;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null){
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.bandrow, null, true);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder)convertView.getTag();
        }

        vh.txtVenue.setText(venue.get(position));
        vh.txtVenue.setTextSize(fontSize);
        vh.txtDate.setText(date.get(position));
        vh.txtDate.setTextSize(fontSize-3);
        vh.txtCity.setText(city.get(position));
        vh.txtCity.setTextSize(fontSize-3);
        vh.txtCountry.setText(country.get(position));
        vh.txtCountry.setTextSize(fontSize-3);

        return convertView;
    }

    class ViewHolder {
        TextView txtVenue;
        TextView txtDate;
        TextView txtCity;
        TextView txtCountry;

        ViewHolder (View v){
            this.txtVenue = v.findViewById(R.id.lvVenue);
            this.txtDate = v.findViewById(R.id.lvDate);
            this.txtCity = v.findViewById(R.id.lvCity);
            this.txtCountry = v.findViewById(R.id.lvCountry);

        }
    }
}
