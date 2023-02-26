package com.dmy.farming.api.data;


import com.dmy.farming.api.data.chat.RENTLIST;
import com.dmy.farming.api.data.chat.SALELIST;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class a_RENT_LIST
{
    public Long lastUpdateTime = 0L;

    public String id = "";
    public String keyword = "";
    public String info_from= "";
    public String type_code = "";
    public String provience = "";
    public String city = "";
    public String district = "";
    public  String lon = "";
    public String lat = "";
    public String page = "";
    public String this_app = "";
    public  String lon1 = "";
    public String lat1 = "";

  //public PAGINATED paginated = new PAGINATED();
    public ArrayList<RENTLIST> data = new ArrayList<RENTLIST>();

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return ;
        }
        id = jsonObject.optString("id");
        info_from = jsonObject.optString("info_from");
        type_code = jsonObject.optString("type_code");
        provience = jsonObject.optString("provience");
        city = jsonObject.optString("city");
        district = jsonObject.optString("district");
        lon = jsonObject.optString("lon");
        lat = jsonObject.optString("lat");
        page = jsonObject.optString("page");
        this_app = jsonObject.optString("this_app");
        lon1 = jsonObject.optString("lon1");
        lat1 = jsonObject.optString("lat1");

        //
        JSONArray subItemArray = jsonObject.optJSONArray("datalist");
        if(null != subItemArray)
        {
            data.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                RENTLIST subItem = new RENTLIST();
                subItem.fromJson(subItemObject);
                this.data.add(subItem);
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
        localItemObject.put("info_from", info_from);
        localItemObject.put("type_code", type_code);
        localItemObject.put("provience", provience);
        localItemObject.put("city", city);
        localItemObject.put("district", district);
        localItemObject.put("lon", lon);
        localItemObject.put("lat", lat);
        localItemObject.put("page", page);
        localItemObject.put("this_app", this_app);
        localItemObject.put("lon1", lon1);
        localItemObject.put("lat1", lat1);
        //
        JSONArray itemJSONArray = new JSONArray();
        for(int i = 0; i< data.size(); i++)
        {
            RENTLIST itemData = data.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("data", itemJSONArray);
        //
     /*   if (paginated != null)
            localItemObject.put("paginated", paginated.toJson());*/
        //
        localItemObject.put("lastUpdateTime", lastUpdateTime);
        //
        return localItemObject;
    }
}
