package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.activityCenterResponse;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.a_ACTIVITYCENTER_LIST;
import com.dmy.farming.api.data.chat.a_EXPERT_LIST;
import com.dmy.farming.api.expertResponse;
import com.dmy.farming.protocol.STATUS;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.model.BaseModel;
import org.bee.model.BeeCallback;
import org.bee.view.MyProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class ExpertModel extends BaseModel {

    public a_EXPERT_LIST data = new a_EXPERT_LIST();
    public STATUS lastStatus;
    String fileName;

    public ExpertModel(Context context) {
        super(context);
        fileName = "/expertList" +".dat";
        readCache();
    }

    public void readCache() {
        String path = DataCleanManager.getCacheDir(mContext) + fileName;
        File f1 = new File(path);
        if (f1.exists()) {
            try {
                InputStream is = new FileInputStream(f1);
                InputStreamReader input = new InputStreamReader(is, "UTF-8");
                BufferedReader bf = new BufferedReader(input);

                parseCache(bf.readLine());

                bf.close();
                input.close();
                is.close();

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public void parseCache(String result) {
        try {
            if (result != null) {
                JSONObject jsonObject = new JSONObject(result);
                data.fromJson(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
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


    public void getExpertList(String info_from, String province, String city, String district,
                              String expert_position, String sort_type, String page,boolean bShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                ExpertModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        expertResponse response = new expertResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.data.clear();
                            data.paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.data.addAll(response.data);
                            }

                            fileSave();
                            ExpertModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            JSONObject itemObject = new JSONObject();
            itemObject.put("info_from", info_from);
            itemObject.put("province", province);
            itemObject.put("city", city);
            itemObject.put("district", district);
            itemObject.put("expert_position", expert_position);
            itemObject.put("sort_type", sort_type);
            itemObject.put("page", page);

            params.put("json", itemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.EXPERT).type(JSONObject.class).params(params);

        if (bShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }


    public void getExpertListMore(String info_from, String province, String city, String district,
                                  String expert_position, String sort_type, String page,boolean bShow) {

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                ExpertModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        expertResponse response = new expertResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.data.addAll(response.data);
                            }
                            fileSave();
                            ExpertModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            JSONObject itemObject = new JSONObject();
            itemObject.put("info_from", info_from);
            itemObject.put("province", province);
            itemObject.put("city", city);
            itemObject.put("district", district);
            itemObject.put("expert_position", expert_position);
            itemObject.put("sort_type", sort_type);
            itemObject.put("page", page);

            params.put("json", itemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.EXPERT).type(JSONObject.class).params(params);

        if (bShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }

    public void evaluateExpert(String info_from, String expert_id, String evaluate1, String count1,
                              String evaluate2, String count2, String other,boolean bShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                ExpertModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        expertResponse response = new expertResponse();
                        response.fromJson(jo);

                        lastStatus = response.status;

                        ExpertModel.this.OnMessageResponse(url, jo, status);
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            JSONObject itemObject = new JSONObject();
            itemObject.put("info_from", info_from);
            itemObject.put("expert_id", expert_id);
            itemObject.put("phone", /*SESSION.getInstance().sid*/ "15040323435");
            JSONObject item = new JSONObject();
            item.put(evaluate1,count1);
            item.put(evaluate2,count2);
            itemObject.put("evaluate_json", item);
            itemObject.put("other", other);

            params.put("json", itemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.EVALUATEEXPERT).type(JSONObject.class).params(params);

        if (bShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }

}
