package com.dmy.farming.api.data.chat;


import com.dmy.farming.api.data.BUYLIST;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class a_WARN_LIST
{
    public Long lastUpdateTime = 0L;

    public String info_from = "";
    public String page = "";

  //public PAGINATED paginated = new PAGINATED();
    public ArrayList<WARNLIST> routes = new ArrayList<WARNLIST>();

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return ;
        }
        //
        info_from = jsonObject.optString("info_from");
        page = jsonObject.optString("page");

        //
        JSONArray subItemArray = jsonObject.optJSONArray("datalist");
        if(null != subItemArray)
        {
            routes.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                WARNLIST subItem = new WARNLIST();
                subItem.fromJson(subItemObject);
                this.routes.add(subItem);
            }
        }
        //
     /*   PAGINATED paginated = new PAGINATED();
        paginated.fromJson(jsonObject.optJSONObject("paginated"));
        this.paginated = paginated;*/
        //
        lastUpdateTime = jsonObject.optLong("lastUpdateTime");
        //
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        //
        localItemObject.put("info_from", info_from);
        localItemObject.put("page", page);
        //
        JSONArray itemJSONArray = new JSONArray();
        for(int i = 0; i< routes.size(); i++)
        {
            WARNLIST itemData = routes.get(i);
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
