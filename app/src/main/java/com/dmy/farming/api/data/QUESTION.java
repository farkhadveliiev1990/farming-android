
package com.dmy.farming.api.data;

import org.json.JSONException;
import org.json.JSONObject;

public class QUESTION
{
    public String question_id;
    public String user_name;
    public String userPhone;
    public String head_url;  //头像
    public String content;  // 内容
    public int accept;     // 采纳状态
    public String img_url;   // 第一个图片地址
    public String problemType;  // 关键字
    public int page_view;  // 浏览量
    public String provience;   //省
    public String city;       // 市
    public String district;  // 区
    public String create_time;
    public String comment_num;
    public String iscollection;
    public String keyWord;
    public int isShield;


    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.question_id = jsonObject.optString("id");
        this.user_name = jsonObject.optString("userName");
        this.userPhone = jsonObject.optString("userPhone");
        this.head_url = jsonObject.optString("headurl");
        this.content = jsonObject.optString("content");
        this.accept = jsonObject.optInt("accept");
        this.img_url = jsonObject.optString("imgUrl");
        this.problemType = jsonObject.optString("problemType");
        this.page_view = jsonObject.optInt("pageView");
        this.provience = jsonObject.optString("provience");
        this.city = jsonObject.optString("city");
        this.district = jsonObject.optString("district");
        this.create_time = jsonObject.optString("createTime");
        this.comment_num = jsonObject.optString("commentNum");
        this.isShield = jsonObject.optInt("isShield");
        this.iscollection = jsonObject.optString("iscollection");

        this.keyWord = jsonObject.optString("keyWord");
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", question_id);
        localItemObject.put("username", user_name);
        localItemObject.put("userPhone", userPhone);
        localItemObject.put("iscollection", iscollection);
        localItemObject.put("head_url", head_url);
        localItemObject.put("content", content);
        localItemObject.put("accept", accept);
        localItemObject.put("img_url", img_url);
        localItemObject.put("problemType", problemType);
        localItemObject.put("page_view", page_view);
        localItemObject.put("provience", provience);
        localItemObject.put("city", city);
        localItemObject.put("district", district);
        localItemObject.put("create_time", create_time);
        localItemObject.put("comment_num", comment_num);
        localItemObject.put("keyWord", keyWord);
        localItemObject.put("isShield", isShield);
        return localItemObject;
    }
}
