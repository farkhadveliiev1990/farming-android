package com.dmy.farming.api;
import com.dmy.farming.api.data.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

public class changePwd2Request
{
     public SESSION session;

     public String oldpass;
     public String newpass;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

          SESSION  session = new SESSION();
          session.fromJson(jsonObject.optJSONObject("session"));
          this.session = session;

          oldpass = jsonObject.optString("oldpass");
          newpass = jsonObject.optString("newpass");
          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          if (null != session) {
               localItemObject.put("session", session.toJson());
          }

          localItemObject.put("oldpass", oldpass);
          localItemObject.put("newpass", newpass);
          return localItemObject;
     }
}
