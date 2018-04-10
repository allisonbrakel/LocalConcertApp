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
 * Created by Allison on 2018-03-09.
 */

public class CustomListView extends ArrayAdapter<String> {
    private ArrayList<String> title;
    private ArrayList<String> pubDate;
    private Activity context;
    private int fontSize = 14;

    public CustomListView(Activity context, ArrayList<String> title, ArrayList<String> pubDate, int fontSize) {
        super(context, R.layout.simplerow, title);

        this.title = title;
        this.context = context;
        this.pubDate = pubDate;
        this.fontSize = fontSize;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null){
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.simplerow, null, true);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder)convertView.getTag();
        }

        vh.txtTitle.setText(title.get(position));
        vh.txtTitle.setTextSize(fontSize);
        if (pubDate == null){
            vh.txtPubDate.setText("");
        } else {
            vh.txtPubDate.setText(pubDate.get(position));
        }
        vh.txtPubDate.setTextSize(fontSize-3);
        return convertView;
    }

    class ViewHolder {
        TextView txtTitle;
        TextView txtPubDate;

        ViewHolder (View v){
            this.txtTitle = v.findViewById(R.id.lvTitle);
            this.txtPubDate = v.findViewById(R.id.lvPubDate);
        }
    }
}
