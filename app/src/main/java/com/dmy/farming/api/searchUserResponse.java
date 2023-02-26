
package com.dmy.farming.api;

import com.dmy.farming.api.data.SEARCHUSER;
import com.dmy.farming.protocol.PAGINATED;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class searchUserResponse
{
    public STATUS status;
    public PAGINATED paginated;
    public ArrayList<SEARCHUSER> data = new ArrayList<>();

    public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        STATUS  status = new STATUS();
        status.fromJson(jsonObject.optJSONObject("status"));
        this.status = status;

        PAGINATED  paginated = new PAGINATED();
        paginated.fromJson(jsonObject.optJSONObject("paginated"));
        this.paginated = paginated;

        JSONArray subItemArray = jsonObject.optJSONArray("data");
        if(null != subItemArray)
        {
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                SEARCHUSER subItem = new SEARCHUSER();
                subItem.fromJson(subItemObject);
                this.data.add(subItem);
            }
        }
        return;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        if(null != status)
        {
            localItemObject.put("status", status.toJson());
        }

        if(null != paginated)
        {
            localItemObject.put("paginated", paginated.toJson());
        }

        JSONArray itemJSONArray = new JSONArray();
        for(int i=0; i< data.size(); i++)
        {
            SEARCHUSER itemData = data.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }

        localItemObject.put("data", itemJSONArray);
        return localItemObject;
    }

}
