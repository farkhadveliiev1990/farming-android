
package com.dmy.farming.api;

import com.dmy.farming.api.data.DIAGNOSTIC;
import com.dmy.farming.api.data.KEYWORD;
import com.dmy.farming.api.data.chat.COMMENT;
import com.dmy.farming.api.data.chat.FARMARTICLE;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class diagnosticResponse
{
    public STATUS status;
    public ArrayList<DIAGNOSTIC> data = new ArrayList<>();

    public ArrayList<COMMENT> comment = new ArrayList<>();
    public ArrayList<KEYWORD> keyWord = new ArrayList<KEYWORD>();


    public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        STATUS  status = new STATUS();
//        status.fromJson(jsonObject.optJSONObject("status"));
        status.succeed = jsonObject.optInt("state");
        status.error_code = jsonObject.optInt("errCode");
        status.error_desc = jsonObject.optString("errMsg");
        this.status = status;

        JSONArray subItemArray = jsonObject.optJSONArray("diagnosebaselist");
        if(null != subItemArray)
        {
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                DIAGNOSTIC subItem = new DIAGNOSTIC();
                subItem.fromJson(subItemObject);
                this.data.add(subItem);
            }
        }


        subItemArray = jsonObject.optJSONArray("data");
        if(null != subItemArray)
        {
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                COMMENT subItem = new COMMENT();
                subItem.fromJson(subItemObject);
                this.comment.add(subItem);
            }
        }
        subItemArray = jsonObject.optJSONArray("keyword");
        if(null != subItemArray)
        {
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
        if(null != status)
        {
            localItemObject.put("status", status.toJson());
        }

        JSONArray itemJSONArray = new JSONArray();
        for(int i=0; i< data.size(); i++)
        {
            DIAGNOSTIC itemData = data.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("data", itemJSONArray);




        return localItemObject;
    }

}
