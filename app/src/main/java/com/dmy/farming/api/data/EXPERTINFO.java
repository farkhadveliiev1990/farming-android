package com.dmy.farming.api.data;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EXPERTINFO
{
    //
    public Long lastUpdateTime = 0L;
    //

    // 不是专家id
    public String id;

    // 呢称
    public String nick_name;

    // 职称
    public String jobTitle;

    // 省
    public String province;

    // 市
    public String city;

    // 区
    public String district;

    // 简介
    public String details;

    // 成就
    public String achievementPersonal;

    // 擅长作物
    public ArrayList<DICTIONARY> goodcrop = new ArrayList<>();

    // 擅长问题
    public ArrayList<DICTIONARY> goodposition = new ArrayList<>();

    // 类型(官方)
    public String expertType;

    public String img_url;

    // 在线状态 0：否 1：是
    public int online_status;

    // 组织机构
    public String organization;

    //
    public String phone;

    // 真实名字
    public String realName;

    // 状态
    public int status;

    // 单位
    public String unit;

    // 账号
    public String user_account;

    // 用户id(专家ID)
    public String userId;

    // 用户类型
    public String userType;

    // 问题类型代码
    public String dicCode;

    // 问题类型名
    public String dicName;

    // 上级问题代码
    public String supCode;

    // 描述
    public String descrtption;

    // 级别
    public String levels;

    // 级别
    public String accept_num;

    // 级别
    public String aswner_num;

    //是否收藏
    public int iscollection;


    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.lastUpdateTime = jsonObject.optLong("lastUpdateTime");
        if (this.lastUpdateTime == Double.NaN) this.lastUpdateTime = 0L;

        this.id = jsonObject.optString("id");
        this.nick_name = jsonObject.optString("nick_name");
        this.jobTitle = jsonObject.optString("jobTitle");
        this.province = jsonObject.optString("provience");
        this.city = jsonObject.optString("city");
        this.district = jsonObject.optString("district");
        this.details = jsonObject.optString("details");
        this.achievementPersonal = jsonObject.optString("achievementPersonal");
        this.expertType = jsonObject.optString("expertType");
        this.img_url = jsonObject.optString("img_url");
        this.online_status = jsonObject.optInt("online_status");
        this.organization = jsonObject.optString("organization");
        this.phone = jsonObject.optString("phone");
        this.realName = jsonObject.optString("realName");
        this.status = jsonObject.optInt("status");
        this.unit = jsonObject.optString("unit");
        this.user_account = jsonObject.optString("user_account");
        this.userId = jsonObject.optString("userId");
        this.userType = jsonObject.optString("user_type");
        this.dicCode = jsonObject.optString("dicCode");
        this.dicName = jsonObject.optString("dicName");
        this.supCode = jsonObject.optString("supCode");
        this.descrtption = jsonObject.optString("descrtption");
        this.levels = jsonObject.optString("levels");
        this.accept_num = jsonObject.optString("accept_num");
        this.aswner_num = jsonObject.optString("aswner_num");
        this.iscollection = jsonObject.optInt("iscollection");

        JSONArray subItemArray = jsonObject.optJSONArray("goodcrop");
        if(null != subItemArray)
        {
            goodcrop.clear();
            JSONObject subItemObject;
            for(int i = 0;i < subItemArray.length();i++)
            {
                try {
                    subItemObject = subItemArray.getJSONObject(i);
                    DICTIONARY subItem = new DICTIONARY();
                    subItem.fromJson(subItemObject);
                    this.goodcrop.add(subItem);
                } catch (Exception ex) {
                    ;
                }
            }
        }

        JSONArray subItemArray2 = jsonObject.optJSONArray("goodposition");
        if(null != subItemArray2)
        {
            goodposition.clear();
            JSONObject subItemObject2;
            for(int i = 0;i < subItemArray2.length();i++)
            {
                try {
                    subItemObject2 = subItemArray2.getJSONObject(i);
                    DICTIONARY subItem2 = new DICTIONARY();
                    subItem2.fromJson(subItemObject2);
                    this.goodposition.add(subItem2);
                } catch (Exception ex) {
                    ;
                }
            }
        }
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("nick_name", nick_name);
        localItemObject.put("jobTitle", jobTitle);
        localItemObject.put("provience", province);
        localItemObject.put("city", city);
        localItemObject.put("district", district);
        localItemObject.put("details", details);
        localItemObject.put("achievementPersonal", achievementPersonal);
        localItemObject.put("expertType", expertType);
        localItemObject.put("img_url", img_url);
        localItemObject.put("online_status", online_status);
        localItemObject.put("organization", organization);
        localItemObject.put("phone", phone);
        localItemObject.put("realName", realName);
        localItemObject.put("status", status);
        localItemObject.put("unit", unit);
        localItemObject.put("user_account", user_account);
        localItemObject.put("userId", userId);
        localItemObject.put("userType", userType);
        localItemObject.put("dicCode", dicCode);
        localItemObject.put("dicName", dicName);
        localItemObject.put("supCode", supCode);
        localItemObject.put("descrtption", descrtption);
        localItemObject.put("levels", levels);
        localItemObject.put("accept_num", accept_num);
        localItemObject.put("aswner_num", aswner_num);
        localItemObject.put("iscollection", iscollection);

        JSONArray itemJSONArray = new JSONArray();
        for(int i = 0; i< goodcrop.size(); i++)
        {
            DICTIONARY itemData = goodcrop.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("goodcrop", itemJSONArray);

        JSONArray itemJSONArray2 = new JSONArray();
        for(int i = 0; i< goodposition.size(); i++)
        {
            DICTIONARY itemData2 = goodposition.get(i);
            JSONObject itemJSONObject2 = itemData2.toJson();
            itemJSONArray2.put(itemJSONObject2);
        }
        localItemObject.put("goodposition", itemJSONArray2);

        localItemObject.put("lastUpdateTime", lastUpdateTime);
        return localItemObject;
    }
}
