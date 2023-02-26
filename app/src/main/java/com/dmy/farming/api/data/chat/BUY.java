
package com.dmy.farming.api.data.chat;
import android.text.TextUtils;

import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

@Table(name = "BUY")
public class BUY extends Model
{
    @Column(name = "USER_id",unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String id;

    // 账号（手机号）
    @Column(name = "username")
    public String username;

    // 0: 普通会员 a1:群主 2:地接
    @Column(name = "identify")
    public int identify;

    @Column(name = "nickname")
    public String nickname;

    @Column(name = "realname")
    public String realname;

    @Column(name = "birthday")
    public String birthday;

    // 性别（a1:男, 0:女）
    @Column(name = "gender")
    public int gender;

    @Column(name = "signature")
    public String signature;

    @Column(name = "avatar")
    public String avatar;

    // 身份证
    @Column(name = "idcard")
    public String idcard;

    // 支付宝
    @Column(name = "alipayNo")
    public String alipayNo;

    // 职业
    @Column(name = "job")
    public String job;

    // 0:未设置支付密码  a1：已设置支付密码
    @Column(name = "paypwd")
    public int paypwd;


    public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.username = jsonObject.optString("username");
        this.identify = jsonObject.optInt("identify");
        this.nickname = jsonObject.optString("nickname");
        this.realname = jsonObject.optString("realname");
        this.birthday = jsonObject.optString("birthday");
        this.gender = jsonObject.optInt("gender");
        this.signature = jsonObject.optString("signature");
        this.avatar = jsonObject.optString("avatar");
        this.idcard = jsonObject.optString("idcard");
        this.alipayNo = jsonObject.optString("alipayNo");
        this.job = jsonObject.optString("job");
        this.paypwd = jsonObject.optInt("paypwd");
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("username", username);
        localItemObject.put("identify", identify);
        localItemObject.put("nickname", nickname);
        localItemObject.put("realname", realname);
        localItemObject.put("birthday", birthday);
        localItemObject.put("gender", gender);
        localItemObject.put("signature", signature);
        localItemObject.put("avatar", avatar);
        localItemObject.put("idcard", idcard);
        localItemObject.put("alipayNo", alipayNo);
        localItemObject.put("job", job);
        localItemObject.put("paypwd", paypwd);
        return localItemObject;
    }

    public String getCheckInfo() {
        String checkString = "";
        if (TextUtils.isEmpty(realname))
            checkString += "实名 ";

        if (TextUtils.isEmpty(idcard))
            checkString += "身份证号 ";

        if (TextUtils.isEmpty(alipayNo))
            checkString += "支付宝账号 ";

        return checkString.trim().replace(" ","、");
    }
}
