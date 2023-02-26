
package com.dmy.farming.api;

import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.protocol.PAGINATION;

import org.json.JSONException;
import org.json.JSONObject;

public class commonRequest
{
     public SESSION session;
     public PAGINATION pagination;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

          SESSION  session = new SESSION();
          session.fromJson(jsonObject.optJSONObject("session"));
          this.session = session;

          PAGINATION  pagination = new PAGINATION();
          pagination.fromJson(jsonObject.optJSONObject("pagination"));
          this.pagination = pagination;
          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          if (null != session) {
               localItemObject.put("session", session.toJson());
          }

          if (null != pagination) {
               localItemObject.put("pagination", pagination.toJson());
          }

          return localItemObject;
     }

}
