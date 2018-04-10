package com.example.allison.localconcerts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Allison on 2018-04-02.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //Define your database name
    private static final String DB_NAME="Concert";

    //Define your table name
    private static final String BAND_TABLE_NAME="Band";
    private static final String EVENT_TABLE_NAME="Event";

    //Create constants defining your column names
    private static final String COL_NAME="Name";
    private static final String COL_VENUE="Venue";
    private static final String COL_DATE="Date";
    private static final String COL_CITY="City";
    private static final String COL_COUNTRY="Country";
    private static final String COL_FK="Band_ID";

    //Define the database version
    private static final int DB_VERSION = 2;

    //Define your create statement in typical sql format
    //CREATE TABLE {Tablename} (
    //Colname coltype
    //)
    private static final String BAND_TABLE_CREATE =
            "CREATE TABLE " + BAND_TABLE_NAME + " (" +
                    COL_NAME + " TEXT NOT NULL," +
                    "UNIQUE(Name)); ";
    private static final String EVENT_TABLE_CREATE =
            "CREATE TABLE " + EVENT_TABLE_NAME + " (" +
                    COL_VENUE + " TEXT NOT NULL," +
                    COL_DATE + " TEXT NOT NULL," +
                    COL_CITY + " TEXT NOT NULL," +
                    COL_COUNTRY + " TEXT NOT NULL," +
                    COL_FK + " INTEGER NOT NULL);";

    //Drop table statement
    private static final String BAND_DROP_TABLE = "DROP TABLE IF EXISTS " + BAND_TABLE_NAME;
    private static final String EVENT_DROP_TABLE = "DROP TABLE IF EXISTS " + EVENT_TABLE_NAME;

    //constructor
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        // TODO Auto-generated constructor stub
    }

    //when you create the class, create the table
    @Override
    public void onCreate(SQLiteDatabase db) {
        // execute the create table code
        db.execSQL(BAND_TABLE_CREATE);
        db.execSQL(EVENT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //drop the table and recreate it
        db.execSQL(EVENT_DROP_TABLE);
        db.execSQL(BAND_DROP_TABLE);
        onCreate(db);
    }

    public void dropTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(EVENT_DROP_TABLE);
        db.execSQL(BAND_DROP_TABLE);
        onCreate(db);
    }

    public boolean saveBandExec(String name)
    {
        boolean success = true;
        //Open your writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //Formulate your statement
        String insertStatement = "INSERT INTO 'Band' VALUES('" + name +"');";

        try {
            //Execute your statement
            db.execSQL(insertStatement);
        } catch (Exception e){
            success = false;
        } finally {
            db.close();

            return success;
        }
    }

    public boolean saveEventExec(String venue, String date, String city, String country, int bandFK)
    {
        boolean success = true;
        //Open your writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //Formulate your statement
        String insertStatement = "INSERT INTO 'Event' VALUES('" + venue +"'," +
                "'" + date + "', " +
                "'" + city + "', " +
                "'" + country + "', " +
                "'" + bandFK + "');";

        try {
            //Execute your statement
            db.execSQL(insertStatement);
        } catch (Exception e){
            success = false;
        } finally {
            db.close();

            return success;
        }
    }

    //Load the data in the table
    public ArrayList<String> loadBandData(String[] selection){

        ArrayList<String> nameData = new ArrayList<String>();
        //open the readable database
        SQLiteDatabase db = this.getReadableDatabase();
        //create an array of the table names
        //Create a cursor item for querying the database
        Cursor c = db.query(BAND_TABLE_NAME,	//The name of the table to query
                selection,				//The columns to return
                null,					//The columns for the where clause
                null,					//The values for the where clause
                null,					//Group the rows
                null,					//Filter the row groups
                null);					//The sort order



        //Move to the first row
        c.moveToFirst();

        //For each row that was retrieved
        for(int i=0; i < c.getCount(); i++)
        {
            //assign the value to the corresponding array
            nameData.add(c.getString(0));
            c.moveToNext();
        }

        //close the cursor
        c.close();
        //close the database
        db.close();

        return nameData;
    }


    //Load the data in the table
    public ArrayList<String> loadEventData(int id){

        ArrayList<String> nameData = new ArrayList<String>();
        //open the readable database
        SQLiteDatabase db = this.getReadableDatabase();
        //create an array of the table names
        String[] selection = {"venue",
                              "date",
                              "city",
                              "country"};
        String whereClause = "band_id == ?";
        String [] whereArgs = {id + ""};

        //Create a cursor item for querying the database
        Cursor c = db.query(EVENT_TABLE_NAME,	//The name of the table to query
                selection,				//The columns to return
                whereClause,					//The columns for the where clause
                whereArgs,					//The values for the where clause
                null,					//Group the rows
                null,					//Filter the row groups
                null);					//The sort order



        //Move to the first row
        c.moveToFirst();

        //For each row that was retrieved
        for(int i=0; i < c.getCount(); i++)
        {
            //assign the value to the corresponding array
            nameData.add(c.getString(0));
            nameData.add(c.getString(1));
            nameData.add(c.getString(2));
            nameData.add(c.getString(3));
            c.moveToNext();
        }

        //close the cursor
        c.close();
        //close the database
        db.close();

        return nameData;
    }

    //This method is used to load the data from the table into a hash map
    //this enables the use of multiple textviews in the listview
    public List<Map<String,String>> loadData2()
    {
        List<Map<String,String>> lm = new ArrayList<Map<String,String>>();

        //open the readable database
        SQLiteDatabase db = this.getReadableDatabase();
        //create an array of the table names
        String[] selection = {COL_NAME};
        //Create a cursor item for querying the database
        Cursor c = db.query(BAND_TABLE_NAME,	//The name of the table to query
                selection,				//The columns to return
                null,					//The columns for the where clause
                null,					//The values for the where clause
                null,					//Group the rows
                null,					//Filter the row groups
                null);					//The sort order



        //Move to the first row
        c.moveToFirst();

        //For each row that was retrieved
        for(int i=0; i < c.getCount(); i++)
        {
            Map<String,String> map = new HashMap<String,String>();
            //assign the value to the corresponding array
            map.put("Name", c.getString(0));

            lm.add(map);
            c.moveToNext();
        }

        //close the cursor
        c.close();
        //close the database
        db.close();
        return lm;

    }

    public int getBandId(String name){
        //open the readable database
        SQLiteDatabase db = this.getReadableDatabase();
        //create an array of the table names
        String[] selection = {"rowid",
                              "name"};

        String whereClause = "name == ?";
        String [] whereArgs = {name};
        //Create a cursor item for querying the database
        Cursor c = db.query(BAND_TABLE_NAME,	//The name of the table to query
                selection,				//The columns to return
                whereClause,					//The columns for the where clause
                whereArgs,					//The values for the where clause
                null,					//Group the rows
                null,					//Filter the row groups
                null);					//The sort order



        //Move to the first row
        c.moveToFirst();

        int id = c.getInt(0);

        //close the cursor
        c.close();
        //close the database
        db.close();

        return id;
    }

}
