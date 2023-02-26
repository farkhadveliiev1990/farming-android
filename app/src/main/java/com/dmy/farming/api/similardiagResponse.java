
package com.dmy.farming.api;

import com.dmy.farming.api.data.DIAGNOSTICDETAIL;
import com.dmy.farming.api.data.VIDEOLIST;
import com.dmy.farming.protocol.PAGINATED;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class similardiagResponse
{
    public STATUS status;
    public PAGINATED paginated;

    public ArrayList<DIAGNOSTICDETAIL> similardiag = new ArrayList<>();

    public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        STATUS  status = new STATUS();
        status.error_code = jsonObject.optInt("errCode");
        status.error_desc = jsonObject.optString("errMsg");
        this.status = status;

        PAGINATED  paginated = new PAGINATED();
//          paginated.fromJson(jsonObject.optJSONObject("ismore"));
        paginated.more = jsonObject.optInt("ismore");
        paginated.count = jsonObject.optInt("count");
        this.paginated = paginated;

        JSONArray commentArray = jsonObject.optJSONArray("datalist");
        if(null != commentArray)
        {
            for(int i = 0;i < commentArray.length();i++)
            {
                JSONObject subItemObject = commentArray.getJSONObject(i);
                DIAGNOSTICDETAIL subItem = new DIAGNOSTICDETAIL();
                subItem.fromJson(subItemObject);
                this.similardiag.add(subItem);
            }
        }

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        if(null != status)
        {
            localItemObject.put("status", status.toJson());
        }


        return localItemObject;
    }

}
