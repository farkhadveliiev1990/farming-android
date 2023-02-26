
package com.dmy.farming.api;

import org.json.JSONException;
import org.json.JSONObject;

public class illegalsRequest
{
     public String info_from;

     // a1: WEIXIN, 2: QQ, 3 : WEIBO
     public String type;

     public String page;
     public String id;


     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

          info_from = jsonObject.optString("info_from");
          type = jsonObject.optString("type");
          page = jsonObject.optString("page");
          id = jsonObject.optString("id");
          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          localItemObject.put("info_from", info_from);
          localItemObject.put("type", type);
          localItemObject.put("page", page);
          localItemObject.put("id", id);

          return localItemObject;
     }

}
