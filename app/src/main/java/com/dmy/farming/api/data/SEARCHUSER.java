
package com.dmy.farming.api.data;

import android.content.Context;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class SEARCHUSER
{
    public long user_id;
    public String user_logo;
    public String user_name;
    public String user_rename;
    public String user_tag;
    public int user_status; // (0:需添加 1:已添加)
    public double user_pos_x;
    public double user_pos_y;

    public String getDistString(Context context) {
        double posX = GLOBAL_DATA.getInstance(context).currLon;
        double posY = GLOBAL_DATA.getInstance(context).currLat;

        if (posX > 0 && posY > 0 && user_pos_x > 0 && user_pos_y > 0) {
            LatLng from = new LatLng(posY, posX);
            LatLng to = new LatLng(user_pos_y, user_pos_x);
            double distance = (int) DistanceUtil.getDistance(from, to);

            DecimalFormat myFormatter = new DecimalFormat("#####0.0");
            if (distance < 500)
                return myFormatter.format(distance) + "m";
            else
                return myFormatter.format(distance * 0.001) + "km";
        } else {
            return "";
        }
    }

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.user_id = jsonObject.optLong("user_id");
        this.user_logo = jsonObject.optString("user_logo");
        this.user_name = jsonObject.optString("user_name");
        this.user_rename = jsonObject.optString("user_rename");
        this.user_tag = jsonObject.optString("user_tag");
        this.user_status = jsonObject.optInt("user_status");
        this.user_pos_x = jsonObject.optDouble("user_pos_x");
        this.user_pos_y = jsonObject.optDouble("user_pos_y");
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("user_id", user_id);
        localItemObject.put("user_logo", user_logo);
        localItemObject.put("user_name", user_name);
        localItemObject.put("user_rename", user_rename);
        localItemObject.put("user_tag", user_tag);
        localItemObject.put("user_status", user_status);
        localItemObject.put("user_pos_x", user_pos_x);
        localItemObject.put("user_pos_y", user_pos_y);
        return localItemObject;
    }
}
