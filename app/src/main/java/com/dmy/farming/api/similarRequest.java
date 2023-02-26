
package com.dmy.farming.api;

import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.protocol.PAGINATION;

import org.json.JSONException;
import org.json.JSONObject;

public class similarRequest
{

     public String info_from;
     public String cycle;
     public String word;
     public String city;
     public String articletype;
     public String id;
     public int page;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

          info_from = jsonObject.optString("info_from");
          id = jsonObject.optString("id");
          cycle = jsonObject.optString("cycle");
          word = jsonObject.optString("word");
          city = jsonObject.optString("city");
          articletype = jsonObject.optString("articletype");
          id = jsonObject.optString("id");
          page = jsonObject.optInt("page");

          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          localItemObject.put("id", id);
          localItemObject.put("cycle", cycle);
          localItemObject.put("word", word);
          localItemObject.put("city", city);
          localItemObject.put("articletype", articletype);
          localItemObject.put("page", page);
          localItemObject.put("info_from", info_from);

          return localItemObject;
     }

}
