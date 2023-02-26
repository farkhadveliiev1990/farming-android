package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.a_HELPCENTER_LIST;
import com.dmy.farming.api.data.a_illegal_LIST;
import com.dmy.farming.api.helpcenterRequest;
import com.dmy.farming.api.helpcenterResponse;
import com.dmy.farming.api.illegalsRequest;
import com.dmy.farming.api.illegalsResponse;
import com.dmy.farming.protocol.PAGINATED;
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


public class IllegalModel extends BaseModel {

    public a_illegal_LIST data = new a_illegal_LIST();
    public PAGINATED paginated = new PAGINATED();
    String fileName;

    public IllegalModel(Context context) {
        super(context);
    }

    public void getillegas( illegalsRequest Request) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                IllegalModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        illegalsResponse response = new illegalsResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.illegals.clear();
                            if (response.data.size() > 0)
                            {
                                data.illegals.addAll(response.data);
                            }
                            //fileSave();
                            IllegalModel.this.OnMessageResponse(url, jo, status);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", Request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
      //  String url = "/"+info_from+"/"+ type+"/"+ page;
    //    cb.url(ApiInterface.USERHELP + url).type(JSONObject.class);
        cb.url(ApiInterface.ILLEGALS ).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);

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
