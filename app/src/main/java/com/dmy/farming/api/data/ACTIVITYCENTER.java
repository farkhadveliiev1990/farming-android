
package com.dmy.farming.api.data;

import org.json.JSONException;
import org.json.JSONObject;

public class ACTIVITYCENTER
{
    public String id;
    public String image;
    public String title;
    public String start_time;
    public String end_time;
    public String isPush;
    public String publisher;
    public String publishUser;
    public String publishTime;
    public String targetUrl;
    public int type;  // 0 外部  1 内部
    public String content;

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.image = jsonObject.optString("image");
        this.title = jsonObject.optString("title");
        this.start_time = jsonObject.optString("startTime");
        this.end_time = jsonObject.optString("endTime");
        this.isPush = jsonObject.optString("isPush");
        this.publisher = jsonObject.optString("publisher");
        this.publishUser = jsonObject.optString("publishUser");
        this.publishTime = jsonObject.optString("publishTime");
        this.targetUrl = jsonObject.optString("targetUrl");
        this.type = jsonObject.optInt("type");
        this.content = jsonObject.optString("content");

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("username", image);
        localItemObject.put("user_phone", title);
        return localItemObject;
    }
}
