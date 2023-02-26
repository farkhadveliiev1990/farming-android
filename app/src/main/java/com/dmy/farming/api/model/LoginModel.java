package com.dmy.farming.api.model;

import java.util.HashMap;
import java.util.Map;

import org.bee.view.MyProgressDialog;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.changePwd2Request;
import com.dmy.farming.api.commonResponse;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.signin3rdRequest;
import com.dmy.farming.api.userSignInRequest;
import com.dmy.farming.api.userSignInResponse;
import com.dmy.farming.protocol.*;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

import com.external.activeandroid.ActiveAndroid;
import com.external.androidquery.callback.AjaxStatus;
import com.external.androidquery.util.Constants;

import org.bee.model.BaseModel;
import org.bee.model.BeeCallback;

public class LoginModel extends BaseModel {

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    public STATUS lastStatus;

    public LoginModel(Context context) {
        super(context);
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();
    }

    void procLogin(JSONObject jo) throws JSONException {
        userSignInResponse response = new userSignInResponse();
        response.fromJson(jo);
        lastStatus = response.status;
        if (response.status.error_code == 200) {
            SESSION session = response.session;
            SESSION.getInstance().updateValue(mContext, session.uid, session.sid, response.user.avatar, response.user.nickname,response.user.identify,response.user.passWord);
            response.user.save();
//            ActiveAndroid.execSQL("drop table USER");
        }
    }

    public void signin(String phone, String password) {
        userSignInRequest request = new userSignInRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                LoginModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        procLogin(jo);
                        LoginModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

//        request.username = username;
//        request.password = password;
//        request.idcode = idcode;
//        request.platform = 1;
//        request.channel_id = AppUtils.getChannelId(mContext);

//        Map<String, String> params = new HashMap<String, String>();
//        try {
//            params.put("json", request.toJson().toString());
//            params.put("json", "1");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        String url = "/1/" + phone + "/" + password + "/" + AppUtils.getChannelId(mContext);

        cb.url(ApiInterface.USER_SIGNIN + url).type(JSONObject.class);
//        cb.url(ApiInterface.USER_SIGNIN).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }



    public void signup(String phone, String password, String reqcode, String province, String city, String district) {
        userSignInRequest request = new userSignInRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                LoginModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
//                        procLogin(jo);
                        userSignInResponse response = new userSignInResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        LoginModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

        request.info_from = AppConst.info_from;
        request.phone = phone;
        request.password = password;
        request.reqcode = reqcode;
        request.province = province;
        request.city = city;
        request.district = district;
//        request.channel_id = AppUtils.getChannelId(mContext);

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.USER_SIGNUP).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    public void changePassword(String phone, String password) {

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                LoginModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        commonResponse response = new commonResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        LoginModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

        String url = "/1/"+ phone + "/" + password;

        cb.url(ApiInterface.CHANGE_PWD + url).type(JSONObject.class);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    public void changePassword(String username, String password, String idcode) {
        userSignInRequest request = new userSignInRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                LoginModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        commonResponse response = new commonResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        LoginModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

//        request.username = username;
//        request.password = password;
//        request.idcode = idcode;

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.CHANGE_PWD).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    public void changePassword2(String oldpass, String newpass) {
        changePwd2Request request = new changePwd2Request();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                LoginModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        commonResponse response = new commonResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        LoginModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

//        request.oldpass = oldpass;
//        request.newpass = newpass;
//        request.session = SESSION.getInstance();
//
//        Map<String, String> params = new HashMap<String, String>();
//        try {
//            params.put("json", request.toJson().toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        String url = "/"+ AppConst.info_from + "/"+ SESSION.getInstance().uid + "/" + newpass;

        cb.url(ApiInterface.CHANGE_PWD2 + url).type(JSONObject.class);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    public void signin3rd(signin3rdRequest request) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                LoginModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        procLogin(jo);
                        LoginModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

//        request.platform = 1;
//        request.channel_id = AppUtils.getChannelId(mContext);

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "/绿云" + "/" + request.out_account_type + "/" + request.out_account + "/" + request.nickname + "/" + request.headimg;

        cb.url(ApiInterface.USER_SIGNIN_3RD ).type(JSONObject.class).params(params);

        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    public void checkIdcode3rd(signin3rdRequest request) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                LoginModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        procLogin(jo);
                        LoginModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

//        request.platform = 1;
//        request.channel_id = AppUtils.getChannelId(mContext);

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.USER_CHECK_3RD).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    public void signup3rd(signin3rdRequest request) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                LoginModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        procLogin(jo);
                        LoginModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

//        request.platform = 1;
//        request.channel_id = AppUtils.getChannelId(mContext);

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.USER_SIGNUP_3RD).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    // 验证是否注册
    public void verificationPhone(String info_from, String phone) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                LoginModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        commonResponse response = new commonResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        LoginModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("info_from",info_from);
            jsonObject.put("phone",phone);

            params.put("json", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.USER_VERIFICATIONPHONE).type(JSONObject.class).params(params);
//        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
//        aq.progress(pd.mDialog).ajax(cb);
        aq.ajax(cb);
    }

    // 绑定手机号
    public void bindPhone(String info_from, String phone) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                LoginModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        procLogin(jo);
                        LoginModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("info_from",info_from);
            jsonObject.put("phone",phone);
            jsonObject.put("id",SESSION.getInstance().uid);

            params.put("json", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.USER_BINDPHONE).type(JSONObject.class).params(params);
//        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
//        aq.progress(pd.mDialog).ajax(cb);
        aq.ajax(cb);
    }


}
