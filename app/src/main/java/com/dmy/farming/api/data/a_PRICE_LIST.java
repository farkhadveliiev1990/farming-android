package com.dmy.farming.api.data;


import com.dmy.farming.api.data.chat.PRICELIST;
import com.dmy.farming.api.data.chat.RENTLIST;
import com.dmy.farming.protocol.PAGINATED;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class a_PRICE_LIST
{
    public Long lastUpdateTime = 0L;
    public PAGINATED paginated = new PAGINATED();
    public ArrayList<PRICELIST> prices = new ArrayList<PRICELIST>();

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return ;
        }
        //
        JSONArray subItemArray = jsonObject.optJSONArray("data");
        if(null != subItemArray)
        {
            prices.clear();
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                PRICELIST subItem = new PRICELIST();
                subItem.fromJson(subItemObject);
                this.prices.add(subItem);
            }
        }
        //
     /*   PAGINATED paginated = new PAGINATED();
        paginated.fromJson(jsonObject.optJSONObject("paginated"));
        this.paginated = paginated;*/
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
        for(int i = 0; i< prices.size(); i++)
        {
            PRICELIST itemData = prices.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("data", itemJSONArray);
        //
     /*   if (paginated != null)
            localItemObject.put("paginated", paginated.toJson());*/
        //
        localItemObject.put("lastUpdateTime", lastUpdateTime);
        //
        return localItemObject;
    }
}
