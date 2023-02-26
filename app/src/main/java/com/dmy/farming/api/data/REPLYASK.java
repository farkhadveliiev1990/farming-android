
package com.dmy.farming.api.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class REPLYASK
{
    public String id;
    public String content;  // 内容
    public String userPhone;
    public String username;
    public int reply_usertype;
    public int like_num;  // 点赞数
    public String expert_type;
    public String comment_time;
    public String byreply_userPhone;
    public String byreply_username;
    public String type;  // 1:追问  2:追答





    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.content = jsonObject.optString("content");
        this.userPhone = jsonObject.optString("userPhone");
        this.username = jsonObject.optString("userName");
        this.reply_usertype = jsonObject.optInt("replyuserType");
        this.like_num = jsonObject.optInt("likeNum");
        this.expert_type = jsonObject.optString("expertType");
        this.comment_time = jsonObject.optString("commentTime");
        this.byreply_userPhone = jsonObject.optString("byreplyUserPhone");
        this.byreply_username = jsonObject.optString("byreplyUsername");
        this.type = jsonObject.optString("type");


        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("username", username);
        localItemObject.put("userPhone", userPhone);
        return localItemObject;
    }
}
