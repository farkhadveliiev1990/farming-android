
package com.dmy.farming.api.data;

import com.dmy.farming.api.data.chat.FARMARTICLE;
import com.dmy.farming.api.data.chat.RENTLIST;
import com.dmy.farming.api.data.chat.SALELIST;
import com.dmy.farming.protocol.PAGINATED;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class a_COLLECTION
{
    public Long lastUpdateTime = 0L;
    public STATUS lastStatus;

    public ArrayList<QUESTIONLIST> questionlists = new ArrayList<QUESTIONLIST>();
    public ArrayList<ARTICLELIST> articlelists = new ArrayList<ARTICLELIST>();
    public ArrayList<VIDEOLIST> videolists = new ArrayList<VIDEOLIST>();
    public ArrayList<SALELIST> salelists = new ArrayList<SALELIST>();
    public ArrayList<BUYLIST> buylists = new ArrayList<BUYLIST>();
    public ArrayList<RENTLIST> rentlists = new ArrayList<RENTLIST>();
    public ArrayList<FINDHELPLIST> findhelplists = new ArrayList<FINDHELPLIST>();
    public ArrayList<FARMARTICLE> farmarticles = new ArrayList<FARMARTICLE>();
    public ArrayList<DIAGNOSTIC> diagnostics = new ArrayList<DIAGNOSTIC>();
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
        JSONArray subItemArray = jsonObject.optJSONArray("datalist");
        if(null != subItemArray)
        {
            questionlists.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                QUESTIONLIST subItem = new QUESTIONLIST();
                subItem.fromJson(subItemObject);
                this.questionlists.add(subItem);
            }
        }

        subItemArray = jsonObject.optJSONArray("datalist");
        if(null != subItemArray)
        {
            videolists.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                VIDEOLIST subItem = new VIDEOLIST();
                subItem.fromJson(subItemObject);
                this.videolists.add(subItem);
            }
        }
        subItemArray = jsonObject.optJSONArray("datalist");
        if(null != subItemArray)
        {
            salelists.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                SALELIST subItem = new SALELIST();
                subItem.fromJson(subItemObject);
                this.salelists.add(subItem);
            }
        }

        subItemArray = jsonObject.optJSONArray("datalist");
        if(null != subItemArray)
        {
            articlelists.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                ARTICLELIST subItem = new ARTICLELIST();
                subItem.fromJson(subItemObject);
                this.articlelists.add(subItem);
            }
        }
        subItemArray = jsonObject.optJSONArray("datalist");
        if(null != subItemArray)
        {
            buylists.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                BUYLIST subItem = new BUYLIST();
                subItem.fromJson(subItemObject);
                this.buylists.add(subItem);
            }
        }

        subItemArray = jsonObject.optJSONArray("datalist");
        if(null != subItemArray)
        {
            rentlists.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                RENTLIST subItem = new RENTLIST();
                subItem.fromJson(subItemObject);
                this.rentlists.add(subItem);
            }
        }

        subItemArray = jsonObject.optJSONArray("datalist");
        if(null != subItemArray)
        {
            findhelplists.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                FINDHELPLIST subItem = new FINDHELPLIST();
                subItem.fromJson(subItemObject);
                this.findhelplists.add(subItem);
            }
        }
        subItemArray = jsonObject.optJSONArray("datalist");
        if(null != subItemArray)
        {
            farmarticles.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                FARMARTICLE subItem = new FARMARTICLE();
                subItem.fromJson(subItemObject);
                this.farmarticles.add(subItem);
            }
        }
        subItemArray = jsonObject.optJSONArray("datalist");
        if(null != subItemArray)
        {
            diagnostics.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                DIAGNOSTIC subItem = new DIAGNOSTIC();
                subItem.fromJson(subItemObject);
                this.diagnostics.add(subItem);
            }
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
//
        localItemObject.put("salelists", itemJSONArray);

        itemJSONArray = new JSONArray();
//        for(int i = 0; i< tags_route.size(); i++)
//        {
//            ROUTETAG itemData =tags_route.get(i);
//            JSONObject itemJSONObject = itemData.toJson();
//            itemJSONArray.put(itemJSONObject);
//        }
        localItemObject.put("videolists", itemJSONArray);

        itemJSONArray = new JSONArray();
        localItemObject.put("questionlists", itemJSONArray);

        itemJSONArray = new JSONArray();
        localItemObject.put("articlelists", itemJSONArray);

        itemJSONArray = new JSONArray();
        localItemObject.put("buylists", itemJSONArray);
        itemJSONArray = new JSONArray();
        localItemObject.put("rentlists", itemJSONArray);
        itemJSONArray = new JSONArray();
        localItemObject.put("findhelplists", itemJSONArray);

        itemJSONArray = new JSONArray();
        localItemObject.put("farmarticles", itemJSONArray);
        itemJSONArray = new JSONArray();
        localItemObject.put("diagnostics", itemJSONArray);


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
