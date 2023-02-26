
package com.dmy.farming.api;

import com.dmy.farming.api.data.HELPCENTER;
import com.dmy.farming.api.data.illegal;
import com.dmy.farming.protocol.PAGINATED;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class illegalsResponse
{
    public STATUS status;
  //  public HELPCENTER data;
    public PAGINATED paginated;
    public ArrayList<illegal> data = new ArrayList<>();


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

        JSONArray subItemArray = jsonObject.optJSONArray("datalist");
        if(null != subItemArray)
        {
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                illegal subItem = new illegal();
                subItem.fromJson(subItemObject);
                this.data.add(subItem);
            }
        }

      /*  JSONArray subItemArray = jsonObject.optJSONArray("data");
        if(null != subItemArray)
        {
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                HELPCENTER subItem = new HELPCENTER();
                subItem.fromJson(subItemObject);
                this.data.add(subItem);
            }
        }*/

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        if(null != status) {
            localItemObject.put("status", status.toJson());
        }
        JSONArray itemJSONArray = new JSONArray();
        for(int i=0; i< data.size(); i++)
        {
            illegal itemData = data.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("data", itemJSONArray);

       /* JSONArray itemJSONArray = new JSONArray();
        for(int i=0; i< data.size(); i++)
        {
            HELPCENTER itemData = data.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("data", itemJSONArray);*/

        if(null != paginated)
        {
            localItemObject.put("paginated", paginated.toJson());
        }

        return localItemObject;
    }

}
