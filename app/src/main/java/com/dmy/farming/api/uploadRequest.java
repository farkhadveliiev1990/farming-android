package com.dmy.farming.api;

import com.dmy.farming.api.data.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

public class uploadRequest
{
     public SESSION session;

     // 0 图片文件 1:Sound 2:video
     public final static int FILETYPE_IMAGE = 0;
     public final static int FILETYPE_SOUND = 1;
     public final static int FILETYPE_VIDEO = 2;
     public int filetype;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

          SESSION  session = new SESSION();
          session.fromJson(jsonObject.optJSONObject("session"));
          this.session = session;

          filetype = jsonObject.optInt("filetype");
          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          if (null != session) {
               localItemObject.put("session", session.toJson());
          }

          localItemObject.put("filetype", filetype);
          return localItemObject;
     }

}
