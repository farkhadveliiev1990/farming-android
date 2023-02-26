
package com.dmy.farming.api;

import com.dmy.farming.api.data.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

public class accusationRequest
{
     public SESSION session;

     public long target_id; //个人ID 或  群ID
     public int target_type; //0:个人 1:群
     public String content; //举报内容
     public String consult_type; //举报类型

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

          SESSION  session = new SESSION();
          session.fromJson(jsonObject.optJSONObject("session"));
          this.session = session;

          target_id = jsonObject.optLong("target_id");
          target_type = jsonObject.optInt("target_type");
          content = jsonObject.optString("content");
          consult_type = jsonObject.optString("consult_type");
          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          localItemObject.put("target_id", target_id);
          localItemObject.put("target_type", target_type);
          localItemObject.put("content", content);
          localItemObject.put("consult_type", consult_type);

          if (null != session) {
               localItemObject.put("session", session.toJson());
          }
          return localItemObject;
     }

}
