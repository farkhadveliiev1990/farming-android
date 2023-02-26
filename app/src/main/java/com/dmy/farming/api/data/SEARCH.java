
package com.dmy.farming.api.data;

import android.content.Context;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class SEARCH
{
    public String title;
    public String titlePicurl;

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.title = jsonObject.optString("title");
        this.titlePicurl = jsonObject.optString("titlePicurl");

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("title", title);
        localItemObject.put("titlePicurl", titlePicurl);

        return localItemObject;
    }
}
