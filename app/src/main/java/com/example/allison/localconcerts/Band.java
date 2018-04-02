package com.example.allison.localconcerts;

/**
 * Created by Allison on 2018-04-02.
 */

public class Band {
    private int id;
    private String name;

    // Empty constructor
    public Band (){    }

    public Band (int id, String name){
        this.id  = id;
        this.name = name;
    }

    public String getName (){
        return this.name;
    }

    public int getId(){
        return this.id;
    }
}
