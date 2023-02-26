
package com.dmy.farming.api.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DIAGNOSTICDETAIL
{
    public String id;
    public String name;
    public String img_url;
    public String typeCode;
    public String createUser;
    public String createTime;
    public String pageView;
    public String shareNum;
    public String collectionNum;
    public String keyWord;
    public String vulgo;  //俗称
    public String imgNum;
    public String commentNum;
    public ArrayList<DIAGNOSTICITEM> item_list = new ArrayList();
    public int islike;
    public int likeNum;
    public int iscollection;
    public String keywordcode;

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.name = jsonObject.optString("name");
        this.img_url = jsonObject.optString("img_url");
        this.typeCode = jsonObject.optString("typeCode");
        this.createUser = jsonObject.optString("createUser");
        this.createTime = jsonObject.optString("createTime");
        this.pageView = jsonObject.optString("pageView");
        this.shareNum = jsonObject.optString("shareNum");
        this.collectionNum = jsonObject.optString("collectionNum");
        this.keyWord = jsonObject.optString("keyWord");
        this.vulgo = jsonObject.optString("vulgo");
        this.imgNum = jsonObject.optString("imgNum");
        this.commentNum = jsonObject.optString("commentNum");
        this.islike = jsonObject.optInt("islike");
        this.likeNum = jsonObject.optInt("likeNum");
        this.iscollection = jsonObject.optInt("iscollection");
        this.keywordcode = jsonObject.optString("keywordcode");

        JSONArray subItemArray = jsonObject.optJSONArray("item_list");
        if(null != subItemArray)
        {
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                DIAGNOSTICITEM subItem = new DIAGNOSTICITEM();
                subItem.fromJson(subItemObject);
                this.item_list.add(subItem);
            }
        }

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("name", name);
        localItemObject.put("img_url", img_url);
        localItemObject.put("typeCode", typeCode);
        localItemObject.put("createUser", createUser);
        localItemObject.put("createTime", createTime);
        localItemObject.put("pageView", pageView);
        localItemObject.put("shareNum", shareNum);
        localItemObject.put("collectionNum", collectionNum);
        localItemObject.put("keyWord", keyWord);
        localItemObject.put("vulgo", vulgo);
        localItemObject.put("imgNum", imgNum);
        localItemObject.put("commentNum", commentNum);
        localItemObject.put("item_list", item_list);
        localItemObject.put("islike", islike);
        localItemObject.put("likeNum", likeNum);
        localItemObject.put("iscollection", iscollection);
        localItemObject.put("keywordcode", keywordcode);


        return localItemObject;
    }
}
