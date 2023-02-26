
package com.dmy.farming.api;

import com.dmy.farming.api.data.a_COMMENT_LIST;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class agrotechniqueCommentResponse
{
    public STATUS status;

    public ArrayList<a_COMMENT_LIST> comment_list = new ArrayList<>();

    public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        STATUS  status = new STATUS();
        status.error_code = jsonObject.optInt("errCode");
        status.error_desc = jsonObject.optString("errMsg");
        this.status = status;

        JSONArray commentArray = jsonObject.optJSONArray("datalist");
        if(null != commentArray)
        {
            for(int i = 0;i < commentArray.length();i++)
            {
                JSONObject subItemObject = commentArray.getJSONObject(i);
                a_COMMENT_LIST subItem = new a_COMMENT_LIST();
                subItem.fromJson(subItemObject);
                this.comment_list.add(subItem);
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


        return localItemObject;
    }

}
