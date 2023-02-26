package com.dmy.farming.api.data;

import com.dmy.farming.protocol.PAGINATED;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class a_SEARCH_LIST
{
    public Long lastUpdateTime = 0L;

    public PAGINATED paginated = new PAGINATED();

    public ArrayList<QUESTIONLIST> questionList = new ArrayList<>();
    public ArrayList<DIAGNOSTIC> diagnosticList = new ArrayList<>();
    public ArrayList<ARTICLELIST> articleList = new ArrayList<>();
    public ArrayList<VIDEOLIST> videoList = new ArrayList<>();
    public ArrayList<EXPERTINFO> expertList = new ArrayList<>();
//    public ArrayList<SEARCH> expertList = new ArrayList<>();

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return ;
        }
        //
        JSONArray friendItemArray = jsonObject.optJSONArray("diagnosebaselist");
        if(null != friendItemArray)
        {
            for(int i = 0;i < friendItemArray.length();i++)
            {
                JSONObject friendItemObject = friendItemArray.getJSONObject(i);
                DIAGNOSTIC subItem = new DIAGNOSTIC();
                subItem.fromJson(friendItemObject);
                this.diagnosticList.add(subItem);
            }
        }
        //
        JSONArray qunItemArray = jsonObject.optJSONArray("faqlist");
        if(null != qunItemArray)
        {
            for(int i = 0;i < qunItemArray.length();i++)
            {
                JSONObject subItemObject = qunItemArray.getJSONObject(i);
                QUESTIONLIST subItem = new QUESTIONLIST();
                subItem.fromJson(subItemObject);
                this.questionList.add(subItem);
            }
        }
        //
        JSONArray huodongItemArray = jsonObject.optJSONArray("articlelist");
        if(null != huodongItemArray)
        {
            for(int i = 0;i < huodongItemArray.length();i++)
            {
                JSONObject subItemObject = huodongItemArray.getJSONObject(i);
                ARTICLELIST subItem = new ARTICLELIST();
                subItem.fromJson(subItemObject);
                this.articleList.add(subItem);
            }
        }
        //
        JSONArray luxianItemArray = jsonObject.optJSONArray("farmvideolist");
        if(null != luxianItemArray)
        {
            for(int i = 0;i < luxianItemArray.length();i++)
            {
                JSONObject subItemObject = luxianItemArray.getJSONObject(i);
                VIDEOLIST subItem = new VIDEOLIST();
                subItem.fromJson(subItemObject);
                this.videoList.add(subItem);
            }
        }
        //
        JSONArray ItemArray = jsonObject.optJSONArray("userinfolist");
        if(null != luxianItemArray)
        {
            for(int i = 0;i < luxianItemArray.length();i++)
            {
                JSONObject subItemObject = ItemArray.getJSONObject(i);
                EXPERTINFO subItem = new EXPERTINFO();
                subItem.fromJson(subItemObject);
                this.expertList.add(subItem);
            }
        }
        //
        PAGINATED paginated = new PAGINATED();
//        paginated.fromJson(jsonObject.optJSONObject("paginated"));
        paginated.more = jsonObject.optInt("ismore");
        paginated.count = jsonObject.optInt("count");
        this.paginated = paginated;
        //
        lastUpdateTime = jsonObject.optLong("lastUpdateTime");
        //
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        //
        JSONArray friendItemArray = new JSONArray();
        for(int i=0; i< questionList.size(); i++)
        {
            QUESTIONLIST itemData = questionList.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            friendItemArray.put(itemJSONObject);
        }
        localItemObject.put("faqlist", friendItemArray);
        //
        JSONArray qunItemArray = new JSONArray();
        for(int i=0; i< diagnosticList.size(); i++)
        {
            DIAGNOSTIC itemData = diagnosticList.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            qunItemArray.put(itemJSONObject);
        }
        localItemObject.put("diagnosebaselist", qunItemArray);
        //
        JSONArray huodongItemArray = new JSONArray();
        for(int i=0; i< articleList.size(); i++)
        {
            ARTICLELIST itemData = articleList.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            huodongItemArray.put(itemJSONObject);
        }
        localItemObject.put("articlelist", huodongItemArray);
        //
        JSONArray luxianItemArray = new JSONArray();
        for(int i=0; i< videoList.size(); i++)
        {
            VIDEOLIST itemData = videoList.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            luxianItemArray.put(itemJSONObject);
        }
        localItemObject.put("farmvideolist", luxianItemArray);
        //
        JSONArray ItemArray = new JSONArray();
        for(int i=0; i< expertList.size(); i++)
        {
            EXPERTINFO itemData = expertList.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            ItemArray.put(itemJSONObject);
        }
        localItemObject.put("userinfolist", ItemArray);
        //
        if (paginated != null)
            localItemObject.put("paginated", paginated.toJson());
        //
        localItemObject.put("lastUpdateTime", lastUpdateTime);
        //
        return localItemObject;
    }
}
