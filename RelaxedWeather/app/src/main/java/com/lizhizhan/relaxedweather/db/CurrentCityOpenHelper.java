package com.lizhizhan.relaxedweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CurrentCityOpenHelper extends SQLiteOpenHelper {

    public CurrentCityOpenHelper(Context context) {
        super(context, "applock.db", null, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table currentcityinfo (_id integer primary key autoincrement,cityname varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
