package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.CollectResponse;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.chat.RENTLIST;
import com.dmy.farming.api.data.chat.SALELIST;
import com.dmy.farming.api.rentResponse;
import com.dmy.farming.api.saleRequest;
import com.dmy.farming.api.saleResponse;
import com.dmy.farming.protocol.STATUS;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.model.BaseModel;
import org.bee.model.BeeCallback;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;


public class RentDetailModel extends BaseModel {

    public RENTLIST data = new RENTLIST();
    public STATUS lastStatus;
    String fileName;
    public String id;

    public RentDetailModel(Context context, String id) {
        super(context);
        this.id = id;
    }

    public void getRoute() {
        saleRequest request = new saleRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
               RentDetailModel.this.callback(url, jo, status);
                try {
                    if (jo != null)
                    {
                        rentResponse response = new rentResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            data=response.data;
                            fileSave();
                        }
                        RentDetailModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                }
            }
        };

       // request.session = SESSION.getInstance();
        Map<String, String> params = new HashMap<String, String>();
        try {
            JSONObject itemObject = new JSONObject();
            itemObject.put("info_from", AppConst.info_from);
            itemObject.put("city", AppUtils.getCurrCity(mContext));
            itemObject.put("userid", SESSION.getInstance().uid);
            itemObject.put("id", id);

            params.put("json", itemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cb.url(ApiInterface.RENTDETAIL).type(JSONObject.class).params(params);

      //  cb.setParams(ApiInterface.SALEDETAIL, params).type(JSONObject.class);

        //MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        //aq.progress(pd.mDialog).ajax(cb);
        aq.ajax(cb);
    }

    public void collection(String id) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                RentDetailModel.this.callback(url, jo, status);
                try {
                    if (jo != null)
                    {
                        CollectResponse response = new CollectResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            fileSave();
                        }
                        RentDetailModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            JSONObject itemObject = new JSONObject();
            itemObject.put("info_from", AppConst.info_from);
            itemObject.put("collection_type", "8");
            itemObject.put("userid", SESSION.getInstance().uid);
            itemObject.put("collection_id", id);

            params.put("json", itemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.COLLECTION).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }
    public void cancelcollection(String id) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                RentDetailModel.this.callback(url, jo, status);
                try {
                    if (jo != null)
                    {
                        CollectResponse response = new CollectResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            fileSave();
                        }
                        RentDetailModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            JSONObject itemObject = new JSONObject();
            itemObject.put("info_from", AppConst.info_from);
            itemObject.put("collection_type", "8");
            itemObject.put("userid", SESSION.getInstance().uid);
            itemObject.put("collection_id", id);

            params.put("json", itemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.CANCELCOLLECTION).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

    // 缓存数据
    private PrintStream ps = null;

    public void fileSave() {
      //  data.lastUpdateTime = System.currentTimeMillis();

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
