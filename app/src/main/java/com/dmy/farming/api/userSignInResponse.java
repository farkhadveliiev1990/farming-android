
package com.dmy.farming.api;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.USER;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONException;
import org.json.JSONObject;

public class userSignInResponse
{
     public STATUS status;
     public SESSION session;
     public USER user;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
               return ;
          }

          STATUS  status = new STATUS();
//          status.fromJson(jsonObject.optJSONObject("status"));
          status.error_code = jsonObject.optInt("errCode");
          status.error_desc = jsonObject.optString("errMsg");
          status.succeed = jsonObject.optInt("state");
          this.status = status;

          USER  user = new USER();
          user.fromJson(jsonObject.optJSONObject("datalist"));
          this.user = user;

          SESSION  session = new SESSION();
//          session.fromJson(jsonObject.optJSONObject("session"));
          session.uid = this.user.id;
          session.sid = this.user.username;
          session.usertype = this.user.identify;
          this.session = session;

          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          if (null != status)
          {
               localItemObject.put("status", status.toJson());
          }

          if (null != session)
          {
               localItemObject.put("session", session.toJson());
          }

          if (null != user)
          {
               localItemObject.put("user", user.toJson());
          }

          return localItemObject;
     }

}
