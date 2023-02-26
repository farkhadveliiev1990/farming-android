package com.dmy.farming.api.data;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class a_HELPCENTER_LIST
{
    public Long lastUpdateTime = 0L;

    public String id = "";
    public String type= "";
    public String app = "";
    public String appid = "";
    public String createUser = "";
    public String createTime = "";
    public  String updataTime = "";
    public String deleted = "";
    public String content = "";
    public String title = "";

  //public PAGINATED paginated = new PAGINATED();
    public ArrayList<HELPCENTER> helpcenters = new ArrayList<HELPCENTER>();

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return ;
        }
        id = jsonObject.optString("id");
        type = jsonObject.optString("type");
        app = jsonObject.optString("app");
        appid = jsonObject.optString("appid");
        createUser = jsonObject.optString("createUser");
        createTime = jsonObject.optString("createTime");
        updataTime = jsonObject.optString("updataTime");
        deleted = jsonObject.optString("deleted");
        content = jsonObject.optString("content");
        title = jsonObject.optString("title");

        //
        JSONArray subItemArray = jsonObject.optJSONArray("datalist");
        if(null != subItemArray)
        {
            helpcenters.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                HELPCENTER subItem = new HELPCENTER();
                subItem.fromJson(subItemObject);
                this.helpcenters.add(subItem);
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
        localItemObject.put("type", type);
        localItemObject.put("app", app);
        localItemObject.put("appid", appid);
        localItemObject.put("createUser", createUser);
        localItemObject.put("createTime", createTime);
        localItemObject.put("updataTime", updataTime);
        localItemObject.put("deleted", deleted);
        localItemObject.put("content", content);
        localItemObject.put("title", title);
        //
        JSONArray itemJSONArray = new JSONArray();
        for(int i = 0; i< helpcenters.size(); i++)
        {
            HELPCENTER itemData = helpcenters.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("helpcenters", itemJSONArray);
        //
     /*   if (paginated != null)
            localItemObject.put("paginated", paginated.toJson());*/
        //
        localItemObject.put("lastUpdateTime", lastUpdateTime);
        //
        return localItemObject;
    }
}
