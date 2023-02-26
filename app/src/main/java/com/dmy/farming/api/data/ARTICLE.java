
package com.dmy.farming.api.data;

import com.dmy.farming.api.data.chat.FARMARTICLE;
import com.dmy.farming.protocol.PAGINATED;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ARTICLE
{
    public Long lastUpdateTime = 0L;

    public String title;
    public String content;
    public ArrayList<FARMARTICLE> article = new ArrayList<FARMARTICLE>();

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

    /*    this.title = jsonObject.optString("title");
        this.content = jsonObject.optString("content");

        //
        lastUpdateTime = jsonObject.optLong("lastUpdateTime");*/

        JSONArray subItemArray = jsonObject.optJSONArray("datalist");
        if(null != subItemArray)
        {
            article.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                FARMARTICLE subItem = new FARMARTICLE();
                subItem.fromJson(subItemObject);
                this.article.add(subItem);
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

        JSONArray itemJSONArray = new JSONArray();
//        for(int i = 0; i< adver_top.size(); i++)
//        {
//            ADVER itemData =adver_top.get(i);
//            JSONObject itemJSONObject = itemData.toJson();
//            itemJSONArray.put(itemJSONObject);
//        }
        localItemObject.put("data", itemJSONArray);

        localItemObject.put("title", title);
        localItemObject.put("content", content);

        localItemObject.put("lastUpdateTime", lastUpdateTime);
        return localItemObject;
    }
}
