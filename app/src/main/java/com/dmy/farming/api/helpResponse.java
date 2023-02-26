
package com.dmy.farming.api;


import com.dmy.farming.api.data.FINDHELPLIST;
import com.dmy.farming.api.data.chat.RENTLIST;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONException;
import org.json.JSONObject;

public class helpResponse
{
    public STATUS status;
    public FINDHELPLIST data;

    public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        STATUS  status = new STATUS();
//          status.fromJson(jsonObject.optJSONObject("statu"));
        status.succeed = jsonObject.optInt("state");
        status.error_code = jsonObject.optInt("errCode");
        status.error_desc = jsonObject.optString("errMsg");
        this.status = status;

        FINDHELPLIST data = new FINDHELPLIST();
        data.fromJson(jsonObject.optJSONObject("datalist"));
        this.data = data;

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        if(null != status) {
            localItemObject.put("status", status.toJson());
        }
        if(null != data) {
            localItemObject.put("data", data.toJson());
        }

        return localItemObject;
    }

}
