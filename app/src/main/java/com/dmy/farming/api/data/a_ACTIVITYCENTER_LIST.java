package com.dmy.farming.api.data;


import com.dmy.farming.protocol.PAGINATED;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class a_ACTIVITYCENTER_LIST
{
    public Long lastUpdateTime = 0L;

    public PAGINATED paginated = new PAGINATED();
    public ArrayList<ACTIVITYCENTER> data = new ArrayList<ACTIVITYCENTER>();

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return ;
        }
        //
        JSONArray subItemArray = jsonObject.optJSONArray("data");
        if(null != subItemArray)
        {
            data.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                ACTIVITYCENTER subItem = new ACTIVITYCENTER();
                subItem.fromJson(subItemObject);
                this.data.add(subItem);
            }
        }
        //
        PAGINATED paginated = new PAGINATED();
//        paginated.fromJson(jsonObject.optJSONObject("paginated"));
        paginated.more = jsonObject.optInt("ismore");
        paginated.count = jsonObject.optInt("count");
        this.paginated = paginated;
        //
        lastUpdateTime = jsonObject.optLong("lastUpdateTime");
        //
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        //
        JSONArray itemJSONArray = new JSONArray();
        for(int i = 0; i< data.size(); i++)
        {
            ACTIVITYCENTER itemData = data.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("routes", itemJSONArray);
        //
     /*   if (paginated != null)
            localItemObject.put("paginated", paginated.toJson());*/
        //
        localItemObject.put("lastUpdateTime", lastUpdateTime);
        //
        return localItemObject;
    }
}
