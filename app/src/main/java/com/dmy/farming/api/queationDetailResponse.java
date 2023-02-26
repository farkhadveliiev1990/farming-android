
package com.dmy.farming.api;

import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.data.QUESTION;
import com.dmy.farming.api.data.chat.REPLY;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class queationDetailResponse
{
    public STATUS status;

    public QUESTION data ;
    public ArrayList<REPLY> reply_list = new ArrayList();

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

        JSONObject subJsonObject = jsonObject.optJSONObject("datalist");
        QUESTION question = new QUESTION();
        question.fromJson(subJsonObject);
        this.data = question;

        JSONArray replyArray = jsonObject.optJSONArray("reply_list");
        if(null != replyArray)
        {
            for(int i = 0;i < replyArray.length();i++)
            {
                JSONObject subItemObject = replyArray.getJSONObject(i);
                REPLY subItem = new REPLY();
                subItem.fromJson(subItemObject);
                this.reply_list.add(subItem);
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
