
package com.dmy.farming.api.data;

import com.dmy.farming.api.data.chat.REPLY;
import com.dmy.farming.protocol.PAGINATED;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class a_QUESTION_DETAIL
{
    public Long lastUpdateTime = 0L;

    public QUESTION question;
    public ArrayList<REPLY> reply_list = new ArrayList();

//    public ArrayList<ROUTETAG> tags_route = new ArrayList<ROUTETAG>();
//
//    public PAGINATED paginated_recom_routes = new PAGINATED();
//    public ArrayList<ROUTELIST> recom_routes = new ArrayList<ROUTELIST>();
//
//    public PAGINATED paginated_recom_activities = new PAGINATED();
//    public ArrayList<RECOMLIST> recom_activities = new ArrayList<RECOMLIST>();
//
//    public PAGINATED paginated_activities = new PAGINATED();
//    public ArrayList<ACTIVITYLIST> home_activities = new ArrayList<ACTIVITYLIST>();


    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return ;
        }
        //
        JSONArray subItemArray = jsonObject.optJSONArray("adver_top");
        if(null != subItemArray)
        {
//            adver_top.clear();
//            for(int i = 0;i < subItemArray.length();i++)
//            {
//                JSONObject subItemObject = subItemArray.getJSONObject(i);
//                ADVER subItem = new ADVER();
//                subItem.fromJson(subItemObject);
//                this.adver_top.add(subItem);
//            }
        }

        subItemArray = jsonObject.optJSONArray("tags_route");
        if(null != subItemArray)
        {
//            tags_route.clear();
//            for(int i = 0;i < subItemArray.length();i++)
//            {
//                JSONObject subItemObject = subItemArray.getJSONObject(i);
//                ROUTETAG subItem = new ROUTETAG();
//                subItem.fromJson(subItemObject);
//                this.tags_route.add(subItem);
//            }
        }

        subItemArray = jsonObject.optJSONArray("recom_routes");
        if(null != subItemArray)
        {
//            recom_routes.clear();
//            for(int i = 0;i < subItemArray.length();i++)
//            {
//                JSONObject subItemObject = subItemArray.getJSONObject(i);
//                ROUTELIST subItem = new ROUTELIST();
//                subItem.fromJson(subItemObject);
//                this.recom_routes.add(subItem);
//            }
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
//        for(int i = 0; i< adver_top.size(); i++)
//        {
//            ADVER itemData =adver_top.get(i);
//            JSONObject itemJSONObject = itemData.toJson();
//            itemJSONArray.put(itemJSONObject);
//        }
        localItemObject.put("adver_top", itemJSONArray);

        itemJSONArray = new JSONArray();
//        for(int i = 0; i< tags_route.size(); i++)
//        {
//            ROUTETAG itemData =tags_route.get(i);
//            JSONObject itemJSONObject = itemData.toJson();
//            itemJSONArray.put(itemJSONObject);
//        }
        localItemObject.put("tags_route", itemJSONArray);

        itemJSONArray = new JSONArray();
//        for(int i = 0; i< recom_routes.size(); i++)
//        {
//            ROUTELIST itemData =recom_routes.get(i);
//            JSONObject itemJSONObject = itemData.toJson();
//            itemJSONArray.put(itemJSONObject);
//        }
        localItemObject.put("recom_routes", itemJSONArray);

//        if (paginated_recom_routes != null)
//            localItemObject.put("paginated_recom_routes", paginated_recom_routes.toJson());

        itemJSONArray = new JSONArray();
//        for(int i = 0; i< home_groups.size(); i++)
//        {
//            GROUPLIST itemData =home_groups.get(i);
//            JSONObject itemJSONObject = itemData.toJson();
//            itemJSONArray.put(itemJSONObject);
//        }
        localItemObject.put("home_groups", itemJSONArray);

//        if (paginated_groups != null)
//            localItemObject.put("paginated_groups", paginated_groups.toJson());

        localItemObject.put("lastUpdateTime", lastUpdateTime);
        //
        return localItemObject;
    }
}
