package com.example.allison.localconcerts;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;

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


/**
 * Created by Allison on 2018-04-10.
 */

public class APIHelper extends Application {
    private ArrayList<ListItem> item = null;
    private String currentUrl="http://acousti.co/feeds/metro_area/Winnipeg";
    private Activity currentActivity;

    // Has a bunch of methods you can override
    // https://developer.android.com/reference/android/app/Application.html
    @Override
    public void onCreate() {
        super.onCreate();
        this.sendRequest();
    }

    public ArrayList<ListItem> getItem(){
        return  item;
    }

    public void setCurrentUrl(String currentUrl){
        this.currentUrl = currentUrl;
    }

    public void sendRequest()  {
        RSSParsingTask task = new RSSParsingTask();
        task.execute();
    }

    class RSSParsingTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SAXParser saxParser = null;
            try {
                saxParser = SAXParserFactory.newInstance().newSAXParser();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

            URL url = null;

            try {
                url = new URL(currentUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                inputStream = httpURLConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FeedHandler li = new FeedHandler();
            try {
                saxParser.parse(inputStream, li);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            item.remove(0);


            // Finds the name of the current activity
            String name = currentActivity.getLocalClassName().toString();

            // Uses the current activity name to trigger get data method
            // **********************************************************
            // Get data method makes the data from the api call available
            // in the activity.
            switch(name){
                case "TabbedActivity":
                    Tab1Location.getData();
                    break;
                case "VenueActivity":
                    VenueActivity.getData();
                    break;
            }
        }

    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }


    class FeedHandler extends DefaultHandler {
        private boolean inTitle, inDescription, inPubDate, inLink;

        private StringBuilder stringBuilder;

        // Initialization block
        {
            item = new ArrayList<ListItem>(10);

        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);

            stringBuilder = new StringBuilder(30);

            if(qName.equals("title")) {
                inTitle = true;
            } else if(qName.equals("description")){
                inDescription = true;
            } else if (qName.equals("pubDate")){
                inPubDate = true;
            } else if (qName.equals("link")){
                inLink = true;
            }

        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            ListItem i;

            if (qName.equals("title")) {
                inTitle = false;
                item.add(new ListItem());
                i = item.get(item.size() - 1);
                i.setTitle(stringBuilder.toString());

            } else {
                i = item.get(item.size() - 1);
                if (qName.equals("description")) {
                    inDescription = false;
                    i.setDescription(stringBuilder.toString());
                } else if (qName.equals("pubDate")) {
                    inPubDate = false;
                    i.setPubDate(stringBuilder.toString());
                } else if (qName.equals("link")) {
                    inLink = false;
                    i.setLink(stringBuilder.toString());
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);

            if(inTitle || inDescription || inPubDate || inLink) {
                stringBuilder.append(ch, start, length);
            }

        }
    }

    class ListItem {
        private String title;
        private String description;
        private String pubDate;
        private String link;

        public void setTitle(String title){
            this.title = title;
        }

        public void setDescription(String description){
            this.description = description;
        }

        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getTitle() {
            return title;
        }

        public String getPubDate() {
            return pubDate;
        }


    }
}

