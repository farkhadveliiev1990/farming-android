
package com.dmy.farming.api;

import com.dmy.farming.protocol.STATUS;

import org.json.JSONException;
import org.json.JSONObject;

public class CollectResponse
{
    public STATUS status;

    public String id;

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

        id = jsonObject.optString("id");
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        if(null != status)
        {
            localItemObject.put("status", status.toJson());
        }

        localItemObject.put("id", id);
        return localItemObject;
    }

}
