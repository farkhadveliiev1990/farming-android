
package com.dmy.farming.api;

import org.json.JSONException;
import org.json.JSONObject;

public class callbackRequest
{
     public String code;
     public double price;
     public int payment_id;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

          code = jsonObject.optString("code");
          price = jsonObject.optDouble("price");
          payment_id = jsonObject.optInt("payment_id");
          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          localItemObject.put("code", code);
          localItemObject.put("price", price);
          localItemObject.put("payment_id", payment_id);
          return localItemObject;
     }

}
