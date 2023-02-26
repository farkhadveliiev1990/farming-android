package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.NoticeResponse;
import com.dmy.farming.api.data.chat.NOTICELIST;
import com.dmy.farming.api.data.chat.WARNLIST;
import com.dmy.farming.api.saleRequest;
import com.dmy.farming.api.warnResponse;
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


public class WarnDetailModel extends BaseModel {

    public WARNLIST data = new WARNLIST();
    public STATUS lastStatus;
    String fileName;
    public String id;

    public WarnDetailModel(Context context, String id) {
        super(context);
        this.id = id;
    }

    public void getRoute() {
        saleRequest request = new saleRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                WarnDetailModel.this.callback(url, jo, status);
                try {
                    if (jo != null)
                    {
                        warnResponse response = new warnResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            data.fromJson(response.data.toJson());
                            fileSave();
                        }
                        WarnDetailModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                }
            }
        };

       // request.session = SESSION.getInstance();
        request.id = id;

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "/"+id;
        cb.url(ApiInterface.WARNDETAIL + url).type(JSONObject.class);
      //  cb.setParams(ApiInterface.SALEDETAIL, params).type(JSONObject.class);

        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
      //  aq.ajax(cb);
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
