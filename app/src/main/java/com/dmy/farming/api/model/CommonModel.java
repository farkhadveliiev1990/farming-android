package com.dmy.farming.api.model;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.changePwd2Request;
import com.dmy.farming.api.commonRequest;
import com.dmy.farming.api.commonResponse;
import com.dmy.farming.api.data.GLOBAL_DATA;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.USER;
import com.dmy.farming.api.uploadRequest;
import com.dmy.farming.api.uploadResponse;
import com.dmy.farming.protocol.STATUS;
import com.dmy.farming.protocol.userReqcodeRequest;
import com.droid.PingYinUtil;
import com.external.activeandroid.query.Select;
import com.external.androidquery.callback.AjaxStatus;
import com.external.androidquery.util.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.IUploadTaskListener;
import com.tencent.cos.utils.FileUtils;

import org.bee.model.BaseModel;
import org.bee.model.BeeCallback;
import org.bee.view.MyProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dmy.farming.api.uploadRequest.FILETYPE_IMAGE;
import static com.dmy.farming.api.uploadRequest.FILETYPE_SOUND;
import static com.dmy.farming.api.uploadRequest.FILETYPE_VIDEO;

public class CommonModel extends BaseModel {
    //
    public String filePath;
    public String fullPath;
    //
    public STATUS lastStatus = new STATUS();
    public String localFilePath;

//    BizServer bizServer;

    //
    public PutObjectResult lastCosResult = new PutObjectResult();
    MyProgressDialog pd = null;
    //
    public CommonModel(Context context) {
        super(context);
//        bizServer = BizServer.getInstance(mContext);
    }

    public void reqCode(String phone, int type) {
        userReqcodeRequest request = new userReqcodeRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                CommonModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        commonResponse response = new commonResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        CommonModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                }
            }
        };

        request.phone = phone;
        request.type = type;

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.USER_REQCODE).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    public void uploadFile(String imgPath) {
        final uploadRequest request = new uploadRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                CommonModel.this.callback(url, jo, status);
                try {
                        if (jo != null) {
                        uploadResponse response = new uploadResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            filePath = response.filePath;
                            fullPath = response.fullPath;
                        }
                        CommonModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

//        request.session = SESSION.getInstance();
//        request.filetype = 0;
//
//        Map<String, String> params = new HashMap<String, String>();
//        try {
//            params.put("json", request.toJson().toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        cb.setParams(ApiInterface.UPLOAD_FILE, params).type(JSONObject.class);

        cb.url(ApiInterface.UPLOAD_FILE).type(JSONObject.class).method(Constants.METHOD_POST);
        cb.param("uploadfile", new File(imgPath));

        MyProgressDialog pd = new MyProgressDialog(mContext,"正在上传中");
        aq.progress(pd.mDialog).ajax(cb);
    }

    public void uploadMultiFile(List<String> imgPath) {
        final uploadRequest request = new uploadRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                CommonModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        uploadResponse response = new uploadResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            filePath = response.filePath;
                            fullPath = response.fullPath;
                        }
                        CommonModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

        cb.url(ApiInterface.UPLOAD_FILE).type(JSONObject.class).method(Constants.METHOD_POST);
        for (int i =0;i< imgPath.size();i++) {
            cb.param("uploadfile" + i, new File(imgPath.get(i)));
        }

        MyProgressDialog pd = new MyProgressDialog(mContext,"正在上传中");
        aq.progress(pd.mDialog).ajax(cb);
    }

    String getAppCosPathName(String filename) {
        String path = "/app";
        path += "/" + SESSION.getInstance().uid;

        DateFormat pathDf = new SimpleDateFormat("yyyyMM");
        path += "/" + pathDf.format(new Date());

        return path + "/" + filename;
    }


    public void uploadCloudFile(final String imgPath) {
        try {
            if (pd != null) {
                pd.dismiss();
                pd = null;
            }
            pd = new MyProgressDialog(mContext, "正在上传中");
            pd.show();
        } catch (Exception ex) {
            pd = null;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(TextUtils.isEmpty(imgPath)){
                    Toast.makeText(mContext,"请选择文件", Toast.LENGTH_SHORT).show();
                    return;
                }
                String filename = FileUtils.getFileName(imgPath);
                String cosPath = getAppCosPathName(filename);

//                bizServer.setFileId(cosPath);
//                String sign = bizServer.getSign();

                PutObjectRequest putObjectRequest = new PutObjectRequest();
//                putObjectRequest.setBucket(bizServer.getBucket());
//                putObjectRequest.setCosPath(cosPath);
//                putObjectRequest.setSign(sign);
                putObjectRequest.setBiz_attr(null);
                putObjectRequest.setSrcPath(imgPath);
                putObjectRequest.setListener(new IUploadTaskListener(){
                    @Override
                    public void onSuccess(COSRequest cosRequest, COSResult cosResult) {

                        PutObjectResult result = (PutObjectResult) cosResult;
                        lastCosResult = result;

                        if(result != null){
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("上传结果： ret=" + result.code + "; msg =" +result.msg + "\n");
                            stringBuilder.append(" access_url= " + result.access_url == null ? "null" :result.access_url + "\n");
                            stringBuilder.append(" resource_path= " + result.resource_path == null ? "null" :result.resource_path + "\n");
                            stringBuilder.append(" url= " + result.url == null ? "null" :result.url);
                            final String strResult = stringBuilder.toString();
                            final String url = result.access_url == null ? "null" :result.access_url;

                            Log.e("cloud res", strResult);
                            Log.e("cloud url", url);

                            filePath = url;
                            fullPath = url;

                            returnUploadFile();
                        }
                    }

                    @Override
                    public void onFailed(COSRequest COSRequest, final COSResult cosResult) {
                        lastCosResult = (PutObjectResult) cosResult;
                        returnUploadFile();
                    }

                    @Override
                    public void onProgress(final COSRequest cosRequest, final long currentSize, final long totalSize) {
                        /*
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                float progress = (float)currentSize/totalSize;
                                progress = progress *100;
                                Log.w("XIAO","进度：  " + (int)progress + "%" + cosRequest.getCosPath());
                            }
                        });
                        */
                    }

                    @Override
                    public void onCancel(COSRequest cosRequest, COSResult cosResult) {
                        lastCosResult = (PutObjectResult) cosResult;
                        returnUploadFile();
                    }
                });

//                bizServer.getCOSClient().putObject(putObjectRequest);
            }}).start();
    }


    void returnUploadFile() {
        if (mContext instanceof Activity) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    returnUploadFileInner();
                }
            });
        } else {
            returnUploadFileInner();
        }
    }

    void returnUploadFileInner() {
        try {
            CommonModel.this.OnMessageResponse(ApiInterface.UPLOAD_FILE, null, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    String getChatCosPathName(String filename, int fileType) {
        String path = "/chat";

        if (fileType == FILETYPE_IMAGE) {
            path += "/image";
        } else if (fileType == FILETYPE_SOUND) {
            path += "/sound";
        } else if (fileType == FILETYPE_VIDEO) {
            path += "/video";
        } else {
            path += "/other";
        }

        DateFormat pathDf = new SimpleDateFormat("yyyyMM");
        path += "/" + pathDf.format(new Date());

        return path + "/" + filename;
    }


    void returnUploadChat() {
        if (mContext instanceof Activity) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    returnUploadChatInner();
                }
            });
        } else {
            returnUploadChatInner();
        }
    }

    void returnUploadChatInner() {
        try {
            CommonModel.this.OnMessageResponse(ApiInterface.UPLOAD_CHAT, null, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
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
