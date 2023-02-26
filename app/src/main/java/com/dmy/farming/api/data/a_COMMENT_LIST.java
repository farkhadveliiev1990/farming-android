package com.dmy.farming.api.data;


import com.dmy.farming.api.data.chat.COMMENT;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class a_COMMENT_LIST
{
    public Long lastUpdateTime = 0L;

    public COMMENT comment = new COMMENT();
    public ArrayList<COMMENT> reply_list = new ArrayList<>();

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return ;
        }
        comment.fromJson(jsonObject);
        //
        JSONArray subItemArray = jsonObject.optJSONArray("reply_list");
        if(null != subItemArray)
        {
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                COMMENT subItem = new COMMENT();
                subItem.fromJson(subItemObject);
                this.reply_list.add(subItem);
            }
        }
        //
     /*   PAGINATED paginated = new PAGINATED();
        paginated.fromJson(jsonObject.optJSONObject("paginated"));
        this.paginated = paginated;*/
        //
        lastUpdateTime = jsonObject.optLong("lastUpdateTime");
        //
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        //
        localItemObject.put("datalist", comment.toJson());
        //
        JSONArray itemJSONArray = new JSONArray();
        for(int i = 0; i< reply_list.size(); i++)
        {
            COMMENT itemData = reply_list.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("reply_list", itemJSONArray);
        //
     /*   if (paginated != null)
            localItemObject.put("paginated", paginated.toJson());*/
        //
        localItemObject.put("lastUpdateTime", lastUpdateTime);
        //
        return localItemObject;
    }
}
