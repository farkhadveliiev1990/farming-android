package com.dmy.farming.api.model;

import android.app.Activity;
import android.content.Context;

import com.dmy.farming.AppConst;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.articleRequest;
import com.dmy.farming.api.commonResponse;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.farmarticleDetailResponse;
import com.dmy.farming.api.uploadRequest;
import com.dmy.farming.api.uploadResponse;
import com.dmy.farming.protocol.STATUS;
import com.dmy.farming.protocol.userReqcodeRequest;
import com.external.androidquery.callback.AjaxStatus;
import com.tencent.cos.model.PutObjectResult;

import org.bee.model.BaseModel;
import org.bee.model.BeeCallback;
import org.bee.view.MyProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.dmy.farming.api.uploadRequest.FILETYPE_IMAGE;
import static com.dmy.farming.api.uploadRequest.FILETYPE_SOUND;
import static com.dmy.farming.api.uploadRequest.FILETYPE_VIDEO;

public class QuestionModel extends BaseModel {
    //
    public STATUS lastStatus = new STATUS();

    MyProgressDialog pd = null;
    //
    public QuestionModel(Context context) {
        super(context);
    }

    public void adoptComment(String inform,String id,String commentid) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                QuestionModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        commonResponse response = new commonResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        QuestionModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                }
            }
        };

        String url = "/" + inform + "/" + id + "/" + commentid;

        cb.url(ApiInterface.ADOPTCOMMENT + url).type(JSONObject.class);
        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    public void answer(String inform,String id,String content,String userid,String type,String byreply_userid,int usertype) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                QuestionModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        commonResponse response = new commonResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        QuestionModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            JSONObject localItemObject = new JSONObject();
            localItemObject.put("info_from", AppConst.info_from);
            localItemObject.put("id", id);
            localItemObject.put("content", content);
            localItemObject.put("userid", userid);
            localItemObject.put("type", type);
            localItemObject.put("byreply_userid", byreply_userid);
            localItemObject.put("usertype", usertype);

            params.put("json", localItemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        String url = "/" + inform + "/" + id + "/" + content + "/" + userid + "/" + type + "/" + byreply_userid + "/" + usertype;

        cb.url(ApiInterface.ANSWER).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    public void askAnswer(String inform,String id,String content,String userid,String type,String replyuserid,String questionid) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                QuestionModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        commonResponse response = new commonResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        QuestionModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                }
            }
        };

        String url = "/" + inform + "/" + id + "/" + content + "/" + userid + "/" + type + "/" + replyuserid + "/" + questionid ;

        cb.url(ApiInterface.ASKANSWER + url).type(JSONObject.class);
        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    public void deleteComment(String commentid ,String questionid) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                QuestionModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        commonResponse response = new commonResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        QuestionModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            JSONObject localItemObject = new JSONObject();
            localItemObject.put("info_from", AppConst.info_from);
            localItemObject.put("comment_id", commentid);
            localItemObject.put("qid", questionid);

            params.put("json", localItemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.DELETECOMMENT).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }


//    public void payPassword(String oldpass, String newpass) {
//        changePwd2Request request = new changePwd2Request();
//        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
//
//            @Override
//            public void callback(String url, JSONObject jo, AjaxStatus status) {
//                CommonModel.this.callback(url, jo, status);
//                try {
//                    if (jo != null) {
//                        commonResponse response = new commonResponse();
//                        response.fromJson(jo);
//                        lastStatus = response.status;
//                        if (lastStatus.succeed == 1) {
//                            USER user = new Select().from(USER.class).where("USER_id = ?", SESSION.getInstance().uid).executeSingle();
//                            if (user != null && user.paypwd == 0) {
//                                USER newUser = new USER();
//                                newUser.fromJson(user.toJson());
//                                newUser.paypwd = 1;
//                                newUser.save();
//                            }
//                        }
//                        CommonModel.this.OnMessageResponse(url, jo, status);
//                    }
//                } catch (JSONException e) {
//
//                }
//            }
//        };
//
//        request.session = SESSION.getInstance();
//
//        // 旧密码  首次登录密码
//        request.oldpass = oldpass;
//
//        // 新密码
//        request.newpass = newpass;
//
//        Map<String, String> params = new HashMap<String, String>();
//        try {
//            params.put("json", request.toJson().toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        cb.setParams(ApiInterface.USER_CHANGE_PAYPWD, params).type(JSONObject.class);
//        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
//        aq.progress(pd.mDialog).ajax(cb);
//    }
}
