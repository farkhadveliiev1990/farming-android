package com.dmy.farming.api.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import org.bee.BeeFrameworkApp;

import org.json.JSONException;
import org.json.JSONObject;

public class SESSION
{
    public String uid = null;
    public String sid = null;

    //
    public String logo = "";
    public String nick = "";
    public int usertype ;
    public String password = "";
    //

    private static SESSION instance;
    public static SESSION getInstance()
    {
        if (instance == null) {
            instance = new SESSION();
        }

        return instance;
    }

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    public void updateValue(Context mContext, String uid, String sid, String logo, String nick, int usertype,String password) {
        boolean isInit = this.uid == null;
        boolean isChanged = !uid.equals(this.uid);

        shared = mContext.getSharedPreferences("userInfo", 0);
        editor = shared.edit();
        editor.putString("uid", uid);
        editor.putString("sid", sid);
        editor.putString("logo", logo);
        editor.putString("nick", nick);
        editor.putInt("usertype",usertype);
        editor.putString("password", password);

//        editor.putString("uid", "");
//        editor.putString("sid", "");
//        editor.putString("logo", "");
//        editor.putString("nick", "");
        editor.commit();

        this.uid = uid;
        this.sid = sid;
        this.logo = logo;
        this.nick = nick;
        this.usertype = usertype;
        this.password = password;

        if (isInit)
            BeeFrameworkApp.getInstance().initActive();
        else if (isChanged)
            BeeFrameworkApp.getInstance().resetDatabase();

        Log.e("session", uid + " " + isChanged);

        // XMPP Init

    }

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.uid = jsonObject.optString("uid");
        this.sid = jsonObject.optString("sid");

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("uid", uid);
        localItemObject.put("sid", sid);

        return localItemObject;
    }
}
