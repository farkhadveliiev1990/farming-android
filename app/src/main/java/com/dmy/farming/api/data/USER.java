
package com.dmy.farming.api.data;
import android.text.TextUtils;

import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

@Table(name = "USER")
public class USER  extends Model
{
    @Column(name = "USER_id",unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String id;

    // 账号（手机号）
    @Column(name = "username")
    public String username;

    // 0: 普通用户 1：专家
    @Column(name = "identify")
    public int identify;

    // 昵称
    @Column(name = "nickname")
    public String nickname;

    //省
    @Column(name = "provience")
    public String provience;

    //市
    @Column(name = "city")
    public String city;

    //地区
    @Column(name = "district")
    public String district;

//     性别（0:男, 1:女）
    @Column(name = "gender")
    public int gender;

//    @Column(name = "signature")
//    public String signature;

    // 头像地址
    @Column(name = "avatar")
    public String avatar;

    // 积分
    @Column(name = "coupon")
    public int coupon;

    // 签到总天数
    @Column(name = "signnum")
    public String signnum;

    // 密码
    @Column(name = "password")
    public String passWord;


    // 0:未设置支付密码  a1：已设置支付密码
//    @Column(name = "paypwd")
//    public int paypwd;


    public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.username = jsonObject.optString("phone");
        this.identify = jsonObject.optInt("userType");
        this.nickname = jsonObject.optString("nickName");
        this.provience = jsonObject.optString("provience");
        this.city = jsonObject.optString("city");
        this.district = jsonObject.optString("district");
        this.avatar = jsonObject.optString("imgUrl");
        this.coupon = jsonObject.optInt("coupon");
        this.signnum = jsonObject.optString("sign_num");
        this.gender = jsonObject.optInt("gender");
        this.passWord = jsonObject.optString("passWord");

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("phone", username);
        localItemObject.put("userType", identify);
        localItemObject.put("nickName", nickname);
        localItemObject.put("provience", provience);
        localItemObject.put("city", city);
        localItemObject.put("district", district);
        localItemObject.put("imgUrl", avatar);
        localItemObject.put("coupon", coupon);
        localItemObject.put("sign_num", signnum);
        localItemObject.put("gender", gender);
        localItemObject.put("passWord", passWord);

        return localItemObject;
    }

    public String getCheckInfo() {
        String checkString = "";
//        if (TextUtils.isEmpty(realname))
//            checkString += "实名 ";
//
//        if (TextUtils.isEmpty(idcard))
//            checkString += "身份证号 ";
//
//        if (TextUtils.isEmpty(alipayNo))
//            checkString += "支付宝账号 ";

        return checkString.trim().replace(" ","、");
    }
}
