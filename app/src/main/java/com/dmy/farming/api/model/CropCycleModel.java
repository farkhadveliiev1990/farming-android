package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.cropcycleResponse;
import com.dmy.farming.api.data.USER;
import com.dmy.farming.api.data.a_DICTIONARY;
import com.dmy.farming.api.data.chat.a_CROPCYCLE;
import com.dmy.farming.api.data.chat.a_WARN_LIST;
import com.dmy.farming.api.dictionaryResponse;
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


public class CropCycleModel extends BaseModel {

    public a_CROPCYCLE data = new a_CROPCYCLE();
    String fileName;
    public a_DICTIONARY saletype = new a_DICTIONARY();

    public CropCycleModel(Context context) {
        super(context);
    }

    public void cropcycleType(String info_from,String cropcycle,boolean isShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                CropCycleModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        cropcycleResponse response = new cropcycleResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {

                            if (response.data != null)
                            {
                                data.crop_cycle = response.data;
                            }
                            //fileSave();
                            CropCycleModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/"+info_from+"/"+cropcycle;
        cb.url(ApiInterface.cropcycle + url).type(JSONObject.class);

        if (isShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }

    public void getsaleTypeList(String info_from,String model_code) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                CropCycleModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        dictionaryResponse response = new dictionaryResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            saletype.sale_label.clear();
                            if (response.data.size() > 0)
                            {
                                saletype.sale_label.addAll(response.data);
                            }
                            //fileSave();
                            CropCycleModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };
        String url = "/"+ info_from + "/"+ model_code;

        cb.url(ApiInterface.SALELABEL + url).type(JSONObject.class);
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
