
package com.dmy.farming.api;

import com.dmy.farming.api.data.DIAGNOSTICDETAIL;
import com.dmy.farming.api.data.KEYWORD;
import com.dmy.farming.api.data.VIDEOLIST;
import com.dmy.farming.api.data.chat.FARMARTICLE;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class farmarticleDetailResponse
{
    public STATUS status;
    public FARMARTICLE farmarticle;
 //   public ArrayList<COMMENT> comment = new ArrayList<>();
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

        FARMARTICLE farmarticle = new FARMARTICLE();
        farmarticle.fromJson(jsonObject.optJSONObject("datalist"));
        this.farmarticle = farmarticle;

        JSONArray subItemArray = jsonObject.optJSONArray("keyword");
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

       /*  subItemArray = jsonObject.optJSONArray("data");
        if(null != subItemArray)
        {
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                COMMENT subItem = new COMMENT();
                subItem.fromJson(subItemObject);
                this.comment.add(subItem);
            }
        }*/

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        if(null != status)
        {
            localItemObject.put("status", status.toJson());
        }

        if (farmarticle != null){
            localItemObject.put("data", farmarticle.toJson());
        }

        /*JSONArray itemJSONArray = new JSONArray();
        for(int i=0; i< data.size(); i++)
        {
            VIDEOLIST itemData = data.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("data", itemJSONArray);*/


        return localItemObject;
    }

}
