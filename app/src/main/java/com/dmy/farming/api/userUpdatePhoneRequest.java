
package com.dmy.farming.api;

import com.dmy.farming.api.data.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

public class userUpdatePhoneRequest
{
     public SESSION session;

     public String newphone;

     // 登陆密码
     public String password;

     // 验证码
     public String idcode;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

          SESSION  session = new SESSION();
          session.fromJson(jsonObject.optJSONObject("session"));
          this.session = session;

          newphone = jsonObject.optString("newphone");
          password = jsonObject.optString("password");
          idcode = jsonObject.optString("idcode");
          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          if (null != session) {
               localItemObject.put("session", session.toJson());
          }

          localItemObject.put("newphone", newphone);
          localItemObject.put("password", password);
          localItemObject.put("idcode", idcode);
          return localItemObject;
     }
}
