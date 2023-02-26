
package com.dmy.farming.api.data.chat;

import com.dmy.farming.protocol.PAGINATED;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class a_COMMENT
{

    public Long lastUpdateTime = 0L;

    public ArrayList<COMMENT> comment = new ArrayList<COMMENT>();

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return ;
        }
        //
        JSONArray subItemArray = jsonObject.optJSONArray("data");
        if(null != subItemArray)
        {
            comment.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                COMMENT subItem = new COMMENT();
                subItem.fromJson(subItemObject);
                this.comment.add(subItem);
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
        localItemObject.put("farmarticles", itemJSONArray);



        localItemObject.put("lastUpdateTime", lastUpdateTime);
        //
        return localItemObject;
    }


}
