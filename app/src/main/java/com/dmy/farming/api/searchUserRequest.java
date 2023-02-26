
package com.dmy.farming.api;

import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.protocol.PAGINATION;

import org.json.JSONException;
import org.json.JSONObject;

public class searchUserRequest
{
//     public SESSION session;
//     public PAGINATION pagination;
     public String info_from;
     public String page;
     public String search_word;
     public String search_type;
     public String userid;

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
          page = jsonObject.optString("page");
          search_word = jsonObject.optString("search_word");
          search_type = jsonObject.optString("search_type");
          userid = jsonObject.optString("userid");

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
          localItemObject.put("page", page);
          localItemObject.put("search_word", search_word);
          localItemObject.put("search_type", search_type);
          localItemObject.put("userid", userid);

          return localItemObject;
     }

}
