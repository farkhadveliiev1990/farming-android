
package com.dmy.farming.api.data.chat;

import com.dmy.farming.api.data.CROPCYCLE;
import com.dmy.farming.api.data.KEYWORD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FARMARTICLE
{
    public String id;
    public String infoFrom;
    public String cropType;
    public String cycle;
    public String publishTime;
    public String title;
    public String pageView;
    public String author;
    public String content;
    public String img_url;
    public String likeNum;
    public int islike;
    public int iscollection;
    public String articleType;
    public String titlePicurl;
    public ArrayList<KEYWORD> keyWord = new ArrayList<KEYWORD>();


    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.infoFrom = jsonObject.optString("infoFrom");
        this.cropType = jsonObject.optString("cropType");
        this.cycle = jsonObject.optString("cycle");
        this.publishTime = jsonObject.optString("publishTime");
        this.title = jsonObject.optString("title");
        this.pageView = jsonObject.optString("pageView");
        this.author = jsonObject.optString("author");
        this.content = jsonObject.optString("content");
        this.img_url = jsonObject.optString("titlePicurl");
        this.likeNum = jsonObject.optString("likeNum");
        this.islike = jsonObject.optInt("islike");
        this.iscollection = jsonObject.optInt("iscollection");
        this.articleType = jsonObject.optString("articleType");
        this.titlePicurl = jsonObject.optString("titlePicurl");


        JSONArray subItemArray = jsonObject.optJSONArray("keyword");
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

        localItemObject.put("id", id);
        localItemObject.put("cropType", cropType);
        localItemObject.put("infoFrom", infoFrom);
        localItemObject.put("cycle", cycle);
        localItemObject.put("publishTime", publishTime);
        localItemObject.put("title", title);
        localItemObject.put("pageView", pageView);
        localItemObject.put("author", author);
        localItemObject.put("content", content);
        localItemObject.put("titlePicurl", img_url);
        localItemObject.put("likeNum", likeNum);
        localItemObject.put("islike", islike);
        localItemObject.put("iscollection", iscollection);
        localItemObject.put("articleType", articleType);
        localItemObject.put("titlePicurl", titlePicurl);

        JSONArray itemJSONArray = new JSONArray();
        itemJSONArray = new JSONArray();
        localItemObject.put("keyWord", itemJSONArray);
        return localItemObject;
    }


}
