
package com.dmy.farming.protocol;

import com.external.activeandroid.annotation.Column;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PAGINATION
{

     @Column(name = "count")
     public int count;

     @Column(name = "page")
     public int page;

 public void fromJson(JSONObject jsonObject)  throws JSONException
 {
     if(null == jsonObject){
       return ;
      }


     JSONArray subItemArray;

     this.count = jsonObject.optInt("count");
     this.page = jsonObject.optInt("page");
     return ;
 }

 public JSONObject  toJson() throws JSONException 
 {
     JSONObject localItemObject = new JSONObject();
     JSONArray itemJSONArray = new JSONArray();
     localItemObject.put("count", count);
     localItemObject.put("page", page);
     return localItemObject;
 }

}
