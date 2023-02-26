
package com.dmy.farming.api.data.chat;

import com.dmy.farming.api.data.CROPCYCLE;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.protocol.PAGINATED;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class a_CROPCYCLE
{
    public Long lastUpdateTime = 0L;

    public ArrayList<CROPCYCLE> crop_cycle = new ArrayList<CROPCYCLE>();


    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return ;
        }
        //
        JSONArray subItemArray = jsonObject.optJSONArray("data");
        if(null != subItemArray)
        {
            crop_cycle.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                CROPCYCLE subItem = new CROPCYCLE();
                subItem.fromJson(subItemObject);
                this.crop_cycle.add(subItem);
            }
        }

        PAGINATED paginated_recom_routes = new PAGINATED();
        paginated_recom_routes.fromJson(jsonObject.optJSONObject("paginated_recom_routes"));
//        this.paginated_recom_routes = paginated_recom_routes;

        subItemArray = jsonObject.optJSONArray("home_groups");
        if(null != subItemArray)
        {
//            home_groups.clear();
//            for(int i = 0;i < subItemArray.length();i++)
//            {
//                JSONObject subItemObject = subItemArray.getJSONObject(i);
//                GROUPLIST subItem = new GROUPLIST();
//                subItem.fromJson(subItemObject);
//                this.home_groups.add(subItem);
//            }
        }

        PAGINATED paginated_groups = new PAGINATED();
        paginated_groups.fromJson(jsonObject.optJSONObject("paginated_groups"));
//        this.paginated_groups = paginated_groups;

        //
        lastUpdateTime = jsonObject.optLong("lastUpdateTime");
        //
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        //
        JSONArray itemJSONArray = new JSONArray();
        itemJSONArray = new JSONArray();
        localItemObject.put("crop_cycle", itemJSONArray);



        localItemObject.put("lastUpdateTime", lastUpdateTime);
        //
        return localItemObject;
    }
}
