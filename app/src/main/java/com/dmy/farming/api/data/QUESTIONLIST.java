
package com.dmy.farming.api.data;

import org.json.JSONException;
import org.json.JSONObject;

public class QUESTIONLIST
{
    public String id;
    public String infoFrom;
    public String createTime;
    public String imgUrl;
    public String status;
    public String cropType;
    public String problemType;
    public String provience;
    public String city;
    public String district;
    public String keyWord;
    public String accept;
    public String acceptId;
    public String pageView;
    public String userPhone;
    public String shareNum;
    public String commentNum;
    public String deleted;
    public String headurl;
    public String userName;
    public String replayNum;
    public String content;
    public String faq_id;


    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.infoFrom = jsonObject.optString("infoFrom");
        this.createTime = jsonObject.optString("createTime");
        this.imgUrl = jsonObject.optString("imgUrl");
        this.status = jsonObject.optString("status");
        this.cropType = jsonObject.optString("cropType");
        this.problemType = jsonObject.optString("problemType");
        this.provience = jsonObject.optString("provience");
        this.city = jsonObject.optString("city");
        this.district = jsonObject.optString("district");
        this.keyWord = jsonObject.optString("keyWord");
        this.accept = jsonObject.optString("accept");
        this.acceptId = jsonObject.optString("acceptId");
        this.pageView = jsonObject.optString("pageView");
        this.userPhone = jsonObject.optString("userPhone");
        this.shareNum = jsonObject.optString("shareNum");
        this.commentNum = jsonObject.optString("commentNum");
        this.deleted = jsonObject.optString("deleted");
        this.headurl = jsonObject.optString("headurl");
        this.userName = jsonObject.optString("userName");
        this.replayNum = jsonObject.optString("replayNum");
        this.content = jsonObject.optString("content");
        this.faq_id = jsonObject.optString("faq_id");
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("infoFrom", infoFrom);
        localItemObject.put("createTime", createTime);
        localItemObject.put("imgUrl", imgUrl);
        localItemObject.put("status", status);
        localItemObject.put("cropType", cropType);
        localItemObject.put("problemType", problemType);
        localItemObject.put("provience", provience);
        localItemObject.put("city", city);
        localItemObject.put("district", district);
        localItemObject.put("keyWord", keyWord);
        localItemObject.put("accept", accept);
        localItemObject.put("acceptId", acceptId);
        localItemObject.put("pageView", pageView);
        localItemObject.put("userPhone", userPhone);
        localItemObject.put("shareNum", shareNum);
        localItemObject.put("commentNum", commentNum);
        localItemObject.put("deleted", deleted);
        localItemObject.put("headurl", headurl);
        localItemObject.put("userName", userName);
        localItemObject.put("replayNum", replayNum);
        localItemObject.put("content", content);
        localItemObject.put("faq_id", faq_id);

        return localItemObject;
    }
}
