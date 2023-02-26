package com.dmy.farming.api;

import com.dmy.farming.api.data.USER;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONException;
import org.json.JSONObject;

public class userInfoResponse
{
     public STATUS status;
     public USER   user;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
               return ;
          }

          STATUS  status = new STATUS();
//          status.fromJson(jsonObject.optJSONObject("status"));
          status.error_code = jsonObject.optInt("errCode");
          status.error_desc = jsonObject.optString("errMsg");
          this.status = status;

          USER  user = new USER();
          user.fromJson(jsonObject.optJSONObject("datalist"));
          this.user = user;

          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          if(null != status)
          {
               localItemObject.put("status", status.toJson());
          }

          if(null != user)
          {
               localItemObject.put("user", user.toJson());
          }

          return localItemObject;
     }

}
