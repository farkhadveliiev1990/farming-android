package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.changePwd2Request;
import com.dmy.farming.api.data.USER;
import com.dmy.farming.api.data.a_BUY_LIST;
import com.dmy.farming.api.data.a_HELPCENTER_LIST;
import com.dmy.farming.api.dictionaryResponse;
import com.dmy.farming.api.helpcenterRequest;
import com.dmy.farming.api.helpcenterResponse;
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


public class HelpCenterModel extends BaseModel {

    public a_HELPCENTER_LIST data = new a_HELPCENTER_LIST();
    public PAGINATED paginated = new PAGINATED();
    String fileName;

    public HelpCenterModel(Context context) {
        super(context);
    }

    public void getuserHelp( helpcenterRequest helpcenterRequest) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                HelpCenterModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        helpcenterResponse response = new helpcenterResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.helpcenters.clear();
                            if (response.data.size() > 0)
                            {
                                data.helpcenters.addAll(response.data);
                            }
                            //fileSave();
                            HelpCenterModel.this.OnMessageResponse(url, jo, status);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", helpcenterRequest.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
      //  String url = "/"+info_from+"/"+ type+"/"+ page;
    //    cb.url(ApiInterface.USERHELP + url).type(JSONObject.class);
        cb.url(ApiInterface.USERHELP ).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);

    }


    public void getBuyListMore(final String info_from,final String type_code,final String provience,final String city,final String district,final String  lon,final String lat,
                               final String page,final String this_app,final String  lon1,final String lat1,final boolean isCache) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                HelpCenterModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        helpcenterResponse response = new helpcenterResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            if (response.data.size() > 0) {
                                data.helpcenters.addAll(response.data);
                            }
                            if (isCache)
                                fileSave();
                            HelpCenterModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/"+info_from+"/"+ type_code+"/"+ provience+"/"+city+"/"+district+"/"+lon+"/"+lat+"/"+lon1 +"/"+lat1+"/"+page+"/"+this_app;
        cb.url(ApiInterface.BUY + url).type(JSONObject.class);

        if (isCache) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
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
