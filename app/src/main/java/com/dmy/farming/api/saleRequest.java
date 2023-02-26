package com.dmy.farming.api;

import org.json.JSONException;
import org.json.JSONObject;

import static com.tencent.cos.common.COSHttpResponseKey.Data.SESSION;

public class saleRequest
{
    // public SESSION session;
     public String id;
    public String info_from;
    public String title;
    public String type_code;
    public String provience;
    public String city;
    public String district;
    public String content;
    public double price;
    public String unit;
    public String link_name;
    public String link_phone;
    public double lon;
    public double lat;
    public String address_details;
    public String img_url;
    public String phone;
    public String source;
    public int page;
    public String this_app;
    public String keyword;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

       /*   SESSION  session = new SESSION();
          session.fromJson(jsonObject.optJSONObject("session"));
          this.session = session;*/

          id = jsonObject.optString("id");
         info_from = jsonObject.optString("info_from");
         title = jsonObject.optString("title");
         type_code = jsonObject.optString("type_code");
         provience = jsonObject.optString("provience");
         city = jsonObject.optString("city");
         district = jsonObject.optString("district");
         content = jsonObject.optString("content");
         price = jsonObject.optDouble("price");
         unit = jsonObject.optString("unit");
         link_name = jsonObject.optString("link_name");
         link_phone = jsonObject.optString("link_phone");
         lon = jsonObject.optDouble("lon");
         lat = jsonObject.optDouble("lat");
         address_details = jsonObject.optString("address_details");
         img_url = jsonObject.optString("img_url");
         phone = jsonObject.optString("phone");
         source = jsonObject.optString("source");
         page = jsonObject.optInt("page");
         this_app = jsonObject.optString("this_app");
         keyword = jsonObject.optString("keyword");
          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

         /* if (null != session) {
               localItemObject.put("session", session.toJson());
          }*/

          localItemObject.put("id", id);
         localItemObject.put("info_from", info_from);
         localItemObject.put("title", title);
         localItemObject.put("type_code", type_code);
         localItemObject.put("provience", provience);
         localItemObject.put("city", city);
         localItemObject.put("district", district);
         localItemObject.put("content", content);
         localItemObject.put("price", price);
         localItemObject.put("unit", unit);
         localItemObject.put("link_name", link_name);
         localItemObject.put("link_phone", link_phone);
         localItemObject.put("lon", lon);
         localItemObject.put("lat", lat);
         localItemObject.put("address_details", address_details);
         localItemObject.put("img_url", img_url);
         localItemObject.put("phone", phone);
         localItemObject.put("source", source);
         localItemObject.put("page", page);
         localItemObject.put("this_app", this_app);
         localItemObject.put("keyword", keyword);
          return localItemObject;
     }

}
