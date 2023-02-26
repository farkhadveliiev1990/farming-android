
package com.dmy.farming.api;

import org.json.JSONException;
import org.json.JSONObject;

public class MarkerPriceRequest
{
     public String info_from;
     public String crop_type;
     public String provience;
     public String city;
     public String district;
     public int page;
     public int this_app;
     public String type;
     public String id;
     public String turn_price;
     public String price;
     public String price1;
     public String user_phone;
     public String pricetype;
     public String priceunit;


     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

          info_from = jsonObject.optString("info_from");
          crop_type = jsonObject.optString("crop_type");
          provience = jsonObject.optString("provience");
          city = jsonObject.optString("city");
          district = jsonObject.optString("district");
          page = jsonObject.optInt("page");
          this_app = jsonObject.optInt("this_app");
          type = jsonObject.optString("type");
          id = jsonObject.optString("id");
          turn_price = jsonObject.optString("turn_price");
          price = jsonObject.optString("price");
          price1 = jsonObject.optString("price1");
          pricetype = jsonObject.optString("pricetype");
          user_phone = jsonObject.optString("user_phone");
          priceunit = jsonObject.optString("priceunit");
          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          localItemObject.put("info_from", info_from);
          localItemObject.put("crop_type", crop_type);
          localItemObject.put("provience", provience);
          localItemObject.put("city", city);
          localItemObject.put("district", district);
          localItemObject.put("page", page);
          localItemObject.put("this_app", this_app);
          localItemObject.put("type", type);
          localItemObject.put("id", id);
          localItemObject.put("turn_price", turn_price);
          localItemObject.put("price", price);
          localItemObject.put("price1", price1);
          localItemObject.put("pricetype", pricetype);
          localItemObject.put("user_phone", user_phone);
          localItemObject.put("priceunit", priceunit);

          return localItemObject;
     }

}
