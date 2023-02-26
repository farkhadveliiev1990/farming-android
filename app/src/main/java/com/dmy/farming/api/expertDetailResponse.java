
package com.dmy.farming.api;

import com.dmy.farming.api.data.EXPERTINFO;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONException;
import org.json.JSONObject;

public class expertDetailResponse
{
    public STATUS status;
    public EXPERTINFO data;

    public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        STATUS  status = new STATUS();
//        status.fromJson(jsonObject.optJSONObject("status"));
        status.error_code = jsonObject.optInt("errCode");
        status.error_desc = jsonObject.optString("errMsg");
        this.status = status;

        EXPERTINFO data = new EXPERTINFO();
        data.fromJson(jsonObject.optJSONObject("datalist"));
        this.data = data;

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        if(null != status)
            localItemObject.put("status", status.toJson());

        if (null != data)
            localItemObject.put("data", data.toJson());

        return localItemObject;
    }

}
