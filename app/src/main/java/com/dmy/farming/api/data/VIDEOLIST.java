
package com.dmy.farming.api.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VIDEOLIST
{
    public String video_id;
   /* public String video_name;
    public String video_img;*/
    public String video_url;
    public String title;
    public String titlePicurl;
    public String viewNum;
    public String create_time;
    public String cycleName;
    public String author;
    public String page_view;
    public String share_num;
    public String collect_num;
    public String comment_num;
    public String content;
    public String key_word;
    public int iscollection;
    public String like_num;
    public int islike;
    public String cycle;
    public String articleType;
    public ArrayList<KEYWORD> keyWord = new ArrayList<KEYWORD>();

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.video_id = jsonObject.optString("id");
      /*  this.video_name = jsonObject.optString("route_name");
        this.video_img = jsonObject.optString("route_img");*/
        this.video_url = jsonObject.optString("videoUrl");
        this.title = jsonObject.optString("title");
        this.titlePicurl = jsonObject.optString("titlePicurl");
        this.viewNum = jsonObject.optString("page_view");
        this.create_time = jsonObject.optString("create_time");
        this.cycleName = jsonObject.optString("cycleName");
        this.author = jsonObject.optString("author");
        this.page_view = jsonObject.optString("page_view");
        this.share_num = jsonObject.optString("share_num");
        this.collect_num = jsonObject.optString("collect_num");
        this.comment_num = jsonObject.optString("comment_num");
        this.content = jsonObject.optString("content");
        this.iscollection = jsonObject.optInt("iscollection");
        this.like_num = jsonObject.optString("like_num");
        this.islike = jsonObject.optInt("islike");
        this.key_word = jsonObject.optString("key_word");
        this.cycle = jsonObject.optString("cycle");
        this.articleType = jsonObject.optString("articleType");

        JSONArray subItemArray = jsonObject.optJSONArray("key_word");
        if(null != subItemArray)
        {
            keyWord.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                KEYWORD subItem = new KEYWORD();
                subItem.fromJson(subItemObject);
                this.keyWord.add(subItem);
            }
        }
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("video_id", video_id);
       /* localItemObject.put("route_name", video_name);
        localItemObject.put("route_img", video_img);*/
        localItemObject.put("video_url", video_url);
        localItemObject.put("title", title);
        localItemObject.put("titlePicurl", titlePicurl);
        localItemObject.put("viewNum", viewNum);
        localItemObject.put("create_time", create_time);
        localItemObject.put("cycleName", cycleName);
        localItemObject.put("author", author);
        localItemObject.put("page_view", page_view);
        localItemObject.put("share_num", share_num);
        localItemObject.put("collect_num", collect_num);
        localItemObject.put("comment_num", comment_num);
        localItemObject.put("content", content);
        localItemObject.put("iscollection", iscollection);
        localItemObject.put("like_num", like_num);
        localItemObject.put("islike", islike);
        localItemObject.put("key_word", key_word);
        localItemObject.put("cycle", cycle);
        localItemObject.put("articleType", articleType);
        JSONArray itemJSONArray = new JSONArray();
        itemJSONArray = new JSONArray();
        localItemObject.put("keyWord", itemJSONArray);

        return localItemObject;
    }
}
