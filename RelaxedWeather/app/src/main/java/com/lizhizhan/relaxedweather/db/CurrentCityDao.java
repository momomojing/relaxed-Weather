package com.lizhizhan.relaxedweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class CurrentCityDao {
    private CurrentCityOpenHelper currentCityOpenHelper;
    private Context context;

    public CurrentCityDao(Context context) {
        this.context = context;
        currentCityOpenHelper = new CurrentCityOpenHelper(context);
    }

    /**
     * 添加
     *
     * @param cityname
     */
    public void add(String cityname) {
        SQLiteDatabase db = currentCityOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cityname", cityname);
        db.insert("currentcityinfo", null, values);
        db.close();
    }

    /**
     * 删除
     *
     * @param cityname
     */
    public void delete(String cityname) {
        SQLiteDatabase db = currentCityOpenHelper.getWritableDatabase();
        db.delete("currentcityinfo", "cityname=?", new String[]{cityname});
        db.close();
    }

    /**
     * 查询
     *
     * @param cityname
     * @return
     */
    public Boolean find(String cityname) {
        Boolean result = false;
        SQLiteDatabase db = currentCityOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("currentcityinfo", null, "cityname=?", new String[]{cityname}, null, null, null);
        if (cursor.moveToNext()) {
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 返回数量
     *
     * @return
     */
    public int getSize() {
        int i = 0;
        SQLiteDatabase db = currentCityOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from currentcityinfo", null);
        cursor.moveToNext();
        i = cursor.getInt(0);
        cursor.close();
        db.close();
        return i;
    }

    /**
     * 查询全部的城市名
     *
     * @return
     */
    public ArrayList<String> findAll() {
        SQLiteDatabase db = currentCityOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("currentcityinfo", new String[]{"cityname"}, null, null, null, null, null);
        ArrayList<String> packnames = new ArrayList<String>();
        while (cursor.moveToNext()) {
            packnames.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return packnames;
    }

}
