
package com.dmy.farming.protocol;

import com.external.activeandroid.annotation.Column;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class STATUS
{

     @Column(name = "succeed")
     public int succeed;

     @Column(name = "error_code")
     public int error_code;

     @Column(name = "error_desc")
     public String error_desc;

 public  void fromJson(JSONObject jsonObject)  throws JSONException
 {
     if(null == jsonObject){
       return ;
      }

     JSONArray subItemArray;

     this.succeed = jsonObject.optInt("succeed");

     this.error_code = jsonObject.optInt("errCode");

     this.error_desc = jsonObject.optString("errMsg");
     return ;
 }

 public JSONObject  toJson() throws JSONException 
 {
     JSONObject localItemObject = new JSONObject();
     JSONArray itemJSONArray = new JSONArray();
     localItemObject.put("succeed", succeed);
     localItemObject.put("errCode", error_code);
     localItemObject.put("errMsg", error_desc);
     return localItemObject;
 }

}
