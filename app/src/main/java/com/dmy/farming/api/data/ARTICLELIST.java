
package com.dmy.farming.api.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ARTICLELIST
{
    public String id;
    public String title;
    public String level;
    public String provience;
    public String city;
    public String district;
    public String publish_time;
    public String publisher;
    public String info_from;
    public String key_word;
    public String columen_ves;
    public String status;
    public String content;
    public String img_url;
    public String pic_type;
    public String page_view;
    public String share_num;
    public String comment_num;
    public String collection_num;
    public String ischoicenss;
    public String iscollection;
    public ArrayList<KEYWORD> keyWord = new ArrayList<KEYWORD>();

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }
        this.id = jsonObject.optString("id");
        this.title = jsonObject.optString("title");
        this.level = jsonObject.optString("level");
        this.provience = jsonObject.optString("provience");
        this.city = jsonObject.optString("city");
        this.district = jsonObject.optString("district");
        this.publish_time = jsonObject.optString("publish_time");
        this.publisher = jsonObject.optString("publisher");
        this.info_from = jsonObject.optString("info_from");
        this.key_word = jsonObject.optString("key_word");
        this.columen_ves = jsonObject.optString("column_ves");
        this.status = jsonObject.optString("status");
        this.content = jsonObject.optString("content");
        this.img_url = jsonObject.optString("img_url");
        this.pic_type = jsonObject.optString("pic_type");
        this.page_view = jsonObject.optString("page_view");
        this.share_num = jsonObject.optString("share_num");
        this.comment_num = jsonObject.optString("comment_num");
        this.collection_num = jsonObject.optString("collection_num");
        this.ischoicenss = jsonObject.optString("ischoicenss");
        this.iscollection = jsonObject.optString("iscollection");

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("title", title);
        localItemObject.put("level", level);
        localItemObject.put("provience", provience);
        localItemObject.put("city", city);
        localItemObject.put("district", district);
        localItemObject.put("publish_time", publish_time);
        localItemObject.put("publisher", publisher);
        localItemObject.put("info_from", info_from);
        localItemObject.put("key_word", key_word);
        localItemObject.put("columen_ves", columen_ves);
        localItemObject.put("status", status);
        localItemObject.put("content", content);
        localItemObject.put("img_url", img_url);
        localItemObject.put("pic_type", pic_type);
        localItemObject.put("page_view", page_view);
        localItemObject.put("share_num", share_num);
        localItemObject.put("comment_num", comment_num);
        localItemObject.put("collection_num", collection_num);
        localItemObject.put("ischoicenss", ischoicenss);
        localItemObject.put("iscollection", iscollection);

        return localItemObject;
    }
}
