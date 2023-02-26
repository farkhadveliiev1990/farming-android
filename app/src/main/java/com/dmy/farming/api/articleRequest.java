
package com.dmy.farming.api;

import org.json.JSONException;
import org.json.JSONObject;

public class articleRequest
{
     // 'register','注册协议'  disclaimer:'免责生命'  'terms':'使用条款' 'privacy':'隐私政策'
     public String type;
     public String id;
     public String info_from;
     public String userid;
     public String city;
     public String from_openid;
     public String comment_id;
     public String about_userid;
     public String publish_userid;
     public String about_id;
     public int like_type;
     public String user_id;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

          type = jsonObject.optString("type");
          id = jsonObject.optString("id");
          info_from = jsonObject.optString("info_from");
          userid = jsonObject.optString("userid");
          city = jsonObject.optString("city");
          from_openid = jsonObject.optString("from_openid");
          comment_id = jsonObject.optString("commentId");
          about_userid = jsonObject.optString("byreplyId");
          publish_userid = jsonObject.optString("author");
          about_id = jsonObject.optString("articleId");
          like_type = jsonObject.optInt("like_type");
          user_id = jsonObject.optString("user_id");


          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          localItemObject.put("type", type);
          localItemObject.put("id", id);
          localItemObject.put("info_from", info_from);
          localItemObject.put("userid", userid);
          localItemObject.put("city", city);
          localItemObject.put("from_openid", from_openid);
          localItemObject.put("comment_id", comment_id);
          localItemObject.put("about_userid", about_userid);
          localItemObject.put("publish_userid", publish_userid);
          localItemObject.put("about_id", about_id);
          localItemObject.put("like_type", like_type);
          localItemObject.put("user_id", user_id);
          return localItemObject;
     }

}
