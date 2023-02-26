
package com.dmy.farming.api;

import org.json.JSONException;
import org.json.JSONObject;

public class diagnosticRequest
{
     public String type;
     public String id;
     public String info_from;
     public String crop_type;
     public String cycle_code;
     public int page;
     public int sort;
     public String provice;
     public String city;


     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

          type = jsonObject.optString("type");
          id = jsonObject.optString("id");
          info_from = jsonObject.optString("info_from");
          crop_type = jsonObject.optString("crop_type");
          cycle_code = jsonObject.optString("cycle_code");
          page = jsonObject.optInt("page");
          sort = jsonObject.optInt("sort");
          provice = jsonObject.optString("provice");
          city = jsonObject.optString("city");


          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          localItemObject.put("type", type);
          localItemObject.put("id", id);
          localItemObject.put("info_from", info_from);
          localItemObject.put("crop_type", crop_type);
          localItemObject.put("cycle_code", cycle_code);
          localItemObject.put("page", page);
          localItemObject.put("sort", sort);
          localItemObject.put("provice", provice);
          localItemObject.put("city", city);
          return localItemObject;
     }

}
