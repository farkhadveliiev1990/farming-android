
package com.dmy.farming.api;

import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.protocol.PAGINATION;

import org.json.JSONException;
import org.json.JSONObject;

public class searchHistoryRequest
{
//     public SESSION session;
//     public PAGINATION pagination;
     public String info_from;
     public String userid;
     public int page;
//     public double posX;
//     public double posY;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

//          SESSION  session = new SESSION();
//          session.fromJson(jsonObject.optJSONObject("session"));
//          this.session = session;

//          PAGINATION pagination = new PAGINATION();
//          pagination.fromJson(jsonObject.optJSONObject("pagination"));
//          this.pagination = pagination;

          info_from = jsonObject.optString("info_from");
          userid = jsonObject.optString("user");
          page = jsonObject.optInt("page");
//          posX = jsonObject.optDouble("posX");
//          posY = jsonObject.optDouble("posY");
          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

//          if (null != session) {
//               localItemObject.put("session", session.toJson());
//          }

//          if (null != pagination) {
//               localItemObject.put("pagination", pagination.toJson());
//          }

          localItemObject.put("info_from", info_from);
          localItemObject.put("user", userid);
          localItemObject.put("page", page);
//          localItemObject.put("posX", posX);
//          localItemObject.put("posY", posY);
          return localItemObject;
     }

}
