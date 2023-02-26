
package com.dmy.farming.api.data.chat;

import com.dmy.farming.api.data.REPLYASK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class REPLY
{
    public String id;
    public String content;  // 内容
    public String userPhone;
    public String username;
    public String headurl;
    public int isshield;  // 是否屏蔽
    public int isadopt;  // 采纳状态 0：未采纳 1：已采纳
    public String byreply_userPhone;
    public int reply_usertype;  // 0 普通 1专家
    public String expert_type;  // 专家职称
    public String comment_time;
    public String like_num;  // 点赞数
    public ArrayList<REPLYASK> subreply_list = new ArrayList();
    public int islike;


    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.content = jsonObject.optString("content");
        this.userPhone = jsonObject.optString("userPhone");
        this.username = jsonObject.optString("userName");
        this.headurl = jsonObject.optString("headurl");
        this.isshield = jsonObject.optInt("isshield");
        this.isadopt = jsonObject.optInt("isadopt");
        this.byreply_userPhone = jsonObject.optString("byreplyUserPhone");
        this.reply_usertype = jsonObject.optInt("replyuserType");
        this.expert_type = jsonObject.optString("expertType");
        this.comment_time = jsonObject.optString("commentTime");
        this.like_num = jsonObject.optString("likeNum");
        this.islike = jsonObject.optInt("islike");

        JSONArray replyArray = jsonObject.optJSONArray("subreply_list");
        if(null != replyArray)
        {
            for(int i = 0;i < replyArray.length();i++)
            {
                JSONObject subItemObject = replyArray.getJSONObject(i);
                REPLYASK subItem = new REPLYASK();
                subItem.fromJson(subItemObject);
                subreply_list.add(subItem);
            }
        }
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("username", username);
        localItemObject.put("userPhone", userPhone);
        localItemObject.put("islike", islike);
        return localItemObject;
    }
}
