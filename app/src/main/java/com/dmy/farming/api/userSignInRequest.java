package com.dmy.farming.api;
import org.json.JSONException;
import org.json.JSONObject;

public class userSignInRequest
{
     public String info_from;
     public String phone;
     public String reqcode;
     public String password;
     public String province;
     public String city;
     public String district;

     // "baidu_channelId"
     public String channel_id;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject) {
               return ;
          }

          this.info_from = jsonObject.optString("info_from");
          this.phone = jsonObject.optString("phone");
          this.reqcode = jsonObject.optString("reqcode");
          this.password = jsonObject.optString("password");
          this.province = jsonObject.optString("province");
          this.city = jsonObject.optString("city");
          this.district = jsonObject.optString("district");
          this.channel_id = jsonObject.optString("channel_id");

          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          localItemObject.put("info_from", info_from);
          localItemObject.put("phone", phone);
          localItemObject.put("reqcode", reqcode);
          localItemObject.put("password", password);
          localItemObject.put("province", province);
          localItemObject.put("city", city);
          localItemObject.put("district", district);
          localItemObject.put("channel_id", channel_id);

          return localItemObject;
     }
}
