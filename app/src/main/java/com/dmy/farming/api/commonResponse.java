
package com.dmy.farming.api;

import com.dmy.farming.protocol.STATUS;

import org.json.JSONException;
import org.json.JSONObject;

public class commonResponse
{
     public STATUS status;
     public String data;

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

          data = jsonObject.optString("datalist");
          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          if(null != status)
          {
               localItemObject.put("status", status.toJson());
          }

          localItemObject.put("data", data);
          return localItemObject;
     }
}
