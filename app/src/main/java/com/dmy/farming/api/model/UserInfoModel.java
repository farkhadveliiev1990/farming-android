package com.dmy.farming.api.model;

import android.content.Context;

import org.bee.model.BaseModel;
import org.bee.model.BeeCallback;
import org.bee.view.MyProgressDialog;

import com.dmy.farming.AppConst;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.commonRequest;
import com.dmy.farming.api.commonResponse;
import com.dmy.farming.api.userInfoResponse;
import com.dmy.farming.api.userSignInResponse;
import com.dmy.farming.api.userUpdatePhoneRequest;
import com.dmy.farming.api.userUpdateRequest;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.USER;
import com.dmy.farming.protocol.STATUS;
import com.external.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserInfoModel extends BaseModel {

    public STATUS lastStatus;
    public USER user;

    public UserInfoModel(Context context) {
        super(context);
    }

    public void getUserInfo() {
        commonRequest request = new commonRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                UserInfoModel.this.callback(url, jo, status);
                try {
                    userInfoResponse response = new userInfoResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                        if (response.status.error_code == 200) {
                            user = response.user;
                            user.save();
                            UserInfoModel.this.OnMessageResponse(url, jo, status);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        request.session = SESSION.getInstance();
        Map<String, String> params = new HashMap<>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {

        }

        cb.setParams(ApiInterface.USER_INFO, params).type(JSONObject.class);
        aq.ajax(cb);
        //MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        //aq.progress(pd.mDialog).ajax(cb);
    }

    public void getUserInfo(boolean isShow) {
        final commonRequest request = new commonRequest();

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                UserInfoModel.this.callback(url, jo, status);
                try {
                    userInfoResponse response = new userInfoResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            user = response.user;
                            user.save();
                            UserInfoModel.this.OnMessageResponse(url, jo, status);
                        }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

        request.session = SESSION.getInstance();

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {

        }
        cb.url(ApiInterface.USER_INFO).type(JSONObject.class).params(params);

        if (isShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }

    public void updateUser(userUpdateRequest request) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                UserInfoModel.this.callback(url, jo, status);
                try {
                    userInfoResponse response = new userInfoResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            user = response.user;
                            SESSION.getInstance().updateValue(mContext, SESSION.getInstance().uid, SESSION.getInstance().sid, user.avatar, user.nickname,user.identify,user.passWord);
                            user.save();
                        }
                        UserInfoModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

//        request.session = SESSION.getInstance();
        request.info_from = AppConst.info_from;
        request.id = SESSION.getInstance().uid;
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {

        }

//        cb.setParams(ApiInterface.USER_UPDATE, params).type(JSONObject.class);
        cb.url(ApiInterface.USER_UPDATE).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }



    public void userLogout() {
//        commonRequest request = new commonRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                UserInfoModel.this.callback(url, jo, status);
                try {
                    commonResponse response = new commonResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                        lastStatus = response.status;
                        UserInfoModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

//        request.session = SESSION.getInstance();
//        Map<String, String> params = new HashMap<String, String>();
//        try {
//            params.put("json", request.toJson().toString());
//        } catch (JSONException e) {
//
//        }
        String url = "/1/" + SESSION.getInstance().uid;
        cb.url(ApiInterface.USER_LOGOUT + url).type(JSONObject.class);
        aq.ajax(cb);
    }

    public void signin(boolean isShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                UserInfoModel.this.callback(url, jo, status);
                try {
                 //   commonResponse response = new commonResponse();
                    userSignInResponse response = new userSignInResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            user = response.user;
                            user.save();
                            UserInfoModel.this.OnMessageResponse(url, jo, status);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", SESSION.getInstance().uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("json", jsonObject.toString());

        cb.url(ApiInterface.SIGNIN).type(JSONObject.class).params(params);

        if (isShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }

}
