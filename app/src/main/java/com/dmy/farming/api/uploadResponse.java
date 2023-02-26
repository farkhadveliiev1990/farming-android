
package com.dmy.farming.api;

import com.dmy.farming.protocol.STATUS;

import org.json.JSONException;
import org.json.JSONObject;

public class uploadResponse
{
    public STATUS status;

    public String filePath;
    public String fullPath;

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

        filePath = jsonObject.optString("filePath");
        fullPath = jsonObject.optString("url");

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        if(null != status)
        {
            localItemObject.put("status", status.toJson());
        }

        localItemObject.put("filePath", filePath);
        localItemObject.put("url", fullPath);
        return localItemObject;
    }

}
