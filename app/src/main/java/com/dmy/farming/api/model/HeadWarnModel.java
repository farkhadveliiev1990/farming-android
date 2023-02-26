package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.USER;
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


public class HeadWarnModel extends BaseModel {

    public a_WARN_LIST data = new a_WARN_LIST();
    public STATUS lastStatus;
    public PAGINATED paginated = new PAGINATED();
    public USER user;
    String fileName;

    public HeadWarnModel(Context context) {
        super(context);
    }

    public void getWarn(final warnRequest wRequest, boolean bShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                HeadWarnModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        WarnResponse response = new WarnResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.routes.clear();
                            //  data.paginated = response.paginated;
                            paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.routes.addAll(response.data);
                            }
                            fileSave();
                            HeadWarnModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", wRequest.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cb.url(ApiInterface.WARNDTEAD).type(JSONObject.class).params(params);
        if (bShow) {
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
