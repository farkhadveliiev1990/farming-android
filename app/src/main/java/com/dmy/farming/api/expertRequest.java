package com.dmy.farming.api;

import org.json.JSONException;
import org.json.JSONObject;

public class expertRequest
{
    // public SESSION session;
     public String id;
     public String info_from;
    public String expert_id;



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
         expert_id = jsonObject.optString("expert_id");
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
         localItemObject.put("expert_id", expert_id);
          return localItemObject;
     }

}
