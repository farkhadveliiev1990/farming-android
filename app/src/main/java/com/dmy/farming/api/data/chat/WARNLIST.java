
package com.dmy.farming.api.data.chat;

import org.json.JSONException;
import org.json.JSONObject;

public class WARNLIST
{
    public String id;
    public String warning_type;
    public String levels;
    public String provience;
    public String city;
    public String district;
    public String publish_userid;
    public String infoFrom;
    public String title;
    public String publishTime;
    public String content;
    public String keyWord;
    public String is_push;
    public String status;
    public String deteled;
    public String page_view;
    public String createtime;
    public String publisher;
    public String readStatus;




    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.title = jsonObject.optString("title");
        this.warning_type = jsonObject.optString("warningType");
        this.levels = jsonObject.optString("levels");
        this.publish_userid = jsonObject.optString("publish_userid");
        this.infoFrom = jsonObject.optString("infoFrom");
        this.publishTime = jsonObject.optString("publishTime");
        this.keyWord = jsonObject.optString("keyWord");
        this.is_push = jsonObject.optString("is_push");
        this.provience = jsonObject.optString("provience");
        this.city = jsonObject.optString("city");
        this.district = jsonObject.optString("district");
        this.deteled = jsonObject.optString("deteled");
        this.page_view = jsonObject.optString("page_view");
        this.content = jsonObject.optString("content");
        this.status = jsonObject.optString("status");
        this.createtime = jsonObject.optString("createTime");
        this.publisher = jsonObject.optString("publisher");
        this.readStatus = jsonObject.optString("readStatus");
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("title", title);
        localItemObject.put("warning_type", warning_type);
        localItemObject.put("levels", levels);
        localItemObject.put("publish_userid", publish_userid);
        localItemObject.put("infoFrom", infoFrom);
        localItemObject.put("publishTime", publishTime);
        localItemObject.put("keyWord", keyWord);
        localItemObject.put("is_push", is_push);
        localItemObject.put("provience", provience);
        localItemObject.put("city", city);
        localItemObject.put("district", district);
        localItemObject.put("deteled", deteled);
        localItemObject.put("page_view", page_view);
        localItemObject.put("status", status);
        localItemObject.put("content", content);
        localItemObject.put("createtime", createtime);
        localItemObject.put("publisher", publisher);
        localItemObject.put("readStatus", readStatus);

        return localItemObject;
    }
}
