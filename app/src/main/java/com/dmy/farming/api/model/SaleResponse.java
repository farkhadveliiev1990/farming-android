package com.dmy.farming.api.model;

import com.dmy.farming.api.data.BUYLIST;
import com.dmy.farming.api.data.chat.SALELIST;
import com.dmy.farming.protocol.PAGINATED;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SaleResponse
{
     public STATUS status;
     public PAGINATED paginated;

     public ArrayList<SALELIST> data = new ArrayList<>();

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

          STATUS  status = new STATUS();
//          status.fromJson(jsonObject.optJSONObject("statu"));
          status.succeed = jsonObject.optInt("state");
          status.error_code = jsonObject.optInt("errCode");
          status.error_desc = jsonObject.optString("errMsg");
          this.status = status;

          PAGINATED  paginated = new PAGINATED();
//          paginated.fromJson(jsonObject.optJSONObject("ismore"));
          paginated.more = jsonObject.optInt("ismore");
          paginated.count = jsonObject.optInt("count");
          this.paginated = paginated;

          JSONArray subItemArray = jsonObject.optJSONArray("datalist");
          if(null != subItemArray)
          {
               for(int i = 0;i < subItemArray.length();i++)
               {
                    JSONObject subItemObject = subItemArray.getJSONObject(i);
                    SALELIST subItem = new SALELIST();
                    subItem.fromJson(subItemObject);
                    this.data.add(subItem);
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

          JSONArray itemJSONArray = new JSONArray();
          for(int i=0; i< data.size(); i++)
          {
               SALELIST itemData = data.get(i);
               JSONObject itemJSONObject = itemData.toJson();
               itemJSONArray.put(itemJSONObject);
          }
          localItemObject.put("data", itemJSONArray);

          if(null != paginated)
          {
               localItemObject.put("paginated", paginated.toJson());
          }

          return localItemObject;
     }

}
