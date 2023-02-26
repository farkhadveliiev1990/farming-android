package com.dmy.farming.api.data;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class a_illegal_LIST
{
    public Long lastUpdateTime = 0L;

    public String id = "";
    public String text= "";
    public String searchTime = "";
    public String addWay = "";
    public String publishTime = "";
    public String status = "";

  //public PAGINATED paginated = new PAGINATED();
    public ArrayList<illegal> illegals = new ArrayList<illegal>();

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return ;
        }
        id = jsonObject.optString("id");
        text = jsonObject.optString("text");
        searchTime = jsonObject.optString("searchTime");
        addWay = jsonObject.optString("addWay");
        publishTime = jsonObject.optString("publishTime");
        status = jsonObject.optString("status");

        //
        JSONArray subItemArray = jsonObject.optJSONArray("datalist");
        if(null != subItemArray)
        {
            illegals.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                illegal subItem = new illegal();
                subItem.fromJson(subItemObject);
                this.illegals.add(subItem);
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
        localItemObject.put("id", id);
        localItemObject.put("text", text);
        localItemObject.put("searchTime", searchTime);
        localItemObject.put("addWay", addWay);
        localItemObject.put("publishTime", publishTime);
        localItemObject.put("status", status);
        //
        JSONArray itemJSONArray = new JSONArray();
        for(int i = 0; i< illegals.size(); i++)
        {
            illegal itemData = illegals.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("illegals", itemJSONArray);
        //
     /*   if (paginated != null)
            localItemObject.put("paginated", paginated.toJson());*/
        //
        localItemObject.put("lastUpdateTime", lastUpdateTime);
        //
        return localItemObject;
    }
}
