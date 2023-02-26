
package com.dmy.farming.api;

import com.dmy.farming.api.data.ARTICLELIST;
import com.dmy.farming.api.data.HELPCENTER;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class serviceAgreementResponse
{
    public STATUS status;
    public HELPCENTER data;

    public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        STATUS  status = new STATUS();
        status.error_code = jsonObject.optInt("errCode");
        status.error_desc = jsonObject.optString("errMsg");
        this.status = status;

        HELPCENTER  data = new HELPCENTER();
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
            localItemObject.put("datalist", data.toJson());
        }

        return localItemObject;
    }

}
