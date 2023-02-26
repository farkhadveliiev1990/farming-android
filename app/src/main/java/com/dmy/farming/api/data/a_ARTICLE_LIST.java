package com.dmy.farming.api.data;

import com.dmy.farming.protocol.PAGINATED;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class a_ARTICLE_LIST
{
    public Long lastUpdateTime = 0L;

    public PAGINATED paginated = new PAGINATED();
    public ArrayList<ARTICLELIST> articleList = new ArrayList<ARTICLELIST>();

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return ;
        }
        //
        JSONArray subItemArray = jsonObject.optJSONArray("data");
        if(null != subItemArray)
        {
            articleList.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                ARTICLELIST subItem = new ARTICLELIST();
                subItem.fromJson(subItemObject);
                this.articleList.add(subItem);
            }
        }
        //
        PAGINATED paginated = new PAGINATED();
//        paginated.fromJson(jsonObject.optJSONObject("paginated"));
        paginated.count = jsonObject.optInt("count");
        paginated.more = jsonObject.optInt("ismore");
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
        JSONArray itemJSONArray = new JSONArray();
        for(int i = 0; i< articleList.size(); i++)
        {
            ARTICLELIST itemData = articleList.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("questions", itemJSONArray);
        //
        if (paginated != null)
            localItemObject.put("paginated", paginated.toJson());
        //
        localItemObject.put("lastUpdateTime", lastUpdateTime);
        //
        return localItemObject;
    }
}
