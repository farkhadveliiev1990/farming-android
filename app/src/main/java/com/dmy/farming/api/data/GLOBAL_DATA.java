
package com.dmy.farming.api.data;

import android.content.Context;
import android.content.SharedPreferences;

public class GLOBAL_DATA
{
    public double currLat;
    public double currLon;

    public String currProvince;
    public String currCity;
    public String currDistrict;
    public String currStreet;

    public String currFullAddr;

    private static GLOBAL_DATA instance;
    public static GLOBAL_DATA getInstance(Context context)
    {
        if (instance == null) {
            instance = new GLOBAL_DATA();
            instance.loadData(context);
        }

        return instance;
    }

    private void loadData(Context context)
    {
        SharedPreferences shared = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        currLat = shared.getFloat("currLat", 39.915f);
        currLon = shared.getFloat("currLon", 116.404f);

        currProvince = shared.getString("currProvince", "北京");
        currCity = shared.getString("currCity", "北京");
        currDistrict = shared.getString("currDistrict", "朝阳区");
        currStreet = shared.getString("currStreet", "建国门");

        currFullAddr = shared.getString("currFullAddr", "中国北京市朝阳区建国门");
    }

    public void saveData(Context context)
    {
        SharedPreferences shared = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putFloat("currLat", (float) currLat);
        editor.putFloat("currLon", (float) currLon);

        editor.putString("currProvince", currProvince);
        editor.putString("currCity", currCity);
        editor.putString("currDistrict", currDistrict);
        editor.putString("currStreet", currStreet);

        editor.putString("currFullAddr", currFullAddr);
        editor.commit();
    }
}
