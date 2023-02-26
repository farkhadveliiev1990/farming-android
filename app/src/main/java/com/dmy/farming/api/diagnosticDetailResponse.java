
package com.dmy.farming.api;

import com.dmy.farming.api.data.DIAGNOSTICDETAIL;
import com.dmy.farming.api.data.VIDEOLIST;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class diagnosticDetailResponse
{
    public STATUS status;
    public DIAGNOSTICDETAIL diagnosticdetail;
    public ArrayList<DIAGNOSTICDETAIL> similarvideo = new ArrayList<>();
 //   public ArrayList<COMMENT> comment = new ArrayList<>();


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

        DIAGNOSTICDETAIL diagnosticdetail = new DIAGNOSTICDETAIL();
        diagnosticdetail.fromJson(jsonObject.optJSONObject("datalist"));
        this.diagnosticdetail = diagnosticdetail;

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

        JSONArray subItemArray = jsonObject.optJSONArray("similarvideo");
        if(null != subItemArray)
        {
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                DIAGNOSTICDETAIL subItem = new DIAGNOSTICDETAIL();
                subItem.fromJson(subItemObject);
                this.similarvideo.add(subItem);
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

        if (diagnosticdetail != null){
            localItemObject.put("data", diagnosticdetail.toJson());
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
