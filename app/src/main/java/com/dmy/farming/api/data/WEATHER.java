package com.dmy.farming.api.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by victory1989 on 2017/8/22.
 */

public class WEATHER {
    public String aqi;
    public String city;
    public String curDate;
    public String currentTemp;
    public String dayCond;  //晴
    public Long lastUpdateTime = 0L;
    public String maxTemp;
    public String minTemp;
    public String qlty;
    public String windDir; //风向
    public String windSc;  //风力

    public void fromJson(JSONObject jsonObject) throws JSONException
    {
        if (jsonObject == null) {
            return;
        }
//        this.aqi = jsonObject.optString("aqi");
//        this.qlty = jsonObject.optString("qlty");
//        this.city = jsonObject.optString("city");
//        this.currentTemp = jsonObject.optString("currentTmp");
//        this.windDir = jsonObject.optString("windDir");
//        this.windSc = jsonObject.optString("windSc");
//        this.maxTemp = jsonObject.optString("maxTmp");
//        this.minTemp = jsonObject.optString("minTmp");
//        this.dayCond = jsonObject.optString("dayCond");
//        this.curDate = jsonObject.optString("curDate");
//        this.lastUpdateTime = jsonObject.optLong("lastUpdateTime");

        this.aqi = jsonObject.optString("aqi");
        this.currentTemp = jsonObject.optString("wendu");
        this.city = jsonObject.optString("city");

        JSONArray jsonArray = jsonObject.optJSONArray("forecast");
        if(null != jsonArray)
        {
            JSONObject itemObject = jsonArray.getJSONObject(0);
            this.windDir = itemObject.optString("fengxiang");
            String fengli= itemObject.optString("fengli");
            this.windSc = fengli.substring(fengli.length()-5,fengli.length()-3);
            this.maxTemp = itemObject.optString("high");
            this.minTemp = itemObject.optString("low");
            this.dayCond = itemObject.optString("type");
        }

    }

    public JSONObject toJson() throws JSONException
    {
        JSONObject localJSONObject = new JSONObject();
        localJSONObject.put("aqi", this.aqi);
        localJSONObject.put("qlty", this.qlty);
        localJSONObject.put("city", this.city);
        localJSONObject.put("currentTmp", this.currentTemp);
        localJSONObject.put("windDir", this.windDir);
        localJSONObject.put("windSc", this.windSc);
        localJSONObject.put("maxTmp", this.maxTemp);
        localJSONObject.put("minTmp", this.minTemp);
        localJSONObject.put("dayCond", this.dayCond);
        localJSONObject.put("curDate", this.curDate);
        localJSONObject.put("lastUpdateTime", this.lastUpdateTime);
        return localJSONObject;
    }
}
