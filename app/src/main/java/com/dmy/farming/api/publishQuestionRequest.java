
package com.dmy.farming.api;

import org.json.JSONException;
import org.json.JSONObject;

public class publishQuestionRequest
{
     public String info_from;
     public String user_phone;
     public String crerate_time;
     public String img_url;
     public String crop_type;
     public String provience;
     public String city;
     public String district;
     public String content;
     public String key_word;
     public String problem_type;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

          info_from = jsonObject.optString("info_from");
          user_phone = jsonObject.optString("user_phone");
          crerate_time = jsonObject.optString("crerate_time");
          img_url = jsonObject.optString("img_url");
          crop_type = jsonObject.optString("crop_type");
          provience = jsonObject.optString("provience");
          city = jsonObject.optString("city");
          district = jsonObject.optString("district");
          content = jsonObject.optString("content");
          key_word = jsonObject.optString("key_word");
          problem_type = jsonObject.optString("problem_type");
          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          localItemObject.put("info_from", info_from);
          localItemObject.put("user_phone", user_phone);
          localItemObject.put("crerate_time", crerate_time);
          localItemObject.put("img_url", img_url);
          localItemObject.put("crop_type", crop_type);
          localItemObject.put("provience", provience);
          localItemObject.put("city", city);
          localItemObject.put("district", district);
          localItemObject.put("content", content);
          localItemObject.put("key_word", key_word);
          localItemObject.put("problem_type", problem_type);

          return localItemObject;
     }

}
