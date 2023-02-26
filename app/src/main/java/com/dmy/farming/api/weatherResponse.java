package com.dmy.farming.api;

import com.dmy.farming.api.data.WEATHER;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by victory1989 on 2017/8/22.
 */

public class weatherResponse {
    public WEATHER data;
    public STATUS status;

    public void fromJson(JSONObject jsonObject) throws JSONException
    {
        if (jsonObject == null) {
            return;
        }

        STATUS status = new STATUS();
//        status.fromJson(jsonObject.optJSONObject("status"));
        status.succeed = jsonObject.optInt("status");
        status.error_desc = jsonObject.optString("desc");
        this.status = status;

        WEATHER data = new WEATHER();
        data.fromJson(jsonObject.optJSONObject("data"));
        this.data = data;
    }

    public JSONObject toJson() throws JSONException
    {
        JSONObject localJSONObject = new JSONObject();
        
        if (this.status != null) {
            localJSONObject.put("status", this.status.toJson());
        }
        
        if (this.data != null) {
            localJSONObject.put("data", this.data.toJson());
        }
        
        return localJSONObject;
    }
}
