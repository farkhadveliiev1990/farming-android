
package com.dmy.farming.protocol;

import org.json.JSONException;
import org.json.JSONObject;

public class userReqcodeRequest
{
     public String phone;

     // 0：找回密码，1：APP端注册， 2：微信端注册
     public int type;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

          this.phone = jsonObject.optString("phone");
          this.type = jsonObject.optInt("type");

          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          localItemObject.put("phone", phone);
          localItemObject.put("type", type);

          return localItemObject;
     }
}
