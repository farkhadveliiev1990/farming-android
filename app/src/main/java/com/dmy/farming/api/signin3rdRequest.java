
package com.dmy.farming.api;

import org.json.JSONException;
import org.json.JSONObject;

public class signin3rdRequest
{
     public String infrom;

     // a1: WEIXIN, 2: QQ, 3 : WEIBO
     public int out_account_type;

     public String out_account;

     // 0:微信端 a1:安卓端 2:苹果端
//     public int platform;

     // "baidu_channelId"
//     public String channel_id;

     // signup
//     public String phonenum;
//     public String idcode;
//     public String password;
     public String nickname;
     public String headimg;
     public String position;
     public String channelid;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

          infrom = jsonObject.optString("infrom");
          out_account = jsonObject.optString("out_account");
          out_account_type = jsonObject.optInt("out_account_type");
          nickname = jsonObject.optString("nickname");
          headimg = jsonObject.optString("headimg");
          position = jsonObject.optString("position");
          channelid = jsonObject.optString("channelid");
          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

          localItemObject.put("info_from", infrom);
          localItemObject.put("login_type", out_account_type);
          localItemObject.put("openid", out_account);

//          localItemObject.put("platform", platform);
//          localItemObject.put("channel_id", channel_id);

//          localItemObject.put("phonenum", phonenum);
//          localItemObject.put("idcode", idcode);
//          localItemObject.put("password", password);
          localItemObject.put("nick_name", nickname);
          localItemObject.put("img_url", headimg);
          localItemObject.put("position", position);
          localItemObject.put("channelid", channelid);
          return localItemObject;
     }

}
