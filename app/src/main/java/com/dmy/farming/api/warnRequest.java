
package com.dmy.farming.api;

import org.json.JSONException;
import org.json.JSONObject;

public class warnRequest
{
     public String info_from;
     public String province;
     public String city;
     public String district;
     public int page;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

          info_from = jsonObject.optString("info_from");
          province = jsonObject.optString("province");
          city = jsonObject.optString("city");
          district = jsonObject.optString("district");
          page = jsonObject.optInt("page");
          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          localItemObject.put("info_from", info_from);
          localItemObject.put("province", province);
          localItemObject.put("city", city);
          localItemObject.put("district", district);
          localItemObject.put("page", page);
          return localItemObject;
     }

}
