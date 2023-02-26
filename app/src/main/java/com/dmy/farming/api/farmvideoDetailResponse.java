
package com.dmy.farming.api;

import com.dmy.farming.api.data.VIDEOLIST;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class farmvideoDetailResponse
{
    public STATUS status;
    public  VIDEOLIST video;
    public ArrayList<VIDEOLIST> similarvideo = new ArrayList<>();


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

        VIDEOLIST farmvideo = new VIDEOLIST();
        farmvideo.fromJson(jsonObject.optJSONObject("datalist"));
        this.video = farmvideo;

        JSONArray subItemArray = jsonObject.optJSONArray("similarvideo");
        if(null != subItemArray)
        {
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                VIDEOLIST subItem = new VIDEOLIST();
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

        if (video != null){
            localItemObject.put("datalist", video.toJson());
        }

        JSONArray itemJSONArray = new JSONArray();
        for(int i=0; i< similarvideo.size(); i++)
        {
            VIDEOLIST itemData = similarvideo.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("similarvideo", itemJSONArray);


        return localItemObject;
    }

}
