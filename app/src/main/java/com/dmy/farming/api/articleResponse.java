
package com.dmy.farming.api;

import com.dmy.farming.api.data.ARTICLE;
import com.dmy.farming.api.data.ARTICLELIST;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class articleResponse
{
    public STATUS status;
    public ARTICLELIST data;
    public ArrayList<ARTICLELIST> dataList = new ArrayList<>();

    public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        STATUS  status = new STATUS();
//          status.fromJson(jsonObject.optJSONObject("statu"));
        status.succeed = jsonObject.optInt("state");
        status.error_code = jsonObject.optInt("errCode");
        status.error_desc = jsonObject.optString("errMsg");
        this.status = status;

        ARTICLELIST  data = new ARTICLELIST();
        data.fromJson(jsonObject.optJSONObject("datalist"));
        this.data = data;

        JSONArray subItemArray = jsonObject.optJSONArray("articlelist");
        if(null != subItemArray)
        {
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                ARTICLELIST subItem = new ARTICLELIST();
                subItem.fromJson(subItemObject);
                this.dataList.add(subItem);
            }
        }

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        if(null != status) {
            localItemObject.put("status", status.toJson());
        }
        if(null != data) {
            localItemObject.put("datalist", data.toJson());
        }

        return localItemObject;
    }

}
