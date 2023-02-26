
package com.dmy.farming.api;

import com.dmy.farming.api.data.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

public class userUpdateRequest
{
//     public SESSION session;

     public String nickname = "";

     public String id;

     public String info_from;

//     public String realname;

     // 0：男 1：女
     public String gender;

     // 标签
//     public String signature;

     // 头像图片上传地址
     public String avatar = "";

     // 生日 yyyy-MM-dd
//     public String birthday;

     // 支付宝账户
//     public String alipayNo;

     // 身份证
//     public String idcard;

     // 职业
//     public String job;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if (null == jsonObject){
               return ;
          }

//          SESSION  session = new SESSION();
//          session.fromJson(jsonObject.optJSONObject("session"));
//          this.session = session;

          info_from = jsonObject.optString("info_from");
          nickname = jsonObject.optString("nick_name");
          id = jsonObject.optString("id");
          gender = jsonObject.optString("gender");
          avatar = jsonObject.optString("img_url");

//          realname = jsonObject.optString("realname");
//          signature = jsonObject.optString("signature");
//          birthday = jsonObject.optString("birthday");
//          alipayNo = jsonObject.optString("alipayNo");
//          idcard = jsonObject.optString("idcard");
//          job = jsonObject.optString("job");
          return ;
     }

     public JSONObject  toJson() throws JSONException
     {
          JSONObject localItemObject = new JSONObject();

//          if (null != session) {
//               localItemObject.put("session", session.toJson());
//          }

          localItemObject.put("info_from", info_from);
          localItemObject.put("nick_name", nickname);
          localItemObject.put("id", id);
          localItemObject.put("gender", gender);
          localItemObject.put("img_url", avatar);
//          localItemObject.put("realname", realname);
//          localItemObject.put("signature", signature);
//          localItemObject.put("birthday", birthday);
//          localItemObject.put("alipayNo", alipayNo);
//          localItemObject.put("idcard", idcard);
//          localItemObject.put("job", job);

          return localItemObject;
     }

}
