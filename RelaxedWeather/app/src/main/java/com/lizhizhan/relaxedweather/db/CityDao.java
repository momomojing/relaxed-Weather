package com.lizhizhan.relaxedweather.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.lizhizhan.relaxedweather.bean.CityInfos;
import com.lizhizhan.relaxedweather.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhizhan on 2017/1/15.
 */

public class CityDao {
    private static String PATH = "data/data/com.lizhizhan.relaxedweather/files/city.db";


    /**
     * 查询所有
     */
    public List<CityInfos> findAll(String cityName) {
        //        cityName="田";
        String newCityname = parseName(cityName);
        List<CityInfos> cityInfoses = new ArrayList<CityInfos>();
        SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
        if (!StringUtils.isEmpty(newCityname)) {
                        String sql = "SELECT * FROM city WHERE city LIKE '%" + newCityname + "%'";
//            String sql = "SELECT * FROM city WHERE city LIKE '%州%'";
            Cursor cursor = database.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                String province = cursor.getString(1);
                String city = cursor.getString(2);
                String firstpy = cursor.getString(6);

                Bundle extras = cursor.getExtras();
                boolean xxx = extras.getBoolean("XXX");

                CityInfos cityInfos = new CityInfos();
                cityInfos.setFirstpy(firstpy);
                cityInfos.setProvince(province);
                cityInfos.setCity(city);
                cityInfoses.add(cityInfos);
            }
            cursor.close();
            database.close();
            //SystemClock.sleep(3000);
            return cityInfoses;
        }
        return null;

    }

    /**
     * 去掉市或县搜索
     *
     * @param city
     * @return
     */
    private String parseName(String city) {
        if (city.contains("市")) {// 如果为空就去掉市字再试试
            String subStr[] = city.split("市");
            city = subStr[0];
        } else if (city.contains("县")) {// 或者去掉县字再试试
            String subStr[] = city.split("县");
            city = subStr[0];
        }
        return city;
    }
}
