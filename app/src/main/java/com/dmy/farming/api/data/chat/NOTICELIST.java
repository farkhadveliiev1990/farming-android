
package com.dmy.farming.api.data.chat;

import org.json.JSONException;
import org.json.JSONObject;

public class NOTICELIST
{
    public String id;
    public String msgType;
    public String type;
    public String imgSize;
    public String imgUrl;
    public String isTimeing;
    public String timingTime;
    public String createUser;
    public String title;
    public String createTime;
    public String content;
    public String keyWordusing;
    public String keyWord;
    public String pageView;
    public String deteled;
    public String shareNum;
    public String publisher;
    public String readNum;




    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.title = jsonObject.optString("title");
        this.msgType = jsonObject.optString("msgType");
        this.imgSize = jsonObject.optString("imgSize");
        this.imgUrl = jsonObject.optString("imgUrl");
        this.isTimeing = jsonObject.optString("isTimeing");
        this.timingTime = jsonObject.optString("timingTime");
        this.createUser = jsonObject.optString("createUser");
        this.createTime = jsonObject.optString("createTime");
        this.keyWordusing = jsonObject.optString("keyWordusing");
        this.keyWord = jsonObject.optString("keyWord");
        this.pageView = jsonObject.optString("pageView");
        this.deteled = jsonObject.optString("deteled");
        this.shareNum = jsonObject.optString("shareNum");
        this.content = jsonObject.optString("content");
        this.publisher = jsonObject.optString("publisher");
        this.readNum = jsonObject.optString("readNum");
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("title", title);
        localItemObject.put("msgType", msgType);
        localItemObject.put("imgSize", imgSize);
        localItemObject.put("imgUrl", imgUrl);
        localItemObject.put("isTimeing", isTimeing);
        localItemObject.put("timingTime", timingTime);
        localItemObject.put("createUser", createUser);
        localItemObject.put("createTime", createTime);
        localItemObject.put("keyWordusing", keyWordusing);
        localItemObject.put("keyWord", keyWord);
        localItemObject.put("pageView", pageView);
        localItemObject.put("deteled", deteled);
        localItemObject.put("shareNum", shareNum);
        localItemObject.put("content", content);
        localItemObject.put("publisher", publisher);
        localItemObject.put("readNum", readNum);

        return localItemObject;
    }
}
