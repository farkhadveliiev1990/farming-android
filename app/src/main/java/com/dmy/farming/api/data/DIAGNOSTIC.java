
package com.dmy.farming.api.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DIAGNOSTIC
{
    public String id;
    public String name;
    public String img_url;
    public String create_time;
    public String page_view;
    public String collection_num;
    public String share_num;
    public String cycle;


    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.name = jsonObject.optString("name");
        this.img_url = jsonObject.optString("img_url");
        this.create_time = jsonObject.optString("createTime");
        this.page_view = jsonObject.optString("pageView");
        this.collection_num = jsonObject.optString("collectionNum");
        this.share_num = jsonObject.optString("shareNum");
        this.cycle = jsonObject.optString("cycle");

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("name", name);
        localItemObject.put("img_url", img_url);
        localItemObject.put("createTime", create_time);
        localItemObject.put("pageView", page_view);
        localItemObject.put("collectionNum", collection_num);
        localItemObject.put("shareNum", share_num);
        localItemObject.put("cycle", cycle);

        return localItemObject;
    }


}
