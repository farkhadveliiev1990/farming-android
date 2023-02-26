
package com.dmy.farming.protocol;

import com.external.activeandroid.annotation.Column;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PAGINATED
{

     @Column(name = "total")
     public int total;

     @Column(name = "more")
     public int more;

     @Column(name = "count")
     public int count;

 public void fromJson(JSONObject jsonObject)  throws JSONException
 {
     if(null == jsonObject){
       return ;
      }


     JSONArray subItemArray;

     this.total = jsonObject.optInt("total");

     this.more = jsonObject.optInt("ismore");

     this.count = jsonObject.optInt("count");
     return ;
 }

 public JSONObject  toJson() throws JSONException 
 {
     JSONObject localItemObject = new JSONObject();
     JSONArray itemJSONArray = new JSONArray();
     localItemObject.put("total", total);
     localItemObject.put("ismore", more);
     localItemObject.put("count", count);
     return localItemObject;
 }

}
