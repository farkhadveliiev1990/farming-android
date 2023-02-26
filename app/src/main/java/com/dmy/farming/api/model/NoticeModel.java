package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.NoticeListResponse;
import com.dmy.farming.api.NoticeNumResponse;
import com.dmy.farming.api.NoticeResponse;
import com.dmy.farming.api.data.GLOBAL_DATA;
import com.dmy.farming.api.data.USER;
import com.dmy.farming.api.data.chat.a_NOTICE_LIST;
import com.dmy.farming.api.data.chat.a_WARN_LIST;
import com.dmy.farming.api.warnRequest;
import com.dmy.farming.protocol.PAGINATED;
import com.dmy.farming.protocol.STATUS;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.model.BaseModel;
import org.bee.model.BeeCallback;
import org.bee.view.MyProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;


public class NoticeModel extends BaseModel {

    public a_NOTICE_LIST data = new a_NOTICE_LIST();
    public STATUS lastStatus;
    public PAGINATED paginated = new PAGINATED();
    public USER user;
    String fileName;
    public int num;

    public NoticeModel(Context context) {
        super(context);
    }

    public void getNoticeList( final String info_from,final String type, final int page,boolean bShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                NoticeModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        NoticeListResponse response = new NoticeListResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.routes.clear();
                            //  data.paginated = response.paginated;
                            paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.routes.addAll(response.data);
                            }
                            fileSave();
                            NoticeModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        };

        String url = "/"+info_from+"/"+type+"/"+page;
        cb.url(ApiInterface.NOTICE + url).type(JSONObject.class);

        if (bShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }


    public void getNoticeListMore(final String info_from,final String type, final int page,final boolean isCache) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                NoticeModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        NoticeListResponse response = new NoticeListResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.routes.addAll(response.data);
                            }
                            fileSave();
                            NoticeModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        };

        String url = "/"+info_from+"/"+type+"/"+page;
        cb.url(ApiInterface.NOTICE + url).type(JSONObject.class);

        if (isCache) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }

    public void getNoticnum() {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                NoticeModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        NoticeNumResponse response = new NoticeNumResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            num = response.data;
                            NoticeModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            JSONObject localItemObject = new JSONObject();

            localItemObject.put("info_from", AppConst.info_from);
            localItemObject.put("province", AppUtils.getCurrProvince(mContext));
            localItemObject.put("city", GLOBAL_DATA.getInstance(mContext).currCity);
            localItemObject.put("district", AppUtils.getCurrCity(mContext));
            params.put("json",localItemObject .toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cb.url(ApiInterface.getNoticnum).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    public void deleteNotice(String id) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                NoticeModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        NoticeListResponse response = new NoticeListResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        NoticeModel.this.OnMessageResponse(url, jo, status);
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            JSONObject localItemObject = new JSONObject();

            localItemObject.put("info_from", AppConst.info_from);
            localItemObject.put("city", AppUtils.getCurrCity(mContext));
            localItemObject.put("id", id);
            params.put("json",localItemObject .toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cb.url(ApiInterface.deleteNoice).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

    // 缓存数据
    private PrintStream ps = null;

    public void fileSave() {
        data.lastUpdateTime = System.currentTimeMillis();

        String path = DataCleanManager.getCacheDir(mContext);
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        File file = new File(filePath + fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            ps = new PrintStream(fos);
            ps.print(data.toJson().toString());
            ps.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
