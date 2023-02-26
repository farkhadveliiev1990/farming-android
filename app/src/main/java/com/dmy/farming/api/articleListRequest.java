
package com.dmy.farming.api;

import org.json.JSONException;
import org.json.JSONObject;

public class articleListRequest
{
     public String info_from;
     public String type_code;
     public String provience;
     public String city;
     public String district;
     public double lon;
     public double lat;
     public int page;
     public String this_app;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

          info_from = jsonObject.optString("info_from");
          type_code = jsonObject.optString("type_code");
          provience = jsonObject.optString("provience");
          city = jsonObject.optString("city");
          district = jsonObject.optString("district");
          lon = jsonObject.optDouble("lon");
          lat = jsonObject.optDouble("lat");
          page = jsonObject.optInt("page");
          this_app = jsonObject.optString("this_app");
          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          localItemObject.put("info_from", info_from);
          localItemObject.put("type_code", type_code);
          localItemObject.put("provience", provience);
          localItemObject.put("city", city);
          localItemObject.put("district", district);
          localItemObject.put("lon", lon);
          localItemObject.put("lat", lat);
          localItemObject.put("page", page);
          localItemObject.put("this_app", this_app);
          return localItemObject;
     }

}
