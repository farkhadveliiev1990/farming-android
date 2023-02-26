
package com.dmy.farming.api.data;

import org.json.JSONException;
import org.json.JSONObject;

public class illegal
{
    public Long lastUpdateTime = 0L;

    public String id ;
    public String text;
    public String searchTime ;
    public String addWay ;
    public String publishTime;
    public String status ;

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.text = jsonObject.optString("text");
        this.searchTime = jsonObject.optString("searchTime");
        this.addWay = jsonObject.optString("addWay");
        this.publishTime = jsonObject.optString("publishTime");
        this.status = jsonObject.optString("status");
        //
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("text", text);
        localItemObject.put("searchTime", searchTime);
        localItemObject.put("addWay", addWay);
        localItemObject.put("publishTime", publishTime);
        localItemObject.put("status", status);
        return localItemObject;
    }

}
