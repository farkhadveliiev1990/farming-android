
package com.dmy.farming.api;

import com.dmy.farming.api.data.SEARCHHISTORY;
import com.dmy.farming.protocol.PAGINATED;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class searchHistoryResponse
{
    public STATUS status;
    public PAGINATED paginated;
    public ArrayList<SEARCHHISTORY> historyList = new ArrayList<>();


    public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        STATUS  status = new STATUS();
//        status.fromJson(jsonObject.optJSONObject("status"));
        status.error_code = jsonObject.optInt("errCode");
        status.error_desc = jsonObject.optString("errMsg");
        this.status = status;

        PAGINATED  paginated = new PAGINATED();
//        paginated.fromJson(jsonObject.optJSONObject("paginated"));
        paginated.more = jsonObject.optInt("ismore");
        paginated.count = jsonObject.optInt("count");
        this.paginated = paginated;

        //
        JSONArray qunItemArray = jsonObject.optJSONArray("datalist");
        if(null != qunItemArray)
        {
            for(int i = 0;i < qunItemArray.length();i++)
            {
                JSONObject subItemObject = qunItemArray.getJSONObject(i);
                SEARCHHISTORY subItem = new SEARCHHISTORY();
                subItem.fromJson(subItemObject);
                this.historyList.add(subItem);
            }
        }

        return;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        if(null != status)
        {
            localItemObject.put("status", status.toJson());
        }

        if(null != paginated)
        {
            localItemObject.put("paginated", paginated.toJson());
        }

        //
        JSONArray qunItemArray = new JSONArray();
        for(int i=0; i< historyList.size(); i++)
        {
            SEARCHHISTORY itemData = historyList.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            qunItemArray.put(itemJSONObject);
        }
        localItemObject.put("datalist", qunItemArray);

        return localItemObject;
    }

}
