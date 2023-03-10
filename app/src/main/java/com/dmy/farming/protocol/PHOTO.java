
package com.dmy.farming.protocol;

import com.external.activeandroid.annotation.Column;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PHOTO
{

	@Column(name = "small")
    public String small;	//小图
	
    @Column(name = "thumb")
    public String thumb;	//中图

    @Column(name = "url")
    public String url;		//大图
     
     

 public void fromJson(JSONObject jsonObject)  throws JSONException
 {
     if(null == jsonObject){
       return ;
      }


     JSONArray subItemArray;

     this.small = jsonObject.optString("small");
     this.thumb = jsonObject.optString("thumb");
     this.url = jsonObject.optString("url");
     return ;
 }

 public JSONObject  toJson() throws JSONException 
 {
     JSONObject localItemObject = new JSONObject();
     JSONArray itemJSONArray = new JSONArray();
     localItemObject.put("small", small);
     localItemObject.put("thumb", thumb);
     localItemObject.put("url", url);
     return localItemObject;
 }

}
