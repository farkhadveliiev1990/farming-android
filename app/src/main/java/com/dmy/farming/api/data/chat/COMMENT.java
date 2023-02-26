
package com.dmy.farming.api.data.chat;

import org.json.JSONException;
import org.json.JSONObject;

public class COMMENT
{
    public String id;
    public String type;
    public String supComid;
    public String commentName;
    public String commentHeader;
    public String commentId;
    public String articleId;
    public String content;
    public String time;
    public String isreport;
    public String info_from;
    public String byreplyName;
    public String byreplyId;
    public String likeNum;
    public String reply_num;
    public int islike;



    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.type = jsonObject.optString("type");
        this.supComid = jsonObject.optString("supComid");
        this.commentName = jsonObject.optString("commentName");
        this.commentHeader = jsonObject.optString("commentHeader");
        this.commentId = jsonObject.optString("commentId");
        this.articleId = jsonObject.optString("articleId");
        this.content = jsonObject.optString("content");
        this.time = jsonObject.optString("time");
        this.isreport = jsonObject.optString("isreport");
        this.info_from = jsonObject.optString("info_from");
        this.byreplyName = jsonObject.optString("byreplyName");
        this.byreplyId = jsonObject.optString("byreplyId");
        this.likeNum = jsonObject.optString("likeNum");
        this.reply_num = jsonObject.optString("reply_num");
        this.islike = jsonObject.optInt("islike");

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("type", type);
        localItemObject.put("supComid", supComid);
        localItemObject.put("commentName", commentName);
        localItemObject.put("commentHeader", commentHeader);
        localItemObject.put("commentId", commentId);
        localItemObject.put("articleId", articleId);
        localItemObject.put("content", content);
        localItemObject.put("time", time);
        localItemObject.put("isreport", isreport);
        localItemObject.put("info_from", info_from);
        localItemObject.put("byreplyName", byreplyName);
        localItemObject.put("byreplyId", byreplyId);
        localItemObject.put("likeNum", likeNum);
        localItemObject.put("reply_num", reply_num);
        localItemObject.put("islike", islike);
        return localItemObject;
    }


}
