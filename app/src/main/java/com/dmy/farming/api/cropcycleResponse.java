
package com.dmy.farming.api;

import com.dmy.farming.api.data.CROPCYCLE;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class cropcycleResponse
{
    public STATUS status;

    public ArrayList<CROPCYCLE> data = new ArrayList<>();

    public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        STATUS  status = new STATUS();
//        status.fromJson(jsonObject.optJSONObject("status"));
        status.succeed = jsonObject.optInt("state");
        status.error_code = jsonObject.optInt("errCode");
        status.error_desc = jsonObject.optString("errMsg");
        this.status = status;

        JSONArray subItemArray = jsonObject.optJSONArray("datalist");
        if(null != subItemArray)
        {
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                CROPCYCLE subItem = new CROPCYCLE();
                subItem.fromJson(subItemObject);
                this.data.add(subItem);
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

        JSONArray itemJSONArray = new JSONArray();
        for(int i=0; i< data.size(); i++)
        {
            CROPCYCLE itemData = data.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("data", itemJSONArray);


        return localItemObject;
    }

}
